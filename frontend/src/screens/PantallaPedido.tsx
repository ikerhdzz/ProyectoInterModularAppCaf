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
  const [productoViendo, setProductoViendo] = useState<ElementoMenu | null>(null);
  const [productoModificando, setProductoModificando] = useState<ElementoMenu | null>(null);
  const [vistaActual, setVistaActual] = useState<'menu' | 'carrito'>('menu');

  useEffect(() => {
    let mounted = true;
    const token = localStorage.getItem('token');

    const base = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:8080';
    const headers: Record<string, string> = {
      'Authorization': token ? `Bearer ${token}` : ''
    };
    //PRODUCTOS
    fetch(`${base}/api/productos`, { headers })
      .then(res => res.json())
      .then(data => {
        if (mounted) {
          const productosAdaptados = data.map((p: any) => ({
            ...p,
            precio: p.precioBase,
            categoriaId: p.categoriaId || (p.categoria && p.categoria.id) || p.categoria_id
          }));
          setMenu(productosAdaptados);
        }
      })
      .catch(err => {
        console.error("Fallo al cargar productos de la base de datos:", err);
      });
    //CATEGORIAS
    fetch(`${base}/api/categorias`, { headers })
      .then(r => {
        if (!r.ok) {
          console.warn(`Error ${r.status} al obtener categorías`);
          return [];
        }
        return r.json();
      })
      .then(data => {

        if (!mounted) return; 

        if (Array.isArray(data)) {
          setCategorias(data);
          console.log("Categorías guardadas correctamente:", data);
        } else {
          console.warn('El backend no devolvió un array de categorías:', data);
          setCategorias([]);
        }
      })
      .catch(err => {
        if (!mounted) return;
        console.error('Error al obtener categorías:', err);
        setCategorias([]);
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

  // ============================================================
  // LÓGICA DE FILTRADO DE CATEGORÍAS
  // ============================================================
  const menuFinal = menuSeguro.filter(producto => {

    if (categoriaSeleccionada === null) return true;

    const categoriaPulsada = categorias.find(c => c.id === categoriaSeleccionada);

    return categoriaPulsada && producto.categoria === categoriaPulsada.nombre;
  });

  console.log("Mira cómo es un producto por dentro:", menuSeguro[0]);

  return (
    <div className="aplicacion">
      <header className="encabezado" onClick={irAStock} style={{ cursor: 'pointer' }}>
        <h1>CaféApp - IES JOSÉ ZERPA</h1>

          {/* Modo oscuro */}
          <button className="btn-darkmode" onClick={(e) => {
            e.stopPropagation();
            document.body.classList.toggle("dark-mode");
          }}>🌙</button>
      </header>

      <main className="contenido-principal">
        {/* Sección menú */}
        {vistaActual === 'menu' ? (
          <SeccionMenu
            elementosMenu={menuFinal}
            categorias={categorias}
            categoriaSeleccionada={categoriaSeleccionada}
            onCambiarCategoria={setCategoriaSeleccionada}
            alSeleccionarProducto={setProductoViendo}
            cantidadPedido={pedido.length}
            onVerPedido={() => setVistaActual('carrito')}
          />
        ) : (
          <div className="contenedor-pedido-pantalla">
             {/* Pedido */}
             <SeccionPedido
                pedido={pedido}
                alAceptar={alAceptar}
                alActualizarCantidad={alActualizar}
                alLimpiarPedido={alLimpiar}
                manejarSalir={() => setVistaActual('menu')}
                onVolver={() => setVistaActual('menu')}
              />
          </div>
        )}
      </main>

      {/* =========================================
                          PRODUCTO
          ========================================= */}
      {productoViendo && (
        <div className="modal-overlay" onClick={() => setProductoViendo(null)}>
          {/* Evitamos que el clic dentro de la tarjeta cierre el modal */}
          <div className="modal-tarjeta" onClick={(e) => e.stopPropagation()}>
            <button className="modal-cerrar" onClick={() => setProductoViendo(null)}>
              ✕
            </button>
            
            <img 
              src={productoViendo.imagenUrl ?? '/img/imagenNoDisponible.jpg'} 
              alt={productoViendo.nombre} 
              className="modal-imagen"
            />
            
            <div className="modal-info">
              <h2>{productoViendo.nombre}</h2>
              <p className="modal-precio">{productoViendo.precio.toFixed(2)}€</p>
              
              {productoViendo.descripcion && (
                <p className="modal-descripcion">{productoViendo.descripcion}</p>
              )}

              {/* Botones de acción del primer modal */}
              <div className="modal-botones">

                <button 
                  className="btn-modificar-modal"
                  onClick={() => {
                    setProductoModificando(productoViendo);
                    setProductoViendo(null); 
                  }}
                >
                  Modificar producto
                </button>
                
                <button 
                  className="btn-añadir-modal"
                  onClick={() => {
                    alAgregar(productoViendo);
                    setProductoViendo(null);
                  }}
                >
                  Añadir al pedido
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* =========================================
                    MODIFICAR PRODUCTO 
          ========================================= */}
      {productoModificando && (
        <div className="modal-overlay" onClick={() => setProductoModificando(null)}>
          <div className="modal-tarjeta" onClick={(e) => e.stopPropagation()}>
            
            {/* Botón para volver al modal anterior */}
            <button 
              className="modal-cerrar" 
              onClick={() => {
                setProductoViendo(productoModificando);
                setProductoModificando(null);
              }}
            >
              ←
            </button>
            
            <img 
              src={productoModificando.imagenUrl ?? '/img/imagenNoDisponible.jpg'} 
              alt={productoModificando.nombre} 
              className="modal-imagen"
            />
            
            <div className="modal-info">
              <h2>{productoModificando.nombre}</h2>
              <p className="modal-precio">{productoModificando.precio.toFixed(2)}€</p>
              
              <button 
                className="btn-añadir-modal"
                onClick={() => {
                  alAgregar(productoModificando);
                  setProductoModificando(null);
                }}
              >
                Confirmar y Añadir
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default PantallaPedido;