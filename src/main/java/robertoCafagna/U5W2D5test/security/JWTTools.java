package robertoCafagna.U5W2D5test.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import robertoCafagna.U5W2D5test.entities.Dipendente;
import robertoCafagna.U5W2D5test.exceptions.UnauthorizedException;

import java.util.Date;

@Component
public class JWTTools {
    private final String secret;

    public JWTTools(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(Dipendente dipendente) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .subject(String.valueOf(dipendente.getId()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public void verifyToken(String token) {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        } catch (Exception ex) {
            throw new UnauthorizedException("problemi con il toker, tenta nuovamente l'accesso");
        }
    }

    public Long extractIdFromToken(String token) {
        return Long.parseLong(Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build().
                parseSignedClaims(token).
                getPayload().
                getSubject());
    }
}
