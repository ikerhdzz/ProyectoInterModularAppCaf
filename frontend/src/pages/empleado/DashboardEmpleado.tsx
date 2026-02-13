import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { 
  getPedidosPendientes, 
  getPedidosPorEstado, 
  actualizarEstadoPedido 
} from "../../services/pedidosService";
import { PedidoModal } from "./PedidoModal";

export const DashboardEmpleado = () => {
  const { user } = useAuth();
  const [pedidos, setPedidos] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [filtro, setFiltro] = useState("PENDIENTE");
  const [pedidoSeleccionado, setPedidoSeleccionado] = useState<any | null>(null);

  const cargarPedidos = async () => {
    if (!user) return;
    setLoading(true);

    try {
      let data;

      if (filtro === "PENDIENTE") {
        data = await getPedidosPendientes(user.token);
      } else {
        data = await getPedidosPorEstado(filtro, user.token);
      }

      setPedidos(data);
    } catch (error) {
      console.error("Error cargando pedidos:", error);
    }

    setLoading(false);
  };

  const cambiarEstado = async (id: number, estado: string) => {
    if (!user) return;
    try {
      await actualizarEstadoPedido(id, estado, user.token);
      cargarPedidos();
    } catch (error) {
      console.error("Error actualizando estado:", error);
    }
  };

  // Actualización automática cada 10 segundos
  useEffect(() => {
    cargarPedidos();
    const interval = setInterval(() => {
      cargarPedidos();
    }, 10000);

    return () => clearInterval(interval);
  }, [filtro]);

  if (loading) return <h2>Cargando pedidos...</h2>;

  return (
    <div style={{ padding: "20px" }}>
      <h1>Gestión de Pedidos</h1>

      {/* SELECTOR DE FILTRO */}
      <div style={{ marginBottom: 20 }}>
        <label style={{ marginRight: 10 }}>Filtrar por estado:</label>
        <select 
          value={filtro} 
          onChange={(e) => setFiltro(e.target.value)}
          style={{ padding: 5 }}
        >
          <option value="PENDIENTE">Pendientes</option>
          <option value="EN_PREPARACION">En preparación</option>
          <option value="LISTO">Listos</option>
          <option value="ENTREGADO">Entregados</option>
        </select>
      </div>

      {pedidos.length === 0 && <p>No hay pedidos con este estado</p>}

      {pedidos.map((p) => (
        <div
          key={p.id}
          style={{
            border: "1px solid #ccc",
            padding: 15,
            marginBottom: 15,
            borderRadius: 10,
            background: "#fafafa",
          }}
        >
          <h3>Pedido #{p.id}</h3>
          <p><strong>Cliente:</strong> {p.clienteNombre}</p>
          <p><strong>Estado:</strong> {p.estado}</p>

          <button 
            onClick={() => setPedidoSeleccionado(p)} 
            style={btn("gray")}
          >
            Ver detalles
          </button>

          <div style={{ marginTop: 10 }}>
            <button onClick={() => cambiarEstado(p.id, "EN_PREPARACION")} style={btn("orange")}>
              En preparación
            </button>

            <button onClick={() => cambiarEstado(p.id, "LISTO")} style={btn("green")}>
              Listo
            </button>

            <button onClick={() => cambiarEstado(p.id, "ENTREGADO")} style={btn("blue")}>
              Entregado
            </button>
          </div>
        </div>
      ))}

      {/* MODAL DE DETALLES */}
      {pedidoSeleccionado && (
        <PedidoModal 
          pedido={pedidoSeleccionado} 
          onClose={() => setPedidoSeleccionado(null)} 
        />
      )}
    </div>
  );
};

const btn = (color: string): React.CSSProperties => ({
  marginRight: 10,
  padding: "8px 12px",
  background: color,
  color: "white",
  border: "none",
  borderRadius: 5,
  cursor: "pointer",
});
