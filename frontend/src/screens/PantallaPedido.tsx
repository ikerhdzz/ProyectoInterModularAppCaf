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

  const [categorias, setCategorias] = useState<any[]>([]);
  const [categoriaSeleccionada, setCategoriaSeleccionada] = useState<number | null>(null);

  useEffect(() => {
    let mounted = true;

    obtenerDatosMenu('').then(list => {
      if (mounted) setMenu(list);
    });

    const base = (import.meta as any).env?.VITE_API_BASE || '';
    fetch(base + "/api/categorias")
      .then(r => r.json())
      .then(data => mounted && setCategorias(data))
      .catch(() => setCategorias([]));

    return () => { mounted = false; };
  }, []);

  return (
    <div className="aplicacion">
      <header
        className="encabezado"
        onClick={irAStock}
        style={{ cursor: 'pointer' }}
        title="Click para ir a Stock (truco)"
      >
        <h1>CaféApp - IES JOSÉ ZERPA</h1>
        
        <button
          className="btn-darkmode"
          onClick={(e) => {
          e.stopPropagation();
          document.body.classList.toggle("dark-mode");
          }}
        >
        🌙
      </button>


      </header>

      <div className="aplicacion__contenedor">

        <SeccionMenu
          elementosMenu={menu}
          categorias={categorias}
          categoriaSeleccionada={categoriaSeleccionada}
          onCambiarCategoria={setCategoriaSeleccionada}
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
