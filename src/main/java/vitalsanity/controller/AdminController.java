package vitalsanity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vitalsanity.dto.RegistroCentroMedicoData;
import vitalsanity.dto.UsuarioData;
import vitalsanity.service.UsuarioService;

@Controller
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/api/admin/registro-centro-medico")
    public String mostrarFormularioRegistroCentroMedico(Model model) {
        model.addAttribute("registroCentroMedicoData", new vitalsanity.dto.RegistroCentroMedicoData());
        return "admin-user/registro-centro-medico-form";
    }


    @PostMapping("/api/admin/registro-centro-medico")
    public String registrarCentroMedico(@ModelAttribute("registroCentroMedicoData") RegistroCentroMedicoData registroCentroMedicoData,
                                        Model model) {
        try {
            UsuarioData usuarioData = usuarioService.registrarCentroMedico(registroCentroMedicoData);
            model.addAttribute("mensaje", "Centro medico registrado correctamente");
            return "admin-user/registro-centro-medico-success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "admin-user/registro-centro-medico-form";
        }
    }

}


