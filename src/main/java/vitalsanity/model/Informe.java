package vitalsanity.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "informes")
public class Informe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String Uuid;

    @NotNull
    @Column(unique = true)
    private String identificadorPublico;

    @NotNull
    private String titulo;

    @NotNull
    private LocalDateTime fechaCreacion;

    @NotNull
    private String nombreCentroMedico;

    @NotNull
    private String nombreProfesionalMedico;

    @NotNull
    private String nifNieProfesionalMedico;

    @NotNull
    private String nombrePaciente;

    @NotNull
    private String nifNiePaciente;

    @NotNull
    private String provincia;

    @NotNull
    private String municipio;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        if (this.paciente == paciente || paciente == null) {
            return;
        }

        if (this.paciente != null) {
            this.paciente.getInformes().remove(this);
        }

        // Asigna el nuevo tipo
        this.paciente = paciente;

        if (!paciente.getInformes().contains(this)) {
            paciente.addInforme(this);
        }
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "profesional_medico_id", nullable = false)
    private ProfesionalMedico profesionalMedico;

    public ProfesionalMedico getProfesionalMedico() {
        return this.profesionalMedico;
    }

    public void setProfesionalMedico(ProfesionalMedico profesionalMedico) {
        if (this.profesionalMedico == profesionalMedico || profesionalMedico == null) {
            return;
        }

        if (this.profesionalMedico != null) {
            this.profesionalMedico.getInformes().remove(this);
        }

        // Asigna el nuevo tipo
        this.profesionalMedico = profesionalMedico;

        if (!profesionalMedico.getInformes().contains(this)) {
            profesionalMedico.addInforme(this);
        }
    }

    @OneToMany(mappedBy = "informe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    Set<Documento> documentos = new HashSet<>();

    public Set<Documento> getDocumentos() {
        return  this.documentos;
    }

    public void addDocumento(Documento documento) {
        if (documentos.contains(documento)) return;
        documentos.add(documento);
        if (documento.getInforme() != this) {
            documento.setInforme(this);
        }
    }

    public Informe() {}

    public Informe(String Uuid, String titulo, LocalDateTime fechaCreacion, String nombreCentroMedico, String nombreProfesionalMedico,
                   String nifNieProfesionalMedico, String nombrePaciente, String nifNiePaciente, String provincia, String municipio) {
        this.Uuid = Uuid;
        this.titulo = titulo;
        this.fechaCreacion = fechaCreacion;
        this.nombreCentroMedico = nombreCentroMedico;
        this.nombreProfesionalMedico = nombreProfesionalMedico;
        this.nifNieProfesionalMedico = nifNieProfesionalMedico;
        this.nombrePaciente = nombrePaciente;
        this.nifNiePaciente = nifNiePaciente;
        this.provincia = provincia;
        this.municipio = municipio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificadorPublico() {
        return identificadorPublico;
    }

    public void setIdentificadorPublico(String identificadorPublico) {
        this.identificadorPublico = identificadorPublico;
    }

    public String getUuid() {
        return Uuid;
    }

    public void setUuid(String Uuid) {
        this.Uuid = Uuid;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNombreCentroMedico() {
        return nombreCentroMedico;
    }

    public void setNombreCentroMedico(String nombreCentroMedico) {
        this.nombreCentroMedico = nombreCentroMedico;
    }

    public String getNombreProfesionalMedico() {
        return nombreProfesionalMedico;
    }

    public void setNombreProfesionalMedico(String nombreProfesionalMedico) {
        this.nombreProfesionalMedico = nombreProfesionalMedico;
    }

    public String getNifNieProfesionalMedico() {
        return nifNieProfesionalMedico;
    }

    public void setNifNieProfesionalMedico(String nifNieProfesionalMedico) {
        this.nifNieProfesionalMedico = nifNieProfesionalMedico;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getNifNiePaciente() {
        return nifNiePaciente;
    }

    public void setNifNiePaciente(String nifNiePaciente) {
        this.nifNiePaciente = nifNiePaciente;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Informe informe = (Informe) o;
        return Objects.equals(id, informe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
