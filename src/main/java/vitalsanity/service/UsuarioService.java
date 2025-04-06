package vitalsanity.service;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vitalsanity.dto.*;
import vitalsanity.model.CentroMedico;
import vitalsanity.model.Paciente;
import vitalsanity.model.TipoUsuario;
import vitalsanity.model.Usuario;
import vitalsanity.repository.TipoUsuarioRepository;
import vitalsanity.repository.UsuarioRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UsuarioService {

    Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public enum LoginStatus {LOGIN_OK, USER_NOT_FOUND, USER_DISABLED, ERROR_PASSWORD}

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Repositorio para tipos de usuario
    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    // Método auxiliar para convertir bytes a hexadecimal
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Método para generar el hash SHA3-256 de una contraseña
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA3-256 no está disponible", e);
        }
    }

    @Transactional(rollbackOn = Exception.class, dontRollbackOn = {})
    public LoginStatus login(String email, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (!usuario.isPresent()) {
            return LoginStatus.USER_NOT_FOUND;
        }

        String hashedPassword = hashPassword(password);

        if (!hashedPassword.equals(usuario.get().getContrasenya())) {
            return LoginStatus.ERROR_PASSWORD;
        } else if (!usuario.get().isActivado()) {
            return LoginStatus.USER_DISABLED;
        } else {
            return LoginStatus.LOGIN_OK;
        }
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public UsuarioData findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null) return null;
        else {
            return modelMapper.map(usuario, UsuarioData.class);
        }
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public UsuarioData findById(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) return null;
        else {
            return modelMapper.map(usuario, UsuarioData.class);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public UsuarioData registrarPaciente(RegistroData registroData) {
        // Verificar que las contrasenyas sean iguales
        if (!registroData.getContrasenya().equals(registroData.getConfirmarContrasenya())) {
            throw new IllegalArgumentException("Las contrasenyas no coinciden");
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setIdentificador(java.util.UUID.randomUUID().toString());
        usuario.setEmail(registroData.getEmail());
        usuario.setNombreCompleto(registroData.getNombreCompleto());
        usuario.setContrasenya(hashPassword(registroData.getContrasenya()));
        usuario.setActivado(true);
        usuario.setNifNie(registroData.getNifNie());
        usuario.setTelefono(registroData.getMovil());
        usuario.setPais(registroData.getPais());
        // Provincia, municipio y codigoPostal se ignoran

        // Asignar tipo de usuario paciente (se asume que existe un TipoUsuario con tipo "paciente")
        TipoUsuario tipoPaciente = tipoUsuarioRepository.findByTipo("paciente")
                .orElseThrow(() -> new IllegalStateException("Tipo de usuario 'paciente' no encontrado"));
        usuario.setTipo(tipoPaciente);

        // Crear entidad Paciente
        Paciente paciente = new Paciente();
        paciente.setGenero(registroData.getGenero());
        paciente.setFechaNacimiento(registroData.getFechaNacimiento());
        // Establecer relacion bidireccional
        usuario.setPaciente(paciente);
        paciente.setUsuario(usuario);

        // Guardar usuario (se guardara el paciente por cascada)
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Mapear a UsuarioData y retornar
        UsuarioData usuarioData = modelMapper.map(savedUsuario, UsuarioData.class);
        return usuarioData;
    }

    // Metodo privado para generar contrasenya segura
    private String generateSecurePassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*!";
        java.security.SecureRandom random = new java.security.SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    @Transactional(rollbackOn = Exception.class)
    public UsuarioData registrarCentroMedico(RegistroCentroMedicoData data) {
        // Generar contrasenya segura
        String generatedPassword = generateSecurePassword(12);

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setIdentificador(java.util.UUID.randomUUID().toString());
        usuario.setEmail(data.getEmail());
        usuario.setNombreCompleto(data.getNombreCompleto());
        usuario.setContrasenya(hashPassword(generatedPassword));
        usuario.setActivado(true);
        usuario.setPrimerAcceso(true);
        usuario.setNifNie(data.getNifNie());
        usuario.setTelefono(data.getMovil());
        usuario.setPais("Espana");
        usuario.setProvincia(data.getProvincia());
        usuario.setMunicipio(data.getMunicipio());
        usuario.setCodigoPostal(data.getCodigoPostal());

        // Asignar tipo de usuario centro-medico
        TipoUsuario tipoCentroMedico = tipoUsuarioRepository.findById(2L)
                .orElseThrow(() -> new IllegalStateException("Tipo de usuario correspondiente a los Centros Médicos no encontrado"));
        usuario.setTipo(tipoCentroMedico);

        // Crear entidad CentroMedico
        CentroMedico centroMedico = new CentroMedico();
        centroMedico.setIban(data.getIban());
        centroMedico.setDireccion(data.getDireccion());
        // Establecer relacion bidireccional
        usuario.setCentroMedico(centroMedico);
        centroMedico.setUsuario(usuario);

        // Guardar usuario (centroMedico se guarda por cascada)
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Enviar email con la contrasenya generada
        emailService.send(data.getEmail(), "Registro Centro Medico",
                "Se ha registrado su centro medico. Su contrasenya de acceso es: " + generatedPassword +
                        ". Cuando inicie sesion por primera vez, debera cambiarla por una nueva.");

        // Mapear a UsuarioData y retornar
        UsuarioData usuarioData = modelMapper.map(savedUsuario, UsuarioData.class);
        return usuarioData;
    }

    @Transactional(rollbackOn = Exception.class)
    public UsuarioData actualizarContrasenya(Long id, ActualizarContrasenyaData data) {
        if (data.getContrasenya() == null || data.getContrasenya().length() < 8) {
            throw new IllegalArgumentException("La contrasenya debe tener al menos 8 caracteres");
        }
        if (!data.getContrasenya().equals(data.getConfirmarContrasenya())) {
            throw new IllegalArgumentException("Las contrasenyas no coinciden");
        }
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        usuario.setContrasenya(hashPassword(data.getContrasenya()));
        usuario.setPrimerAcceso(false);
        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return modelMapper.map(updatedUsuario, UsuarioData.class);
    }

    @Transactional(rollbackOn = Exception.class)
    public UsuarioData actualizarDatosResidencia(Long id, ResidenciaData data) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        usuario.setProvincia(data.getProvincia());
        usuario.setMunicipio(data.getMunicipio());
        usuario.setCodigoPostal(data.getCodigoPostal());
        usuario.setPrimerAcceso(false);
        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return modelMapper.map(updatedUsuario, UsuarioData.class);
    }


}
