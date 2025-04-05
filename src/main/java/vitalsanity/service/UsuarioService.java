package vitalsanity.service;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vitalsanity.dto.RegistroData;
import vitalsanity.dto.UsuarioData;
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

}
