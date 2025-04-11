package vitalsanity.service.profesional_medico;

import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import vitalsanity.dto.general_user.UsuarioData;
import vitalsanity.dto.paciente.BuscarPacienteResponse;
import vitalsanity.dto.paciente.PacienteData;
import vitalsanity.dto.profesional_medico.ProfesionalMedicoData;
import vitalsanity.dto.profesional_medico.SolicitudAutorizacionData;
import vitalsanity.model.Paciente;
import vitalsanity.model.ProfesionalMedico;
import vitalsanity.model.SolicitudAutorizacion;
import vitalsanity.model.Usuario;
import vitalsanity.repository.PacienteRepository;
import vitalsanity.repository.ProfesionalMedicoRepository;
import vitalsanity.repository.SolicitudAutorizacionRepository;
import vitalsanity.repository.UsuarioRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProfesionalMedicoService {

    @Autowired
    private ProfesionalMedicoRepository profesionalMedicoRepository;

    @Autowired
    private SolicitudAutorizacionRepository solicitudAutorizacionRepository;

    @Autowired
    private  UsuarioRepository usuarioRepository;

    @Autowired
    private  PacienteRepository pacienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public ProfesionalMedicoData encontrarPorId(Long profesionalMedicoId) {
        ProfesionalMedico profesionalMedico = profesionalMedicoRepository.findById(profesionalMedicoId).orElse(null);
        if (profesionalMedico == null) return null;
        else return modelMapper.map(profesionalMedico, ProfesionalMedicoData.class);
    }

    @Transactional(readOnly = true)
    public String obtenerNombreCentroMedico(Long profesionalMedicoId) {
        ProfesionalMedico profesionalMedico = profesionalMedicoRepository.findById(profesionalMedicoId).orElse(null);
        if (profesionalMedico == null) return null;
        return profesionalMedico.getCentroMedico().getUsuario().getNombreCompleto();
    }

    @Transactional
    public SolicitudAutorizacionData nuevaSolicitudAutorizacion(Long idUsuarioProfesionalMedico,
                                                                String nombreProfesional,
                                                                String nifNieProfesional,
                                                                String nombreCentroMedico,
                                                                String nombrePaciente,
                                                                String nifNiePaciente,
                                                                String motivo,
                                                                String descripcion) {
        try {
            SolicitudAutorizacion solicitudAutorizacion = new SolicitudAutorizacion();

            solicitudAutorizacion.setNombreProfesionalMedico(nombreProfesional);
            solicitudAutorizacion.setNifNieProfesionalMedico(nifNieProfesional);
            solicitudAutorizacion.setNombreCentroMedico(nombreCentroMedico);
            solicitudAutorizacion.setNombrePaciente(nombrePaciente);
            solicitudAutorizacion.setNifNiePaciente(nifNiePaciente);
            solicitudAutorizacion.setMotivo(motivo);
            solicitudAutorizacion.setDescripcion(descripcion);
            solicitudAutorizacion.setFirmada(false);
            solicitudAutorizacion.setCofirmada(false);
            solicitudAutorizacion.setFechaCreacion(LocalDateTime.now());

            ProfesionalMedico profesionalMedico = profesionalMedicoRepository.findByUsuarioId(idUsuarioProfesionalMedico).orElse(null);
            Paciente paciente = pacienteRepository.findByUsuarioNifNie(nifNiePaciente).orElse(null);

            solicitudAutorizacion.setProfesionalMedico(profesionalMedico);
            solicitudAutorizacion.setPaciente(paciente);

            solicitudAutorizacionRepository.save(solicitudAutorizacion);
            return modelMapper.map(solicitudAutorizacion, SolicitudAutorizacionData.class);

        } catch (Exception e) {
            // Puedes usar un logger aquí si tienes uno (recomendado)
            System.err.println("Error al crear nueva solicitud de autorización: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    @NotNull
    private boolean firmada;

    @NotNull
    private boolean cofirmada;

    @NotNull
    private LocalDateTime fechaCreacion;

}
