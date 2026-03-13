# 🔒 Análisis de Seguridad y Bugs - CafeAppBackend
## Reporte de Senior Developer (Backend + Ciberseguridad)

**Fecha:** 13 Marzo 2026  
**Severidad Crítica:** 5 issues  
**Severidad Alta:** 8 issues  
**Severidad Media:** 6 issues  

---

## 🚨 CRÍTICOS (Priority P0)

### 1. **Exposición de Stripe Secret Key en Logs y Potencial Hardcoding**
**Archivo:** `PaymentController.java`  
**Severidad:** CRÍTICA  
**Riesgo:** Acceso no autorizado a pagos, fraude, robo de fondos

```java
@Value("${stripe.api.key}")
private String stripeApiKey;
...
Stripe.apiKey = stripeApiKey;
```

**Problemas:**
- La "stripe.api.key" debería ser la **Publishable Key** (pública), no la Secret Key
- Si alguien obtiene la Secret Key, puede acceder a TODOS los pagos de la cuenta
- El logging de Stripe podría exponerla si hay algún error

**Solución:**
```java
// Solo usar Publishable Key en el backend (para validaciones)
@Value("${stripe.publishable.key}")
private String stripePublishableKey;

// La Secret Key debe usarse SOLO server-side y NUNCA loguear
@Value("${stripe.secret.key}")
private String stripeSecretKey;

@PostMapping("/crear-intento")
public Map<String, String> crearIntentoPago(@Valid @RequestBody PaymentIntentRequest request) throws Exception {
    // NUNCA setear Stripe.apiKey aquí - usar el constructor de Stripe
    PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(request.getTotalEnCentimos())
            .setCurrency("eur")
            .setAutomaticPaymentMethods(...)
            .build();

    PaymentIntent paymentIntent = PaymentIntent.create(params, 
        RequestOptions.builder().setApiKey(stripeSecretKey).build());
    
    return Map.of("clientSecret", paymentIntent.getClientSecret());
}
```

---

### 2. **Escalamiento de Privilegios en Registro - Usuario Puede Seleccionar Rol**
**Archivo:** `AuthController.java`, `RegistroRequest.java`  
**Severidad:** CRÍTICA  
**Riesgo:** Usuarios podrían auto-asignarse roles de administrador

**Problema:**
```java
// En RegistroRequest
public record RegistroRequest(
    // ...
    @NotNull(message = "El ID del rol es obligatorio")
    Long rolId,  // ❌ EL USUARIO DEFINE SU PROPIO ROL
    // ...
)
```

```java
// En AuthController
@PostMapping("/register")
public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody RegistroRequest request) {
    Rol rol = rolRepository.findById(request.rolId())  // ❌ LO TOMA DIRECTAMENTE
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    usuario.setRol(rol);  // ❌ PUEDE SER ADMIN
}
```

**Impacto:**
- Cualquier usuario podría registrarse como administrador
- Acceso total a todas las funciones de administración

**Solución:**
```java
// 1. Remover rolId de RegistroRequest
public record RegistroRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,
    
    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    String email,
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "Mínimo 8 caracteres")
    String password,
    
    @NotBlank(message = "El DNI es obligatorio")
    String dni,
    
    @NotBlank(message = "La clase es obligatoria")
    String clase,
    
    @NotNull(message = "El ID del curso es obligatorio")
    Long cursoId
    
    // ❌ REMOVER rolId - el rol se asigna automáticamente como USUARIO
)

// 2. Asignar rol automático a usuarios nuevos
@PostMapping("/register")
public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody RegistroRequest request) {
    Usuario usuario = new Usuario();
    usuario.setNombre(request.nombre());
    usuario.setEmail(request.email());
    usuario.setPassword(passwordEncoder.encode(request.password()));
    usuario.setDni(request.dni());
    usuario.setClase(request.clase());

    Curso curso = cursoRepository.findById(request.cursoId())
            .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

    // ✅ ROL POR DEFECTO: USUARIO (no puede ser modificado)
    Rol rolPorDefecto = rolRepository.findByNombre("USUARIO")
            .orElseThrow(() -> new RuntimeException("Rol USUARIO no configurado"));

    usuario.setCurso(curso);
    usuario.setRol(rolPorDefecto);

    Usuario guardado = usuarioService.guardar(usuario);
    return ResponseEntity.ok(new UsuarioResponse(guardado));
}
```

