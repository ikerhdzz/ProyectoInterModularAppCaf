import React, { useEffect, useState } from 'react';
import { SeccionMenu } from '../componentes/SeccionMenu';
import { SeccionPedido } from '../componentes/SeccionPedido';
import type { ElementoMenu, ElementoPedido } from '../datos/tipos';
import '../App.css';

interface Props {
  pedido: ElementoPedido[];
  alergenosUsuario: number[];
  alAgregar: (elemento: ElementoMenu) => void;
  alActualizar: (nombre: string, cantidad: number) => void;
  alLimpiar: () => void;
  alAceptar: () => void;
  manejarSalir: () => void;
  irAStock: () => void;
}

export const PantallaPedido: React.FC<Props> = ({
  pedido,
  alergenosUsuario,
  alAgregar,
  alActualizar,
  alLimpiar,
  alAceptar,
  manejarSalir,
  irAStock
}) => {

  const [menu, setMenu] = useState<ElementoMenu[]>([]);
  const [categorias, setCategorias] = useState<any[]>([]);
  const [categoriaSeleccionada, setCategoriaSeleccionada] = useState<number | null>(null);

  useEffect(() => {
    let mounted = true;
    const token = localStorage.getItem('token');

    const base = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:8080';
    const headers: Record<string, string> = {
      'Authorization': token ? `Bearer ${token}` : ''
    };
    
    fetch(`${base}/api/productos`, {
      headers: { 'Authorization': `Bearer ${token}` }
    })
      .then(res => res.json())
      .then(data => {
        if (mounted) {
          const productosAdaptados = data.map((p: any) => ({
            ...p,
            precio: p.precioBase
          }));
          
          console.log("Productos adaptados:", productosAdaptados);
          setMenu(productosAdaptados);
        }
      })
      .catch(err => {
        console.error("Fallo al cargar productos de la base de datos:", err);
      });

    return () => { mounted = false; };
  }, []);

  // ============================================================
  // LÓGICA DE FILTRADO DE ALÉRGENOS (POR ID)
  // ============================================================
  const menuSeguro = menu.length > 0 
    ? menu.filter(producto => {
        // Si el producto no tiene alérgenos registrados, es seguro
        if (producto.alergenosIds === undefined) return true;
         
        if (producto.alergenosIds.length === 0) return true;

        // Si alguno de los IDs de alérgenos del producto coincide 
        // con los IDs que el usuario marcó, el producto es peligroso.
        const esPeligroso = producto.alergenosIds.some(idAlergenoProducto => 
          alergenosUsuario.includes(idAlergenoProducto)
        );

        return !esPeligroso;
      })
    : [];

  return (
    <div className="aplicacion">
      <header className="encabezado" onClick={irAStock} style={{ cursor: 'pointer' }}>
        <h1>CaféApp - IES JOSÉ ZERPA</h1>
        <button className="btn-darkmode" onClick={(e) => {
          e.stopPropagation();
          document.body.classList.toggle("dark-mode");
        }}>🌙</button>
      </header>

      <div className="aplicacion__contenedor">
        <SeccionMenu
          elementosMenu={menuSeguro}
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