import React from "react";

interface Props {
  irAStock: () => void;
  irAPedidos: () => void;
  irAEmpleados: () => void;
  irAEstadisticas: () => void;
  alSalir: () => void;
}

export default function PantallaAdmin({
  irAStock,
  irAPedidos,
  irAEmpleados,
  irAEstadisticas,
  alSalir
}: Props) {
  return (
    <div className="admin-contenedor">
      <h1>Panel de Administración</h1>

      <div className="admin-botones">
        <button onClick={irAStock}>Gestión de Stock</button>
        <button onClick={irAPedidos}>Gestión de Pedidos</button>
        <button onClick={irAEmpleados}>Gestión de Empleados</button>
        <button onClick={irAEstadisticas}>Estadísticas</button>
      </div>

      <button className="btn-salir" onClick={alSalir}>
        Salir
      </button>
    </div>
  );
}