---

### 3. **Sin Validación de Contraseña Fuerte**
**Archivo:** `AuthController.java`, `RegistroRequest.java`  
**Severidad:** CRÍTICA  
**Riesgo:** Contraseñas débiles, ataques de fuerza bruta exitosos

**Problema:**
```java
public record RegistroRequest(
    @NotBlank(message = "La contraseña es obligatoria")
    String password,  // ❌ SIN VALIDACIÓN DE FORTALEZA
)
```

**Solución:**
```java
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Pattern;

public record RegistroRequest(
    // ... otros campos
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
        message = "La contraseña debe contener mayúsculas, minúsculas, números y caracteres especiales"
    )
    String password
)
```

**Agregar CustomValidator (opcional pero recomendado):**
```java
@Component
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.length() < 8) return false;
        
        // Verificar que no sea contraseña común
        Set<String> commonPasswords = Set.of("password", "123456", "qwerty", "admin");
        if (commonPasswords.contains(password.toLowerCase())) return false;
        
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).*$");
    }
}
```

---

### 4. **Inyección en PaymentController - Map Sin Validación**
**Archivo:** `PaymentController.java`  
**Severidad:** CRÍTICA  
**Riesgo:** Inyección de datos maliciosos, bypass de validaciones

**Problema:**
```java
@PostMapping("/crear-intento")
public Map<String, String> crearIntentoPago(
    @Valid @RequestBody Map<String, Object> datos  // ❌ ACEPTAR CUALQUIER MAP
) throws Exception {
    
    List<Map<String, Object>> items = (List<Map<String, Object>>) datos.get("items");
    
    for (Map<String, Object> item : items) {
        Long id = Long.parseLong(item.get("id").toString());  // ❌ PARSING INSEGURO
        int cantidad = Integer.parseInt(item.get("cantidad").toString());  // ❌ CASTING SIN VALIDAR
        
        // ... calcular total sin validar precios
    }
}
```

**Solución:**
```java
// 1. Crear DTOs para validación
public record PaymentIntentRequest(
    @NotNull(message = "Items es requerido")
    @NotEmpty(message = "Debe haber al menos un item")
    List<PaymentItemRequest> items
) {}

public record PaymentItemRequest(
    @NotNull(message = "El ID es requerido")
    Long productoId,
    
    @Min(value = 1, message = "Cantidad mínima es 1")
    @Max(value = 999, message = "Cantidad máxima es 999")
    Integer cantidad
) {}

// 2. Usar DTO en el controlador
@PostMapping("/crear-intento")
public Map<String, String> crearIntentoPago(
    @Valid @RequestBody PaymentIntentRequest request
) throws Exception {
    
    // Validar stock y precios antes de crear el PaymentIntent
    long totalEnCentimos = 0;
    
    for (PaymentItemRequest item : request.items()) {
        Producto producto = productoRepository.findById(item.productoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // ✅ Validar stock
        if (!validarStock(producto, item.cantidad())) {
            throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
        }
        
        // ✅ Usar precio verificado desde BD (no del cliente)
        long precioEnCentimos = (long) (producto.getPrecioBase() * 100);
        totalEnCentimos += precioEnCentimos * item.cantidad();
    }
    
    // ... crear PaymentIntent
}
```

---

### 5. **Sin Validación de Propiedad del Carrito en Pagos**
**Archivo:** `PaymentController.java`, `CarritoController.java`  
**Severidad:** CRÍTICA  
**Riesgo:** Usuario A puede pagar con el carrito de Usuario B

**Problema:**
```java
// El PaymentController no verifica que el carrito pertenezca al usuario autenticado
// El usuario podría enviar un carritoId de otro usuario
```

**Solución:**
```java
@PostMapping("/crear-intento")
public Map<String, String> crearIntentoPago(
    @Valid @RequestBody PaymentIntentRequest request
) throws Exception {
    
    // ✅ Obtener usuario autenticado
    String emailActual = SecurityContextHolder.getContext()
            .getAuthentication().getName();
    Usuario usuarioActual = usuarioService.obtenerPorEmail(emailActual);
    
    // ✅ Obtener su carrito
    Carrito carrito = carritoService.obtenerCarritoPorUsuario(usuarioActual);
    
    // ✅ Validar que los productos en la request coincidan con su carrito
    if (!validarCarritoDelUsuario(carrito, request)) {
        throw new RuntimeException("El carrito no pertenece al usuario autenticado");
    }
    
    // ... proceder con pago
}
```

