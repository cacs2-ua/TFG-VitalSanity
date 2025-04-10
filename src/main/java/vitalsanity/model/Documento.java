package vitalsanity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import vitalsanity.service.utils.EmailService;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "documentos")
public class Documento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private String s3_key;

    @NotNull
    private String tipo_archivo;

    @NotNull
    private Long tamanyo;

    @NotNull
    private LocalDateTime fechaSubida;

    // constructores

    public Documento() {}

    public Documento(String nombre, String s3_key, String tipo_archivo, Long tamanyo, LocalDateTime fechaSubida) {
        this.nombre = nombre;
        this.s3_key = s3_key;
        this.tipo_archivo = tipo_archivo;
        this.tamanyo = tamanyo;
        this.fechaSubida = fechaSubida;
    }

    // getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getS3_key() {
        return s3_key;
    }

    public void setS3_key(String s3_key) {
        this.s3_key = s3_key;
    }

    public String getTipo_archivo() {
        return tipo_archivo;
    }

    public void setTipo_archivo(String tipo_archivo) {
        this.tipo_archivo = tipo_archivo;
    }

    public Long getTamanyo() {
        return tamanyo;
    }

    public void setTamanyo(Long tamanyo) {
        this.tamanyo = tamanyo;
    }

    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Documento documento = (Documento) o;
        return Objects.equals(id, documento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
