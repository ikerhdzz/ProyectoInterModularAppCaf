import { useCarrito } from "../../context/CarritoContext";

export const Carrito = () => {
  const { carrito } = useCarrito();

  return (
    <div>
      <h1>Tu Carrito</h1>

      {carrito.length === 0 && <p>El carrito está vacío</p>}

      {carrito.map((p, i) => (
        <div key={i} style={{ border: "1px solid #ccc", padding: 10, marginBottom: 10 }}>
          <h3>{p.nombre}</h3>
          <p>Precio: {p.precio}€</p>
        </div>
      ))}
    </div>
  );
};
