package vitalsanity.service.utils.autofirma;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vitalsanity.model.CentroMedico;
import vitalsanity.model.Paciente;
import vitalsanity.model.ProfesionalMedico;
import vitalsanity.model.Usuario;
import vitalsanity.repository.PacienteRepository;
import vitalsanity.repository.ProfesionalMedicoRepository;
import vitalsanity.repository.UsuarioRepository;

import java.io.ByteArrayOutputStream;

@Service
public class GenerarPdf {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProfesionalMedicoRepository profesionalMedicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public byte[] generarPdfAutorizacion(
                              Long idUsuarioProfesionalMedico,
                              Long idUsuarioPaciente,
                              String motivo,
                              String descripcion) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Usuario usuarioProfesionalMedico = usuarioRepository.findById(idUsuarioProfesionalMedico).orElse(null);
            Usuario usuarioPaciente = usuarioRepository.findById(idUsuarioPaciente).orElse(null);

            ProfesionalMedico profesionalMedico = profesionalMedicoRepository.findByUsuarioId(idUsuarioProfesionalMedico).orElse(null);
            Paciente paciente = pacienteRepository.findByUsuarioId(idUsuarioPaciente).orElse(null);

            CentroMedico centro = profesionalMedico.getCentroMedico();

            Hibernate.initialize(centro);
            Hibernate.initialize(centro.getUsuario());

            // Crear PDFWriter y PDFDocument en memoria
            PdfWriter pdfWriter = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDoc);

            // Añadir contenido al PDF (texto de ejemplo)
            document.add(new Paragraph("DATOS DE LA AUTORIZACIÓN"));
            document.add(new Paragraph("Nombre del profesional médico: " + usuarioProfesionalMedico.getNombreCompleto()));
            document.add(new Paragraph("NIF/NIE del profesional médico: " + usuarioProfesionalMedico.getNifNie()));
            document.add(new Paragraph("Especialidad del profesional médico: " + profesionalMedico.getEspecialidadMedica().getNombre()));
            document.add(new Paragraph("NIF del Centro médico desde el que se ha solicitado la autorización: " + centro.getUsuario().getNifNie()));
            document.add(new Paragraph("Nombre del Centro médico desde el que se ha solicitado la autorización: " + centro.getUsuario().getNombreCompleto()));
            document.add(new Paragraph("Nombre del paciente: " + usuarioPaciente.getNombreCompleto()));
            document.add(new Paragraph("NIF/NIE del paciente: " + usuarioPaciente.getNifNie()));
            document.add(new Paragraph("Motivo: " + motivo));
            document.add(new Paragraph("Descripción: " + descripcion));

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }
}
