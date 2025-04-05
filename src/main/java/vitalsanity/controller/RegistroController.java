package vitalsanity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import vitalsanity.authentication.ManagerUserSession;
import vitalsanity.dto.LoginData;
import vitalsanity.dto.RegistroData;
import vitalsanity.dto.UsuarioData;
import vitalsanity.service.UsuarioService;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ManagerUserSession managerUserSession;

    private Long getUsuarioLogeadoId() {
        return managerUserSession.usuarioLogeado();
    }

    @GetMapping("/registro")
    public String registroForm(Model model) {
        if (getUsuarioLogeadoId() != null) {
            return "redirect:/api/general/home";
        }
        model.addAttribute("registroData", new RegistroData());
        return "guest-user/registro-form"; // Plantilla adaptada con thymeleaf
    }
}
