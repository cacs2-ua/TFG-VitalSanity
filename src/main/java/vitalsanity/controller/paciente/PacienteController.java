package vitalsanity.controller.paciente;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import vitalsanity.authentication.ManagerUserSession;
import vitalsanity.dto.paciente.AutorizacionFirmadaResponse;
import vitalsanity.dto.paciente.PacienteData;
import vitalsanity.dto.paciente.ResidenciaData;
import vitalsanity.dto.general_user.UsuarioData;
import vitalsanity.dto.profesional_medico.SolicitudAutorizacionData;
import vitalsanity.service.general_user.UsuarioService;
import vitalsanity.service.paciente.PacienteService;
import vitalsanity.service.profesional_medico.ProfesionalMedicoService;
import vitalsanity.service.utils.EmailService;
import vitalsanity.service.utils.aws.S3VitalSanityService;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Controller
public class PacienteController{

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ManagerUserSession managerUserSession;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private ProfesionalMedicoService profesionalMedicoService;

    @Autowired
    private S3VitalSanityService s3VitalSanityService;

    @Autowired
    private EmailService emailService;

    private Long getUsuarioLogeadoId() {
        return managerUserSession.usuarioLogeado();
    }


    @GetMapping("/api/paciente/informes/{idInforme}")
    public String detallesInformeMedico(@PathVariable(value="idInforme") Long idInforme,
                                  Model model) {
        return "paciente/ver-detalles-informe";
    }

    @GetMapping("/api/paciente/{idPaciente}/informes")
    public String verInformesPropios(@PathVariable(value="idPaciente") Long idInforme,
                                  Model model) {
        return "paciente/ver-informes-propios";
    }

    @GetMapping("/api/paciente/{idPaciente}/profesionales-autorizados")
    public String verProfesionalesMedicosAutorizados(@PathVariable(value="idPaciente") Long idInforme,
                                                  Model model) {
        return "paciente/ver-profesionales-medicos-autorizados";
    }

    @GetMapping("/api/paciente/{idPaciente}/datos-residencia")
    public String datosResidenciaForm(@PathVariable("idPaciente") Long idPaciente, Model model) {
        model.addAttribute("residenciaData", new ResidenciaData());
        return "paciente/completar-datos-residencia";
    }

    @PostMapping("/api/paciente/{idPaciente}/datos-residencia")
    public String completarDatosResidencia(@PathVariable("idPaciente") Long idPaciente,
                                           @ModelAttribute("residenciaData") ResidenciaData residenciaData,
                                           Model model) {

        UsuarioData usuario = usuarioService.findById(idPaciente);
        // Llama a la capa de servicio para actualizar los datos de residencia y setear primerAcceso a false
        UsuarioData updatedUsuario = usuarioService.actualizarDatosResidencia(idPaciente, residenciaData);
        // Redirige a alguna página (por ejemplo, al dashboard del paciente)
        return "redirect:/api/paciente/" + usuario.getId() + "/informes";
    }

    // LÓGICA COFIRMA

    @GetMapping("/api/paciente/notificaciones")
    public String verNotificacionesDeAutorizacion(Model model,
                                                  HttpServletRequest request) {
        Long idUsuarioPaciente = getUsuarioLogeadoId();

        PacienteData pacienteData = pacienteService.encontrarPorIdUsuario(idUsuarioPaciente);

        boolean noHaySolicitudes = false;

        List<SolicitudAutorizacionData> solicitudesAutorizacion = pacienteService.obtenerTodasLasSolicitudesValidas(pacienteData.getId());
        if (solicitudesAutorizacion.isEmpty()) {
            noHaySolicitudes = true;
        }
        model.addAttribute("solicitudesAutorizacion", solicitudesAutorizacion);
        model.addAttribute("contextPath", request.getContextPath());
        model.addAttribute("noHaySolicitudes", noHaySolicitudes);
        return "paciente/ver-notificaciones-de-autorizacion";
    }

    @GetMapping("/api/paciente/pop-up-autofirma-autorizacion")
    public String cofirmarAutorizacionForm(@RequestParam("solicitudId") Long solicitudId,
                                           Model model,
                                           HttpServletRequest request) {
        SolicitudAutorizacionData solicitud = pacienteService.obtenerSolicitudPorId(solicitudId);
        model.addAttribute("solicitud", solicitud);
        model.addAttribute("contextPath", request.getContextPath());
        return "paciente/pop-up-autofirma-autorizacion";
    }

