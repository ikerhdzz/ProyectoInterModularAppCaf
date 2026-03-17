import React from 'react';
import type { ElementoMenu } from '../datos/tipos';
import { useRef } from 'react';

interface Props {
  elementosMenu: ElementoMenu[];
  categorias: { id: number; nombre: string; icono_url?: string }[];
  categoriaSeleccionada: number | null;
  onCambiarCategoria: (id: number | null) => void;
  alSeleccionarProducto: (elemento: ElementoMenu) => void; 
}

export const SeccionMenu: React.FC<Props> = ({
  elementosMenu,
  categorias,
  categoriaSeleccionada,
  onCambiarCategoria,
  alSeleccionarProducto
}) => {


const scrollRef = useRef<HTMLDivElement>(null);
const isDragging = useRef(false);
const startX = useRef(0);
const scrollLeft = useRef(0);
//Agarrar
const onMouseDown = (e: React.MouseEvent) => {
  isDragging.current = true;
  if (!scrollRef.current) return;
  startX.current = e.pageX - scrollRef.current.offsetLeft;
  scrollLeft.current = scrollRef.current.scrollLeft;
};
//Soltar
const onMouseLeaveOrUp = () => {
  isDragging.current = false;
};

//Mover
const onMouseMove = (e: React.MouseEvent) => {
  if (!isDragging.current || !scrollRef.current) return;
  e.preventDefault();
  const x = e.pageX - scrollRef.current.offsetLeft;
  const recorrido = (x - startX.current) * 2;
  scrollRef.current.scrollLeft = scrollLeft.current - recorrido;
};

  return (
    <div className="seccion-menu">
      <h2>Menú</h2>

      {/* CUADRÍCULA DE CATEGORÍAS */}
      <div 
        className="categorias-nav-scroll"
        ref={scrollRef}
        onMouseDown={onMouseDown}
        onMouseLeave={onMouseLeaveOrUp}
        onMouseUp={onMouseLeaveOrUp}
        onMouseMove={onMouseMove}
      >
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
        {elementosMenu.map((item, i) => (
          <div
            key={i}
            className="tarjeta-menu"
            onClick={() => alSeleccionarProducto(item)}
          >
            <img
              src={item.imagenUrl ?? '/img/imagenNoDisponible.jpg'}
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