package com.cafeapp.backend.seguridad;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cafeapp.backend.modelo.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

/**
 * Utilidad para generar, validar y extraer información de tokens JWT.
 *
 * Funciones:
 * - Generar tokens para usuarios autenticados.
 * - Validar tokens recibidos.
 * - Extraer email, rol y otros datos desde el token.
 * - Manejo seguro de excepciones JWT.
 */
@Component
public class JwtUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    /** Clave secreta definida en application.properties */
    @Value("${jwt.secret}")
    private String secret;

    /** Tiempo de expiración del token en milisegundos */
    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    /**
     * Obtiene la clave de firma para JWT usando HS256.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token JWT para un usuario autenticado.
     *
     * @param usuario Usuario autenticado
     * @return Token JWT firmado
     */
    public String generarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("rol", usuario.getRol().getNombre())
                .claim("centro", usuario.getCentro() != null ? usuario.getCentro().getId() : null)
                .claim("id", usuario.getId())
                .claim("nombre", usuario.getNombre())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Obtiene los claims del token ya validado.
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrae el email (subject) del token.
     */
    public String obtenerEmail(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extrae el rol del token.
     */
    public String obtenerRol(String token) {
        return (String) getClaims(token).get("rol");
    }
    /**
     * Valida si un token es correcto y no ha expirado.
     *
     * @param token Token JWT
     * @return true si es válido, false si no
     */
    public boolean esValido(String token) {
        try {
            getClaims(token);
            return true;

        } catch (ExpiredJwtException e) {
            logger.warn("Intento de usar token expirado");
            return false;
        } catch (MalformedJwtException e) {
            logger.warn("Intento de usar token malformado");
            return false;
        } catch (SignatureException e) {
            logger.warn("Intento de usar token con firma inválida");
            return false;
        } catch (UnsupportedJwtException e) {
            logger.warn("Token JWT no soportado");
            return false;
        } catch (IllegalArgumentException e) {
            logger.warn("Token vacío o inválido");
            return false;
        } catch (Exception e) {
            logger.error("Error inesperado validando JWT", e);
            return false;
        }
    }
}
