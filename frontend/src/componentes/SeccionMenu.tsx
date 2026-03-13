import React from 'react';
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
          </div>
        ))}
      </div>
    </div>
  );
};
