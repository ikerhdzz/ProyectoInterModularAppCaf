import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { getProductos, crearProducto, eliminarProducto } from "../../services/productosService";

export const DashboardAdmin = () => {
  const { user } = useAuth();
  const [productos, setProductos] = useState<any[]>([]);
  const [nuevo, setNuevo] = useState({ nombre: "", precio: 0 });

  const cargarProductos = async () => {
    try {
      const data = await getProductos();
      setProductos(data);
    } catch (error) {
      console.error("Error cargando productos:", error);
    }
  };

  const handleCrear = async () => {
    if (!user) return;
    await crearProducto(nuevo, user.token);
    setNuevo({ nombre: "", precio: 0 });
    cargarProductos();
  };

  const handleEliminar = async (id: number) => {
    if (!user) return;
    await eliminarProducto(id, user.token);
    cargarProductos();
  };

  useEffect(() => {
    cargarProductos();
  }, []);

  return (
    <div>
      <h1>Panel de Administrador</h1>

      <h2>Crear Producto</h2>
      <input
        type="text"
        placeholder="Nombre"
        value={nuevo.nombre}
        onChange={(e) => setNuevo({ ...nuevo, nombre: e.target.value })}
      />
      <input
        type="number"
        placeholder="Precio"
        value={nuevo.precio}
        onChange={(e) => setNuevo({ ...nuevo, precio: Number(e.target.value) })}
      />
      <button onClick={handleCrear}>Crear</button>

      <h2>Productos</h2>
      {productos.map((p) => (
        <div key={p.id} style={{ border: "1px solid #ccc", padding: 10, marginBottom: 10 }}>
          <p>{p.nombre} — {p.precio}€</p>
          <button onClick={() => handleEliminar(p.id)}>Eliminar</button>
        </div>
      ))}
    </div>
  );
};
