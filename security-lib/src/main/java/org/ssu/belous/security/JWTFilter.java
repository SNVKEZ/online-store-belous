package org.ssu.belous.security;

import com.auth0.jwt.interfaces.Claim;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String HEADER_NAME = "Authorization";
    private static final String USERNAME_KEY = "username";
    private static final String ROLE_KEY = "role";

    private final JWTService jwtService;

    @Autowired
    public JWTFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HEADER_NAME);

        log.info("Authorization header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());

            try {
                Map<String, Claim> claims = jwtService.validTokenAndRetrieveSubject(token);

                Claim usernameClaim = claims.get(USERNAME_KEY);
                Claim roleClaim = claims.get(ROLE_KEY);

                if (usernameClaim == null || roleClaim == null) {
                    log.warn("JWT claims missing username or role");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                String username = usernameClaim.asString();
                String role = roleClaim.asString();

                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                log.warn("JWT verification failed: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}