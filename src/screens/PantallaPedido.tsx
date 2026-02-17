import React, { useEffect, useState } from 'react';
import { SeccionMenu } from '../componentes/SeccionMenu';
import { SeccionPedido } from '../componentes/SeccionPedido';
import type { ElementoMenu, ElementoPedido } from '../datos/tipos';
import { datosMenu, obtenerDatosMenu } from '../datos/datosMenu';
import '../App.css';

interface Props {
  pedido: ElementoPedido[];
  alAgregar: (elemento: ElementoMenu) => void;
  alActualizar: (nombre: string, cantidad: number) => void;
  alLimpiar: () => void;
  alAceptar: () => void;
  manejarSalir: () => void;
  irAStock: () => void; 
}

export const PantallaPedido: React.FC<Props> = ({
  pedido,
  alAgregar,
  alActualizar,
  alLimpiar,
  alAceptar,
  manejarSalir,
  irAStock
}) => {

  const [menu, setMenu] = useState<ElementoMenu[]>(datosMenu);

  useEffect(() => {
    let mounted = true;
    obtenerDatosMenu('').then(list => { if (mounted) setMenu(list); });
    return () => { mounted = false; };
  }, []);

  return (
    <div className="aplicacion">
      <header className="encabezado" onClick={irAStock} style={{cursor: 'pointer'}} title="Click para ir a Stock (truco)">
        <h1>CaféApp - IES JOSÉ ZERPA</h1>
      </header>

      <div className="aplicacion__contenedor">
        <SeccionMenu
          elementosMenu={menu}
          alAgregarAlPedido={alAgregar}
        />

        <SeccionPedido
          pedido={pedido}
          alAceptar={alAceptar}
          alActualizarCantidad={alActualizar}
          alLimpiarPedido={alLimpiar}
          manejarSalir={manejarSalir}
        />
      </div>
    </div>
  );
};

export default PantallaPedido;