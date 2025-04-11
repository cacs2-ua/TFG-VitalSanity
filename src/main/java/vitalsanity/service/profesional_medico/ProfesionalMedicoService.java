package vitalsanity.service.profesional_medico;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Transactional
    public SolicitudAutorizacionData nuevaSolicitudAutorizacion(Long idUsuarioProfesionalMedico,
                                                                Long idUsuarioPaciente,
                                                                String motivo,
                                                                String descripcion) {
        try {
            Usuario usuarioProfesionalMedico = usuarioRepository.findById(idUsuarioProfesionalMedico).orElse(null);
            Usuario usuarioPaciente = usuarioRepository.findById(idUsuarioPaciente).orElse(null);

            SolicitudAutorizacion solicitudAutorizacion = new SolicitudAutorizacion();

            solicitudAutorizacion.setNombreProfesionalMedico(usuarioProfesionalMedico.getNombreCompleto());
            solicitudAutorizacion.setNifNieProfesionalMedico(usuarioProfesionalMedico.getNifNie());
            solicitudAutorizacion.setNombrePaciente(usuarioPaciente.getNombreCompleto());
            solicitudAutorizacion.setNifNiePaciente(usuarioPaciente.getNifNie());
            solicitudAutorizacion.setMotivo(motivo);
            solicitudAutorizacion.setDescripcion(descripcion);
            solicitudAutorizacion.setFirmada(false);
            solicitudAutorizacion.setCofirmada(false);
            solicitudAutorizacion.setFechaCreacion(LocalDateTime.now());

            ProfesionalMedico profesionalMedico = profesionalMedicoRepository.findByUsuarioId(idUsuarioProfesionalMedico).orElse(null);
            Paciente paciente = pacienteRepository.findByUsuarioId(idUsuarioPaciente).orElse(null);

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

}
