package vitalsanity.service.profesional_medico;

import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import vitalsanity.dto.general_user.UsuarioData;
import vitalsanity.dto.paciente.BuscarPacienteResponse;
import vitalsanity.dto.paciente.PacienteData;
import vitalsanity.dto.profesional_medico.DocumentoData;
import vitalsanity.dto.profesional_medico.ProfesionalMedicoData;
import vitalsanity.dto.profesional_medico.SolicitudAutorizacionData;
import vitalsanity.model.*;
import vitalsanity.repository.*;

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
    private DocumentoRepository documentoRepository;

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

    @Transactional
    public SolicitudAutorizacionData obtenerUltimaAutorizacionCreadaPorUnProfesionalMedico(Long idUsuarioProfesionalMedico) {
        try {
            ProfesionalMedico profesionalMedico = profesionalMedicoRepository.findByUsuarioId(idUsuarioProfesionalMedico).orElse(null);

            SolicitudAutorizacion solicitudAutorizacion = solicitudAutorizacionRepository.findTopByProfesionalMedicoIdOrderByIdDesc(profesionalMedico.getId()).orElse(null);

            return modelMapper.map(solicitudAutorizacion, SolicitudAutorizacionData.class);

        } catch (Exception e) {
            // Puedes usar un logger aquí si tienes uno (recomendado)
            System.err.println("Error al obtener la última solicitud del profesional médico: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    @Transactional
    public  void marcarSolicitudAutorizacionComoFirmada(Long idSolicitudAutorizacion) {
        SolicitudAutorizacion solicitudAutorizacion = solicitudAutorizacionRepository.findById(idSolicitudAutorizacion).orElse(null);
        solicitudAutorizacion.setFirmada(true);
        solicitudAutorizacionRepository.save(solicitudAutorizacion);
    }

    @Transactional
    public ProfesionalMedicoData encontrarPorIdUsuario(Long idUsuarioProfesionalMedico) {
        ProfesionalMedico profesionalMedico = profesionalMedicoRepository.findByUsuarioId(idUsuarioProfesionalMedico).orElse(null);
        if (profesionalMedico == null) return null;
        else return modelMapper.map(profesionalMedico, ProfesionalMedicoData.class);
    }

    @Transactional
    public DocumentoData guardarEnBdInformacionSobreElDocumentoAsociadoALaSolicitudDeAutorizacion(
            Long idSolicitudAutorizacion,
            String nombre,
            String s3_key,
            String tipo_archivo,
            Long tamanyo,
            LocalDateTime fechaSubida) {

        Documento documento = new Documento();

        documento.setNombre(nombre);
        documento.setS3_key(s3_key);
        documento.setTipo_archivo(tipo_archivo);
        documento.setTamanyo(tamanyo);
        documento.setFechaSubida(fechaSubida);

        SolicitudAutorizacion solicitudAutorizacion = solicitudAutorizacionRepository.findById(idSolicitudAutorizacion).orElse(null);

        documento.setSolicitudAutorizacion(solicitudAutorizacion);

        documentoRepository.save(documento);

        return modelMapper.map(documento, DocumentoData.class);

    }

}
