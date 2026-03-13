interface Props {
  centroId: number;
  alSalir: () => void;
}

export default function PantallaEstadisticas({ centroId, alSalir }: Props) {
  return (
    <div className="pantalla-estadisticas">
      <h1>Estadísticas Globales</h1>
      <p>Centro seleccionado: {centroId}</p>

      <button onClick={alSalir}>Volver</button>
    </div>
  );
}
