package pProject.pPro.securityConfig;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity.JwtSpec;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;


@Component
public class JWTUtil {

	private SecretKey secretKey;
	public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
		secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
		
	}
	public String getEmail(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }
    public String getCategory(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public Boolean isExpired(String token) {
    	
        Date expirationDate = Jwts.parser()
        		
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();


        return expirationDate.before(new Date());
    }
    public Long getRemainingTime(String token) {
    	JwtParser parser = Jwts.parser()
    	        .verifyWith(secretKey)  // Signature verification은 verifyWith 사용
    	        .build();

    	Claims claims = parser.parseSignedClaims(token).getPayload();

        Date expiration = claims.getExpiration();
        long now = System.currentTimeMillis();
        long diffMs = expiration.getTime() - now;

        return  Math.max(diffMs / 1000, 0);
    }


    
    public String createJwt(String category,String email, String role, Long expiredMs) {

        return Jwts.builder()
        		.claim("category", category)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
