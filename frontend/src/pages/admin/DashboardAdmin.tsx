import { Link } from "react-router-dom";

export const DashboardAdmin = () => {
  return (
    <div style={{ padding: "20px" }}>
      <h1>Panel del Administrador</h1>

      <div style={{
        display: "flex",
        gap: "20px",
        marginTop: "30px",
        flexWrap: "wrap"
      }}>
        
        <Link to="/admin/productos" style={cardStyle}>
          <h2>Productos</h2>
          <p>Gestionar productos del menú</p>
        </Link>

        <Link to="/admin/usuarios" style={cardStyle}>
          <h2>Usuarios</h2>
          <p>Gestionar usuarios del sistema</p>
        </Link>

      </div>
    </div>
  );
};

const cardStyle: React.CSSProperties = {
  padding: "20px",
  border: "1px solid #ccc",
  borderRadius: "10px",
  width: "250px",
  textDecoration: "none",
  color: "black",
  background: "#f9f9f9",
  transition: "0.2s",
};
