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
import PantallaAlergenos from './screens/PantallaAlergenos';

import SelectorCentro from './componentes/SelectorCentro';

export const App: React.FC = () => {

  const [pantalla, setPantalla] = useState<Pantalla>("login");
  const [token, setToken] = useState<string | null>(localStorage.getItem("token"));
  const [usuario, setUsuario] = useState<any>(null);

  const [pedido, setPedido] = useState<ElementoPedido[]>([]);

  // Estado para guardar los alérgenos del usuario actual
  const [alergenosUsuario, setAlergenosUsuario] = useState<number[]>([]);

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

      // Si es cliente (3), lo mandamos primero a declarar alérgenos
      if (rol === 3) setPantalla("alergenos"); 
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
    setAlergenosUsuario([]); // Limpiamos alérgenos al salir
  };

  // ============================================================
  // LÓGICAS DE PEDIDO
  // ============================================================

  const agregarAlPedido = (elemento: ElementoMenu) => {
    const existente = pedido.find(item => item.nombre === elemento.nombre);
    if (existente) {
      setPedido(pedido.map(item => item.nombre === elemento.nombre ? { ...item, cantidad: item.cantidad + 1 } : item));
    } else {
      setPedido([...pedido, { ...elemento, cantidad: 1 }]);
    }
  };

  const actualizarCantidad = (nombre: string, cantidad: number) => {
    if (cantidad <= 0) {
      setPedido(pedido.filter(item => item.nombre !== nombre));
    } else {
      setPedido(pedido.map(item => item.nombre === nombre ? { ...item, cantidad } : item));
    }
  };

  const limpiarPedido = () => setPedido([]);

  const calcularTotal = (): number => {
    const subtotal = pedido.reduce((t, item) => t + item.precio * item.cantidad, 0);
    const impuesto = subtotal * 0.07;
    return subtotal + impuesto;
  };

  // ============================================================
  // NAVEGACIÓN
  // ============================================================

  const irAPago = () => {
    if (pedido.length === 0) return alert("El pedido está vacío");
    if (window.confirm("¿Confirmar pedido e ir a pagar?")) setPantalla("pago");
  };

  const salir = () => {
    if (window.confirm("¿Seguro que quieres cerrar sesión?")) {
      manejarLogout();
    }
  };

  const confirmarPagoExitoso = () => {
    limpiarPedido();
    setPantalla("menu");
  };

  // ============================================================
  // AUTOLOGIN
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

        let usuarioRes = await res.json();

        // Normalizamos el rol
        if (usuarioRes && typeof usuarioRes.rol === 'string') {
          const mapaRoles: Record<string, number> = {
            'admin': 1,
            'empleado': 2,
            'cliente': 3,
          };
          usuarioRes = {
            ...usuarioRes,
            rol: { id: mapaRoles[usuarioRes.rol] ?? 3 }
          };
        }

        setUsuario(usuarioRes);

        const rol = usuarioRes.rol.id;

        // Solo cambiamos la pantalla si el usuario acaba de entrar
        setPantalla((pantallaActual) => {
          if (pantallaActual === "login" || pantallaActual === "registro") {
            if (rol === 3) return "alergenos";
            if (rol === 2) return "cocina";
            if (rol === 1) return "admin";
          }
          return pantallaActual;
        });

      } catch (e) {
        manejarLogout();
      }
    };
    cargarUsuario();
  }, [token]);

  // ============================================================
  // RENDERIZADO
  // ============================================================

  return (
    <>
      {usuario?.rol?.id === 1 && (
        <SelectorCentro centroId={centroSeleccionado} onChange={setCentroSeleccionado} />
      )}

      {pantalla === "login" && (
        <PantallaLogin alLoginExitoso={manejarLoginExitoso} irARegistro={() => setPantalla("registro")} />
      )}

      {pantalla === "registro" && (
        <PantallaRegistro alRegistroExitoso={() => setPantalla("login")} irALogin={() => setPantalla("login")} />
      )}

      {/* PANTALLA ALÉRGENOS */}
      {pantalla === "alergenos" && usuario?.rol?.id === 3 && (
        <PantallaAlergenos 
          onContinuar={(ids: number[]) => {
            setAlergenosUsuario(ids);
            setPantalla("menu");
          }} 
        />
      )}

      {/* CLIENTE */}
      {pantalla === "menu" && usuario?.rol?.id === 3 && (
        <PantallaPedido
          pedido={pedido}
          alergenosUsuario={alergenosUsuario}
          alAgregar={agregarAlPedido}
          alActualizar={actualizarCantidad}
          alLimpiar={limpiarPedido}
          alAceptar={irAPago}
          manejarSalir={salir}
          irAStock={() => setPantalla("stock")}
        />
      )}

      {pantalla === "pago" && (
        <PantallaPago pedido={pedido} total={calcularTotal()} alSalir={() => setPantalla("menu")} alConfirmarPago={confirmarPagoExitoso} />
      )}

      {pantalla === "admin" && usuario?.rol?.id === 1 && (
        <PantallaAdmin irAStock={() => setPantalla("stock")} irAPedidos={() => setPantalla("pedidosAdmin")} irAEmpleados={() => setPantalla("empleados")} irAEstadisticas={() => setPantalla("estadisticas")} alSalir={manejarLogout} />
      )}

      {pantalla === "stock" && usuario?.rol?.id === 1 && (
        <PantallaStock centroId={centroSeleccionado} alSalir={() => setPantalla("admin")} />
      )}

      {pantalla === "pedidosAdmin" && usuario?.rol?.id === 1 && (
        <PantallaPedidosAdmin centroId={centroSeleccionado} alSalir={() => setPantalla("admin")} />
      )}

      {pantalla === "empleados" && usuario?.rol?.id === 1 && (
        <PantallaEmpleados centroId={centroSeleccionado} alSalir={() => setPantalla("admin")} />
      )}

      {pantalla === "estadisticas" && usuario?.rol?.id === 1 && (
        <PantallaEstadisticas centroId={centroSeleccionado} alSalir={() => setPantalla("admin")} />
      )}

      {pantalla === "cocina" && usuario?.rol?.id === 2 && (
        <PantallaCocina centroId={usuario.centro?.id} alSalir={manejarLogout} />
      )}
    </>
  );
};

export default App;