package vitalsanity.controller.profesional_medico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vitalsanity.dto.paciente.BuscarPacienteData;
import vitalsanity.dto.paciente.BuscarPacienteResponse;
import vitalsanity.service.paciente.PacienteService;

@Controller
public class ProfesionalMedicoController {

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
            model.addAttribute("paciente", pacienteResponse);
        }
        return "profesional_medico/buscar-paciente";
    }

    @GetMapping("/api/profesional-medico/{idProfesionalMedico}/solicitar-autorizacion/{idPaciente}")
    public String solicitarAutorizacion(@PathVariable(value="idProfesionalMedico") Long idProfesionalMedico,
                                        @PathVariable(value="idPaciente") Long idPaciente,
                                        Model model) {
        return "profesional_medico/solicitar-autorizacion";
    }

    @GetMapping("/api/profesional-medico/pop-up-autofirma-autorizacion")
    public String mostrarPopUpFirmaAutorizacion() {
        return "profesional_medico/pop-up-autofirma-autorizacion";
    }



}



