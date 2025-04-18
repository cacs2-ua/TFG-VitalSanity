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
import vitalsanity.dto.profesional_medico.*;
import vitalsanity.repository.EspecialidadMedicaRepository;
import vitalsanity.service.documento.DocumentoService;
import vitalsanity.service.especialidad_medica.EspecialidadMedicaService;
import vitalsanity.service.general_user.UsuarioService;
import vitalsanity.service.informe.InformeService;
import vitalsanity.service.paciente.PacienteService;
import vitalsanity.service.profesional_medico.ProfesionalMedicoService;
import vitalsanity.service.utils.EmailService;
import vitalsanity.service.utils.aws.S3VitalSanityService;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private EspecialidadMedicaService especialidadMedicaService;

    @Autowired
    private S3VitalSanityService s3VitalSanityService;

    @Autowired
    private InformeService informeService;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private EmailService emailService;

    private Long getUsuarioLogeadoId() {
        return managerUserSession.usuarioLogeado();
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

        String s3Key = "autorizaciones/cofirmadas/" + uuidUsuarioProfesionalMedico + "_" + uuidUsuarioPaciente  + "_" + System.currentTimeMillis() + ".pdf";

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
    public String denegarSolicitudAutorizacionForm(@RequestParam Long idSolicitudAutorizacion,
                                                         Model model) {

        model.addAttribute("idSolicitudAutorizacion", idSolicitudAutorizacion);
        return "paciente/pop-up-denegar-solicitud-autorizacion";
    }

    @PostMapping("/api/paciente/denegar-solicitud-autorizacion")
    public String denegarSolicitudAutorizacion(@RequestParam Long idSolicitudAutorizacion,
                                                   Model model) {
        System.out.println("Iniciando el proceso de denegación de la solicitud de autorización");
        Long pacienteId = pacienteService.encontrarPacienteAPartirDeIdSolicitudAutorizacion(idSolicitudAutorizacion).getId();
        Long profesionalMedicoId = Long.parseLong(profesionalMedicoService.encontrarProfesionalMedicoAPartirDeIdSolicitudAutorizacion(idSolicitudAutorizacion).getId());
        pacienteService.agregarProfesionalMedicoDesautorizado(pacienteId, profesionalMedicoId);

        pacienteService.marcarSolicitudAutorizacionComoDenegada(idSolicitudAutorizacion);

        return "paciente/denegacion-exitosa";
    }

    @GetMapping("/api/paciente/profesionales-autorizados")
    public String verProfesionalesMedicosAutorizados(Model model) {
        Long idUsuarioPaciente = getUsuarioLogeadoId();
        PacienteData pacienteData = pacienteService.encontrarPorIdUsuario(idUsuarioPaciente);
        List<ProfesionalMedicoData> profesionalesMedicosData = pacienteService.obtenerProfesionalesMedicosAutorizados(pacienteData.getId());

        if (profesionalesMedicosData.isEmpty()) {
            boolean noHayProfesionalesAutorizados = true;
            model.addAttribute("noHayProfesionalesAutorizados", noHayProfesionalesAutorizados);
        }
        model.addAttribute("profesionalesMedicosAutorizados", profesionalesMedicosData);

        return "paciente/ver-profesionales-medicos-autorizados";
    }

    @PostMapping("/api/paciente/desautorizar-profesional-medico")
    public String desautorizarProfesionalMedico(@RequestParam("id") String idProfesionalMedicoStr,
                                                Model model) {
        Long idUsuarioPaciente = getUsuarioLogeadoId();
        Long idPaciente = pacienteService.encontrarPorIdUsuario(idUsuarioPaciente).getId();
        Long idProfesionalMedico = Long.parseLong(idProfesionalMedicoStr);
        SolicitudAutorizacionData solicitudAutorizacionData = pacienteService.obtenerUltimaSolicitudAutorizacionValida(idProfesionalMedico,idPaciente);
        pacienteService.marcarSolicitudAutorizacionComoDenegada(solicitudAutorizacionData.getId());
        pacienteService.agregarProfesionalMedicoDesautorizado(idPaciente, idProfesionalMedico);

        return "redirect:/api/paciente/profesionales-autorizados";
    }

    @GetMapping("/api/paciente/informes")
    public String verInformesPropios(Model model,
                                     @RequestParam(required = false) String informeIdentificadorPublico,
                                     @RequestParam(required = false) String centroMedicoNombre,
                                     @RequestParam(required = false) String profesionalMedicoNombre,
                                     @RequestParam(required = false) String especialidadNombre,
                                     @RequestParam(required = false) LocalDate fechaDesde,
                                     @RequestParam(required = false) LocalDate fechaHasta,
                                     @RequestParam(required = false) String profesionalMedicoId) {
        Long idPacienteUsuario = getUsuarioLogeadoId();
        Long idPaciente = pacienteService.encontrarPorIdUsuario(idPacienteUsuario).getId();
        List<InformeData> informes = informeService.
                obtenerFiltradosTodosLosInformesDeUnPaciente(
                        idPaciente,
                        informeIdentificadorPublico,
                        centroMedicoNombre,
                        profesionalMedicoNombre,
                        especialidadNombre,
                        fechaDesde,
                        fechaHasta,
                        false,
                        null,
                        profesionalMedicoId);

        boolean noHayInformes = false;
        if (informes.isEmpty()) {
            noHayInformes = true;
        }

        model.addAttribute("idPaciente", idPaciente);
        model.addAttribute("noHayInformes", noHayInformes);
        model.addAttribute("informes", informes);


        // FILTROS

        List<EspecialidadMedicaData> especialidadesMedicas = especialidadMedicaService.encontrarTodasLasEspecialidadesMedicas();


        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String fechaDesdeStr = (fechaDesde != null)
                ? fechaDesde.format(fmt)
                : "";

        String fechaHastaStr = (fechaHasta != null)
                ? fechaHasta.format(fmt)
                : "";

        model.addAttribute("informeIdentificadorPublico", informeIdentificadorPublico);
        model.addAttribute("centroMedicoNombre", centroMedicoNombre);
        model.addAttribute("profesionalMedicoNombre", profesionalMedicoNombre);
        model.addAttribute("especialidadesMedicas", especialidadesMedicas);
        model.addAttribute("especialidadNombre", especialidadNombre);
        model.addAttribute("fechaDesdeStr", fechaDesdeStr);
        model.addAttribute("fechaHastaStr", fechaHastaStr);

        return "paciente/ver-informes-propios";
    }

    @GetMapping("/api/paciente/informes/{informeId}")
    public String detallesInformeMedico(@PathVariable(value="informeId") Long informeId,
                                        Model model) {
        InformeData informe = informeService.encontrarInformeFullGraphPorId(informeId);
        List <DocumentoData> documentos = documentoService.obtenerDocumentosAsociadosAUnInforme(informeId);

        boolean noHayDocumentos = false;

        if (documentos.isEmpty()) {
            noHayDocumentos = true;
        }

        model.addAttribute("informeId", informeId );
        model.addAttribute("informe", informe);
        model.addAttribute("documentos", documentos);
        model.addAttribute("noHayDocumentos", noHayDocumentos);
        return "paciente/ver-detalles-informe";
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

}
