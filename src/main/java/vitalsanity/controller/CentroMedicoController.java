package vitalsanity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CentroMedicoController {
    @GetMapping("/api/centro-medico/initial-check")
    public String detallesInformeMedico(@PathVariable(value="idInforme") Long idInforme,
                                        Model model) {
        return "debug/centro-medico-check";
    }

    @GetMapping("/api/centro-medico/profesionales-medicos")
    public String registrarProfesionalesMedicosForm(@PathVariable(value="idInforme") Long idInforme,
                                        Model model) {
        return "debug/centro-medico-check";
    }
}
