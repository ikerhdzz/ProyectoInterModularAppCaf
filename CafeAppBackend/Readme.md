☕ CaféApp Backend – Documentación Técnica
Backend desarrollado en Spring Boot 3, con arquitectura modular orientada a servicios, validación, seguridad JWT y gestión de stock por centro.

🧱 Arquitectura del Proyecto
Código
src/main/java/com/cafeapp/backend
│
├── controlador/        → Controladores REST (API pública)
├── dto/                → Objetos de transferencia de datos
├── excepciones/        → Manejo centralizado de errores
├── modelo/             → Entidades JPA (mapeo BD)
├── repositorio/        → Repositorios Spring Data JPA
├── seguridad/          → JWT, filtros, autenticación
└── servicio/           → Lógica de negocio
🗄️ Base de Datos
El backend está alineado con la BD real:

IDs de entidades principales (Producto, ExtraProducto, Pedido, etc.) → Long

IDs de entidades internas (ItemCarrito, StockCentro, etc.) → Integer

Relaciones:

Centro 1–N Usuario

Centro 1–N StockCentro

Producto 1–N StockCentro

Carrito 1–N ItemCarrito

ItemCarrito 1–N ItemCarritoExtra

Pedido 1–N DetallePedido

DetallePedido 1–N DetalleExtra

Todo el backend ha sido saneado para respetar estos tipos.

🔐 Seguridad
Autenticación mediante JWT

Login por email + password

Cada usuario pertenece a un Centro

El stock y los pedidos se gestionan por centro

🛒 Carrito (opcional según decisión del equipo)
El backend incluye un sistema de carrito totalmente funcional, pero su uso final depende del equipo.

Características:

Un carrito por usuario

Items con cantidad

Extras por item

Validación de stock antes de agregar o actualizar

Conversión del carrito → pedido

Si el equipo decide eliminarlo, está aislado en:

Código
CarritoService
CarritoController
ItemCarrito / ItemCarritoExtra
📦 Gestión de Stock por Centro
Implementado en:

StockCentroService

StockCentroRepository

StockCentro (entidad)

Funciones:

Obtener stock por centro

Obtener stock por producto

Crear stock inicial

Actualizar stock

Aumentar stock

Restar stock (validación incluida)

Integración con carrito y pedidos

🧾 Pedidos
Implementado en:

PedidoServiceImpl

PedidoController

Flujo:

Validación de turno

Validación de stock

Creación del pedido

Creación de detalles

Descuento de stock real

Generación de ticket (opcional)

Cálculo de totales

Listado por usuario o por centro

📡 Endpoints Principales
Autenticación
Código
POST /api/auth/login
POST /api/auth/registro
Carrito
Código
GET    /api/carrito
POST   /api/carrito/agregar
PUT    /api/carrito/cantidad
DELETE /api/carrito/eliminar
POST   /api/carrito/extra/agregar
DELETE /api/carrito/extra/quitar
Pedidos
Código
POST   /api/pedidos/carrito
POST   /api/pedidos/frontend
GET    /api/pedidos/usuario
GET    /api/pedidos/{id}/detalles
PUT    /api/pedidos/{id}/estado
POST   /api/pedidos/{id}/ticket
GET    /api/pedidos/{id}/totales
GET    /api/pedidos/centro/{centroId}
Stock
Código
GET    /api/stock/centro/{centroId}
GET    /api/stock/{centroId}/{productoId}
POST   /api/stock/crear
PUT    /api/stock/actualizar
PUT    /api/stock/aumentar
PUT    /api/stock/restar
🧪 Pruebas
El backend está preparado para pruebas con:

Postman

Thunder Client

Frontend React +  TypeScript

🚀 Cómo ejecutar
Configurar application.properties

Crear BD

Ejecutar:

Código
mvn spring-boot:run
🧩 Decisiones pendientes del equipo
¿Se mantiene el carrito?

¿Se simplifica el flujo de pedidos?

¿Se añade un sistema de roles más avanzado?


¿Se añade auditoría de movimientos de stock?



✔ Ya está hecho
Unificación de tipos Integer/Long

Limpieza de servicios

Eliminación de métodos duplicados

Integración correcta de stock

Controladores coherentes

PedidoServiceImpl completo

CarritoService corregido

StockCentroService completo

Código compilando sin errores

Flujo estable

🔧 Falta por hacer
1. Comentar todas las clases
   Explicar propósito

Explicar métodos clave

