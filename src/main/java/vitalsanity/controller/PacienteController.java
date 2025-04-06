package vitalsanity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PacienteController{

    @GetMapping("/api/paciente/informes/{idInforme}")
    public String detallesInformeMedico(@PathVariable(value="idInforme") Long idInforme,
                                  Model model) {
        return "paciente/ver-detalles-informe";
    }

    @GetMapping("/api/paciente/{idPaciente}/informes")
    public String verInformesPropios(@PathVariable(value="idPaciente") Long idInforme,
                                  Model model) {
        return "paciente/ver-informes-propios";
    }

    @GetMapping("/api/paciente/{idPaciente}/notificaciones")
    public String verNotificacionesDeAutorizacion(@PathVariable(value="idPaciente") Long idInforme,
                                     Model model) {
        return "paciente/ver-notificaciones-de-autorizacion";
    }

    @GetMapping("/api/paciente/{idPaciente}/profesionales-autorizados")
    public String verProfesionalesMedicosAutorizados(@PathVariable(value="idPaciente") Long idInforme,
                                                  Model model) {
        return "paciente/ver-profesionales-medicos-autorizados";
    }

    @GetMapping("/api/paciente/{idPaciente}/datos-residencia")
    public String datosResidenciaForm(@PathVariable(value="idPaciente") Long idInforme,
                                      Model model) {
        return "paciente/completar-datos-residencia";
    }
}
