import React, { useState } from 'react';
import type { ElementoMenu } from '../datos/tipos';

interface Props {
  elementosMenu: ElementoMenu[];
  categorias: { id: number; nombre: string; icono_url?: string }[];
  categoriaSeleccionada: number | null;
  onCambiarCategoria: (id: number | null) => void;
  alAgregarAlPedido: (elemento: ElementoMenu) => void;
}

export const SeccionMenu: React.FC<Props> = ({
  elementosMenu,
  categorias,
  categoriaSeleccionada,
  onCambiarCategoria,
  alAgregarAlPedido
}) => {
  // 1. Estado para controlar qué producto se muestra en la ventana de detalles
  const [productoModal, setProductoModal] = useState<ElementoMenu | null>(null);

  const elementosFiltrados = categoriaSeleccionada
    ? elementosMenu.filter(e => e.categoriaId === categoriaSeleccionada)
    : elementosMenu;

  return (
    <div className="seccion-menu">
      <h2>Menú</h2>

      {/* CUADRÍCULA DE CATEGORÍAS */}
      <div className="categorias-grid">
        <div
          className={`categoria-card ${!categoriaSeleccionada ? "cat-activa" : ""}`}
          onClick={() => onCambiarCategoria(null)}
        >
          <span>Todo</span>
        </div>

        {categorias.map(cat => (
          <div
            key={cat.id}
            className={`categoria-card ${categoriaSeleccionada === cat.id ? "cat-activa" : ""}`}
            onClick={() => onCambiarCategoria(cat.id)}
          >
            <span>{cat.nombre}</span>
          </div>
        ))}
      </div>

      {/* PRODUCTOS */}
      <div className="seccion-menu__cuadricula">
        {elementosFiltrados.map((item, i) => (
          <div
            key={i}
            className="tarjeta-menu"
            onClick={() => alAgregarAlPedido(item)}
          >
            <img
              src={item.imagen ?? '/img/imagenNoDisponible.jpg'}
              alt={item.nombre}
              className="tarjeta-menu__imagen"
            />

            <div className="tarjeta-menu__texto">
              <span className="tarjeta-menu__nombre">{item.nombre}</span>
              <span className="tarjeta-menu__precio">
                {item.precio.toFixed(2)}€
              </span>
            </div>

            {/* 2. Botón de Detalles */}
            <button 
              className="boton-detalles"
              onClick={(e) => {
                e.stopPropagation(); // Evita que se añada al pedido al hacer clic aquí
                setProductoModal(item); // Abre la ventana con este producto
              }}
            >
              Detalles
            </button>
          </div>
        ))}
      </div>

      {/* 3. VENTANA FLOTANTE (MODAL) DE DETALLES */}
      {productoModal && (
        <div className="modal-overlay" onClick={() => setProductoModal(null)}>
          <div 
            className="modal-contenido" 
            onClick={(e) => e.stopPropagation()} // Evita que se cierre al hacer clic dentro de la tarjeta blanca
          >
            <button className="modal-cerrar" onClick={() => setProductoModal(null)}>
              ✖
            </button>
            
            <img
              src={productoModal.imagen ?? '/img/imagenNoDisponible.jpg'}
              alt={productoModal.nombre}
              className="modal-imagen"
            />
            
            <h3 className="modal-titulo">{productoModal.nombre}</h3>
            <p className="modal-precio">{productoModal.precio.toFixed(2)}€</p>
            
            {/* Si tu tipo ElementoMenu tiene una descripción u otros datos, ponlos aquí */}
            {/* <p className="modal-descripcion">{productoModal.descripcion}</p> */}

            <button 
              className="modal-boton-añadir"
              onClick={() => {
                alAgregarAlPedido(productoModal);
                setProductoModal(null); // Cierra el modal después de añadirlo
              }}
            >
              Añadir al pedido
            </button>
          </div>
        </div>
      )}
    </div>
  );
};