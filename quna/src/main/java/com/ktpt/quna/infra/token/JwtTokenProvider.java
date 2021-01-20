package com.ktpt.quna.infra.token;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.ktpt.quna.application.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
    private static final String SECRET_KEY = "ktktktktktktktktktkt";
    private static final long VALIDATION_INTERVAL = 3600000;

    public String createToken(Long memberId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDATION_INTERVAL);

        return Jwts.builder()
                .claim("memberId", memberId)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Long getMemberId(String token) {
        return getClaims(token).get("memberId", Long.class);
    }

    public Claims getClaims(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return claims.getBody();
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("유효하지 않은 토큰");
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("만료된 토큰.");
        }
    }
}
