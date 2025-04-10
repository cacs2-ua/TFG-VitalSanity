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
@Table(name = "solicitudes_autorizacion")
public class SolicitudAutorizacion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombreProfesionalMedico;

    @NotNull
    private String nifNieProfesionalMedico;

    @NotNull
    private String nombrePaciente;

    @NotNull
    private String nifNiePaciente;

    @NotNull
    private String motivo;

    @NotNull
    private String descripcion;

    // constructores

    public SolicitudAutorizacion() {}

    public SolicitudAutorizacion(String nombreProfesionalMedico, String nifNieProfesionalMedico, String nombrePaciente,
                                  String nifNiePaciente, String motivo, String descripcion) {
        this.nombreProfesionalMedico = nombreProfesionalMedico;
        this.nifNieProfesionalMedico = nifNieProfesionalMedico;
        this.nombrePaciente = nombrePaciente;
        this.nifNiePaciente = nifNiePaciente;
        this.motivo = motivo;
        this.descripcion = descripcion;
    }

    // getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SolicitudAutorizacion solicitudAutorizacion = (SolicitudAutorizacion) o;
        return Objects.equals(id, solicitudAutorizacion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
