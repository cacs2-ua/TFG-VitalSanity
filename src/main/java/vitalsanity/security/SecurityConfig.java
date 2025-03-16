package vitalsanity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.x509.SubjectDnX509PrincipalExtractor;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;

@Configuration
public class SecurityConfig {

    @Autowired
    private CertificateAuthenticationSuccessHandler successHandler;

    @Autowired
    private CertificateUserDetailsService certificateUserDetailsService;

    @Bean
    @Order(1)
    public SecurityFilterChain certificateFilterChain(HttpSecurity http) throws Exception {
        SubjectDnX509PrincipalExtractor principalExtractor = new SubjectDnX509PrincipalExtractor();
        principalExtractor.setSubjectDnRegex(".*-\\s*(?:NIF:)?([0-9]{8}[A-Z])");  // Método corregido

        X509AuthenticationFilter x509Filter = new X509AuthenticationFilter();
        x509Filter.setPrincipalExtractor(principalExtractor);
        x509Filter.setAuthenticationSuccessHandler(successHandler);
        x509Filter.setContinueFilterChainOnUnsuccessfulAuthentication(true);

        PreAuthenticatedAuthenticationProvider authProvider = new PreAuthenticatedAuthenticationProvider();
        authProvider.setPreAuthenticatedUserDetailsService(
                new UserDetailsByNameServiceWrapper<>(certificateUserDetailsService)
        );

        AuthenticationManager authManager = new ProviderManager(authProvider);
        x509Filter.setAuthenticationManager(authManager);

        http
                .securityMatcher(new AntPathRequestMatcher("/login/certificate"))
                .addFilter(x509Filter)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

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