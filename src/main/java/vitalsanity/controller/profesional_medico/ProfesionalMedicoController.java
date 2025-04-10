package vitalsanity.controller.profesional_medico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vitalsanity.dto.general_user.UsuarioData;
import vitalsanity.dto.paciente.BuscarPacienteData;
import vitalsanity.dto.paciente.BuscarPacienteResponse;
import vitalsanity.service.general_user.UsuarioService;
import vitalsanity.service.paciente.PacienteService;
import vitalsanity.service.profesional_medico.ProfesionalMedicoService;

@Controller
public class ProfesionalMedicoController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProfesionalMedicoService profesionalMedicoService;

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/api/profesional-medico/pacientes/{idPaciente}/informes/nuevo")
    public String crearNuevoInforme(@PathVariable(value="idPaciente") Long idPaciente,
                                                    Model model) {
        return "profesional_medico/crear-nuevo-informe";
    }

    @GetMapping("/api/profesional-medico/pacientes/{idPaciente}/informes/{idInforme}/editar")
    public String editarInforme(@PathVariable(value="idPaciente") Long idPaciente,
                                @PathVariable(value="idPaciente") Long idInforme,
                                    Model model) {
        return "profesional_medico/editar-informe";
    }

    @GetMapping("/api/profesional-medico/pacientes/{idPaciente}/informes")
    public String verInformesPaciente(@PathVariable(value="idPaciente") Long idPaciente,
                                             Model model) {
        return "profesional_medico/ver-informes-del-paciente";
    }

    @GetMapping("/api/profesional-medico/pacientes/{idPaciente}/informes/{idInforme}")
    public String verDetallesInformePaciente(@PathVariable(value="idPaciente") Long idPaciente,
                                             @PathVariable(value="idInforme") Long idInforme,
                                                    Model model) {
        return "profesional_medico/ver-detalles-informe";
    }

    @GetMapping("/api/profesional-medico/{idProfesionalMedico}/pacientes-que-han-autorizado")
    public String verPacientesQueHanAutorizado(@PathVariable(value="idProfesionalMedico") Long idProfesionalMedico,
                                             Model model) {
        return "profesional_medico/listado-pacientes-que-han-autorizado";
    }

    @GetMapping("/api/profesional-medico/{idProfesionalMedico}/pacientes-que-han-desautorizado")
    public String verPacientesQueHanDesautorizado(@PathVariable(value="idProfesionalMedico") Long idProfesionalMedico,
                                               Model model) {
        return "profesional_medico/listado-pacientes-que-han-desautorizado";
    }

    @GetMapping("/api/profesional-medico/{idProfesionalMedico}/buscar-paciente")
    public String buscarPacienteForm(@PathVariable(value="idProfesionalMedico") Long idProfesionalMedico,
                                     Model model) {
        model.addAttribute("buscarPacienteData", new BuscarPacienteData());
        return "profesional_medico/buscar-paciente";
    }

    @PostMapping("/api/profesional-medico/{idProfesionalMedico}/buscar-paciente")
    public String buscarPacienteSubmit(@PathVariable(value="idProfesionalMedico") Long idProfesionalMedico,
                                       @ModelAttribute("buscarPacienteData") BuscarPacienteData buscarPacienteData,
                                       Model model) {
        String nif = buscarPacienteData.getNifNie().trim();
        BuscarPacienteResponse pacienteResponse = pacienteService.buscarPacientePorNifNie(nif);

        if (pacienteResponse == null) {
            model.addAttribute("error", "Paciente no encontrado");
        } else {
            UsuarioData usuarioPaciente = usuarioService.encontrarPorIdPaciente(pacienteResponse.getId());
            model.addAttribute("paciente", pacienteResponse);
            model.addAttribute("usuarioPaciente", usuarioPaciente);
        }
        return "profesional_medico/buscar-paciente";
    }

    @GetMapping("/api/profesional-medico/{idUsuarioProfesionalMedico}/solicitar-autorizacion/{idUsuarioPaciente}")
    public String solicitarAutorizacion(@PathVariable(value="idUsuarioProfesionalMedico") Long idUsuarioProfesionalMedico,
                                        @PathVariable(value="idUsuarioPaciente") Long idUsuarioPaciente,
                                        Model model) {
        UsuarioData usuarioProfesionalMedico = usuarioService.findById(idUsuarioProfesionalMedico);
        UsuarioData usuarioPaciente = usuarioService.findById(idUsuarioPaciente);

        model.addAttribute("usuarioProfesionalMedico", usuarioProfesionalMedico);
        model.addAttribute("usuarioPaciente", usuarioPaciente);

        return "profesional_medico/solicitar-autorizacion";
    }

    @GetMapping("/api/profesional-medico/{idUsuarioProfesionalMedico}/pop-up-autofirma-autorizacion/{idUsuarioPaciente}")
    public String mostrarPopUpFirmaAutorizacion(@PathVariable(value="idUsuarioProfesionalMedico") Long idUsuarioProfesionalMedico,
                                                @PathVariable(value="idUsuarioPaciente") Long idUsuarioPaciente,
                                                Model model) {
        UsuarioData usuarioProfesionalMedico = usuarioService.findById(idUsuarioProfesionalMedico);
        UsuarioData usuarioPaciente = usuarioService.findById(idUsuarioPaciente);

        model.addAttribute("usuarioProfesionalMedico", usuarioProfesionalMedico);
        model.addAttribute("usuarioPaciente", usuarioPaciente);

        return "profesional_medico/pop-up-autofirma-autorizacion";
    }



}



