package vitalsanity.service.utils.autofirma;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

            // Recuperar datos de la base
            Usuario usuarioProfesionalMedico = usuarioRepository.findById(idUsuarioProfesionalMedico)
                    .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));
            Usuario usuarioPaciente = usuarioRepository.findById(idUsuarioPaciente)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            ProfesionalMedico profesionalMedico = profesionalMedicoRepository
                    .findByUsuarioId(idUsuarioProfesionalMedico)
                    .orElseThrow(() -> new RuntimeException("Profesional médico no encontrado"));
            Paciente paciente = pacienteRepository
                    .findByUsuarioId(idUsuarioPaciente)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            CentroMedico centro = profesionalMedico.getCentroMedico();
            Hibernate.initialize(centro);
            Hibernate.initialize(centro.getUsuario());

            // Crear PDF en memoria
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Formatear fecha en español: "20 de abril de 2025"
            LocalDate hoy = LocalDate.now();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
            String fechaLarga = hoy.format(fmt);
            // Formatear fecha corta: "20/04/2025"
            DateTimeFormatter fmtCorto = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaCorta = hoy.format(fmtCorto);

            // Encabezado con lugar y fecha, alineado a la derecha
            String lugar = centro.getUsuario().getMunicipio() + " (" + centro.getUsuario().getProvincia() + ")";
            document.add(new Paragraph(lugar + ", a " + fechaLarga + ".")
                    .setTextAlignment(TextAlignment.RIGHT)
            );

            // Título centrado y en negrita
            document.add(new Paragraph("AUTORIZACIÓN DE ACCESO AL HISTORIAL MÉDICO CENTRALIZADO")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setMarginTop(10)
                    .setMarginBottom(10)
            );

            // Resumen Ejecutivo
            document.add(new Paragraph("Resumen Ejecutivo").setBold());
            document.add(new Paragraph(
                    "A continuación, se presenta el texto completo de la autorización de acceso al historial médico centralizado, " +
                            "incluyendo todos los datos proporcionados, estructurado conforme a los requisitos de información y consentimiento " +
                            "recogidos en el Reglamento General de Protección de Datos (RGPD) y en la Ley Orgánica 3/2018 de Protección de Datos " +
                            "Personales y garantía de los derechos digitales (LOPDGDD). Se detallan las partes intervinientes, la finalidad del " +
                            "tratamiento, la base jurídica, el alcance de los derechos del paciente, el mecanismo de revocación, la identificación " +
                            "del responsable del tratamiento, así como las firmas electrónica y cofirma mediante AutoFirma/@firma."
            ));

            // 1. Identificación de las partes
            document.add(new Paragraph("1. Identificación de las partes").setBold().setMarginTop(10));
            document.add(new Paragraph("Profesional médico").setBold().setMarginLeft(10));
            document.add(new Paragraph()
                    .add(new Text("• Nombre: ").setBold())
                    .add(usuarioProfesionalMedico.getNombreCompleto())
                    .setMarginLeft(20)
            );
            document.add(new Paragraph()
                    .add(new Text("• NIF/NIE: ").setBold())
                    .add(usuarioProfesionalMedico.getNifNie())
                    .setMarginLeft(20)
            );
            document.add(new Paragraph()
                    .add(new Text("• Especialidad: ").setBold())
                    .add(profesionalMedico.getEspecialidadMedica().getNombre())
                    .setMarginLeft(20)
            );

            document.add(new Paragraph("Centro médico solicitante").setBold().setMarginLeft(10));
            document.add(new Paragraph()
                    .add(new Text("• NIF: ").setBold())
                    .add(centro.getUsuario().getNifNie())
                    .setMarginLeft(20)
            );
            document.add(new Paragraph()
                    .add(new Text("• Nombre: ").setBold())
                    .add(centro.getUsuario().getNombreCompleto())
                    .setMarginLeft(20)
            );
            document.add(new Paragraph()
                    .add(new Text("• Provincia: ").setBold())
                    .add(centro.getUsuario().getProvincia())
                    .setMarginLeft(20)
            );
            document.add(new Paragraph()
                    .add(new Text("• Municipio: ").setBold())
                    .add(centro.getUsuario().getMunicipio())
                    .setMarginLeft(20)
            );
            document.add(new Paragraph()
                    .add(new Text("• Dirección: ").setBold())
                    .add(centro.getDireccion())
                    .setMarginLeft(20)
            );

            document.add(new Paragraph("Paciente autorizado").setBold().setMarginLeft(10));
            document.add(new Paragraph()
                    .add(new Text("• Nombre: ").setBold())
                    .add(usuarioPaciente.getNombreCompleto())
                    .setMarginLeft(20)
            );
            document.add(new Paragraph()
                    .add(new Text("• NIF/NIE: ").setBold())
                    .add(usuarioPaciente.getNifNie())
                    .setMarginLeft(20)
            );

            // 2. Objeto y Finalidad
            document.add(new Paragraph("2. Objeto y Finalidad de la autorización").setBold().setMarginTop(10));
            document.add(new Paragraph(
                    "Mediante el presente documento, yo, " + usuarioPaciente.getNombreCompleto() + ", otorgo mi consentimiento libre, " +
                            "específico, informado e inequívoco al profesional identificado en el apartado 1 y al centro sanitario al que presta " +
                            "servicios para acceder íntegramente a mi historial médico centralizado en la plataforma VitalSanity, con la única " +
                            "finalidad de posibilitar la adecuada prestación de atención sanitaria, de conformidad con el artículo 9.2.a) RGPD " +
                            "que permite el tratamiento de categorías especiales de datos previo consentimiento explícito del interesado Reglamento GDPR."
            ));
            document.add(new Paragraph(
                    "El acceso comprenderá todos los informes médicos y la documentación clínica asociada que obren en el repositorio, " +
                            "incluidos los informes futuros que se incorporen mientras la presente autorización permanezca vigente."
            ));

            // 3. Información de la autorización
            document.add(new Paragraph("3. Información de la autorización").setBold().setMarginTop(10));
            document.add(new Paragraph()
                    .add(new Text("• Motivo de la solicitud: ").setBold())
                    .add(motivo)
                    .setMarginLeft(10)
            );
            document.add(new Paragraph()
                    .add(new Text("• Descripción complementaria: ").setBold())
                    .add(descripcion)
                    .setMarginLeft(10)
            );

            // 4. Base Jurídica del Tratamiento
            document.add(new Paragraph("4. Base Jurídica del Tratamiento").setBold().setMarginTop(10));
            document.add(new Paragraph("1. Consentimiento informado: La legitimación para el tratamiento se basa en el consentimiento libre, específico, informado e inequívoco del paciente, tal como exige el artículo 6 de la LOPDGDD BOE y el artículo 6.1.a) del RGPD EUR-Lex."));
            document.add(new Paragraph("2. Datos de salud (categoría especial): Se trata de datos relativos a la salud, cuya recogida y tratamiento requieren consentimiento explícito conforme al artículo 9.2.a) del RGPD Agencia Española de Protección de Datos."));
            document.add(new Paragraph("3. Obligación de información: El responsable ha facilitado a la paciente toda la información prevista en el artículo 13 del RGPD en el momento de la obtención de sus datos EUR-Lex."));
            document.add(new Paragraph("4. Deber de confidencialidad: El profesional y el centro están sujetos al deber de confidencialidad establecido en el artículo 5 de la LOPDGDD BOE y en la Ley 41/2002, que protege la confidencialidad de los datos de salud BOE."));

            // 5. Datos objeto de tratamiento
            document.add(new Paragraph("5. Datos objeto de tratamiento").setBold().setMarginTop(10));
            document.add(new Paragraph(
                    "Se incluyen todos los datos clínicos recogidos en el historial médico centralizado, incluidos informes, diagnósticos, " +
                            "pruebas complementarias y demás documentación sanitaria. Se hace especial mención a que se tratarán datos especialmente " +
                            "protegidos (relativos a la salud) conforme al artículo 9.1 h) del RGPD."
            ));

            // 6. Principios aplicables
            document.add(new Paragraph("6. Principios aplicables").setBold().setMarginTop(10));
            document.add(new Paragraph(
                    "El tratamiento de los datos se regirá por los principios de licitud, lealtad, transparencia, limitación de la finalidad, " +
                            "minimización de datos, exactitud, limitación del plazo de conservación, integridad y confidencialidad establecidos en el " +
                            "artículo 5 del RGPD EUR-Lex, así como por las garantías adicionales recogidas en la LOPDGDD BOE."
            ));

            // 7. Derechos de las partes involucradas
            document.add(new Paragraph("7. Derechos de las partes involucradas").setBold().setMarginTop(10));
            document.add(new Paragraph(
                    "Cualquiera de las partes podrá ejercer los derechos reconocidos en la LOPDGDD y el RGPD:\n" +
                            "• Acceso a sus datos y a la información relacionada (art. 15 RGPD).\n" +
                            "• Rectificación de datos inexactos (art. 16 RGPD).\n" +
                            "• Supresión de datos («derecho al olvido», art. 17 RGPD).\n" +
                            "• Limitación del tratamiento (art. 18 RGPD).\n" +
                            "• Portabilidad de los datos (art. 20 RGPD).\n" +
                            "• Oposición al tratamiento (art. 21 RGPD).\n" +
                            "• Revocación del consentimiento en cualquier momento, sin afectar la licitud del tratamiento previo (art. 7.3 RGPD).\n" +
                            "• Derecho a no ser objeto de decisiones automatizadas.\n" +
                            "Para ejercitarlos, puede contactar con el Delegado de Protección de Datos en [dpd@centro-medico-default.es] o presentar reclamación ante la AEPD."
            ));

            // 8. Firma electrónica y validez jurídica
            document.add(new Paragraph("8. Firma electrónica y validez jurídica").setBold().setMarginTop(10));
            document.add(new Paragraph(
                    "1. Profesional médico: firmado electrónicamente por el Dr. " + usuarioProfesionalMedico.getNombreCompleto() +
                            " mediante AutoFirma, conforme a la definición de firma electrónica avanzada en el art. 26 del Reglamento (UE) 910/2014 (eIDAS).\n" +
                            "2. Paciente: co-firma electrónica avanzada con la versión móvil @firma de AutoFirma.\n" +
                            "3. Validez jurídica: ambas firmas cumplen los requisitos de seguridad y legalidad establecidos en la Ley 59/2003, de firma electrónica, y tienen plena eficacia probatoria."
            ));

            // 9. Revocación y Plazo de Conservación
            document.add(new Paragraph("9. Revocación y Plazo de Conservación").setBold().setMarginTop(10));
            document.add(new Paragraph(
                    "La paciente podrá revocar libremente su consentimiento en cualquier momento, sin que ello afecte la licitud del tratamiento realizado con anterioridad a la revocación, conforme al artículo 7.3 del RGPD. " +
                            "Los datos se conservarán únicamente durante el tiempo necesario para cumplir las finalidades sanitarias y las obligaciones legales y de custodia."
            ));

            // 10. Responsable del Tratamiento
            document.add(new Paragraph("10. Responsable del Tratamiento").setBold().setMarginTop(10));
            document.add(new Paragraph(
                    "El Responsable del Tratamiento es el Centro Médico \"" + centro.getUsuario().getNombreCompleto() + "\" (NIF " +
                            centro.getUsuario().getNifNie() + "), con domicilio en " + centro.getDireccion() + ", " +
                            centro.getUsuario().getMunicipio() + " (" + centro.getUsuario().getProvincia() + "). Para cualquier consulta, puede contactar con el centro."
            ));

            // 11. Medidas de seguridad
            document.add(new Paragraph("11. Medidas de seguridad").setBold().setMarginTop(10));
            document.add(new Paragraph(
                    "El responsable garantiza la aplicación de medidas técnicas y organizativas apropiadas para proteger los datos frente a destrucción, pérdida, alteración, divulgación o acceso no autorizado, conforme al artículo 32 del RGPD."
            ));

            // 12. Firma y Fecha
            document.add(new Paragraph("12. Firma y Fecha").setBold().setMarginTop(10));
            document.add(new Paragraph("En prueba de conformidad y aceptación, las partes han firmado electrónicamente el presente documento mediante AutoFirma o Cliente móvil @firma."));
            document.add(new Paragraph("Fecha de expedición de la autorización: " + fechaCorta).setMarginTop(5));

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de autorización", e);
        }
    }


    public byte[] generarPdfInforme(
            Long profesionalMedicoId,
            Long pacienteId,
            String tituloInforme,
            String descripcion,
            String observaciones) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            ProfesionalMedico profesionalMedico = profesionalMedicoRepository.findById(profesionalMedicoId).orElse(null);
            Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);

            Usuario usuarioProfesionalMedico = profesionalMedico.getUsuario();
            Usuario usuarioPaciente = paciente.getUsuario();


            CentroMedico centro = profesionalMedico.getCentroMedico();

            Usuario usuarioCentroMedico = usuarioRepository.findByCentroMedicoId(centro.getId()).orElse(null);

            // Crear PDFWriter y PDFDocument en memoria
            PdfWriter pdfWriter = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDoc);

            // Añadir contenido al PDF (texto de ejemplo)
            document.add(new Paragraph("DATOS DEL INFORME"));
            document.add(new Paragraph("Título: " + tituloInforme));
            document.add(new Paragraph("Descripcion: " + descripcion));
            document.add(new Paragraph("Observaciones: " + observaciones));
            document.add(new Paragraph("Nombre del profesional médico: " + usuarioProfesionalMedico.getNombreCompleto()));
            document.add(new Paragraph("NIF/NIE del profesional médico: " + usuarioProfesionalMedico.getNifNie()));
            document.add(new Paragraph("Especialidad del profesional médico: " + profesionalMedico.getEspecialidadMedica().getNombre()));
            document.add(new Paragraph("NIF del Centro médico desde el que se ha solicitado la autorización: " + usuarioCentroMedico.getNifNie()));
            document.add(new Paragraph("Nombre del Centro médico desde el que se ha solicitado la autorización: " + usuarioCentroMedico.getNombreCompleto()));
            document.add(new Paragraph("Provincia del Centro médico desde el que se ha solicitado la autorización: " + usuarioCentroMedico.getProvincia()));
            document.add(new Paragraph("Municipio del Centro médico desde el que se ha solicitado la autorización: " + usuarioCentroMedico.getMunicipio()));
            document.add(new Paragraph("Dirección del Centro médico desde el que se ha solicitado la autorización: " + centro.getDireccion()));
            document.add(new Paragraph("Nombre del paciente: " + usuarioPaciente.getNombreCompleto()));
            document.add(new Paragraph("NIF/NIE del paciente: " + usuarioPaciente.getNifNie()));

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }
}
