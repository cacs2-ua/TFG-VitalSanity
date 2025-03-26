package vitalsanity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PacienteController{

    @GetMapping("/api/paciente/informes/{idInforme}")
    public String detallesInforme(@PathVariable(value="idInforme") Long idInforme,
                                  Model model) {
        return "paciente/paciente-ver-detalles-informe";
    }

}
