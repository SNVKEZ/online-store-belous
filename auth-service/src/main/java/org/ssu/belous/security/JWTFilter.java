package org.ssu.belous.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.ssu.belous.exception.NotAcceptableHeaderException;
import org.ssu.belous.service.UserService;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String HEADER_NAME = "Authorization";
    private static final String USERNAME_KEY = "username";
    private final JWTService jwtService;
    private final UserService userService;

    @Autowired
    public JWTFilter(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HEADER_NAME);
        if (!StringUtils.isEmpty(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
            String jwt = authHeader.substring(BEARER_PREFIX.length());
            UserDetails userDetails = getUserDetailsFromHeader(authHeader);
            setSecurityContext(userDetails, request, jwt);
        }
        filterChain.doFilter(request, response);
    }

    private UserDetails getUserDetailsFromHeader(String authHeader) {
        String jwt = authHeader.substring(BEARER_PREFIX.length());
        String username = jwtService.validTokenAndRetrieveSubject(jwt).get(USERNAME_KEY).asString();

        if (!StringUtils.isEmpty(username) &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            return userService.loadUserByUsername(username);
        } else {
            throw new NotAcceptableHeaderException("В заголовке нет информации о пользователе");
        }
    }

    private void setSecurityContext(UserDetails userDetails, HttpServletRequest request,
                                    String jwt) {
        if (!jwtService.validTokenAndRetrieveSubject(jwt).isEmpty()) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);
        }
    }
}