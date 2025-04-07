package vitalsanity.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import vitalsanity.authentication.ManagerUserSession;
import vitalsanity.model.Usuario;
import vitalsanity.repository.UsuarioRepository;

@Component
public class CertificateAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ManagerUserSession managerUserSession;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String nif = authentication.getName();
        List<Usuario> usuarios = usuarioRepository.findByNifNie(nif);
        if (usuarios == null || usuarios.isEmpty()) {
            response.sendRedirect("/login?error=usuario_no_encontrado");
            return;
        }
        Usuario usuario = usuarios.get(0);
        managerUserSession.logearUsuario(usuario.getId());
        if (usuario.getTipo() != null) {
            Long tipoId = usuario.getTipo().getId();
            if (tipoId == 1) {
                response.sendRedirect("https://192.168.133.218:8058/vital-sanity/api/admin/check");
                return;
            }
            if (tipoId == 2) {
                response.sendRedirect("https://192.168.133.218:8058/vital-sanity/api/centro-medico/check");
                return;
            }
            if (tipoId == 3) {
                response.sendRedirect("https://192.168.133.218:8058/vital-sanity/api/profesional-medico/5/pacientes-que-han-autorizado");
                return;
            }
            if (tipoId == 4) {
                response.sendRedirect("https://192.168.133.218:8058/vital-sanity/api/paciente/check");
                return;
            }
        }
        response.sendRedirect("https://192.168.133.218:8058/vital-sanity/api/auth/check");
    }
}