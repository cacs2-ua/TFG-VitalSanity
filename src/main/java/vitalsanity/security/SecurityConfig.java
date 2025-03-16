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

    // Cadena de filtros para autenticacion con certificado en el endpoint /login/certificate
    @Bean
    @Order(1)
    public SecurityFilterChain certificateFilterChain(HttpSecurity http) throws Exception {
        // Se crea el filtro x509 personalizado
        X509AuthenticationFilter x509Filter = new X509AuthenticationFilter();
        AuthenticationManager authManager = http.getSharedObject(AuthenticationManager.class);
        x509Filter.setAuthenticationManager(authManager);
        x509Filter.setAuthenticationSuccessHandler(successHandler);

        http
                .securityMatcher(new AntPathRequestMatcher("/login/certificate"))
                .x509(x -> x
                        .subjectPrincipalRegex(".*-\\s*(?:NIF:)?([0-9]{8}[A-Z])")
                        .userDetailsService(certificateUserDetailsService)
                )
                // Se agrega el filtro x509 personalizado en la cadena de filtros
                .addFilterAfter(x509Filter, X509AuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
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
