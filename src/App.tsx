import React, { useState } from 'react';
import type { Pantalla, ElementoPedido, ElementoMenu } from './datos/tipos';
import './App.css';

import PantallaPedido from './screens/PantallaPedido';
import PantallaPago from './screens/PantallaPago';
import PantallaStock from './screens/PantallaStock';

import PantallaLogin from './screens/PantallaLogin';
import PantallaRegistro from './screens/PantallaRegistro';

export const App: React.FC = () => {

  // Pantalla inicial → login
  const [pantalla, setPantalla] = useState<Pantalla>("login");

  //  Token del usuario (cuando inicie sesión)
  const [token, setToken] = useState<string | null>(localStorage.getItem("token"));

  //  Estado del pedido
  const [pedido, setPedido] = useState<ElementoPedido[]>([]);

  // ============================================================
  // 1. LOGIN / REGISTRO
  // ============================================================

  const manejarLoginExitoso = (nuevoToken: string) => {
    setToken(nuevoToken);
    localStorage.setItem("token", nuevoToken);
    setPantalla("menu");
  };

  const manejarLogout = () => {
    setToken(null);
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
  // 4. RENDERIZADO DE PANTALLAS
  // ============================================================

  return (
    <>
      {/* LOGIN */}
      {pantalla === "login" && (
        <PantallaLogin
          alLoginExitoso={manejarLoginExitoso}
          irARegistro={() => setPantalla("registro")}
        />
      )}

      {/* REGISTRO */}
      {pantalla === "registro" && (
        <PantallaRegistro
          alRegistroExitoso={() => setPantalla("login")}
          irALogin={() => setPantalla("login")}
        />
      )}

      {/* MENÚ PRINCIPAL */}
      {pantalla === "menu" && (
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

      {/* PAGO */}
      {pantalla === "pago" && (
        <PantallaPago
          pedido={pedido}
          total={calcularTotal()}
          alSalir={() => setPantalla("menu")}
          alConfirmarPago={confirmarPagoExitoso}
        />
      )}

      {/* STOCK */}
      {pantalla === "stock" && (
        <PantallaStock alSalir={() => setPantalla("menu")} />
      )}
    </>
  );
};

export default App;
