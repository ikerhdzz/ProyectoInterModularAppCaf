import React from 'react';
import type { ElementoPedido as TipoElementoPedido } from '../datos/tipos';

interface PropsElementoPedido {
  elemento: TipoElementoPedido;
  alActualizarCantidad: (nombre: string, cantidad: number) => void;
}

export const ElementoPedido: React.FC<PropsElementoPedido> = ({
  elemento,
  alActualizarCantidad
}) => {
  const manejarDisminuir = () => {
    alActualizarCantidad(elemento.nombre, elemento.cantidad - 1);
  };

  const manejarAumentar = () => {
    alActualizarCantidad(elemento.nombre, elemento.cantidad + 1);
  };

  const manejarCambioInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    const valor = parseInt(e.target.value) || 0;
    alActualizarCantidad(elemento.nombre, valor);
  };

  return (
    <div className="elemento-pedido">
      <div className="elemento-pedido__info">
        <img src={elemento.imagen ?? '/img/imagenNoDisponible.jpg'} alt={elemento.nombre} className="elemento-pedido__imagen" />
        <div>
          <span className="elemento-pedido__nombre">{elemento.nombre}</span>
          <span className="elemento-pedido__precio">{elemento.precio.toFixed(2)}€</span>
        </div>
      </div>
      <div className="elemento-pedido__controles">
        <button onClick={manejarDisminuir}>-</button>
        <input 
          type="number" 
          value={elemento.cantidad}
          onChange={manejarCambioInput}
          min="0"
        />
        <button onClick={manejarAumentar}>+</button>
      </div>
    </div>
  );
};