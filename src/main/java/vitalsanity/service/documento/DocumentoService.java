package vitalsanity.service.documento;

import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
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
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;


@Service
public class DocumentoService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private InformeRepository informeRepository;

    @Transactional
    public DocumentoData crearNuevoDocumento(
            Long informeId,
            String uuid,
            String nombre,
            String s3Key,
            String tipoArchivo,
            Long tamanyo) {
        Documento documento = new Documento();

        Informe informe = informeRepository.findById(informeId).orElse(null);

        documento.setInforme(informe);
        documento.setUuid(uuid);
        documento.setNombre(nombre);
        documento.setS3_key(s3Key);
        documento.setTipo_archivo(tipoArchivo);
        documento.setTamanyo(tamanyo);
        documento.setFechaSubida(LocalDateTime.now());

        documentoRepository.save(documento);

        return modelMapper.map(documento, DocumentoData.class);
    }

    @Transactional(readOnly = true)
    public  DocumentoData encontrarPorUuid(String uuid) {
        Documento documento = documentoRepository.findByUuid(uuid).orElse(null);
        return modelMapper.map(documento, DocumentoData.class);
    }
}
