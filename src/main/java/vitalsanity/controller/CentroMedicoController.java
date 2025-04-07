package vitalsanity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vitalsanity.dto.RegistroProfesionalesMedicosData;
import vitalsanity.dto.UsuarioData;
import vitalsanity.service.UsuarioService;

import java.util.List;

@Controller
public class CentroMedicoController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/api/centro-medico/profesionales-medicos")
    public String registrarProfesionalesMedicosForm(Model model) {
        model.addAttribute("registroProfesionalesMedicosData", new vitalsanity.dto.RegistroProfesionalesMedicosData());
        return "centro-medico/registro-profesionales-medicos";
    }

    @PostMapping("/api/centro-medico/profesionales-medicos")
    public String registrarProfesionalesMedicos(@ModelAttribute("registroProfesionalesMedicosData") RegistroProfesionalesMedicosData data,
                                                Model model) {
        try {
            List<UsuarioData> registrados = usuarioService.registrarProfesionalesMedicos(data.getCsvFile());
            model.addAttribute("mensaje", "Registro completado con exito para " + registrados.size() + " profesionales medicos.");
            return "centro-medico/registro-profesionales-medicos-success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "centro-medico/registro-profesionales-medicos";
        }
    }
}
