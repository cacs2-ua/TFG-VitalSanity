package vitalsanity.service.paciente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vitalsanity.dto.paciente.BuscarPacienteResponse;
import vitalsanity.model.Paciente;
import vitalsanity.model.Usuario;
import vitalsanity.repository.UsuarioRepository;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Metodo para buscar paciente por nifNie (ignora mayusculas/minusculas)
    public BuscarPacienteResponse buscarPacientePorNifNie(String nifNie) {
        if (nifNie == null || nifNie.trim().isEmpty()) {
            return null;
        }
        List<Usuario> usuarios = usuarioRepository.findByNifNie(nifNie);
        for (Usuario usuario : usuarios) {
            if (usuario.getNifNie().equalsIgnoreCase(nifNie) && usuario.getPaciente() != null) {
                Paciente paciente = usuario.getPaciente();
                BuscarPacienteResponse response = new BuscarPacienteResponse();
                response.setNombreCompleto(usuario.getNombreCompleto());
                response.setNifNie(usuario.getNifNie());
                response.setGenero(paciente.getGenero());
                // Se asume que la fecha de nacimiento esta en formato "yyyy-MM-dd"
                try {
                    LocalDate fechaNacimiento = LocalDate.parse(paciente.getFechaNacimiento(), DateTimeFormatter.ISO_LOCAL_DATE);
                    int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
                    response.setEdad(edad);
                } catch (Exception e) {
                    response.setEdad(0);
                }
                return response;
            }
        }
        return null;
    }
}
