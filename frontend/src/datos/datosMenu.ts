import type { ElementoMenu } from './tipos';

// Datos por defecto (fallback) — mantienen la app funcional si el backend no responde
const datosPorDefecto: ElementoMenu[] = [
  { id: 1, nombre: 'Agua', precio: 1.5, imagen: '/img/imagenNoDisponible.jpg' },
  { id: 2, nombre: 'Refresco', precio: 2, imagen: '/img/imagenNoDisponible.jpg' },
  { id: 3, nombre: 'Croissant', precio: 2.5, imagen: '/img/imagenNoDisponible.jpg' },
  { id: 4, nombre: 'Ensalada', precio: 3.5, imagen: '/img/imagenNoDisponible.jpg' },
  { id: 5, nombre: 'Hamburguesa', precio: 3, imagen: '/img/imagenNoDisponible.jpg' },
  { id: 6, nombre: 'Perrito', precio: 2.5, imagen: '/img/imagenNoDisponible.jpg' },
  { id: 7, nombre: 'Sandwich', precio: 2.5, imagen: '/img/imagenNoDisponible.jpg' },
  { id: 8, nombre: 'Postre', precio: 3, imagen: '/img/imagenNoDisponible.jpg' },
  { id: 9, nombre: 'Paquete de papas', precio: 1, imagen: '/img/imagenNoDisponible.jpg' },
];

// Fetch del backend: /api/productos
export async function obtenerDatosMenu(baseUrl = ''): Promise<ElementoMenu[]> {
  const envBase = typeof import.meta !== 'undefined' && (import.meta as any).env?.VITE_API_BASE;
  const base = baseUrl || envBase || '';
  const url = base + '/api/productos';
  try {
    const res = await fetch(url, { headers: { 'Accept': 'application/json' } });
    if (!res.ok) {
      console.warn(`obtenerDatosMenu: respuesta ${res.status}, usando datos por defecto`);
      return datosPorDefecto;
    }
    const productos = await res.json();
    const lista = (productos || []).map((p: any, idx: number) => {
      const rawImg = p.imagen ?? p.imagen_url ?? null;
      let imagenVal: string;
      if (rawImg) {
        const s = String(rawImg).trim();
        if (s.startsWith('http')) {
          imagenVal = s;
        } else if (s.startsWith('/')) {
          imagenVal = s;
        } else if (s.includes('/')) {
          // ya contiene una ruta relativa, simplemente asegurar prefijo '/'
          imagenVal = s.startsWith('img/') ? `/${s}` : `/${s}`;
        } else {
          // solo nombre de archivo
          imagenVal = `/img/${s}`;
        }
      } else {
        imagenVal = '/img/imagenNoDisponible.jpg';
      }

      return {
        id: Number(p.id ?? p.id_producto ?? idx + 1),
        nombre: String(p.nombre ?? p.nombre_producto ?? ''),
        precio: Number(p.precio ?? p.precio_base ?? 0),
        imagen: imagenVal,
        categoriaId: p.categoriaId ?? p.categoria_id ?? (p.categoriaId ? Number(p.categoriaId) : undefined),
        categoriaNombre: p.categoriaNombre ?? p.categoria_nombre ?? (p.categoria ? (p.categoria.nombre ?? p.categoriaName ?? undefined) : undefined)
      };
    });
    return lista.length ? lista : datosPorDefecto;
  } catch (e) {
    console.error('obtenerDatosMenu:', e);
    return datosPorDefecto;
  }
}

// Para compatibilidad con código que importe datos estáticos
export const datosMenu: ElementoMenu[] = datosPorDefecto;