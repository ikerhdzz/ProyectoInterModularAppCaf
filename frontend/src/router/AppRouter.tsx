import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

import { Login } from "../auth/Login";
import { DashboardAdmin } from "../pages/admin/DashboardAdmin";
import { DashboardEmpleado } from "../pages/empleado/DashboardEmpleado";
import { DashboardCliente } from "../pages/cliente/DashboardCliente";
import { Carrito } from "../pages/cliente/Carrito";

// Nuevos imports del admin
import { ProductosAdmin } from "../pages/admin/ProductosAdmin";
import { UsuariosAdmin } from "../pages/admin/UsuariosAdmin";

const PrivateRoute = ({
  children,
  role,
}: {
  children: JSX.Element;
  role: string;
}) => {
  const { user } = useAuth();

  if (!user) return <Navigate to="/login" />;
  if (user.rol !== role) return <Navigate to="/login" />;

  return children;
};

export const AppRouter = () => {
  return (
    <Routes>
      {/* Login */}
      <Route path="/login" element={<Login />} />

      {/* Dashboard Admin */}
      <Route
        path="/admin"
        element={
          <PrivateRoute role="ADMIN">
            <DashboardAdmin />
          </PrivateRoute>
        }
      />

      {/* Gestión de productos */}
      <Route
        path="/admin/productos"
        element={
          <PrivateRoute role="ADMIN">
            <ProductosAdmin />
          </PrivateRoute>
        }
      />

      {/* Gestión de usuarios */}
      <Route
        path="/admin/usuarios"
        element={
          <PrivateRoute role="ADMIN">
            <UsuariosAdmin />
          </PrivateRoute>
        }
      />

      {/* Dashboard Empleado */}
      <Route
        path="/empleado"
        element={
          <PrivateRoute role="EMPLEADO">
            <DashboardEmpleado />
          </PrivateRoute>
        }
      />

      {/* Dashboard Cliente */}
      <Route
        path="/cliente"
        element={
          <PrivateRoute role="CLIENTE">
            <DashboardCliente />
          </PrivateRoute>
        }
      />

      {/* Carrito del cliente */}
      <Route
        path="/cliente/carrito"
        element={
          <PrivateRoute role="CLIENTE">
            <Carrito />
          </PrivateRoute>
        }
      />

      {/* Redirección por defecto */}
      <Route path="*" element={<Navigate to="/login" />} />
    </Routes>
  );
};
