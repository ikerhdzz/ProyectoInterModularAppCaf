export const getPedidosPendientes = async (token: string) => {
  const res = await fetch("http://localhost:8080/pedidos/pendientes", {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });

  if (!res.ok) {
    throw new Error("Error al obtener pedidos pendientes");
  }

  return res.json();
};

export const actualizarEstadoPedido = async (id: number, estado: string, token: string) => {
  const res = await fetch(`http://localhost:8080/pedidos/${id}/estado`, {
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
