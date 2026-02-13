export const PedidoModal = ({ pedido, onClose }: any) => {
  return (
    <div style={overlay}>
      <div style={modal}>
        <h2>Detalles del Pedido #{pedido.id}</h2>

        <p><strong>Cliente:</strong> {pedido.clienteNombre}</p>
        <p><strong>Estado:</strong> {pedido.estado}</p>

        <h3>Productos:</h3>
        <ul>
          {pedido.detalles.map((d: any) => (
            <li key={d.id}>
              {d.productoNombre} x{d.cantidad} — {d.precio}€
            </li>
          ))}
        </ul>

        <h3>Total: {pedido.total}€</h3>

        <button onClick={onClose} style={btnCerrar}>
          Cerrar
        </button>
      </div>
    </div>
  );
};

const overlay: React.CSSProperties = {
  position: "fixed",
  top: 0,
  left: 0,
  width: "100vw",
  height: "100vh",
  background: "rgba(0,0,0,0.5)",
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  zIndex: 1000,
};

const modal: React.CSSProperties = {
  background: "white",
  padding: 20,
  borderRadius: 10,
  width: "400px",
  maxHeight: "80vh",
  overflowY: "auto",
};

const btnCerrar: React.CSSProperties = {
  marginTop: 20,
  padding: "8px 12px",
  background: "red",
  color: "white",
  border: "none",
  borderRadius: 5,
  cursor: "pointer",
};
