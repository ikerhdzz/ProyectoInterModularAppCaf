const API_URL = "http://localhost:8080/api/productos";

// Obtener todos los productos
export const getProductos = async () => {
  const res = await fetch(API_URL);

  if (!res.ok) {
    throw new Error("Error al obtener productos");
  }

  return res.json();
};

// Crear producto
export const crearProducto = async (producto: any, token: string) => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(producto),
  });

  if (!res.ok) {
    throw new Error("Error al crear producto");
  }

  return res.json();
};

// Eliminar producto
export const eliminarProducto = async (id: number, token: string) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!res.ok) {
    throw new Error("Error al eliminar producto");
  }
};


// Editar producto
export const editarProducto = async (id: number, producto: any, token: string) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(producto),
  });

  if (!res.ok) {
    throw new Error("Error al editar producto");
  }

  return res.json();
};

