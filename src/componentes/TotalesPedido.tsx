import React from 'react';

interface PropsTotalesPedido {
  subtotal: number;
  impuesto: number;
  total: number;
}

export const TotalesPedido: React.FC<PropsTotalesPedido> = ({
  subtotal,
  impuesto,
  total
}) => {
  return (
    <div className="totales-pedido">
      <div className="totales-pedido__fila">
        <span>Venta total:</span>
        <span>{subtotal.toFixed(2)}€</span>
      </div>
      <div className="totales-pedido__fila">
        <span>Impuesto (7% IGIC):</span>
        <span>{impuesto.toFixed(2)}€</span>
      </div>
      <div className="totales-pedido__fila totales-pedido__fila--final">
        <span>TOTAL:</span>
        <span>{total.toFixed(2)}€</span>
      </div>
    </div>
  );
};