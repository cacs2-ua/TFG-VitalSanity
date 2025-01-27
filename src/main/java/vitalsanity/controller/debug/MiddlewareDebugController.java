package vitalsanity.controller.debug;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vitalsanity.authentication.ManagerUserSession;
import vitalsanity.dto.LoginData;
import vitalsanity.dto.RegistroData;
import vitalsanity.dto.UsuarioData;
import vitalsanity.service.UsuarioService;
import vitalsanity.service.exception.UsuarioServiceException;

@Controller
public class MiddlewareDebugController {

    @GetMapping("/api/admin/check")
    public String adminCheck(Model model) {
        return "debug/adminCheck";
    }

    @GetMapping("/api/tecnico/check")
    public String tecnicoCheck(Model model) {
        return "debug/tecnicoCheck";
    }

    @GetMapping("/api/comercio/check")
    public String comercioCheck(Model model) {
        return "debug/comercioCheck";
    }

    @GetMapping("/api/tecnico-or-admin/check")
    public String tecnicoOrAdminCheck(Model model) {
        return "debug/tecnicoOrAdminCheck";
    }

    @GetMapping("/api/general/check")
    public String generalCheck(Model model) {
        return "debug/generalCheck";
    }
}

