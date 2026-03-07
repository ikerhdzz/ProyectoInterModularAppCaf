package com.cafeapp.backend.seguridad;

import com.cafeapp.backend.modelo.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

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
            System.out.println(" Token expirado");
        } catch (MalformedJwtException e) {
            System.out.println(" Token malformado");
        } catch (SignatureException e) {
            System.out.println(" Firma JWT inválida");
        } catch (UnsupportedJwtException e) {
            System.out.println(" Token JWT no soportado");
        } catch (IllegalArgumentException e) {
            System.out.println(" Token vacío o inválido");
        }

        return false;
    }
}
