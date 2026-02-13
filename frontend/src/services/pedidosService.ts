const API_URL = "http://localhost:8080/pedidos";

// Obtener SOLO pedidos pendientes
export const getPedidosPendientes = async (token: string) => {
  const res = await fetch(`${API_URL}/pendientes`, {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });

  if (!res.ok) {
    throw new Error("Error al obtener pedidos pendientes");
  }

  return res.json();
};

// Obtener pedidos por estado (PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO)
export const getPedidosPorEstado = async (estado: string, token: string) => {
  const res = await fetch(`${API_URL}/estado/${estado}`, {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });

  if (!res.ok) {
    throw new Error("Error al obtener pedidos por estado");
  }

  return res.json();
};

// Actualizar estado del pedido
export const actualizarEstadoPedido = async (id: number, estado: string, token: string) => {
  const res = await fetch(`${API_URL}/${id}/estado`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify({ estado })
  });

  if (!res.ok) {
    throw new Error("Error al actualizar el estado del pedido");
  }

  return res.json();
};
