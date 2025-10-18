package com.example.SpringOauth.Utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtils {
    private final String SECRET = "supersecretkey1234567890ggffvfr1234567890ggffvfr";


    public  String generateToken(String name,String email){

        return Jwts.builder()
                .setSubject(email) // Use email as the subject
                .claim("name", name) // Add name as a claim
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public  String validateAndExtractEmail(String token) {
        return Jwts
                .parser()
                .setSigningKey(SECRET.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
