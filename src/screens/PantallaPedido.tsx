import React from 'react';
import { SeccionMenu } from '../componentes/SeccionMenu';
import { SeccionPedido } from '../componentes/SeccionPedido';
import type { ElementoMenu, ElementoPedido } from '../datos/tipos';
import { datosMenu } from '../datos/datosMenu';
import '../App.css';

// NUEVA INTERFAZ DE PROPS
interface Props {
  pedido: ElementoPedido[];
  alAgregar: (elemento: ElementoMenu) => void;
  alActualizar: (nombre: string, cantidad: number) => void;
  alLimpiar: () => void;
  alAceptar: () => void;
  manejarSalir: () => void;
  irAStock: () => void; // Opcional si quieres un botón para ir a stock
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
  // YA NO HAY useState AQUÍ, TODO VIENE DE APP

  return (
    <div className="aplicacion">
      <header className="encabezado" onClick={irAStock} style={{cursor: 'pointer'}} title="Click para ir a Stock (truco)">
        <h1>CaféApp - IES JOSÉ ZERPA</h1>
      </header>

      <div className="aplicacion__contenedor">
        <SeccionMenu
          elementosMenu={datosMenu}
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