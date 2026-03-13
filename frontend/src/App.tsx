import React, { useEffect, useState } from 'react';
import type { Pantalla, ElementoPedido, ElementoMenu } from './datos/tipos';
import './App.css';

import PantallaPedido from './screens/PantallaPedido';
import PantallaPago from './screens/PantallaPago';
import PantallaStock from './screens/PantallaStock';
import PantallaLogin from './screens/PantallaLogin';
import PantallaRegistro from './screens/PantallaRegistro';
import PantallaCocina from './screens/PantallaCocina';

import PantallaAdmin from './screens/PantallaAdmin';
import PantallaEmpleados from './screens/PantallaEmpleados';
import PantallaPedidosAdmin from './screens/PantallaPedidosAdmin';
import PantallaEstadisticas from './screens/PantallaEstadisticas';

import SelectorCentro from './componentes/SelectorCentro';

export const App: React.FC = () => {

  const [pantalla, setPantalla] = useState<Pantalla>("login");
  const [token, setToken] = useState<string | null>(localStorage.getItem("token"));
  const [usuario, setUsuario] = useState<any>(null);

  const [pedido, setPedido] = useState<ElementoPedido[]>([]);

  // Centro seleccionado SOLO para admin
  const [centroSeleccionado, setCentroSeleccionado] = useState<number>(1);

  // ============================================================
  // 1. LOGIN / REGISTRO
  // ============================================================

  const manejarLoginExitoso = (loginData: { token: string; usuario: any } | string) => {
    let token: string;
    let usuario: any = null;

    if (typeof loginData === 'string') {
      token = loginData;
    } else {
      token = loginData.token;
      usuario = loginData.usuario;
    }

    // Normalizar rol si viene como string en vez de objeto
    if (usuario && typeof usuario.rol === 'string') {
      const mapaRoles: Record<string, number> = {
        'admin': 1,
        'empleado': 2,
        'cliente': 3,
      };
      usuario = {
        ...usuario,
        rol: { id: mapaRoles[usuario.rol] ?? 3 }
      };
    }

    setToken(token);
    if (usuario) {
      setUsuario(usuario);
      localStorage.setItem("token", token);

      const rol = usuario.rol.id;
      console.log('🎭 Rol detectado:', rol);

      if (rol === 3) setPantalla("menu");
      else if (rol === 2) setPantalla("cocina");
      else if (rol === 1) setPantalla("admin");
    }
  };

  const manejarLogout = () => {
    setToken(null);
    setUsuario(null);
    localStorage.removeItem("token");
    setPantalla("login");
    setPedido([]);
  };

  // ============================================================
  // 2. LÓGICA DEL PEDIDO
  // ============================================================

  const agregarAlPedido = (elemento: ElementoMenu) => {
    const existente = pedido.find(item => item.nombre === elemento.nombre);

    if (existente) {
      setPedido(
        pedido.map(item =>
          item.nombre === elemento.nombre
            ? { ...item, cantidad: item.cantidad + 1 }
            : item
        )
      );
    } else {
      setPedido([...pedido, { ...elemento, cantidad: 1 }]);
    }
  };

  const actualizarCantidad = (nombre: string, cantidad: number) => {
    if (cantidad <= 0) {
      setPedido(pedido.filter(item => item.nombre !== nombre));
    } else {
      setPedido(
        pedido.map(item =>
          item.nombre === nombre ? { ...item, cantidad } : item
        )
      );
    }
  };

  const limpiarPedido = () => setPedido([]);

  const calcularTotal = (): number => {
    const subtotal = pedido.reduce((t, item) => t + item.precio * item.cantidad, 0);
    const impuesto = subtotal * 0.07;
    return subtotal + impuesto;
  };

  // ============================================================
  // 3. NAVEGACIÓN ENTRE PANTALLAS
  // ============================================================

  const irAPago = () => {
    if (pedido.length === 0) {
      alert("El pedido está vacío");
      return;
    }
    if (window.confirm("¿Confirmar pedido e ir a pagar?")) {
      setPantalla("pago");
    }
  };

  const salir = () => {
    if (window.confirm("¿Seguro que quieres volver al inicio/salir?")) {
      setPantalla("menu");
      limpiarPedido();
    }
  };

  const confirmarPagoExitoso = () => {
    limpiarPedido();
    setPantalla("menu");
  };

  // ============================================================
  // 4. AUTOLOGIN SI YA HAY TOKEN
  // ============================================================

  useEffect(() => {
    const cargarUsuario = async () => {
      const BASE = (import.meta as any).env?.VITE_API_BASE || "";

      if (!token) return;

      try {
        const res = await fetch(BASE + "/api/auth/me", {
          headers: { Authorization: `Bearer ${token}` }
        });

        if (!res.ok) {
          manejarLogout();
          return;
        }

        const usuario = await res.json();
        setUsuario(usuario);

        const rol = usuario.rol.id;

        if (rol === 3) setPantalla("menu");
        if (rol === 2) setPantalla("cocina");
        if (rol === 1) setPantalla("admin"); // admin → PantallaAdmin

      } catch (e) {
        manejarLogout();
      }
    };

    cargarUsuario();
  }, []);

  // ============================================================
  // 5. RENDERIZADO DE PANTALLAS
  // ============================================================

  return (
    <>

      {/* Selector de centro SOLO para admin */}
      {usuario?.rol?.id === 1 && (
        <SelectorCentro
          centroId={centroSeleccionado}
          onChange={setCentroSeleccionado}
        />
      )}

      {pantalla === "login" && (
        <PantallaLogin
          alLoginExitoso={manejarLoginExitoso}
          irARegistro={() => setPantalla("registro")}
        />
      )}

      {pantalla === "registro" && (
        <PantallaRegistro
          alRegistroExitoso={() => setPantalla("login")}
          irALogin={() => setPantalla("login")}
        />
      )}

      {/* CLIENTE */}
      {pantalla === "menu" && usuario && usuario.rol.id === 3 && (
        <PantallaPedido
          pedido={pedido}
          alAgregar={agregarAlPedido}
          alActualizar={actualizarCantidad}
          alLimpiar={limpiarPedido}
          alAceptar={irAPago}
          manejarSalir={salir}
          irAStock={() => setPantalla("stock")}
        />
      )}

      {pantalla === "pago" && (
        <PantallaPago
          pedido={pedido}
          total={calcularTotal()}
          alSalir={() => setPantalla("menu")}
          alConfirmarPago={confirmarPagoExitoso}
        />
      )}

      {/* ADMIN → PantallaAdmin */}
      {pantalla === "admin" && usuario && usuario.rol.id === 1 && (
        <PantallaAdmin
          irAStock={() => setPantalla("stock")}
          irAPedidos={() => setPantalla("pedidosAdmin")}
          irAEmpleados={() => setPantalla("empleados")}
          irAEstadisticas={() => setPantalla("estadisticas")}
          alSalir={manejarLogout}
        />
      )}

      {/* ADMIN → Gestión de Stock */}
      {pantalla === "stock" && usuario && usuario.rol.id === 1 && (
        <PantallaStock
          centroId={centroSeleccionado}
          alSalir={() => setPantalla("admin")}
        />
      )}

      {/* ADMIN → Gestión de Pedidos */}
      {pantalla === "pedidosAdmin" && usuario && usuario.rol.id === 1 && (
        <PantallaPedidosAdmin
          centroId={centroSeleccionado}
          alSalir={() => setPantalla("admin")}
        />
      )}

      {/* ADMIN → Gestión de Empleados */}
      {pantalla === "empleados" && usuario && usuario.rol.id === 1 && (
        <PantallaEmpleados
          centroId={centroSeleccionado}
          alSalir={() => setPantalla("admin")}
        />
      )}

      {/* ADMIN → Estadísticas */}
      {pantalla === "estadisticas" && usuario && usuario.rol.id === 1 && (
        <PantallaEstadisticas
          centroId={centroSeleccionado}
          alSalir={() => setPantalla("admin")}
        />
      )}

      {/*  EMPLEADO */}
      {pantalla === "cocina" && usuario && usuario.rol.id === 2 && (
        <PantallaCocina
          centroId={usuario.centro?.id}
          alSalir={() => setPantalla("menu")}
        />
      )}

    </>
  );
};

export default App;
