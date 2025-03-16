package vitalsanity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Autowired
    private CertificateAuthenticationSuccessHandler successHandler;

    @Autowired
    private CertificateUserDetailsService certificateUserDetailsService;

    // Cadena de filtros para autenticacion con certificado en /login/certificate
    @Bean
    @Order(1)
    public SecurityFilterChain certificateFilterChain(HttpSecurity http) throws Exception {
        X509AuthenticationFilter x509Filter = new X509AuthenticationFilter();
        AuthenticationManager authManager = http.getSharedObject(AuthenticationManager.class);
        x509Filter.setAuthenticationManager(authManager);
        x509Filter.setAuthenticationSuccessHandler(successHandler);

        // IMPORTANTE: si la autenticacion con certificado falla (por no presentar uno)
        // queremos CONTINUAR la cadena para no devolver 403
        x509Filter.setContinueFilterChainOnUnsuccessfulAuthentication(true);

        http
                // Solo aplica esta configuracion a la ruta /login/certificate
                .securityMatcher(new AntPathRequestMatcher("/login/certificate"))
                .x509(x -> x
                        // Regex para extraer el NIF, soportando FNMT y ACCV
                        .subjectPrincipalRegex(".*-\\s*(?:NIF:)?([0-9]{8}[A-Z])")
                        .userDetailsService(certificateUserDetailsService)
                )
                // Se agrega el filtro x509 personalizado
                .addFilterAfter(x509Filter, X509AuthenticationFilter.class)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .csrf(csrf -> csrf.disable())

                // Permitimos pasar por /login/certificate, incluso si no hay certificado,
                // para que no de 403 y podamos seguir con el login normal en caso de no presentar certificado
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    // Cadena de filtros por defecto para el resto de endpoints
    @Bean
    @Order(2)
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .rememberMe(rememberMe -> rememberMe
                        .key("m3JW/0pp2Qmu6W/5y3Xs8XIQrDwVMxH+FnZt0CTnYsU=")
                        .tokenValiditySeconds(86400)
                )
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }
}
