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
      <img src={elemento.imagen ?? '/img/imagenNoDisponible.jpg'} alt={elemento.nombre} className="tarjeta-menu__imagen" />
      <div className="tarjeta-menu__texto">
        <span className="tarjeta-menu__nombre">{elemento.nombre}</span>
        {elemento.categoriaNombre && <small style={{opacity:0.9}}>{elemento.categoriaNombre}</small>}
        <span className="tarjeta-menu__precio">{elemento.precio.toFixed(2)}€</span>
      </div>
    </div>
  );
};