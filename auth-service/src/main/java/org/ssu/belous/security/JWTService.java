package org.ssu.belous.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.ssu.belous.security.helpers.JWTVerifier;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
public class JWTService {

    @Value("${jwt.token.liveTime}")
    private final int timeLive;

    private final static String USERNAME_KEY = "username";
    private final static String ROLE_KEY = "roles";
    private final static String SUBJECT = "Userdetails";
    private final static String ISSUE = "ssu_belous";
    private final JWTVerifier jwtVerifier;

    private final Algorithm algorithm;

    @Autowired
    public JWTService(JWTVerifier jwtVerifier, @Value("${jwt.token.liveTime}") int timeLive,
                      @Value("${jwt.secret}") String secret, @Value("${jwt.algorithm}") String algorithmName) {
        this.jwtVerifier = jwtVerifier;
        this.timeLive = timeLive;
        this.algorithm = initAlgorithm(Objects.requireNonNull(secret, "Secret не может быть null"), algorithmName);
    }

    private Algorithm initAlgorithm(String secret, String algorithmName) {
        try {
            Method method = Algorithm.class.getMethod(Objects.requireNonNull(algorithmName, "Название алгоритма кодирования JWT-токена не может быть null"), String.class);
            return (Algorithm) method.invoke(null, secret);
        } catch (Exception e) {
            throw new IllegalArgumentException("Алгоритм не существует: " + algorithmName, e);
        }
    }

    public String generateToken(String username, String roles) {
        return JWT.create()
                .withSubject(Objects.requireNonNull(SUBJECT, "Ключ SUBJECT не может быть null"))
                .withClaim(Objects.requireNonNull(USERNAME_KEY, "Ключ USERNAME_KEY не может быть null"), Objects.requireNonNull(username, "Username не может быть null"))
                .withClaim(Objects.requireNonNull(ROLE_KEY, "Ключ ROLE_KEY не может быть null"), Objects.requireNonNull(roles, "Список ролей не может быть null"))
                .withIssuedAt(new Date())
                .withIssuer(Objects.requireNonNull(ISSUE, "Ключ ISSUE не может быть null"))
                .withExpiresAt(Instant.now().plus(timeLive, ChronoUnit.MINUTES))
                .sign(algorithm);
    }

    public Map<String, Claim> validTokenAndRetrieveSubject(String token) throws JWTVerificationException {
        return jwtVerifier.verifier(token, SUBJECT, ISSUE).getClaims();
    }
}