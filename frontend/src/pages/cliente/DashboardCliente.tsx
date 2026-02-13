import { useEffect, useState } from "react";
import { getProductos } from "../../services/productosService";
import { useCarrito } from "../../context/CarritoContext";

export const DashboardCliente = () => {
  const [productos, setProductos] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const { agregar } = useCarrito();

  const cargarProductos = async () => {
    try {
      const data = await getProductos();
      setProductos(data);
    } catch (error) {
      console.error("Error cargando productos:", error);
    }
    setLoading(false);
  };

  useEffect(() => {
    cargarProductos();
  }, []);

  if (loading) return <h2>Cargando menú...</h2>;

  return (
    <div>
      <h1>Menú del Cliente</h1>

      {productos.map((p) => (
        <div
          key={p.id}
          style={{
            border: "1px solid #ccc",
            padding: 10,
            marginBottom: 10,
            borderRadius: 6,
          }}
        >
          <h3>{p.nombre}</h3>
          <p>Precio: {p.precio}€</p>
          <button onClick={() => agregar(p)}>Añadir al carrito</button>
        </div>
      ))}
    </div>
  );
};
