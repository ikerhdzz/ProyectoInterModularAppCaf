import React from 'react';
import type { ElementoMenu } from '../datos/tipos';
import { TarjetaMenu } from './TarjetaMenu';

interface PropsSeccionMenu {
  elementosMenu: ElementoMenu[];
  alAgregarAlPedido: (elemento: ElementoMenu) => void;
}

export const SeccionMenu: React.FC<PropsSeccionMenu> = ({
  elementosMenu,
  alAgregarAlPedido
}) => {
  return (
    <div className="seccion-menu">
      <h2>Menú</h2>
      <div className="seccion-menu__cuadricula">
        {elementosMenu.map((elemento, idx) => (
          <TarjetaMenu 
            key={elemento.id ?? `${elemento.nombre}-${idx}`} 
            elemento={elemento}
            alAgregarAlPedido={alAgregarAlPedido}
          />
        ))}
      </div>
    </div>
  );
};