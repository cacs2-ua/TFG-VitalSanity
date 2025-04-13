package vitalsanity.controller.paciente;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import vitalsanity.authentication.ManagerUserSession;
import vitalsanity.dto.paciente.PacienteData;
import vitalsanity.dto.paciente.ResidenciaData;
import vitalsanity.dto.general_user.UsuarioData;
import vitalsanity.dto.profesional_medico.SolicitudAutorizacionData;
import vitalsanity.service.general_user.UsuarioService;
import vitalsanity.service.paciente.PacienteService;

import java.util.List;

@Controller
public class PacienteController{

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ManagerUserSession managerUserSession;
    @Autowired
    private PacienteService pacienteService;

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


}
