package vitalsanity.service.utils.autofirma;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class GenerarPdf {

    public byte[] generarPdfAutorizacion(String nombreProfesional,
                              String nifNieProfesional,
                              String nombrePaciente,
                              String nifNiePaciente,
                              String motivo,
                              String descripcion) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Crear PDFWriter y PDFDocument en memoria
            PdfWriter pdfWriter = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDoc);

            // Añadir contenido al PDF (texto de ejemplo)
            document.add(new Paragraph("DATOS DEL FORMULARIO"));
            document.add(new Paragraph("Nombre del profesional médico: " + nombreProfesional));
            document.add(new Paragraph("NIF/NIE del profesional médico: " + nifNieProfesional));
            document.add(new Paragraph("Nombre del paciente: " + nombrePaciente));
            document.add(new Paragraph("NIF/NIE del paciente: " + nifNiePaciente));
            document.add(new Paragraph("Motivo: " + motivo));
            document.add(new Paragraph("Descripción: " + descripcion));

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }
}