---

## ⚠️ ALTOS (Priority P1)

### 6. **Logging con System.out.println en Producción**
**Artchivo:** `JwtUtil.java`  
**Severidad:** ALTA  
**Riesgo:** Información sensible en logs, difícil de auditar

**Problema:**
```java
public boolean esValido(String token) {
    try {
        getClaims(token);
        return true;
    } catch (ExpiredJwtException e) {
        System.out.println(" Token expirado");  // ❌ MAL
    } catch (MalformedJwtException e) {
        System.out.println(" Token malformado");  // ❌ MAL
    } catch (SignatureException e) {
        System.out.println(" Firma JWT inválida");  // ❌ MAL
    }
}
```

**Solución:**
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    
    public boolean esValido(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("Intento con token expirado");
            return false;
        } catch (MalformedJwtException e) {
            logger.warn("Intento con token malformado");
            return false;
        } catch (SignatureException e) {
            logger.warn("Intento con firma JWT inválida");
            return false;
        } catch (Exception e) {
            logger.error("Error inesperado validando JWT", e);
            return false;
        }
    }
}
```

---

### 7. **Webhook Sin Validación Adecuada de Errores**
**Archivo:** `StripeWebhookService.java`  
**Severidad:** ALTA  
**Riesgo:** Pérdida de eventos de pago, inconsistencia de datos

**Problema:**
```java
public void procesarEvento(String payload, String sigHeader) {
    Event event;
    try {
        event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
    } catch (SignatureVerificationException e) {
        throw new RuntimeException("Firma de webhook inválida");  // ❌ LANZAR NO LOGUEAR
    }
    
    // ❌ Sin logging de eventos recibidos
    // ❌ Sin reintentos en caso de fallo
    // ❌ Sin confirmación de procesamiento
}
```

**Solución:**
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StripeWebhookService {
    
    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookService.class);
    
    public void procesarEvento(String payload, String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            logger.error("Firma de webhook inválida - evento rechazado", e);
            throw new InvalidWebhookSignatureException("Firma inválida");
        }
        
        logger.info("Webhook recibido: {}", event.getType());
        
        try {
            switch(event.getType()) {
                case "payment_intent.succeeded":
                    procesarPagoExitoso(event);
                    break;
                case "payment_intent.payment_failed":
                    procesarPagoFallido(event);
                    break;
                case "charge.refunded":
                    procesarReembolso(event);
                    break;
                default:
                    logger.debug("Evento no manejado: {}", event.getType());
            }
            logger.info("Webhook procesado exitosamente: {}", event.getId());
        } catch (Exception e) {
            logger.error("Error procesando webhook: {}", event.getId(), e);
            throw new RuntimeException("Error procesando webhook", e);
        }
    }
    
    private void procesarPagoExitoso(Event event) {
        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("PaymentIntent no encontrado"));
        
        String pedidoIdStr = intent.getMetadata().get("pedidoId");
        if (pedidoIdStr == null) {
            logger.warn("Pago exitoso sin pedidoId en metadata: {}", intent.getId());
            return;
        }
        
        try {
            Long pedidoId = Long.valueOf(pedidoIdStr);
            Pedido pedido = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
            
            pedidoStateService.cambiarEstado(pedidoId, "PREPARANDO");
            logger.info("Pedido {} marcado como PREPARANDO tras pago exitoso", pedidoId);
        } catch (NumberFormatException e) {
            logger.error("pedidoId no es un número válido: {}", pedidoIdStr, e);
        }
    }
}
```

---

### 8. **GlobalExceptionHandler Expone Información Sensible**
**Archivo:** `GlobalExceptionHandler.java`  
**Severidad:** ALTA  
**Riesgo:** Revelación de detalles internos de la aplicación a atacantes

**Problema:**
```java
@ExceptionHandler(RuntimeException.class)
public ResponseEntity<ApiError> manejarRuntime(
    RuntimeException ex,
    HttpServletRequest request
) {
    // ❌ EXPONE EL MENSAJE COMPLETO DE LA EXCEPCIÓN
    ApiError apiError = new ApiError(
        HttpStatus.BAD_REQUEST.value(),
        "Error de negocio",
        ex.getMessage(),  // ❌ PELIGROSO EN PRODUCCIÓN
        request.getRequestURI(),
        LocalDateTime.now()
    );
}
```

