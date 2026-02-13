import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export const Navbar = () => {
  const { user, logout } = useAuth();

  if (!user) return null;

  return (
    <nav style={{
      display: "flex",
      justifyContent: "space-between",
      padding: "10px",
      background: "#eee",
      marginBottom: "20px",
      borderRadius: 6
    }}>
      <div>
        {user.rol === "ADMIN" && (
          <>
            <Link to="/admin">Panel Admin</Link>{" | "}
          </>
        )}

        {user.rol === "EMPLEADO" && (
          <>
            <Link to="/empleado">Pedidos</Link>{" | "}
          </>
        )}

        {user.rol === "CLIENTE" && (
          <>
            <Link to="/cliente">Menú</Link>{" | "}
            <Link to="/cliente/carrito">Carrito</Link>{" | "}
          </>
        )}
      </div>

      <div>
        <span style={{ marginRight: "10px" }}>
          {user.nombre} ({user.rol})
        </span>
        <button onClick={logout}>Cerrar Sesión</button>
      </div>
    </nav>
  );
};
