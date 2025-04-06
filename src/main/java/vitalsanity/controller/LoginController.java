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

    // Mapping para /login/certificate (fallback cuando no se selecciona certificado)
    @GetMapping("/login/certificate")
    public String certificateLoginFallback(Model model) {
        model.addAttribute("error", "No se selecciono certificado. Por favor, inicie sesion manualmente.");
        model.addAttribute("loginData", new LoginData());
        return "login-form";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginData loginData, Model model) {
        Long idUsuario = getUsuarioLogeadoId();
        UsuarioService.LoginStatus loginStatus = usuarioService.login(
                loginData.getEmail(),
                loginData.getContrasenya()
        );

        if (loginStatus == UsuarioService.LoginStatus.LOGIN_OK) {
            UsuarioData usuario = usuarioService.findByEmail(loginData.getEmail());
            managerUserSession.logearUsuario(usuario.getId());

            if (usuario.getTipoId() == 1){
                return "redirect:/api/admin/registro-centro-medico";
            }
            if (usuario.getTipoId() == 2){
                if (usuario.getPrimerAcceso()){
                    return "redirect:/api/general/usuarios/" + usuario.getId() + "/contrasenya";
                }

                return "redirect:/api/centro-medico/check";
            }
            if (usuario.getTipoId() == 3){
                return "redirect:/api/profesional-medico/pacientes/1/informes/nuevo";
            }
            if (usuario.getTipoId() == 4){
                return "redirect:/api/paciente/" + usuario.getId() + "/informes";
            }
            return "redirect:/api/auth/check";

        } else if (loginStatus == UsuarioService.LoginStatus.USER_DISABLED) {
            model.addAttribute("error", "No puedes iniciar sesion. Tu usuario esta deshabilitado");
            return "login-form";
        } else {
            model.addAttribute("error", "Ha habido algun error al iniciar sesion");
            return "login-form";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        managerUserSession.logout();
        return "redirect:/login";
    }
}