**Solución:**
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @Value("${app.environment:production}")
    private String environment;
    
    // Excepciones conocidas con mensajes seguros
    private static final Map<String, String> SAFE_MESSAGES = Map.ofEntries(
        Map.entry("Usuario no encontrado", "El usuario solicitado no existe"),
        Map.entry("Producto no encontrado", "El producto solicitado no existe"),
        Map.entry("Stock insuficiente", "Stock insuficiente para este producto"),
        Map.entry("Rol no encontrado", "Rol no configurado en el sistema"),
        Map.entry("Curso no encontrado", "El curso solicitado no existe")
    );
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> manejarRuntime(
        RuntimeException ex,
        HttpServletRequest request
    ) {
        logger.error("RuntimeException en {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        
        // Buscar mensaje seguro
        String mensajeSeguro = SAFE_MESSAGES.getOrDefault(
            ex.getMessage(),
            "Ha ocurrido un error. Por favor, intente más tarde."
        );
        
        // En desarrollo, mostrar más detalles
        if ("development".equals(environment)) {
            mensajeSeguro = ex.getMessage();
        }
        
        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Error de negocio",
            mensajeSeguro,
            request.getRequestURI(),
            LocalDateTime.now()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarGeneral(
        Exception ex,
        HttpServletRequest request
    ) {
        logger.error("Exception no manejada en {}", request.getRequestURI(), ex);
        
        ApiError apiError = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Error interno",
            "Ha ocurrido un error inesperado. El equipo de soporte ha sido notificado.",
            request.getRequestURI(),
            LocalDateTime.now()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}
```

---

### 9. **Sin Rate Limiting en Endpoints Públicos**
**Archivo:** `SecurityConfig.java`, `AuthController.java`  
**Severidad:** ALTA  
**Riesgo:** Ataque de fuerza bruta en login, DDoS

**Solución (Agregar a pom.xml):**
```xml
<dependency>
    <groupId>io.github.bucket4j</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>7.6.0</version>
</dependency>
```

**Interceptor de Rate Limiting:**
```java
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String clientId = request.getRemoteAddr();
        String endpoint = request.getRequestURI();
        
        // Solo aplicar rate limiting a endpoints públicos sensibles
        if (endpoint.startsWith("/auth/login")) {
            if (!isAllowed(clientId, "login")) {
                response.setStatus(429);  // Too Many Requests
                response.getWriter().write("{\"error\": \"Demasiadas peticiones. Intenta más tarde.\"}");
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    private boolean isAllowed(String clientId, String bucket) {
        String key = bucket + ":" + clientId;
        
        Bucket b = buckets.computeIfAbsent(key, k -> {
            // 5 intentos por minuto
            Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));
            return Bucket4j.builder()
                    .addLimit(limit)
                    .build();
        });
        
        return b.tryConsume(1);
    }
}
```

**Registrar en SecurityConfig:**
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http,
                                       RateLimitingFilter rateLimitingFilter) throws Exception {
    http.addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class);
    // ...
}
```

---

### 10. **No Hay Validación de Capacidad Antes de Crear PaymentIntent**
**Archivo:** `PaymentController.java`  
**Severidad:** ALTA  
**Riesgo:** Aceptar pagos cuando el stock es insuficiente

**Solución:** (Ver en sección de CRÍTICOS #4)

---

### 11. **JWT Sin Expiración Explícita en Logout**
**Archivo:** `AuthController.java`, `JwtUtil.java`  
**Severidad:** ALTA  
**Riesgo:** Token puede ser usado después de logout si alguien lo intercepta

**Solución:**
```java
// 1. Crear tabla de blacklist en BD
@Entity
@Table(name = "token_blacklist")
public class TokenBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String token;
    
    @Column(nullable = false)
    private LocalDateTime expirationTime;
}

// 2. Servicio de blacklist
@Service
public class TokenBlacklistService {
    private final TokenBlacklistRepository tokenRepository;
    
    public void blacklistToken(String token, LocalDateTime expirationTime) {
        TokenBlacklist blacklisted = new TokenBlacklist();
        blacklisted.setToken(token);
        blacklisted.setExpirationTime(expirationTime);
        tokenRepository.save(blacklisted);
    }
    
    public boolean isBlacklisted(String token) {
        return tokenRepository.existsByToken(token);
    }
}

// 3. Agregar endpoint de logout
@PostMapping("/logout")
public ResponseEntity<Void> logout(HttpServletRequest request) {
    String token = request.getHeader("Authorization").substring(7);
    LocalDateTime expiration = jwtUtil.obtenerExpiration(token);
    tokenBlacklistService.blacklistToken(token, expiration);
    return ResponseEntity.ok().build();
}

// 4. Validar en JwtFilter
@Override
protected void doFilterInternal(...) {
    String token = header.substring(7);
    
    // Verificar que no esté en blacklist
    if (tokenBlacklistService.isBlacklisted(token)) {
        throw new RuntimeException("Token ha sido revocado");
    }
    
    if (jwtUtil.esValido(token)) {
        // ... continuar
    }
}
```

---

## 📋 MEDIOS (Priority P2)

### 12. **Sin Validación de Email Único en Registro**
**Archivo:** `AuthController.java`  
**Severidad:** MEDIA  
**Riesgo:** Múltiples cuentas con el mismo email

**Solución:**
```java
// 1. Agregar validación en UsuarioService
@Service
public class UsuarioService {
    
    public void validarEmailUnico(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya está registrado en el sistema");
        }
    }
}

// 2. Usar en AuthController
@PostMapping("/register")
public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody RegistroRequest request) {
    usuarioService.validarEmailUnico(request.email());  // ✅ Validar primero
    
    Usuario usuario = new Usuario();
    // ... resto del código
}
```

---

### 13. **Sin Encriptación de Datos Sensibles en BD**
**Archivo:** Modelos de Usuario, Producto, etc.  
**Severidad:** MEDIA  
**Riesgo:** Si la BD se compromete, datos sensibles quedan expuestos

**Solución:**
```java
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionService {
    
    @Value("${encryption.key}")
    private String encryptionKey;
    
    public String encrypt(String data) throws Exception {
        SecretKeySpec key = new SecretKeySpec(
            encryptionKey.getBytes(), 0, encryptionKey.getBytes().length, "AES"
        );
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }
    
    public String decrypt(String encrypted) throws Exception {
        SecretKeySpec key = new SecretKeySpec(
            encryptionKey.getBytes(), 0, encryptionKey.getBytes().length, "AES"
        );
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
    }
}

// Usar en modelos sensibles como DNI:
@Entity
public class Usuario {
    
    @Converter(autoApply = true)
    public static class DniConverter implements AttributeConverter<String, String> {
        @Autowired
        private EncryptionService encryptionService;
        
        @Override
        public String convertToDatabaseColumn(String attribute) {
            try {
                return encryptionService.encrypt(attribute);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public String convertToEntityAttribute(String dbData) {
            try {
                return encryptionService.decrypt(dbData);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    @Column(name = "dni")
    @Convert(converter = DniConverter.class)
    private String dni;
}
```

---

### 14. **CORS Muy Permisivo en Desarrollo - Riesgo en Producción**
**Archivo:** `CorsConfig.java`  
**Severidad:** MEDIA  
**Riesgo:** Cualquier origen podría acceder en desarrollo, fácil olvidado en prod

**Solución:**
```java
@Configuration
public class CorsConfig {
    
    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;
    
    @Value("${app.environment:production}")
    private String environment;
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                CorsRegistration registration = registry.addMapping("/**");
                
                if ("development".equals(environment)) {
                    // Desarrollo: permitir localhost
                    registration.allowedOrigins(
                        "http://localhost:5173",
                        "http://localhost:5174",
                        "http://localhost:3000"
                    );
                } else {
                    // Producción: solo dominios específicos
                    String[] origins = allowedOrigins.split(",");
                    registration.allowedOrigins(origins);
                }
                
                registration
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);
            }
        };
    }
}
```

**En application.properties:**
```properties
app.environment=production
app.cors.allowed-origins=https://cafeapp.com,https://www.cafeapp.com
```

---

### 15. **Sin Auditoría de Acciones de Seguridad**
**Archivo:** (Transversal)  
**Severidad:** MEDIA  
**Riesgo:** No hay registro de quién hizo qué, difícil investigar incidentes

**Solución:**
```java
@Entity
@Table(name = "audit_log")
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String usuarioEmail;
    
    @Column(nullable = false)
    private String action; // LOGIN, CREATE_PAYMENT, CHANGE_ROLE, etc
    
    @Column(nullable = false)
    private String resourceType; // Usuario, Producto, Pedido, etc
    
    private Long resourceId;
    
    @Column(nullable = false)
    private String ipAddress;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    private String detalles;
}

@Component
public class AuditService {
    private final AuditLogRepository auditRepository;
    
    public void log(String usuarioEmail, String action, String resourceType, 
                    Long resourceId, String ipAddress, String detalles) {
        AuditLog log = new AuditLog();
        log.setUsuarioEmail(usuarioEmail);
        log.setAction(action);
        log.setResourceType(resourceType);
        log.setResourceId(resourceId);
        log.setIpAddress(ipAddress);
        log.setDetalles(detalles);
        log.setTimestamp(LocalDateTime.now());
        auditRepository.save(log);
    }
}

// Usar en AuthController:
@PostMapping("/login")
public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request, 
                                          HttpServletRequest httpRequest) {
    try {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        
        auditService.log(
            request.email(),
            "LOGIN_SUCCESS",
            "Usuario",
            null,
            httpRequest.getRemoteAddr(),
            "Inicio de sesión exitoso"
        );
        
        // ... resto del código
    } catch (Exception e) {
        auditService.log(
            request.email(),
            "LOGIN_FAILED",
            "Usuario",
            null,
            httpRequest.getRemoteAddr(),
            "Intento de login fallido: " + e.getMessage()
        );
        throw e;
    }
}
```

---

### 16. **ProductoController - Sin Validación de Permisos para Subir Imágenes**
**Archivo:** `ProductoController.java`  
**Severidad:** MEDIA  
**Riesgo:** Cualquier usuario autenticado podría subir imágenes/sobreescribir productos

**Solución:**
```java
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    
    @Operation(summary = "Subir imagen de producto")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")  // ✅ SOLO ADMINS/GERENTES
    @PostMapping("/upload/{productoId}")
    public ResponseEntity<String> subirImagenProducto(
        @PathVariable Long productoId,
        @RequestParam("imagen") MultipartFile imagen
    ) {
        // Validar que el archivo sea imagen
        if (!esImagenValida(imagen)) {
            throw new RuntimeException("El archivo debe ser una imagen (JPEG, PNG, GIF)");
        }
        
        // Validar tamaño
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (imagen.getSize() > maxSize) {
            throw new RuntimeException("La imagen no puede superar 5MB");
        }
        
        String publicId = "producto_" + productoId;
        String url = cloudinaryService.subirImagenConOverwrite(imagen, publicId);
        return ResponseEntity.ok(url);
    }
    
    private boolean esImagenValida(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && 
               (contentType.equals("image/jpeg") || 
                contentType.equals("image/png") || 
                contentType.equals("image/gif"));
    }
}

// Habilitar PreAuthorize en SecurityConfig:
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    // ...
}
```

---

## 📝 Resumen de Cambios Recomendados

| Archivo | Cambio | Prioridad |
|---------|--------|-----------|
| `PaymentController.java` | Separar Publishable/Secret Key, añadir DTOs, validar propiedad del carrito | P0 |
| `AuthController.java` | Remover rolId del registro, agregar logout, agregar rate limiting | P0 |
| `RegistroRequest.java` | Agregar validación de contraseña fuerte | P0 |
| `JwtUtil.java` | Reemplazar System.out.println con Logger | P1 |
| `StripeWebhookService.java` | Agregar logging y manejo de errores | P1 |
| `GlobalExceptionHandler.java` | Ocultar mensajes sensibles en producción | P1 |
| `CorsConfig.java` | Hacer CORS configurable por ambiente | P2 |
| `SecurityConfig.java` | Agregar PreAuthorize en toda la aplicación | P1 |
| `pom.xml` | Agregar Bucket4j para rate limiting | P1 |

---

## 🔧 Stack de Dependencias a Agregar

```xml
<!-- Logging -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.9</version>
</dependency>

<!-- Rate Limiting -->
<dependency>
    <groupId>io.github.bucket4j</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>7.6.0</version>
</dependency>

<!-- Validación avanzada -->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>8.0.1.Final</version>
</dependency>

<!-- JWT manejo estándar -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.11.5</version>
</dependency>
```

---

**Análisis completado por Senior Backend Developer + Security Expert**  
**Recomendación: Implementar en orden P0 → P1 → P2**