    //Este método se encarga de obtener la solicitud de autorización firmada anteriormene para poder cofirmarla
    @PostMapping("/api/paciente/solicitud-autorizacion-firmada")
    @ResponseBody
    public AutorizacionFirmadaResponse  obtenerSolicitudAutorizacionFirmada(@RequestParam Long idSolicitudAutorizacion) {
        try {
            System.out.println("Iniciando el proceso de obtención de la solicitud de autorización firmada");
            SolicitudAutorizacionData solicitudAutorizacionData = pacienteService.obtenerSolicitudPorId(idSolicitudAutorizacion);
            String s3Key = solicitudAutorizacionData.getDocumentos().iterator().next().getS3_key();
            byte[] pdfFirmado = s3VitalSanityService.obtenerBytesFicheroAPartirDeS3Key(s3Key);

            String pdfFirmadoBase64 = Base64.getEncoder().encodeToString(pdfFirmado);

            return new AutorizacionFirmadaResponse(idSolicitudAutorizacion, pdfFirmadoBase64);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/api/paciente/aws-pdf-autorizacion-cofirmada")
    @ResponseBody
    public String subirPdfAutorizacionCofirmadaEnAws(@RequestParam Long idSolicitudAutorizacion,
                                                     @RequestParam String cosignedPdfBase64
                                                     ) throws IOException {
        Long idPaciente = pacienteService.encontrarPacienteAPartirDeIdSolicitudAutorizacion(idSolicitudAutorizacion).getId();
        Long idProfesionalMedico = Long.parseLong(profesionalMedicoService.encontrarProfesionalMedicoAPartirDeIdSolicitudAutorizacion(idSolicitudAutorizacion).getId());

        pacienteService.agregarProfesionalMedicoAutorizado(idPaciente, idProfesionalMedico);

        UsuarioData usuarioProfesionalMedico = usuarioService.encontrarPorIdProfesionalMedico(idProfesionalMedico);
        String uuidUsuarioProfesionalMedico = usuarioProfesionalMedico.getUuid();

        Long idUsuarioPaciente = getUsuarioLogeadoId();
        UsuarioData usuarioPaciente = usuarioService.findById(idUsuarioPaciente);
        String uuidUsuarioPaciente = usuarioPaciente.getUuid();

        String s3Key = "debug/autorizaciones/cofirmadas/" + uuidUsuarioProfesionalMedico + "_" + uuidUsuarioPaciente  + "_" + System.currentTimeMillis() + ".pdf";

        byte[] cosignedPdfBytes = Base64.getDecoder().decode(cosignedPdfBase64);
        s3VitalSanityService.subirFicheroBytes(s3Key, cosignedPdfBytes);





        // Actualizar Información de la Solicitud de Autorización en la base de datos

        pacienteService.marcarSolicitudAutorizacionComoCofirmada(idSolicitudAutorizacion);
        pacienteService.establecerFechaCofirmaAutorizacion(idSolicitudAutorizacion, LocalDateTime.now());




        // Guardar en la base de datos la información del documento asociado a la solicitud de autorización

        String nombreArchivo = uuidUsuarioProfesionalMedico + "_" + uuidUsuarioPaciente  + "_" + System.currentTimeMillis() + ".pdf";
        String tipoArchivo = "application/pdf";

        Long tamano = (long) cosignedPdfBytes.length;
        LocalDateTime fechaSubida = LocalDateTime.now();

        profesionalMedicoService.guardarEnBdInformacionSobreElDocumentoAsociadoALaSolicitudDeAutorizacion(
                idSolicitudAutorizacion,
                nombreArchivo,
                s3Key,
                tipoArchivo,
                tamano,
                fechaSubida
        );

        String subject = "Acceso autorizado al historial médico del paciente: '" + usuarioPaciente.getNombreCompleto() + "'";

        String text = "El paciente: '" + usuarioPaciente.getNombreCompleto() + "' con NIF/NIE: '"
                + usuarioPaciente.getNifNie() + "' le ha autorizado el acceso para acceder a su historial médico centralizado. "
                + "A partir de ahora podrá acceder al historial médico del paciente desde la pestaña 'Pacientes que han autorizado'.  ";

        // emailService.send(usuarioProfesionalMedico.getEmail(), subject, text);

        return s3Key;
    }

    @GetMapping("/api/paciente/pdf-autorizacion-cofirmada")
    public String descargarPdfAutorizacionCofirmadaDeAws(@RequestParam String s3Key,
                                                         Model model) {

        String urlPrefirmada = s3VitalSanityService.generarUrlPrefirmada(
                s3Key,
                Duration.ofMinutes(5));
        model.addAttribute("urlPrefirmada", urlPrefirmada);
        return "paciente/descargar-pdf-autorizacion-cofirmada";

    }

    @GetMapping("/api/paciente/denegar-solicitud-autorizacion")
    public String denegarSolicitudAutorizacion(@RequestParam Long idSolicitudAutorizacion,
                                                         Model model) {

        return "paciente/pop-up-denegar-solicitud-autorizacion";
    }


}
