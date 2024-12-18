package com.hampus.projektuppgiftapi.util;

import com.hampus.projektuppgiftapi.model.user.UserRoles;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtUtil {

    private static final long REFRESH_TOKEN_EXPIRATION = 604800000; //7 days
    private final SecretKey KEY;

    public JwtUtil(@Value("${jwt.key}") String secretKey) {
        this.KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }

    @Value("${jwt.expiration}")
    private long expirationTime; //1 day

    public String generateToken(String username, UserRoles roles){

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username, UserRoles roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
            return true;
        }catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody().getSubject();
    }

    public UserRoles extractRoles(String token) {
        Claims claim = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
        String role = claim.get("roles", String.class);
        return UserRoles.valueOf(role);
    }
}
