package maa.belajar_JWT_springboot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Autowired
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Mematikan CSRF protection (tidak disarankan tapi ikutin tutorial dulo aja)
                .authorizeHttpRequests((request) -> {
                    request
                            .requestMatchers("")
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .sessionManagement((sessionManagementCustomizer) -> {
                    // Membuat Session baru untuk setiap request baru
                    sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                // Masukin Filter Class yang kita bikin manual serta type Class yang disetting ke SecurityContextHolder

        return httpSecurity.build();
    }
}
