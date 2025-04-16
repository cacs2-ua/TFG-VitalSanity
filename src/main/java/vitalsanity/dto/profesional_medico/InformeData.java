package vitalsanity.dto.profesional_medico;

import java.time.LocalDateTime;
import java.util.Objects;

public class InformeData {

    private Long id;
    private String uuid;
    private String identificadorPublico;
    private String titulo;
    private String descripcion;
    private String observaciones;
    private LocalDateTime fechaCreacion;

    // getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIdentificadorPublico() {
        return identificadorPublico;
    }

    public void setIdentificadorPublico(String identificadorPublico) {
        this.identificadorPublico = identificadorPublico;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InformeData)) return false;
        InformeData informeData = (InformeData) o;
        return Objects.equals(id, informeData.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
