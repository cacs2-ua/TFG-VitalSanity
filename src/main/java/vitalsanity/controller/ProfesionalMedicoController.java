package vitalsanity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProfesionalMedicoController {

    @GetMapping("/api/profesional-medico/pacientes/{idPaciente}/informes/nuevo")
    public String crearNuevoInforme(@PathVariable(value="idPaciente") Long idPaciente,
                                                    Model model) {
        return "profesional-medico/crear-nuevo-informe";
    }

    @GetMapping("/api/profesional-medico/pacientes/{idPaciente}/informes/{idInforme}/editar")
    public String editarInforme(@PathVariable(value="idPaciente") Long idPaciente,
                                @PathVariable(value="idPaciente") Long idInforme,
                                    Model model) {
        return "profesional-medico/editar-informe";
    }

    @GetMapping("/api/profesional-medico/pacientes/{idPaciente}/informes")
    public String verDetallesInformePaciente(@PathVariable(value="idPaciente") Long idPaciente,
                                                    Model model) {
        return "profesional-medico/ver-informes-del-paciente";
    }

    @GetMapping("/api/profesional-medico/{idProfesionalMedico}/pacientes-que-han-autorizado")
    public String verPacientesQueHanAutorizado(@PathVariable(value="idProfesionalMedico") Long idProfesionalMedico,
                                             Model model) {
        return "profesional-medico/listado-pacientes-que-han-autorizado";
    }

    @GetMapping("/api/profesional-medico/{idProfesionalMedico}/buscar-paciente")
    public String buscarPaciente(@PathVariable(value="idProfesionalMedico") Long idProfesionalMedico,
                                        Model model) {
        return "profesional-medico/buscar-paciente";
    }

    @GetMapping("/api/profesional-medico/{idProfesionalMedico}/solicitar-autorizacion")
    public String solicitarAutorizacion(@PathVariable(value="idProfesionalMedico") Long idProfesionalMedico,
                                               Model model) {
        return "profesional-medico/solicitar-autorizacion";
    }



}



