import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

import { Login } from "../auth/Login";
import { DashboardAdmin } from "../pages/admin/DashboardAdmin";
import { DashboardEmpleado } from "../pages/empleado/DashboardEmpleado";
import { DashboardCliente } from "../pages/cliente/DashboardCliente";
import { Carrito } from "../pages/cliente/Carrito";

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
      <Route path="/login" element={<Login />} />

      <Route
        path="/admin"
        element={
          <PrivateRoute role="ADMIN">
            <DashboardAdmin />
          </PrivateRoute>
        }
      />

      <Route
        path="/empleado"
        element={
          <PrivateRoute role="EMPLEADO">
            <DashboardEmpleado />
          </PrivateRoute>
        }
      />

      <Route
        path="/cliente"
        element={
          <PrivateRoute role="CLIENTE">
            <DashboardCliente />
          </PrivateRoute>
        }
      />

      <Route
        path="/cliente/carrito"
        element={
          <PrivateRoute role="CLIENTE">
            <Carrito />
          </PrivateRoute>
        }
      />

      <Route path="*" element={<Navigate to="/login" />} />
    </Routes>
  );
};
