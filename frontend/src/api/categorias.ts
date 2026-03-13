const BASE = (import.meta as any).env?.VITE_API_BASE || '';

export async function obtenerCategorias() {
  const res = await fetch(BASE + "/api/categorias");
  if (!res.ok) throw new Error("Error cargando categorías");
  return res.json();
}
