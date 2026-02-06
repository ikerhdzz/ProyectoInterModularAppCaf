import React from 'react';
import type { ElementoMenu } from '../datos/tipos';

interface PropsTarjetaMenu {
  elemento: ElementoMenu;
  alAgregarAlPedido: (elemento: ElementoMenu) => void;
}

export const TarjetaMenu: React.FC<PropsTarjetaMenu> = ({
  elemento,
  alAgregarAlPedido
}) => {
  const manejarClick = () => {
    alAgregarAlPedido(elemento);
  };

  return (
    <div className="tarjeta-menu" onClick={manejarClick}>
      <span className="tarjeta-menu__nombre">{elemento.nombre}</span>
      <span className="tarjeta-menu__precio">{elemento.precio.toFixed(2)}€</span>
    </div>
  );
};