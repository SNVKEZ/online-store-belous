package org.ssu.belous.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.ssu.belous.security.JWTFilter;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        if ("docker".equalsIgnoreCase(activeProfile)) {
            // ⚡ В докере разрешаем всё, т.к. фронт и бек могут быть в разных контейнерах
            config.setAllowedOriginPatterns(List.of("*"));
            System.out.println(">>> CORS configured for Docker: * <<<");
        } else {
            // ⚡ Локальный запуск: только фронт
            config.setAllowedOrigins(List.of("http://localhost:3000"));
            System.out.println(">>> CORS configured for Localhost: 3000 <<<");
        }

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        System.out.println(">>> SecurityFilterChain LOADED <<<");
        return http.build();
    }
}
