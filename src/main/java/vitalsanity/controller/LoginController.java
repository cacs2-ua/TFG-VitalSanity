package vitalsanity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import vitalsanity.authentication.ManagerUserSession;
import vitalsanity.dto.LoginData;
import vitalsanity.dto.UsuarioData;
import vitalsanity.service.UsuarioService;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ManagerUserSession managerUserSession;

    private Long getUsuarioLogeadoId() {
        return managerUserSession.usuarioLogeado();
    }

    @GetMapping("/")
    public String home(Model model) {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        if (getUsuarioLogeadoId() != null) {
            return "redirect:/api/general/home";
        }

        model.addAttribute("loginData", new LoginData());
        return "login-form"; // Plantilla adaptada con thymeleaf
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginData loginData, Model model) {
        UsuarioService.LoginStatus loginStatus = usuarioService.login(
                loginData.getEmail(),
                loginData.getContrasenya()
        );

        if (loginStatus == UsuarioService.LoginStatus.LOGIN_OK) {
            UsuarioData usuario = usuarioService.findByEmail(loginData.getEmail());
            managerUserSession.logearUsuario(usuario.getId());

            if (usuario.getTipoId() == 1){
                return "redirect:/api/administrador/bienvenida";
            }
            if (usuario.getTipoId() == 2){
                return "redirect:/api/centro-medico/bienvenida";
            }
            if (usuario.getTipoId() == 3){
                return "redirect:/api/profesional-medico/bienvenida";
            }
            if (usuario.getTipoId() == 4){
                return "redirect:/api/paciente/bienvenida";
            }
            // Redirigir a la página de bienvenida
            return "redirect:/api/paciente/bienvenida";

        } else if (loginStatus == UsuarioService.LoginStatus.USER_DISABLED) {
            model.addAttribute("error", "No puedes iniciar sesión. Tu usuario está deshabilitado");
            return "login-form";
        } else {
            model.addAttribute("error", "Ha habido algún error al iniciar sesión");
            return "login-form";
        }

    }

    @GetMapping("/logout")
    public String logout() {
        managerUserSession.logout();
        return "redirect:/login";
    }

}
