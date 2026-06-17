package cl.duoc.innovatech.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF ya que usamos JWT y la app es stateless
            .csrf(csrf -> csrf.disable())
            
            // Configurar la gestión de sesiones como Stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Autorizar peticiones
            .authorizeHttpRequests(auth -> auth
                // Permite que el Gateway consuma los endpoints de la API v2 del BFF
                .requestMatchers("/api/v2/bff/**").permitAll()
                // Cualquier otra ruta requiere autenticación por defecto
                .anyRequest().authenticated()
            );

        return http.build();
    }
}