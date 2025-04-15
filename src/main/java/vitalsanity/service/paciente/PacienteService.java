package vitalsanity.service.paciente;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import vitalsanity.service.profesional_medico.ProfesionalMedicoService;


@Service
public class PacienteService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SolicitudAutorizacionRepository solicitudAutorizacionRepository;

    @Autowired
    private ProfesionalMedicoRepository profesionalMedicoRepository;
    @Autowired
    private ProfesionalMedicoService profesionalMedicoService;

    // Metodo para buscar paciente por nifNie (ignora mayusculas/minusculas)
    public BuscarPacienteResponse buscarPacientePorNifNie(String nifNie) {
        if (nifNie == null || nifNie.trim().isEmpty()) {
            return null;
        }
        List<Usuario> usuarios = usuarioRepository.findByNifNie(nifNie);
        for (Usuario usuario : usuarios) {
            if (usuario.getNifNie().equalsIgnoreCase(nifNie)
                    && usuario.getPaciente() != null
                    && usuario.getTipo() != null
                    && usuario.getTipo().getId().equals(4L)) {
                Paciente paciente = usuario.getPaciente();
                BuscarPacienteResponse response = new BuscarPacienteResponse();
                response.setId(usuario.getPaciente().getId());
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

    @Transactional(readOnly = true)
    public PacienteData encontrarPorId(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) return null;
        else return modelMapper.map(paciente, PacienteData.class);
    }

    @Transactional
    public PacienteData encontrarPorIdUsuario(Long idUsuarioPaciente) {
        Paciente paciente = pacienteRepository.findByUsuarioId(idUsuarioPaciente).orElse(null);
        if (paciente == null) return null;
        return modelMapper.map(paciente, PacienteData.class);
    }

    @Transactional
    public  void marcarSolicitudAutorizacionComoDenegada(Long idSolicitudAutorizacion) {
        SolicitudAutorizacion solicitudAutorizacion = solicitudAutorizacionRepository.findById(idSolicitudAutorizacion).orElse(null);
        solicitudAutorizacion.setDenegada(true);
        solicitudAutorizacionRepository.save(solicitudAutorizacion);
    }

    @Transactional(readOnly = true)
    public List<SolicitudAutorizacionData> obtenerTodasLasSolicitudesValidas(Long pacienteId) {
        List<SolicitudAutorizacion> solicitudesAutorizacion =
                solicitudAutorizacionRepository.findByPacienteIdAndDenegadaFalseAndFirmadaTrueAndCofirmadaFalseOrderByFechaFirmaDesc(pacienteId);

        List<SolicitudAutorizacionData> solicitudesAutorizacionData = solicitudesAutorizacion.stream()
                .map(solicitudAutorizacion -> modelMapper.map(solicitudAutorizacion, SolicitudAutorizacionData.class))
                .collect(Collectors.toList());

        return solicitudesAutorizacionData;
    }

    @Transactional(readOnly = true)
    public SolicitudAutorizacionData obtenerSolicitudPorId(Long solicitudId) {
        SolicitudAutorizacion solicitudAutorizacion = solicitudAutorizacionRepository.findById(solicitudId).orElse(null);
        if (solicitudAutorizacion == null) return null;
        return modelMapper.map(solicitudAutorizacion, SolicitudAutorizacionData.class);
    }

    @Transactional
    public  void marcarSolicitudAutorizacionComoCofirmada(Long idSolicitudAutorizacion) {
        SolicitudAutorizacion solicitudAutorizacion = solicitudAutorizacionRepository.findById(idSolicitudAutorizacion).orElse(null);
        solicitudAutorizacion.setCofirmada(true);
        solicitudAutorizacionRepository.save(solicitudAutorizacion);
    }

    @Transactional
    public  void establecerFechaCofirmaAutorizacion(Long idSolicitudAutorizacion, LocalDateTime fechaCofirma) {
        SolicitudAutorizacion solicitudAutorizacion = solicitudAutorizacionRepository.findById(idSolicitudAutorizacion).orElse(null);
        solicitudAutorizacion.setFechaCofirma(fechaCofirma);
        solicitudAutorizacionRepository.save(solicitudAutorizacion);
    }

    @Transactional(readOnly = true)
    public PacienteData encontrarPacienteAPartirDeIdSolicitudAutorizacion(Long solicitudId) {
        Paciente paciente = pacienteRepository.findBySolicitudesAutorizacion_Id(solicitudId).orElse(null);
        if (paciente == null) return null;
        return modelMapper.map(paciente, PacienteData.class);
    }

    @Transactional
    public void agregarProfesionalMedicoAutorizado(Long idPaciente, Long idProfesionalMedico) {
        Paciente paciente = pacienteRepository.findById(idPaciente).orElse(null);
        ProfesionalMedico profesionalMedico = profesionalMedicoRepository.findById(idProfesionalMedico).orElse(null);
        paciente.addProfesionalMedicoAutorizado(profesionalMedico);
        paciente.quitarProfesionalMedicoDesautorizado(profesionalMedico);
    }

    @Transactional
    public void agregarProfesionalMedicoDesautorizado(Long idPaciente, Long idProfesionalMedico) {
        Paciente paciente = pacienteRepository.findById(idPaciente).orElse(null);
        ProfesionalMedico profesionalMedico = profesionalMedicoRepository.findById(idProfesionalMedico).orElse(null);
        paciente.addProfesionalMedicoDesautorizado(profesionalMedico);
    }

    @Transactional(readOnly = true)
    public List<ProfesionalMedicoData> obtenerProfesionalesMedicosAutorizados(Long idPaciente) {
        List<ProfesionalMedico> profesionalMedicosAutorizados = profesionalMedicoRepository.findByPacientesQueHanAutorizado_IdOrderByIdAsc(idPaciente);

        List<ProfesionalMedicoData> profesionalesMedicosAutorizadosData = profesionalMedicosAutorizados.stream()
                .map(profesionalMedicoAutorizado -> modelMapper.map(profesionalMedicoAutorizado, ProfesionalMedicoData.class))
                .collect(Collectors.toList());

        return  profesionalesMedicosAutorizadosData;
    }

}
