import React, { useState } from 'react';
import { datosMenu } from '../datos/datosMenu';
import type { ElementoMenu } from '../datos/tipos';

// Extendemos el tipo localmente para simular el stock
interface ItemStock extends ElementoMenu {
  stock: number;
}

interface Props {
  alSalir: () => void;
}

export const PantallaStock: React.FC<Props> = ({ alSalir }) => {
  // Inicializamos el estado con los datos del menú y un stock simulado de 50
  const [inventario, setInventario] = useState<ItemStock[]>(
    datosMenu.map(item => ({ ...item, stock: Math.floor(Math.random() * 50) + 10 }))
  );

  const actualizarItem = (index: number, campo: keyof ItemStock, valor: number) => {
    const nuevoInventario = [...inventario];
    // @ts-ignore 
    nuevoInventario[index][campo] = valor;
    setInventario(nuevoInventario);
  };

  const guardarCambios = () => {
    // Cambios al backend
    console.log("Datos a enviar al backend:", inventario);
    alert("Inventario actualizado correctamente (Simulado)");
  };

  return (
    <div className="aplicacion">
      <header className="encabezado">
        <h1>Gestión de Stock y Precios</h1>
      </header>

      <div className="aplicacion__contenedor contenedor-stock">
        <div className="panel-stock">
          <div className="cabecera-stock">
            <h2>Inventario Actual</h2>
            <div className="acciones-stock">
              <button className="btn-cancelar" onClick={alSalir}>Salir</button>
              <button className="btn-guardar" onClick={guardarCambios}>Guardar Cambios</button>
            </div>
          </div>

          <div className="grid-stock">
            {/* Encabezados de la tabla (ocultos en móvil) */}
            <div className="fila-stock encabezado-fila">
              <span>Producto</span>
              <span>Precio (€)</span>
              <span>Stock (Uds)</span>
            </div>

            {inventario.map((item, index) => (
              <div key={index} className="fila-stock">
                <div className="celda-nombre">
                  <span className="etiqueta-movil">Producto:</span>
                  <strong>{item.nombre}</strong>
                </div>
                
                <div className="celda-input">
                  <span className="etiqueta-movil">Precio:</span>
                  <input 
                    type="number" 
                    step="0.10"
                    min="0"
                    value={item.precio}
                    onChange={(e) => actualizarItem(index, 'precio', parseFloat(e.target.value) || 0)}
                  />
                  <span className="sufijo">€</span>
                </div>

                <div className="celda-input">
                  <span className="etiqueta-movil">Stock:</span>
                  <input 
                    type="number" 
                    min="0"
                    value={item.stock}
                    onChange={(e) => actualizarItem(index, 'stock', parseInt(e.target.value) || 0)}
                  />
                  <span className="sufijo">uds</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default PantallaStock;