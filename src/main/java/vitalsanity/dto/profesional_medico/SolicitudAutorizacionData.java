package vitalsanity.dto.profesional_medico;

import vitalsanity.model.Documento;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SolicitudAutorizacionData {

    private Long id;
    private String nombreProfesionalMedico;
    private String nifNieProfesionalMedico;
    private  String nombreCentroMedico;
    private String nombrePaciente;
    private String nifNiePaciente;
    private String motivo;
    private String descripcion;
    private boolean firmada;
    private boolean cofirmada;
    private LocalDateTime fechaCreacion;
    private boolean denegada;

    Set<Documento> documentos = new HashSet<>();

    // Getters y Setters

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

    public String getNombreCentroMedico() {
        return nombreCentroMedico;
    }

    public void setNombreCentroMedico(String nombreCentroMedico) {
        this.nombreCentroMedico = nombreCentroMedico;
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

    public boolean isFirmada() {
        return firmada;
    }

    public void setFirmada(boolean firmada) {
        this.firmada = firmada;
    }

    public boolean isCofirmada() {
        return cofirmada;
    }

    public void setCofirmada(boolean cofirmada) {
        this.cofirmada = cofirmada;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public boolean isDenegada() {
        return denegada;
    }

    public void setDenegada(boolean denegada) {
        this.denegada = denegada;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Set<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Set<Documento> documentos) {
        this.documentos = documentos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SolicitudAutorizacionData that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


}
