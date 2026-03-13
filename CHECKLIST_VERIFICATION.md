# ✅ Checklist de Verificación - Fixes Aplicados

## Cambios Críticos (P0) - TODOS IMPLEMENTADOS ✅

### PaymentController.java
- [x] Cambio: `@Value("${stripe.api.key}")` → `@Value("${stripe.secret.key}")`
- [x] Cambio: Crear `PaymentIntentRequest` y `PaymentItemRequest` DTOs
- [x] Cambio: Reemplazar `Map<String, Object>` con DTO validado
- [x] Cambio: Agregar validación de stock antes de PaymentIntent
- [x] Cambio: Obtener usuario autenticado con `SecurityContextHolder`
- [x] Cambio: Usar `RequestOptions.builder().setApiKey(stripeSecretKey)`
- [x] Cambio: Agregar Logger en lugar de `System.out.println`
- [x] Cambio: Agregar metadata a PaymentIntent

### RegistroRequest.java
- [x] Validación: Contraseña con patrón fuerte (mayús, minús, números, especiales)
- [x] Validación: `@Size(min = 8, max = 100)` en contraseña
- [x] Validación: DNI con patrón `^\\d{8}[A-Z]$`
- [x] Validación: Nombre con `@Size(min = 2, max = 100)`
- [x] **REMOVIDO:** Campo `rolId` (evita escalamiento de privilegios)

### AuthController.java
- [x] Cambio: Inyectar `UsuarioValidationService`
- [x] Cambio: Llamar `usuarioValidationService.validarEmailUnico(email)`
- [x] Cambio: Buscar rol por defecto `rolRepository.findByNombre("USUARIO")`
- [x] Cambio: Asignar rol automático (no tomar del cliente)

### JwtUtil.java
- [x] Cambio: Agregar `private static final Logger logger = LoggerFactory.getLogger()`
- [x] Cambio: Reemplazar `System.out.println(" Token expirado")` → `logger.warn(...)`
- [x] Cambio: Reemplazar `System.out.println(" Token malformado")` → `logger.warn(...)`
- [x] Cambio: Reemplazar `System.out.println(" Firma JWT inválida")` → `logger.warn(...)`
- [x] Cambio: Agregar captura de `UnsupportedJwtException`
- [x] Cambio: Agregar captura genérica de `Exception`

### pom.xml
- [x] Dependencia: Agregar `bucket4j-core` para Rate Limiting
- [x] Dependencia: Agregar `spring-boot-starter-logging`

### Nuevos archivos creados
- [x] `UsuarioValidationService.java` - Validaciones reutilizables
- [x] `PaymentItemRequest.java` - DTO para item de pago
- [x] `PaymentIntentRequest.java` - DTO para request de pago

---

## ✅ Verificación de Cambios - PASOS PRÁCTICOS

### 1. Compilar proyecto Maven
```bash
cd CafeAppBackend
mvn clean install
```
**Esperado:** ✅ Build success (sin errores de compilación)

---

### 2. Verificar que los archivos se crearon correctamente
```bash
# Verificar DTOs de pago
ls -la src/main/java/com/cafeapp/backend/dto/pago/
# Debe mostrar: PaymentItemRequest.java, PaymentIntentRequest.java

# Verificar servicio de validación
ls -la src/main/java/com/cafeapp/backend/servicio/
# Debe mostrar: UsuarioValidationService.java
```

---

### 3. Pruebas de Registro (POST /auth/register)
**Caso 1: Contraseña débil (debe fallar)**
```json
{
  "nombre": "Juan Test",
  "email": "juan@test.com",
  "password": "123456",
  "dni": "12345678A",
  "clase": "4A",
  "cursoId": 1
}
```
**Esperado:** ❌ Error 400 - "La contraseña debe contener mayúsculas, minúsculas, números..."

**Caso 2: Contraseña fuerte (debe funcionar)**
```json
{
  "nombre": "Juan Test",
  "email": "juan@test.com",
  "password": "Password123!",
  "dni": "12345678A",
  "clase": "4A",
  "cursoId": 1
}
```
**Esperado:** ✅ Usuario creado con rol USUARIO (no debe permitir elegir rol)

**Caso 3: Email duplicado (debe fallar)**
```json
{
  "nombre": "Juan Duplicado",
  "email": "juan@test.com",
  "password": "Password123!",
  "dni": "87654321B",
  "clase": "4B",
  "cursoId": 1
}
```
**Esperado:** ❌ Error 400 - "El email ya está registrado"

---

