# ☕ AGENTS.md - Proyecto CafeApp (Fullstack)

Este documento define la estructura, reglas y contexto del proyecto para agentes de IA y desarrolladores.

## 1. Contexto del Proyecto
Aplicación de gestión de pedidos para una cafetería (estilo McDonald's). Permite la gestión de inventario por centros, procesamiento de pagos con Stripe, gestión de alérgenos y flujos de pedidos en tiempo real.

## 2. Estructura del Workspace
El proyecto es un monorepo híbrido:
- **Backend:** Ubicado en la raíz y `/src/main/java/com/cafeapp/backend`. Basado en Spring Boot y Maven.
- **Frontend:** Ubicado en la carpeta `/frontend`. Basado en React + Vite + TypeScript.

## 3. Guía del Backend (Java / Spring Boot)
- **Controladores (`/controlador`):** Endpoints REST. Manejan la lógica de entrada/salida.
- **Seguridad (`/config`, `/seguridad`):** Configuración de CORS, Cloudinary, Stripe Webhooks y JWT/Auth.
- **Modelos y Repositorios:** Uso de JPA/Hibernate para la persistencia.
- **DTOs (`/dto`):** Obligatorio usar DTOs para la transferencia de datos entre cliente y servidor.
- **Servicios (`/servicio`):** Aquí reside toda la lógica de negocio.

**Regla de Oro:** No escribir lógica de base de datos directamente en los controladores.

## 4. Guía del Frontend (React + Vite + TypeScript)
- **Pantallas (`/screens`):** Componentes de página completa (Admin, Cocina, Pago, Pedido, etc.).
- **Componentes (`/componentes`):** Elementos UI reutilizables (Botones, Tarjetas, Selectores).
- **API (`/api`):** Archivos de servicio para llamadas al backend (auth.ts, categorias.ts).
- **Tipos (`/tipos.ts`):** Definiciones globales de TypeScript para asegurar la consistencia con el backend.

## 5. Convenciones de Código
- **Lenguaje:** Código (clases, métodos, variables) en **Inglés**. Documentación y comentarios en **Español**.
- **Estilos:** Uso de **Bootstrap** (clases nativas o React-Bootstrap).
- **Nomenclatura:**
  - Java: `PascalCase` para clases, `camelCase` para métodos.
  - React: `PascalCase` para componentes (`.tsx`), `camelCase` para funciones y hooks.
- **Pagos:** La lógica de Stripe se gestiona en `PaymentController.java` y `StripeWebhookController.java`.

## 6. Flujo de Trabajo para la IA
1. **Frontend:** Antes de crear un componente, verifica si ya existen tipos en `tipos.ts`.
2. **Backend:** Al añadir un nuevo modelo, asegúrate de crear su correspondiente `Repository`, `Service`, `Controller` y sus `DTOs`.
3. **Imágenes:** Las imágenes se gestionan vía Cloudinary (ver `CloudinaryConfig.java`) y se sirven localmente desde `uploads/images/`.
4. **Seguridad:** Siempre verificar los permisos en `SecurityConfig.java` al añadir nuevos endpoints.