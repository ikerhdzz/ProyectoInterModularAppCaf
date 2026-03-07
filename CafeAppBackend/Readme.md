CaféApp Backend – Documentación Técnica (Versión Final)
Backend desarrollado en Spring Boot 3, con arquitectura modular, seguridad JWT, gestión de stock por centro, sistema de pedidos, integración con Stripe y soporte para carrito de compras.

Este documento sirve como guía técnica para el equipo y como base para futuras decisiones sobre la evolución del proyecto.

🧱 Arquitectura del Proyecto
Código
src/main/java/com/cafeapp/backend
│
├── controlador/        → Controladores REST (API pública)
├── dto/                → Objetos de transferencia de datos (Request/Response)
├── excepciones/        → Manejo centralizado de errores (GlobalExceptionHandler)
├── modelo/             → Entidades JPA (mapeo BD)
├── repositorio/        → Repositorios Spring Data JPA
├── seguridad/          → JWT, filtros, autenticación
└── servicio/           → Lógica de negocio
La arquitectura sigue un patrón en capas, con responsabilidades bien separadas:

Controladores → Exponen la API

Servicios → Contienen la lógica de negocio

Repositorios → Acceso a datos

DTOs → Aíslan la API del modelo interno

Excepciones → Manejo uniforme de errores

🗄️ Base de Datos
El backend está alineado con la estructura real de la BD:

✔ Tipos de ID
Entidades principales → Long

Entidades internas → Integer

✔ Relaciones clave
Centro 1–N Usuario

Centro 1–N StockCentro

Producto 1–N StockCentro

Carrito 1–N ItemCarrito

ItemCarrito 1–N ItemCarritoExtra

Pedido 1–N DetallePedido

DetallePedido 1–N DetalleExtra

Toda la lógica del backend ha sido revisada para respetar estos tipos y relaciones.

🔐 Seguridad
Sistema de autenticación basado en JWT:

Login por email + contraseña

Generación de token JWT

Validación automática mediante filtro

Acceso a usuario autenticado vía SecurityContextHolder

Cada usuario pertenece a un Centro, lo que afecta:

Stock disponible

Pedidos visibles

Gestión interna

🛒 Carrito (opcional según decisión del equipo)
El backend incluye un sistema de carrito completo y funcional, pero su uso final depende del equipo.

Funcionalidades:
Un carrito por usuario

Items con cantidad

Extras por ítem

Validación de stock antes de agregar o actualizar

Conversión del carrito → pedido

Cálculo automático de totales

Si se decide eliminarlo, está aislado en:
CarritoService

CarritoController

Entidades: ItemCarrito, ItemCarritoExtra

📦 Gestión de Stock por Centro
Implementado en:

StockCentroService

StockCentroRepository

StockCentro (entidad)

StockCentroController

Funciones:
Obtener stock por centro

Obtener stock por producto

Crear stock inicial

Actualizar stock

Aumentar stock

Restar stock (con validación)

Integración con carrito y pedidos

🧾 Pedidos
Implementado en:

PedidoService

PedidoServiceImpl

PedidoController

Flujo completo:
Validación del turno

Validación de stock

Creación del pedido

Creación de detalles

Descuento de stock real

Generación de ticket (string)

Cálculo de totales

Listado por usuario o por centro

💳 Pagos (Stripe)
Implementado en:

PaymentController

StripeWebhookController

StripeWebhookService

Funciones:
Crear PaymentIntent

Calcular totales con impuestos

Recibir webhooks de Stripe

Validar firma del webhook

🖼️ Subida de Imágenes (Cloudinary)
Implementado en:

CloudinaryService

ProductoController

Funciones:
Subir imagen

Subir imagen sobrescribiendo la existente

Generar URL pública

📡 Endpoints Principales (Versión Real)
🔐 Autenticación
Código
POST /auth/login
POST /auth/register
GET  /auth/me
🛒 Carrito
Código
GET    /carrito
POST   /carrito/items
PUT    /carrito/items/{productoId}
DELETE /carrito/items/{productoId}
POST   /carrito/items/{itemId}/extras
DELETE /carrito/items/{itemId}/extras/{extraId}
DELETE /carrito
🧾 Pedidos
Código
POST /pedidos/carrito/{turnoId}
POST /pedidos/frontend
GET  /pedidos/mis
GET  /pedidos/{pedidoId}/detalles
PATCH /pedidos/{pedidoId}/estado
GET  /pedidos/{pedidoId}/ticket
GET  /pedidos/{pedidoId}/totales
GET  /pedidos/centro/{centroId}
📦 Stock por centro
Código
GET    /stock-centro
GET    /stock-centro/{id}
POST   /stock-centro
PUT    /stock-centro/{id}
DELETE /stock-centro/{id}
🛍️ Productos
Código
GET    /productos
GET    /productos/{id}
POST   /productos
PUT    /productos/{id}
DELETE /productos/{id}
POST   /productos/upload
POST   /productos/upload/{productoId}
GET    /productos/categoria/{nombre}
🏫 Centros
Código
GET    /centros
GET    /centros/{id}
POST   /centros
PUT    /centros/{id}
DELETE /centros/{id}
🍽️ Categorías
Código
GET    /categorias
GET    /categorias/{id}
POST   /categorias
PUT    /categorias/{id}
DELETE /categorias/{id}
🧪 Pruebas
El backend está preparado para pruebas con:

Postman

Thunder Client

Frontend React + TypeScript

Stripe CLI (para webhooks)

🚀 Cómo ejecutar
Configurar application.properties

Crear la base de datos

Ejecutar:

Código
mvn spring-boot:run
🧩 Decisiones pendientes del equipo
Mantener o eliminar el carrito

Simplificar el flujo de pedidos

Añadir roles avanzados

Permitir o no stock negativo

Añadir auditoría de movimientos de stock

✔ Trabajo ya realizado
Unificación de tipos Long/Integer

Eliminación del DTO duplicado UsuarioResponse

Limpieza completa de servicios

Controladores coherentes

StockCentroService completo

PedidoServiceImpl completo

CarritoService corregido

Excepciones centralizadas

Código compilando sin errores

Flujo estable