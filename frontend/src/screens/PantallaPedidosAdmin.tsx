interface Props {
  centroId: number;
  alSalir: () => void;
}

export default function PantallaPedidosAdmin({ centroId, alSalir }: Props) {
  return (
    <div className="pantalla-pedidos-admin">
      <h1>Pedidos del Centro</h1>
      <p>Centro seleccionado: {centroId}</p>

      <button onClick={alSalir}>Volver</button>
    </div>
  );
}
