package vitalsanity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {
    @GetMapping("/api/admin/registro-centro-medico")
    public String detallesInformeMedico(Model model) {
        return "admin-user/registro-centro-medico-form";
    }
}
