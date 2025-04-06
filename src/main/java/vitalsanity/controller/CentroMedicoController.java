package vitalsanity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CentroMedicoController {
    @GetMapping("/api/centro-medico/profesionales-medicos")
    public String registrarProfesionalesMedicosForm(Model model) {
        return "centro-medico/registro-profesionales-medicos";
    }
}
