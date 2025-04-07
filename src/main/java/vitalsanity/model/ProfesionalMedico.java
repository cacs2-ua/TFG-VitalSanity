package vitalsanity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "profesionales_medicos")
public class ProfesionalMedico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String naf;

    @NotNull
    @Column(unique = true)
    private String ccc;

    @NotNull
    private String iban;

    @NotNull
    private String genero;

    @NotNull
    private String fechaNacimiento;

    @NotNull
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null.");
        }
        if (this.usuario != null && this.usuario != usuario) {
            throw new IllegalStateException("El profesional médico ya está asignado a un usuario. Desvincule el usuario existente antes de asignar uno nuevo.");
        }
        if (this.usuario == usuario) {
            return; // No hacer nada si ya están vinculados
        }
        this.usuario = usuario;
        if (usuario.getProfesionalMedico() != this) {
            usuario.setProfesionalMedico(this);
        }
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "centro_medico_id", nullable = false)
    private CentroMedico centroMedico;

    public CentroMedico getCentroMedico() {
        return centroMedico;
    }

    public void setCentroMedico(CentroMedico centroMedico) {
        if (this.centroMedico == centroMedico || centroMedico == null) {
            return;
        }

        if (this.centroMedico != null) {
            this.centroMedico.getProfesionalesMedicos().remove(this);
        }

        this.centroMedico = centroMedico;

        if (!centroMedico.getProfesionalesMedicos().contains(this)) {
            centroMedico.addProfesionalMedico(this);
        }
    }

    // getter y setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaf() {
        return naf;
    }

    public void setNaf(String naf) {
        this.naf = naf;
    }

    public String getCcc() {
        return ccc;
    }

    public void setCcc(String ccc) {
        this.ccc = ccc;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfesionalMedico profesionalMedicoo = (ProfesionalMedico) o;
        return Objects.equals(id, profesionalMedicoo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
