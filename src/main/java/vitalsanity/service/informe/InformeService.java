package vitalsanity.service.informe;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import vitalsanity.dto.centro_medico.CentroMedicoData;
import vitalsanity.dto.general_user.UsuarioData;
import vitalsanity.dto.paciente.BuscarPacienteResponse;
import vitalsanity.dto.paciente.PacienteData;
import vitalsanity.dto.profesional_medico.DocumentoData;
import vitalsanity.dto.profesional_medico.InformeData;
import vitalsanity.dto.profesional_medico.ProfesionalMedicoData;
import vitalsanity.dto.profesional_medico.SolicitudAutorizacionData;
import vitalsanity.model.*;
import vitalsanity.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;


@Service
public class InformeService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private InformeRepository informeRepository;

    @Autowired
    private ProfesionalMedicoRepository profesionalMedicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private CentroMedicoRepository centroMedicoRepository;

    @Autowired
    private  UsuarioRepository usuarioRepository;

    @Transactional
    public InformeData crearNuevoInforme(
                                  Long profesionalMedicoId,
                                  Long pacienteId,
                                  String titulo,
                                  String descripcion,
                                  String observaciones) {
        ProfesionalMedico profesionalMedico = profesionalMedicoRepository.findById(profesionalMedicoId).orElse(null);
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);

        Informe informe = new Informe();

        String uuid = "";

        do {
            uuid = UUID.randomUUID().toString();
        } while(informeRepository.existsByUuid(uuid));

        informe.setUuid(uuid);

        // NUMERO RANDOM DE 11 CIFRAS

        long numero = (long)(Math.random() * 9000000000L) + 1000000000L;
        String identificadorPublico = "INF-" + String.valueOf(numero);

        informe.setIdentificadorPublico(identificadorPublico);

        informe.setTitulo(titulo);
        informe.setDescripcion(descripcion);
        informe.setObservaciones(observaciones);
        informe.setFechaCreacion(LocalDateTime.now());

        informe.setProfesionalMedico(profesionalMedico);
        informe.setPaciente(paciente);

        informeRepository.save(informe);
        return modelMapper.map(informe, InformeData.class);
    }

    @Transactional
    public InformeData editarInforme(
            Long informeId,
            Long profesionalMedicoId,
            Long pacienteId,
            String titulo,
            String descripcion,
            String observaciones) {
        Informe informe = informeRepository.findById(informeId).orElseThrow(() -> new EntityNotFoundException("Informe no encontrado"));
        ProfesionalMedico profesionalMedico = profesionalMedicoRepository.findById(profesionalMedicoId).orElseThrow(() -> new EntityNotFoundException("Profesional no encontrado"));
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);

        String uuid = "";

        do {
            uuid = UUID.randomUUID().toString();
        } while(informeRepository.existsByUuid(uuid));

        informe.setUuid(uuid);

        // NUMERO RANDOM DE 11 CIFRAS

        long numero = (long)(Math.random() * 9000000000L) + 1000000000L;
        String identificadorPublico = "INF-" + String.valueOf(numero);

        informe.setIdentificadorPublico(identificadorPublico);

        informe.setTitulo(titulo);
        informe.setDescripcion(descripcion);
        informe.setObservaciones(observaciones);
        informe.setFechaCreacion(LocalDateTime.now());

        informe.setProfesionalMedico(profesionalMedico);
        informe.setPaciente(paciente);

        informeRepository.save(informe);
        return modelMapper.map(informe, InformeData.class);
    }


    @Transactional
    public  void establecerInformacionFirma(Long informeId) {
        Informe informe = informeRepository.findById(informeId).orElse(null);

        informe.setFirmado(true);
        informe.setFechaFirma(LocalDateTime.now());
        informeRepository.save(informe);
    }

    @Transactional(readOnly = true)
    public  InformeData encontrarPorId(Long informeId) {
        Informe informe = informeRepository.findById(informeId).orElse(null);
        return modelMapper.map(informe, InformeData.class);
    }

    @Transactional(readOnly = true)
    public  InformeData encontrarPorUuid(String uuid) {
        Informe informe = informeRepository.findByUuid(uuid).orElse(null);
        return modelMapper.map(informe, InformeData.class);
    }

    @Transactional(readOnly = true)
    public  List<InformeData> obtenerTodosLosInformesDeLosProfesionalesMedicosAutorizados (Long pacienteId) {
        List<Informe> informes = informeRepository.findAllByPacienteId(pacienteId);

        List<InformeData> informesData = informes.stream()
                .map(informe -> modelMapper.map(informe, InformeData.class))
                .collect(Collectors.toList());

        for (int i = 0; i < informesData.size(); i++) {
            ProfesionalMedico profesionalMedico = informes.get(i).getProfesionalMedico();
            CentroMedico centroMedico = profesionalMedico.getCentroMedico();
            Usuario centroMedicoUsuario = centroMedico.getUsuario();

            informesData.get(i).setCentroMedicoUsuario(modelMapper.map(centroMedicoUsuario, UsuarioData.class));
        }

        return informesData;
    }

    @Transactional(readOnly = true)
    public InformeData encontrarInformeFullGraphPorId (Long informeId) {

        Informe informe = informeRepository.findWithEverythingById(informeId).orElse(null);

        InformeData informeData = modelMapper.map(informe, InformeData.class);

        ProfesionalMedico profesionalMedico = informe.getProfesionalMedico();
        CentroMedico centroMedico = profesionalMedico.getCentroMedico();
        Usuario centroMedicoUsuario = centroMedico.getUsuario();

        informeData.setCentroMedicoUsuario(modelMapper.map(centroMedicoUsuario, UsuarioData.class));

        LocalDate fechaNacimiento = LocalDate.parse(informeData.getPaciente().getFechaNacimiento(), DateTimeFormatter.ISO_LOCAL_DATE);
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        informeData.getPaciente().setEdad(edad);

        return informeData;
    }

}
