import React, { useEffect, useState } from 'react';
import { datosMenu, obtenerDatosMenu } from '../datos/datosMenu';
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
  const [creating, setCreating] = useState(false);
  const [showAddForm, setShowAddForm] = useState(false);
  const [newNombre, setNewNombre] = useState('');
  const [newPrecio, setNewPrecio] = useState<number>(0);
  const [newStock, setNewStock] = useState<number>(0);
  const [newImageFile, setNewImageFile] = useState<File | null>(null);
  const [newCategoriaId, setNewCategoriaId] = useState<number | undefined>(undefined);
  const [editingImageIndex, setEditingImageIndex] = useState<number | null>(null);
  const [editingImageValue, setEditingImageValue] = useState<string>('');

  useEffect(() => {
    let mounted = true;
    const base = (import.meta as any).env?.VITE_API_BASE || '';
    (async () => {
      try {
        const prods = await obtenerDatosMenu(base);
        const res = await fetch(base + '/api/productos/stock');
        let stocks: any[] = [];
        if (res.ok) {
          try { stocks = await res.json(); } catch (e) { console.error('No JSON en /stock:', e); stocks = []; }
        } else {
          const body = await res.text().catch(() => '<no body>');
          console.error(`Error GET /api/productos/stock ${res.status}:`, body);
          stocks = [];
        }
        const stockMap = new Map<number, number>();
        (stocks || []).forEach((s: any) => stockMap.set(Number(s.id_producto), Number(s.stock)));

        if (!mounted) return;
        setInventario(
          prods.map(item => ({
            ...item,
            stock: stockMap.get(item.id) ?? Math.floor(Math.random() * 50) + 10
          }))
        );
      } catch (e) {
        console.error('Error cargando inventario:', e);
        if (!mounted) return;
        setInventario(datosMenu.map(item => ({ ...item, stock: Math.floor(Math.random() * 50) + 10 })));
      }
    })();
    return () => { mounted = false; };
  }, []);

  const actualizarItem = (index: number, campo: keyof ItemStock, valor: number) => {
    const nuevoInventario = [...inventario];
    // @ts-ignore 
    nuevoInventario[index][campo] = valor;
    setInventario(nuevoInventario);
  };

  const guardarCambios = () => {
    const base = (import.meta as any).env?.VITE_API_BASE || '';
    const actualizar = inventario.map(async item => {
      try {
        // Actualizar precio/nombre en el producto (PUT /api/productos/{id})
        await fetch(base + `/api/productos/${item.id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ nombre: item.nombre, precio: item.precio, imagen: item.imagen ?? '', categoriaId: item.categoriaId ?? null })
        });

        // Actualizar stock en el endpoint dedicado
        await fetch(base + `/api/productos/${item.id}/stock`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ stock: item.stock })
        });
        return { id: item.id, ok: true };
      } catch (e) {
        console.error('Error actualizando item', item, e);
        return { id: item.id, ok: false };
      }
    });

    Promise.all(actualizar).then(results => {
      const fallos = results.filter((r: any) => !r.ok);
      if (fallos.length) {
        alert(`Actualizado con ${fallos.length} fallos. Revisa la consola.`);
      } else {
        alert('Inventario actualizado correctamente.');
      }
    });
  };

  const anadirProducto = () => {
    setNewNombre('');
    setNewPrecio(0);
    setNewStock(0);
    setShowAddForm(true);
  };

  const confirmarAnadir = async () => {
    if (!newNombre.trim()) {
      alert('Introduce un nombre para el producto');
      return;
    }
    if (!confirm('¿Seguro que quieres añadir este producto?')) return;

    const base = (import.meta as any).env?.VITE_API_BASE || '';
    const nuevoProducto: any = { nombre: newNombre.trim(), precio: newPrecio };
    if (newCategoriaId) nuevoProducto.categoriaId = newCategoriaId; else nuevoProducto.categoriaId = 1;

    setCreating(true);
    try {
      const res = await fetch(base + '/api/productos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(nuevoProducto)
      });

      if (!res.ok) {
        const text = await res.text().catch(() => '<no body>');
        console.error('Error creando producto', res.status, text);
        alert(`No se pudo añadir el producto (status ${res.status}). Mira la consola.`);
        setCreating(false);
        return;
      }

      const creado = await res.json();
      const nuevoItem = {
        id: Number(creado.id ?? creado.id_producto ?? Date.now()),
        nombre: String(creado.nombre ?? nuevoProducto.nombre ?? ''),
        precio: Number(creado.precio ?? creado.precio_base ?? 0),
        stock: Number(newStock ?? 0),
        imagen: String(creado.imagen ?? creado.imagen_url ?? '')
      } as ItemStock;

      try {
        await fetch(base + `/api/productos/${nuevoItem.id}/stock`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ stock: nuevoItem.stock })
        });

        // Subir imagen si se seleccionó fichero en el formulario de creación
        if (newImageFile) {
          try {
            const form = new FormData();
            form.append('file', newImageFile);
            const up = await fetch(base + `/api/productos/${nuevoItem.id}/imagen`, { method: 'POST', body: form });
            if (up.ok) {
              const js = await up.json().catch(() => ({}));
              const nuevoImg = js.imagen ?? js.imagen_url ?? js.path ?? js;
              // actualizar inventario local más abajo
              // @ts-ignore
              nuevoItem.imagen = nuevoImg;
            } else {
              console.warn('No se pudo subir imagen tras crear producto', await up.text().catch(() => '<no body>'));
            }
          } catch (e) {
            console.warn('Error subiendo imagen tras crear producto', e);
          }
        }
      } catch (e) {
        console.warn('No se pudo guardar stock tras crear producto:', e);
      }

      setInventario(prev => ([...prev, nuevoItem]));
      setShowAddForm(false);
      setNewImageFile(null);
    } catch (e) {
      console.error('Excepción al crear producto:', e);
      alert('No se pudo añadir el producto (excepción). Mira la consola.');
    } finally {
      setCreating(false);
    }
  };

  const cancelarAnadir = () => { setShowAddForm(false); setNewImageFile(null); };

  const borrarProducto = async (id: number) => {
    const base = (import.meta as any).env?.VITE_API_BASE || '';
    // Si no tiene id válido (p.ej. temporal), borramos solo localmente
    if (!id) {
      if (!confirm('Este producto no está guardado en la base de datos. ¿Eliminar localmente?')) return;
      setInventario(prev => prev.filter(item => item.id !== id));
      return;
    }

    if (!confirm('¿Seguro que quieres borrar este producto?')) return;

    try {
      const res = await fetch(base + `/api/productos/${id}`, {
        method: 'DELETE'
      });

      if (!res.ok) throw new Error('Error borrando producto');

      setInventario(prev => prev.filter(item => item.id !== id));
    } catch (e) {
      console.error(e);
      alert('No se pudo borrar el producto');
    }
  };

  const toggleEditarImagen = (index: number) => {
    if (editingImageIndex === index) {
      setEditingImageIndex(null);
      setEditingImageValue('');
      return;
    }
    setEditingImageIndex(index);
    setEditingImageValue(inventario[index]?.imagen ?? '');
  };

  const guardarImagen = async (index: number) => {
    const item = inventario[index];
    if (!item) return;
    const base = (import.meta as any).env?.VITE_API_BASE || '';
    try {
      const body = { imagen: editingImageValue };
      const res = await fetch(base + `/api/productos/${item.id}`, {
        method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body)
      });
      if (!res.ok) throw new Error('Error guardando imagen');
      // actualizar localmente
      const nuevo = [...inventario];
      // @ts-ignore
      nuevo[index].imagen = editingImageValue;
      setInventario(nuevo);
      setEditingImageIndex(null);
      setEditingImageValue('');
    } catch (e) {
      console.error('No se pudo guardar la imagen:', e);
      alert('Error guardando la imagen. Mira la consola.');
    }
  };

  const quitarImagen = async (index: number) => {
    const item = inventario[index];
    if (!item) return;
    if (!confirm('¿Eliminar la imagen de este producto?')) return;
    const base = (import.meta as any).env?.VITE_API_BASE || '';
    try {
      const res = await fetch(base + `/api/productos/${item.id}`, {
        method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ imagen: '' })
      });
      if (!res.ok) throw new Error('Error eliminando imagen');
      const nuevo = [...inventario];
      // @ts-ignore
      nuevo[index].imagen = '';
      setInventario(nuevo);
      setEditingImageIndex(null);
      setEditingImageValue('');
    } catch (e) {
      console.error('No se pudo eliminar la imagen:', e);
      alert('Error eliminando la imagen. Mira la consola.');
    }
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
              <button className="btn-anadir" onClick={anadirProducto}>Añadir producto</button>
            </div>
          </div>

          {showAddForm && (
            <div className="form-nuevo">
              <h3>Detalles del Nuevo Producto</h3> {/* Opcional: añade un subtítulo */}
              <div>
                <label>Nombre del Producto</label>
                <input
                  placeholder="Ej: Café con leche"
                  value={newNombre}
                  onChange={(e) => setNewNombre(e.target.value)}
                />
              </div>
              <div>
                <label>Imagen (opcional)</label>
                <input type="file" accept="image/*" onChange={(e) => setNewImageFile(e.target.files ? e.target.files[0] : null)} />
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px' }}>
                <div>
                  <label>Precio (€)</label>
                  <input
                    type="number"
                    step="0.01"
                    value={newPrecio}
                    onChange={(e) => setNewPrecio(parseFloat(e.target.value) || 0)}
                  />
                </div>
                <div>
                  <label>Stock Inicial</label>
                  <input
                    type="number"
                    value={newStock}
                    onChange={(e) => setNewStock(parseInt(e.target.value) || 0)}
                  />
                </div>
                <div>
                  <label>Categoria Id (opcional)</label>
                  <input
                    type="number"
                    value={newCategoriaId ?? ''}
                    placeholder="Ej: 1"
                    onChange={(e) => setNewCategoriaId(e.target.value ? parseInt(e.target.value) : undefined)}
                  />
                </div>
              </div>

              <div className="botones-form"> {/* Añadida esta clase */}
                <button className="btn-guardar" onClick={confirmarAnadir} disabled={creating}>
                  {creating ? 'Guardando...' : 'Confirmar Registro'}
                </button>
                <button className="btn-cancelar" onClick={cancelarAnadir} disabled={creating}>
                  Cancelar
                </button>
              </div>
            </div>
          )}

          <div className="grid-stock">
            {/* Encabezados de la tabla (ocultos en móvil) */}
            <div className="fila-stock encabezado-fila">
              <span>Producto</span>
              <span>Precio (€)</span>
              <span>Stock (Uds)</span>
            </div>

            {inventario.map((item, index) => (
              <div key={item.id ?? index} className="fila-stock">
                <div className="celda-nombre">
                  <span className="etiqueta-movil">Producto:</span>
                  <img src={item.imagen ?? '/img/imagenNoDisponible.jpg'} alt={item.nombre} className="mini-imagen" />
                  <strong style={{marginLeft:8}}>{item.nombre}</strong>
                  {item.categoriaNombre && <div style={{fontSize:12, color:'#6b7280', marginTop:6}}>{item.categoriaNombre}</div>}
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
                <div className="celda-input">
                  <span className="etiqueta-movil">Cat. Id:</span>
                  <input
                    type="number"
                    min="0"
                    value={item.categoriaId ?? ''}
                    onChange={(e) => {
                      const val = e.target.value ? parseInt(e.target.value) : undefined;
                      const nuevo = [...inventario];
                      // @ts-ignore
                      nuevo[index].categoriaId = val;
                      setInventario(nuevo);
                    }}
                  />
                </div>
                <div className="celda-acciones">
                  <div style={{display: 'flex', flexDirection: 'column', gap: 8}}>
                    <button className="btn-borrar" onClick={() => borrarProducto(item.id)}>Borrar</button>
                    <button className="btn-guardar" onClick={() => toggleEditarImagen(index)} style={{padding:'8px 12px', fontSize:14}}>
                      {editingImageIndex === index ? 'Cancelar imagen' : 'Gestionar imagen'}
                    </button>
                  </div>
                </div>
                {editingImageIndex === index && (
                  <div className="imagen-editor" style={{gridColumn: '1 / -1'}}>
                    <input type="file" accept="image/*" onChange={(e) => {
                      const f = e.target.files ? e.target.files[0] : null;
                      if (!f) return;
                      // subir inmediatamente
                      (async () => {
                        const base = (import.meta as any).env?.VITE_API_BASE || '';
                        const form = new FormData();
                        form.append('file', f);
                        try {
                          const res = await fetch(base + `/api/productos/${inventario[index].id}/imagen`, {
                            method: 'POST', body: form
                          });
                          if (!res.ok) throw new Error('Upload failed');
                          const json = await res.json();
                          const nuevo = [...inventario];
                          // @ts-ignore
                          nuevo[index].imagen = json.imagen ?? json.imagen_url ?? json.imagenUrl ?? json.image ?? json;
                          setInventario(nuevo);
                          setEditingImageIndex(null);
                        } catch (err) {
                          console.error('Error subiendo imagen:', err);
                          alert('No se pudo subir la imagen. Mira la consola.');
                        }
                      })();
                    }} />
                    <button className="btn-remove-img" onClick={() => quitarImagen(index)}>Eliminar</button>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default PantallaStock;