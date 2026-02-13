export const getProductos = async () => {
  const res = await fetch("http://localhost:8080/productos");

  if (!res.ok) {
    throw new Error("Error al obtener productos");
  }

  return res.json();
};

export const crearProducto = async (producto: any, token: string) => {
  const res = await fetch("http://localhost:8080/productos", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(producto)
  });

  if (!res.ok) {
    throw new Error("Error al crear producto");
  }

  return res.json();
};

export const eliminarProducto = async (id: number, token: string) => {
  const res = await fetch(`http://localhost:8080/productos/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`
    }
  });

  if (!res.ok) {
    throw new Error("Error al eliminar producto");
  }
};
