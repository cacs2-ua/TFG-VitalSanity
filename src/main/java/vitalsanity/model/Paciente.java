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
@Table(name = "pacientes")
public class Paciente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String genero;

    @NotNull
    private String fechaNacimiento;


    @NotNull
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToMany
    @JoinTable(name = "paciente_profesional_medico_autorizado",
            joinColumns = { @JoinColumn(name = "fk_paciente") },
            inverseJoinColumns = {@JoinColumn(name = "fk_profesional_medico_autorizado") })
    Set<ProfesionalMedico> profesionalesMedicosAutorizados = new HashSet<>();

    public Set<ProfesionalMedico> getProfesionalesMedicosAutorizados() {
        return this.profesionalesMedicosAutorizados;
    }

    public void agregarProfesionalMedicoAutorizado(ProfesionalMedico profesionalMedicoAutorizado) {
        this.getProfesionalesMedicosAutorizados().add(profesionalMedicoAutorizado);
        profesionalMedicoAutorizado.getPacientesQueHanAutorizado().add(this);
    }

    public  void  quitarProfesionalMedicoAutorizado (ProfesionalMedico profesionalMedicoAutorizado) {
        this.getProfesionalesMedicosAutorizados().remove(profesionalMedicoAutorizado);
        profesionalMedicoAutorizado.getPacientesQueHanAutorizado().remove(this);
    }


    @ManyToMany
    @JoinTable(name = "paciente_profesional_medico_desautorizado",
            joinColumns = { @JoinColumn(name = "fk_paciente") },
            inverseJoinColumns = {@JoinColumn(name = "fk_profesional_medico_desautorizado") })
    Set<ProfesionalMedico> profesionalesMedicosDesautorizados = new HashSet<>();


    public Set<ProfesionalMedico> getProfesionalesMedicosDesautorizados() {
        return this.profesionalesMedicosDesautorizados;
    }

    public void agregarProfesionalMedicoDesautorizado(ProfesionalMedico profesionalMedicoDesautorizado) {
        this.getProfesionalesMedicosDesautorizados().add(profesionalMedicoDesautorizado);
        profesionalMedicoDesautorizado.getPacientesQueHanDesautorizado().add(this);
    }

    public void quitarProfesionalMedicoDesautorizado(ProfesionalMedico profesionalMedicoDesautorizado) {
        this.getProfesionalesMedicosDesautorizados().remove(profesionalMedicoDesautorizado);
        profesionalMedicoDesautorizado.getPacientesQueHanDesautorizado().remove(this);
    }

    //getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

        Paciente paciente = (Paciente) o;
        return Objects.equals(id, paciente.id);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null.");
        }
        if (this.usuario != null && this.usuario != usuario) {
            throw new IllegalStateException("El paciente ya está asignado a un usuario. Desvincule el usuario existente antes de asignar uno nuevo.");
        }
        if (this.usuario == usuario) {
            return; // No hacer nada si ya están vinculados
        }
        this.usuario = usuario;
        if (usuario.getPaciente() != this) {
            usuario.setPaciente(this);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
