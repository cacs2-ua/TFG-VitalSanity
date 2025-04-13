package vitalsanity.controller.paciente;

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
import vitalsanity.service.utils.aws.S3VitalSanityService;

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
    private S3VitalSanityService s3VitalSanityService;

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

    @GetMapping("/api/paciente/notificaciones")
    public String verNotificacionesDeAutorizacion(Model model) {
        Long idUsuarioPaciente = getUsuarioLogeadoId();

        PacienteData pacienteData = pacienteService.encontrarPorIdUsuario(idUsuarioPaciente);

        List<SolicitudAutorizacionData> solicitudesAutorizacion = pacienteService.obtenerTodasLasSolicitudesValidas(pacienteData.getId());
        model.addAttribute("solicitudesAutorizacion", solicitudesAutorizacion);
        return "paciente/ver-notificaciones-de-autorizacion";
    }

    @GetMapping("/api/paciente/pop-up-autofirma-autorizacion")
    public String cofirmarAutorizacionForm(@RequestParam("solicitudId") Long solicitudId, Model model) {
        SolicitudAutorizacionData solicitud = pacienteService.obtenerSolicitudPorId(solicitudId);
        model.addAttribute("solicitud", solicitud);
        return "paciente/pop-up-autofirma-autorizacion";
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

            return new AutorizacionFirmadaResponse(pdfFirmadoBase64, idSolicitudAutorizacion);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
