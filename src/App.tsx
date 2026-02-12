import React, { useState, useEffect } from 'react';
import { SeccionMenu } from './componentes/SeccionMenu';
import { SeccionPedido } from './componentes/SeccionPedido';
import type { ElementoMenu, ElementoPedido } from './datos/tipos';
import './App.css';

export const App: React.FC = () => {

  // ESTADO DEL MENÚ (productos del backend)
  const [menu, setMenu] = useState<ElementoMenu[]>([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/productos")
      .then(res => res.json())
      .then(data => setMenu(data))
      .catch(err => console.error("Error cargando productos:", err));
  }, []);

  // ESTADO DEL PEDIDO
  const [pedido, setPedido] = useState<ElementoPedido[]>([]);

  const agregarAlPedido = (elemento: ElementoMenu) => {
    const existente = pedido.find(p => p.id === elemento.id);

    if (existente) {
      setPedido(
        pedido.map(p =>
          p.id === elemento.id
            ? { ...p, cantidad: p.cantidad + 1 }
            : p
        )
      );
    } else {
      setPedido([
        ...pedido,
        {
          id: elemento.id,
          nombre: elemento.nombre,
          precio: elemento.precio,
          cantidad: 1
        }
      ]);
    }
  };

  // AHORA SE USA ID EN VEZ DE NOMBRE
  const actualizarCantidad = (id: number, cantidad: number) => {
    if (cantidad <= 0) {
      setPedido(pedido.filter(e => e.id !== id));
    } else {
      setPedido(
        pedido.map(e =>
          e.id === id ? { ...e, cantidad } : e
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
          elementosMenu={menu}
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
