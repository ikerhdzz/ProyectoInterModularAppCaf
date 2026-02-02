package com.cafeapp.backend.seguridad;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    // En un proyecto real, esto debería ir en configuración externa
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_MS = 1000 * 60 * 60; // 1 hora

    public static String generarToken(String email) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static String obtenerEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

