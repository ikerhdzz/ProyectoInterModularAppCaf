import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],

  // Proxy para desarrollo: redirige /api/* al backend de Spring Boot.
  // Esto permite que el frontend use rutas relativas (/api/...) sin depender
  // de variables de entorno y evita problemas de CORS/host cuando se usa Vite.
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
