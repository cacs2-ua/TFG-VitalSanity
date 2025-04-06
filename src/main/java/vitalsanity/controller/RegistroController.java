package vitalsanity.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vitalsanity.authentication.ManagerUserSession;
import vitalsanity.dto.RegistroData;
import vitalsanity.dto.UsuarioData;
import vitalsanity.service.EmailService;
import vitalsanity.service.UsuarioService;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ManagerUserSession managerUserSession;

    @Autowired
    private EmailService emailService;

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

    @PostMapping("/registro")
    public String registroSubmit(@ModelAttribute RegistroData registroData, HttpSession session, Model model) {
        // Validar que las contrasenyas coincidan
        if (!registroData.getContrasenya().equals(registroData.getConfirmarContrasenya())) {
            model.addAttribute("error", "Las contrasenyas no coinciden");
            return "guest-user/registro-form";
        }
        // Generar codigo de confirmacion (por ejemplo, 6 digitos)
        String codigoConfirmacion = String.valueOf((int)(Math.random() * 900000) + 100000);
        // Almacenar datos de registro y codigo en la sesion
        session.setAttribute("registroData", registroData);
        session.setAttribute("codigoConfirmacion", codigoConfirmacion);

        // Enviar email de confirmacion (se usa Mailtrap) usando el nuevo metodo send
        emailService.send(registroData.getEmail(), "Registration Confirmation Code",
                "Your registration confirmation code is: " + codigoConfirmacion);

        // Redirigir al formulario de codigo de confirmacion
        return "redirect:/registro/codigo-confirmacion-form";
    }

    @GetMapping("/registro/codigo-confirmacion-form")
    public String registroCodigoConfirmacionForm(Model model) {
        return "guest-user/registro-codigo-confirmacion-form"; // Plantilla adaptada con thymeleaf
    }

    @PostMapping("/registro/codigo-confirmacion-form")
    public String codigoConfirmacionSubmit(@RequestParam("codigo") String codigoIngresado,
                                           HttpSession session, Model model) {
        String codigoGuardado = (String) session.getAttribute("codigoConfirmacion");
        RegistroData registroData = (RegistroData) session.getAttribute("registroData");

        if (codigoGuardado == null || registroData == null) {
            model.addAttribute("error", "Sesion expirada. Por favor, reinicie el proceso de registro.");
            return "guest-user/registro-form";
        }

        if (!codigoGuardado.equals(codigoIngresado)) {
            model.addAttribute("error", "Codigo de confirmacion incorrecto");
            return "guest-user/registro-codigo-confirmacion-form";
        }

        // Si el codigo es correcto, completar el registro
        UsuarioData usuarioData = usuarioService.registrarPaciente(registroData);

        // Limpiar datos de la sesion
        session.removeAttribute("registroData");
        session.removeAttribute("codigoConfirmacion");

        // Loguear usuario automaticamente
        managerUserSession.logearUsuario(usuarioData.getId());

        // Redirigir a la vista de paciente
        return "redirect:/api/paciente/" + usuarioData.getId() + "/informes";
    }
}
