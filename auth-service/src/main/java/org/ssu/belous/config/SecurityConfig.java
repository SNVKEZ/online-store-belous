package org.ssu.belous.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.ssu.belous.security.JWTFilter;
import org.ssu.belous.service.UserService;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTFilter jwtFilter;
    private final UserService userService;
    private static final String LOGIN_URL = "/api/auth/login/**";
    private static final String REGISTER_URL = "/api/auth/registration/**";

    @Autowired
    public SecurityConfig(JWTFilter jwtFilter, UserService userService) {
        this.jwtFilter = jwtFilter;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(auth -> {
                    auth.requestMatchers(LOGIN_URL, REGISTER_URL, "/")
                            .permitAll();
                    auth.anyRequest().authenticated();
                })
                .userDetailsService(userService)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(PasswordEncoder encoder, UserDetailsService userService) {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}