### 4. Pruebas de Pago (POST /pagos/crear-intento)
**Caso 1: DTO inválido (lista vacía)**
```json
{
  "items": []
}
```
**Esperado:** ❌ Error 400 - "Debe haber al menos un item"

**Caso 2: Item sin cantidad**
```json
{
  "items": [
    {
      "productoId": 1
    }
  ]
}
```
**Esperado:** ❌ Error 400 - Validación falla (cantidad requerida)

**Caso 3: Stock insuficiente**
```json
{
  "items": [
    {
      "productoId": 1,
      "cantidad": 999
    }
  ]
}
```
**Esperado:** ❌ Error - "Stock insuficiente para: [nombre producto]"

**Caso 4: Pago exitoso**
```json
{
  "items": [
    {
      "productoId": 1,
      "cantidad": 2
    }
  ]
}
```
**Esperado:** ✅ Retorna `clientSecret` para Stripe

---

### 5. Verificar Logging
**Buscar logs en:**
```bash
# Linux/Mac
tail -f logs/application.log | grep "PaymentIntent\|JWT"

# Windows (desde terminal de Spring Boot)
# Debe mostrar: "INFO: PaymentIntent creado exitosamente..."
# Debe mostrar: "WARN: Intento de usar token expirado" (si aplica)
```

---

### 6. Verificar que Stripe Key está protegida
**Verificar que NO aparezca en logs:**
```bash
grep -r "stripe.api.key" src/main/java/
# Debe retornar VACÍO (no debería encontrar nada)

grep -r "stripe.secret.key" src/main/java/
# Solo debe aparecer en: PaymentController.java (con @Value, protegido)
```

---

### 7. Verificar que rolId fue removido
```bash
grep -r "rolId" src/main/java/com/cafeapp/backend/dto/auth/
# Debe retornar VACÍO en RegistroRequest.java
```

---

## ⚠️ Problemas Potenciales y Soluciones

### Problema 1: "Symbol not found: Logger"
**Causa:** Falta import de SLF4J  
**Solución:**
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
```

### Problema 2: "Role 'USUARIO' no encontrado"
**Causa:** La tabla `rol` no tiene el registro  
**Solución:** Insertar en BD:
```sql
INSERT INTO rol (nombre, descripcion) VALUES ('USUARIO', 'Usuario normal del sistema');
INSERT INTO rol (nombre, descripcion) VALUES ('ADMIN', 'Administrador');
INSERT INTO rol (nombre, descripcion) VALUES ('GERENTE', 'Gerente');
```

### Problema 3: "Could not resolve RolRepository method findByNombre"
**Causa:** Falta el método en RolRepository  
**Solución:** Agregar:
```java
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}
```

### Problema 4: "Could not resolve UsuarioRepository method existsByEmail"
**Causa:** Falta el método en UsuarioRepository  
**Solución:** Agregar:
```java
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
}
```

### Problema 5: "JSON parse error: Unrecognized field 'rolId'"
**Causa:** Cliente sigue enviando `rolId`  
**Solución:** Actualizar cliente para NO enviar `rolId` en registro

---

## 🧪 Test Units Recomendados

### RegistroRequestValidationTest
```java
@Test
void passwordTooWeak_shouldFail() {
    // Contraseña: "weak123" (sin mayúsculas)
    // Esperado: ValidationException
}

@Test
void passwordStrong_shouldSucceed() {
    // Contraseña: "StrongPass123!"
    // Esperado: Válido
}
```

### PaymentIntentRequestValidationTest
```java
@Test
void emptyItems_shouldFail() {
    // items: []
    // Esperado: ValidationException
}

@Test
void validItems_shouldSucceed() {
    // items: [{productoId: 1, cantidad: 2}]
    // Esperado: Válido
}
```

---

## 📋 Documentación Actualizada

Los siguientes documentos han sido creados/actualizados:

1. **SECURITY_AND_BUGS_ANALYSIS.md** - Análisis completo de 16 vulnerabilidades
2. **IMPLEMENTATION_SUMMARY.md** - Resumen ejecutivo de cambios
3. **CHECKLIST_VERIFICATION.md** - Este archivo (pasos de verificación)

---

## 🎯 Resumen Final

✅ **5 Vulnerabilidades P0 Corregidas**
- Stripe Key Protection
- Privilege Escalation Prevention
- Strong Password Enforcement
- Input Validation (DTO)
- Proper Logging

⏳ **5+ Vulnerabilidades P1 Pendientes**
- Rate Limiting
- Error Handling
- Audit Logging
- PreAuthorize
- Token Blacklist

---

**Última actualización:** 13 Marzo 2026  
**Implementación completada por:** Senior Backend Developer + Security Expert
