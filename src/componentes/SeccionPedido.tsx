import React from 'react';
import type { ElementoPedido as TipoElementoPedido } from '../datos/tipos';
import { ElementoPedido } from './ElementoPedido';
import { TotalesPedido } from './TotalesPedido';
import { BotonesAccion } from './BotonesAccion';

interface PropsSeccionPedido {
  pedido: TipoElementoPedido[];
  alActualizarCantidad: (nombre: string, cantidad: number) => void;
  alLimpiarPedido: () => void;
  alAceptar: () => void;
  manejarSalir: () => void;
}

export const SeccionPedido: React.FC<PropsSeccionPedido> = ({
  pedido,
  alActualizarCantidad,
  alLimpiarPedido,
  alAceptar,
  manejarSalir
}) => {
  const calcularSubtotal = (): number => {
    return pedido.reduce((total, elemento) => 
      total + (elemento.precio * elemento.cantidad), 0
    );
  };

  const calcularImpuesto = (): number => {
    return calcularSubtotal() * 0.07;
  };

  const calcularTotal = (): number => {
    return calcularSubtotal() + calcularImpuesto();
  };

  const subtotal = calcularSubtotal();
  const impuesto = calcularImpuesto();
  const total = calcularTotal();

  return (
    <div className="seccion-pedido">
      <h2>Pedido Actual</h2>
      
      {pedido.length === 0 ? (
        <p className="seccion-pedido__vacio">No hay productos en el pedido</p>
      ) : (
        <>
          <div className="seccion-pedido__lista">
            {pedido.map((elemento) => (
              <ElementoPedido 
                key={elemento.nombre} 
                elemento={elemento}
                alActualizarCantidad={alActualizarCantidad}
              />
            ))}
          </div>

          <TotalesPedido 
            subtotal={subtotal}
            impuesto={impuesto}
            total={total}
          />

          <BotonesAccion 
            alAceptar={alAceptar}
            alLimpiar={alLimpiarPedido}
            alSalir={manejarSalir}
          />
        </>
      )}
    </div>
  );
};