Explicar relaciones con BD

Revisar nombres de paquetes

controlador → OK

servicio → OK

repositorio → OK

modelo → OK

Revisar imports no usados

Revisar warnings del IDE

Confirmar que no quedan Long/Integer mezclados


☕ CaféApp Backend – Documentación Técnica
Backend desarrollado en Spring Boot 3, con arquitectura modular, seguridad JWT, gestión de stock por centro y soporte para carrito y pedidos. Este documento sirve como guía para el equipo y como base para futuras decisiones sobre la evolución del proyecto.

🧱 Arquitectura del Proyecto
Código
src/main/java/com/cafeapp/backend
│
├── controlador/        → Controladores REST (API pública)
├── dto/                → Objetos de transferencia de datos
├── excepciones/        → Manejo centralizado de errores
├── modelo/             → Entidades JPA (mapeo BD)
├── repositorio/        → Repositorios Spring Data JPA
├── seguridad/          → JWT, filtros, autenticación
└── servicio/           → Lógica de negocio
El proyecto sigue una arquitectura en capas clara y separada, facilitando mantenimiento, pruebas y escalabilidad.

🗄️ Base de Datos
El backend está alineado con la estructura real de la BD:

Entidades principales (Producto, ExtraProducto, Pedido, etc.) → Long

Entidades internas (ItemCarrito, StockCentro, etc.) → Integer

Relaciones clave:

Centro 1–N Usuario

Centro 1–N StockCentro

Producto 1–N StockCentro

Carrito 1–N ItemCarrito

ItemCarrito 1–N ItemCarritoExtra

Pedido 1–N DetallePedido

DetallePedido 1–N DetalleExtra

Toda la lógica del backend ha sido saneada para respetar estos tipos y relaciones.

🔐 Seguridad
Autenticación mediante JWT

Login por email y contraseña

Cada usuario pertenece a un Centro

El stock y los pedidos se gestionan por centro

🛒 Carrito (pendiente de decisión del equipo)
El backend incluye un sistema de carrito funcional, pero su uso final dependerá del equipo.

Características implementadas:

Un carrito por usuario

Items con cantidad

Extras por item

Validación de stock antes de agregar o actualizar

Conversión del carrito a pedido

Si se decide eliminarlo, está aislado en:

Código
CarritoService
CarritoController
ItemCarrito / ItemCarritoExtra
📦 Gestión de Stock por Centro
Implementado en:

StockCentroService

StockCentroRepository

StockCentro (entidad)

Funciones disponibles:

Obtener stock por centro

Obtener stock por producto

Crear stock inicial

Actualizar stock

Aumentar stock

Restar stock con validación

Integración con carrito y pedidos

🧾 Pedidos
Implementado en:

PedidoServiceImpl

PedidoController

Flujo completo:

Validación del turno

Validación de stock

Creación del pedido

Creación de detalles

Descuento de stock real

Generación de ticket

Cálculo de totales

Listado por usuario o por centro

📡 Endpoints Principales
Autenticación
Código
POST /api/auth/login
POST /api/auth/registro
Carrito
Código
GET    /api/carrito
POST   /api/carrito/agregar
PUT    /api/carrito/cantidad
DELETE /api/carrito/eliminar
POST   /api/carrito/extra/agregar
DELETE /api/carrito/extra/quitar
Pedidos
Código
POST   /api/pedidos/carrito
POST   /api/pedidos/frontend
GET    /api/pedidos/usuario
GET    /api/pedidos/{id}/detalles
PUT    /api/pedidos/{id}/estado
POST   /api/pedidos/{id}/ticket
GET    /api/pedidos/{id}/totales
GET    /api/pedidos/centro/{centroId}
Stock
Código
GET    /api/stock/centro/{centroId}
GET    /api/stock/{centroId}/{productoId}
POST   /api/stock/crear
PUT    /api/stock/actualizar
PUT    /api/stock/aumentar
PUT    /api/stock/restar
🧪 Pruebas
El backend está preparado para pruebas mediante:

Postman

Thunder Client

Frontend React + TypeScript

🚀 Cómo ejecutar
Configurar application.properties

Crear la base de datos

Ejecutar:

Código
mvn spring-boot:run
🧩 Decisiones pendientes del equipo
Mantener o eliminar el carrito

Simplificar o modificar el flujo de pedidos

Añadir roles avanzados

Permitir o no stock negativo

Añadir auditoría de movimientos de stock