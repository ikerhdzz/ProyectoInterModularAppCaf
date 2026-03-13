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

    // Obtener token del localStorage
    const token = localStorage.getItem('token');

    obtenerDatosMenu('', token).then(list => {
      if (mounted) setMenu(list);
    });

    const base = (import.meta as any).env?.VITE_API_BASE || '';
    const headers: Record<string, string> = {};
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    
    fetch(base + "/api/categorias", { headers })
      .then(r => {
        if (!r.ok) {
          console.warn(`Error ${r.status} al obtener categorías`);
          return [];
        }
        return r.json();
      })
      .then(data => {
        if (mounted && Array.isArray(data)) {
          setCategorias(data);
        } else {
          console.warn('Categorías no es un array:', data);
          setCategorias([]);
        }
      })
      .catch(err => {
        console.error('Error al obtener categorías:', err);
        setCategorias([]);
      });

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
