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
@Table(name = "usuarios")
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String nombreCompleto;

    @NotNull
    private String contrasenya;

    @NotNull
    private boolean activado = false;

    @NotNull
    private String nifNie;

    private String naf;

    @NotNull
    private String genero;

    @NotNull
    private LocalDate fechaNacimiento;

    @NotNull
    private String telefono;

    private String pais;

    private String provincia;

    private String municipio;

    private String codigoPostal;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "tipo_id", nullable = false)
    private TipoUsuario tipo;

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        // Si el nuevo tipo es el mismo que el actual, no hace nada
        if (this.tipo == tipo || tipo == null) {
            return;
        }

        // Si ya tiene un tipo, lo desvincula de la lista de usuarios de ese tipo
        if (this.tipo != null) {
            this.tipo.getUsuarios().remove(this);
        }

        // Asigna el nuevo tipo
        this.tipo = tipo;

        // Si el tipo no es nulo, lo añade a la lista de usuarios de ese tipo
        if (!tipo.getUsuarios().contains(this)) {
            tipo.addUsuario(this);
        }
    }


    public Usuario() {
    }

    //Constructor con todos los atributos
    public Usuario(String email, String nombreCompleto, String contrasenya, boolean activado, String nifNie, String naf, String genero, LocalDate fechaNacimiento, String telefono, String pais, String provincia, String municipio, String codigoPostal) {
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.contrasenya = contrasenya;
        this.activado = activado;
        this.nifNie = nifNie;
        this.naf = naf;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.pais = pais;
        this.provincia = provincia;
        this.municipio = municipio;
        this.codigoPostal = codigoPostal;
    }

    // getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public boolean isActivado() {
        return activado;
    }

    public void setActivado(boolean activado) {
        this.activado = activado;
    }

    public String getNifNie() {
        return nifNie;
    }

    public void setNifNie(String nifNie) {
        this.nifNie = nifNie;
    }

    public String getNaf() {
        return naf;
    }

    public void setNaf(String naf) {
        this.naf = naf;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
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

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;
        if (id != null && usuario.id != null)
            return Objects.equals(id, usuario.id);
        return Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }


}
