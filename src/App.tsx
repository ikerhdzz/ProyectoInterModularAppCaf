import React, { useState } from 'react';
import type { Pantalla, ElementoPedido, ElementoMenu } from './datos/tipos';
import './App.css';
import PantallaPedido from './screens/PantallaPedido';
import PantallaPago from './screens/PantallaPago';
import PantallaStock from './screens/PantallaStock'; // Asegúrate de importar esto

export const App: React.FC = () => {
  const [pantalla, setPantalla] = useState<Pantalla>("menu");
  
  // 1. MOVEMOS EL ESTADO DEL PEDIDO AQUÍ
  const [pedido, setPedido] = useState<ElementoPedido[]>([]);

  // 2. LÓGICA PARA AGREGAR (Traída desde PantallaPedido)
  const agregarAlPedido = (elemento: ElementoMenu) => {
    const elementoExistente = pedido.find(
      item => item.nombre === elemento.nombre
    );

    if (elementoExistente) {
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

  // 3. LÓGICA PARA ACTUALIZAR CANTIDAD
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

  const limpiarPedido = () => {
    setPedido([]);
  };

  // 4. CALCULAR TOTAL (Para pasarlo a PantallaPago)
  const calcularTotal = (): number => {
    const subtotal = pedido.reduce((total, item) => total + (item.precio * item.cantidad), 0);
    const impuesto = subtotal * 0.07;
    return subtotal + impuesto;
  };

  // NAVEGACIÓN
  const irAPago = () => {
    if (pedido.length === 0) {
      alert("El pedido está vacío");
      return;
    }
    if (window.confirm('¿Confirmar pedido e ir a pagar?')) {
      setPantalla("pago");
    }
  };

  const salir = () => {
    if (window.confirm('¿Seguro que quieres volver al inicio/salir?')) {
      setPantalla("menu"); // O lo que consideres "salir"
      limpiarPedido();
    }
  };

  const confirmarPagoExitoso = () => {
    setPantalla("menu");
    limpiarPedido();
  };

  return (
    <>
      {pantalla === 'menu' && (
        <PantallaPedido 
          pedido={pedido} // AHORA PASAMOS EL ESTADO
          alAgregar={agregarAlPedido}
          alActualizar={actualizarCantidad}
          alLimpiar={limpiarPedido}
          alAceptar={irAPago} 
          manejarSalir={salir}
          irAStock={() => setPantalla("stock")} // Opcional: botón secreto para stock
        />
      )}

      {pantalla === 'pago' && (
        <PantallaPago 
          pedido={pedido}   // ERROR CORREGIDO: Pasamos la variable real
          total={calcularTotal()} // Pasamos el cálculo real
          alSalir={() => setPantalla("menu")}
          alConfirmarPago={confirmarPagoExitoso}
        />
      )}

      {pantalla === 'stock' && (
        <PantallaStock alSalir={() => setPantalla("menu")} />
      )}
    </>
  );
};

export default App;