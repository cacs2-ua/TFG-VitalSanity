package vitalsanity.dto;

import java.util.Objects;

public class PacienteData {

    private Long id;
    private String genero;
    private String fechaNacimiento;

    // getters and setters
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
        if (!(o instanceof PacienteData)) return false;
        PacienteData pacienteData = (PacienteData) o;
        return Objects.equals(id, pacienteData.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
