package vitalsanity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PacienteController{

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

}
