import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { getPedidosPendientes, actualizarEstadoPedido } from "../../services/pedidosService";

export const DashboardEmpleado = () => {
  const { user } = useAuth();
  const [pedidos, setPedidos] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  const cargarPedidos = async () => {
    if (!user) return;
    setLoading(true);
    try {
      const data = await getPedidosPendientes(user.token);
      setPedidos(data);
    } catch (error) {
      console.error("Error cargando pedidos:", error);
    }
    setLoading(false);
  };

  const marcarComoListo = async (id: number) => {
    if (!user) return;
    await actualizarEstadoPedido(id, "LISTO", user.token);
    cargarPedidos();
  };

  useEffect(() => {
    cargarPedidos();
  }, []);

  if (loading) return <h2>Cargando pedidos...</h2>;

  return (
    <div>
      <h1>Pedidos Pendientes</h1>

      {pedidos.length === 0 && <p>No hay pedidos pendientes</p>}

      {pedidos.map((p) => (
        <div key={p.id} style={{ border: "1px solid #ccc", padding: 10, marginBottom: 10 }}>
          <h3>Pedido #{p.id}</h3>
          <p>Cliente: {p.clienteNombre}</p>
          <p>Estado: {p.estado}</p>

          <h4>Productos:</h4>
          <ul>
            {p.detalles.map((d: any) => (
              <li key={d.id}>
                {d.productoNombre} x{d.cantidad}
              </li>
            ))}
          </ul>

          <button onClick={() => marcarComoListo(p.id)}>Marcar como LISTO</button>
        </div>
      ))}
    </div>
  );
};
