package vitalsanity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vitalsanity.authentication.ManagerUserSession;

@Controller
public class PacienteController {
    @Autowired
    private ManagerUserSession managerUserSession;

    private Long getUsuarioLogeadoId() {
        return managerUserSession.usuarioLogeado();
    }

    @GetMapping("/api/paciente/bienvenida")
    public String dashboardComercio(Model model) {
        return "paciente-bienvenida";
    }
}
