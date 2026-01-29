import React, { useState } from 'react';
import { SeccionMenu } from './componentes/SeccionMenu';
import { SeccionPedido } from './componentes/SeccionPedido';
import type { ElementoMenu, ElementoPedido } from './datos/tipos';
import { datosMenu } from './datos/datosMenu';
import './App.css';

export const App: React.FC = () => {
  const [pedido, setPedido] = useState<ElementoPedido[]>([]);

  const agregarAlPedido = (elemento: ElementoMenu) => {
    const elementoExistente = pedido.find(
      elementoPedido => elementoPedido.nombre === elemento.nombre
    );

    if (elementoExistente) {
      setPedido(
        pedido.map(elementoPedido =>
          elementoPedido.nombre === elemento.nombre
            ? { ...elementoPedido, cantidad: elementoPedido.cantidad + 1 }
            : elementoPedido
        )
      );
    } else {
      setPedido([...pedido, { ...elemento, cantidad: 1 }]);
    }
  };

  const actualizarCantidad = (nombre: string, cantidad: number) => {
    if (cantidad <= 0) {
      setPedido(pedido.filter(elemento => elemento.nombre !== nombre));
    } else {
      setPedido(
        pedido.map(elemento =>
          elemento.nombre === nombre ? { ...elemento, cantidad } : elemento
        )
      );
    }
  };

  const limpiarPedido = () => {
    setPedido([]);
  };

  return (
    <div className="aplicacion">
      <header className="encabezado">
        <h1>Restaurante - IES Lomo de la Herradura</h1>
      </header>

      <div className="aplicacion__contenedor">
        <SeccionMenu
          elementosMenu={datosMenu}
          alAgregarAlPedido={agregarAlPedido}
        />

        <SeccionPedido
          pedido={pedido}
          alActualizarCantidad={actualizarCantidad}
          alLimpiarPedido={limpiarPedido}
        />
      </div>
    </div>
  );
};

export default App;