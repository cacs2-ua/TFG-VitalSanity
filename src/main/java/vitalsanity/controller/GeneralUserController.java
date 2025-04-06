package vitalsanity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GeneralUserController {

    @GetMapping("/api/general/usuarios/{id}/contrasenya")
    public String actualizarContrasenya(@PathVariable(value="id") Long idUsuario,
                                        Model model) {
        return "general-user/actualizar-contrasenya";
    }

}
