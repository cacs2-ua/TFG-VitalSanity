package vitalsanity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http)
            throws Exception {

        http
                // Configuración de CSRF:
                // 1. Ignoramos CSRF en /h2-console/**
                // 2. Usamos CookieCsrfTokenRepository para el resto
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )

                // Permitir iframes en la misma ruta (necesario para la consola H2)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                // Gestión de sesiones: permitimos la creación de sesiones cuando sea necesario
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                // Configuración de Remember Me:
                // Se genera una cookie persistente para recordar al usuario
                .rememberMe(rememberMe -> rememberMe
                        .key("m3JW/0pp2Qmu6W/5y3Xs8XIQrDwVMxH+FnZt0CTnYsU=")                // Clave para encriptar el token
                        .tokenValiditySeconds(86400)          // Tiempo de validez del token (1 día)
                )

                // Reglas de autorización: se permite el acceso a todas las solicitudes
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        // Construye el SecurityFilterChain
        return http.build();
    }
}
