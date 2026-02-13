import React from "react";
import type { ElementoPedido } from "../datos/tipos";

interface Props {
  pedido: ElementoPedido[];
  alActualizarCantidad: (id: number, cantidad: number) => void;
  alLimpiarPedido: () => void;
}

export const SeccionPedido: React.FC<Props> = ({
  pedido,
  alActualizarCantidad,
  alLimpiarPedido
}) => {

  const subtotal = pedido.reduce(
    (acc, item) => acc + item.precio * item.cantidad,
    0
  );

  const impuesto = subtotal * 0.07;
  const total = subtotal + impuesto;

  const manejarAceptar = async () => {
    if (pedido.length === 0) {
      alert("No hay productos en el pedido");
      return;
    }

    const payload = {
      turnoId: 1,
      items: pedido.map(p => ({
        productoId: p.id,
        cantidad: p.cantidad
      }))
    };

    try {
      const res = await fetch("http://localhost:8080/api/pedido/frontend", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (!res.ok) throw new Error("Error al crear el pedido");

      const data = await res.json();
      alert(`Pedido creado correctamente. Total: ${data.total.toFixed(2)}€`);
      alLimpiarPedido();

    } catch (error) {
      console.error(error);
      alert("Hubo un error al enviar el pedido");
    }
  };

  return (
    <section className="seccion-pedido">
      <h2>Pedido Actual</h2>

      {pedido.length === 0 ? (
        <p className="seccion-pedido__vacio">No hay productos en el pedido</p>
      ) : (
        <>
          <ul className="seccion-pedido__lista">
            {pedido.map(item => (
              <li key={item.id} className="elemento-pedido">
                <div className="elemento-pedido__info">
                  <span className="elemento-pedido__nombre">{item.nombre}</span>
                  <span className="elemento-pedido__precio">{item.precio.toFixed(2)}€</span>
                </div>

                <div className="elemento-pedido__controles">
                  <button onClick={() => alActualizarCantidad(item.id, item.cantidad - 1)}>-</button>
                  <input
                    type="number"
                    value={item.cantidad}
                    onChange={(e) => alActualizarCantidad(item.id, parseInt(e.target.value) || 0)}
                    min="0"
                  />
                  <button onClick={() => alActualizarCantidad(item.id, item.cantidad + 1)}>+</button>
                </div>
              </li>
            ))}
          </ul>

          <div className="totales-pedido">
            <div className="totales-pedido__fila">
              <span>Subtotal:</span>
              <span>{subtotal.toFixed(2)}€</span>
            </div>

            <div className="totales-pedido__fila">
              <span>Impuesto (7% IGIC):</span>
              <span>{impuesto.toFixed(2)}€</span>
            </div>

            <div className="totales-pedido__fila totales-pedido__fila--final">
              <span>Total:</span>
              <span>{total.toFixed(2)}€</span>
            </div>
          </div>

          <div className="botones-accion">
            <button className="botones-accion__aceptar" onClick={manejarAceptar}>Aceptar</button>
            <button className="botones-accion__limpiar" onClick={alLimpiarPedido}>Limpiar</button>
            <button className="botones-accion__salir">Salir</button>
          </div>
        </>
      )}
    </section>
  );
};
