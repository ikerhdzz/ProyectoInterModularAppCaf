import React, { useState } from 'react';
import type { ElementoPedido } from '../datos/tipos';

interface Props {
  pedido: ElementoPedido[];
  total: number;
  alSalir: () => void;
  alConfirmarPago: () => void;
}

export const PantallaPago: React.FC<Props> = ({ pedido, total, alSalir, alConfirmarPago }) => {
  const [procesando, setProcesando] = useState(false);

  // Estado del formulario de tarjeta
  const [datosTarjeta, setDatosTarjeta] = useState({
    titular: '',
    numero: '',
    expiracion: '',
    cvv: ''
  });

  const manejarCambioInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setDatosTarjeta({
      ...datosTarjeta,
      [e.target.name]: e.target.value
    });
  };

  const manejarPago = async (e: React.FormEvent) => {
    e.preventDefault();
    setProcesando(true);

    /* AQUÍ ES DONDE CONECTARÍAS EL BACKEND.
       Si usaras Stripe, aquí llamarías a stripe.confirmCardPayment()
    */

    try {
      // Simulación de conexión con API (backend)
      console.log("Enviando pago al backend...", { total, ...datosTarjeta });

      // Ejemplo de cómo sería la llamada real (comentado):
      /*
      const respuesta = await fetch('http://localhost:3000/api/pagar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ 
          items: pedido, 
          detallesPago: datosTarjeta // ¡OJO! Con Stripe no envías esto, envías un token.
        })
      });
      if (!respuesta.ok) throw new Error('Error en el pago');
      */

      // Simular espera de 2 segundos
      await new Promise(resolve => setTimeout(resolve, 2000));

      alert('¡Pago realizado con éxito!');
      alConfirmarPago();

    } catch (error) {
      alert('Hubo un error al procesar el pago.');
    } finally {
      setProcesando(false);
    }
  };

  return (
    <div className="aplicacion">
      <header className="encabezado">
        <h1>Finalizar Pedido</h1>
      </header>

      <div className="aplicacion__contenedor contenedor-pago">
        <div className="seccion-resumen">
          <h2>Tu Pedido</h2>
          <div className="lista-resumen">
            {pedido.map((item, i) => (
              <div key={i} className="item-resumen">
                <span>{item.cantidad}x {item.nombre}</span>
                <span>{(item.precio * item.cantidad).toFixed(2)}€</span>
              </div>
            ))}
          </div>
          <div className="total-final">
            <span>Total:</span>
            <span>{total.toFixed(2)}€</span>
          </div>

          <button className="boton-volver" onClick={alSalir}>
            <span></span> Volver al Menú
          </button>
        </div>

        <div className="seccion-pago">
          <div className="cabecera-pago">
            <h2>Datos de Pago</h2>
            <span>Transacción segura cifrada</span>
          </div>
          <form className="formulario-tarjeta" onSubmit={manejarPago}>
            <div className="grupo-input">
              <label>Titular</label>
              <input type="text" name="titular" placeholder="Nombre como aparece en la tarjeta" onChange={manejarCambioInput} required />
            </div>
            <div className="grupo-input">
              <label>Número de Tarjeta</label>
              <input type="text" name="numero" placeholder="xxxx xxxx xxxx xxxx" maxLength={19} onChange={manejarCambioInput} required />
            </div>
            <div className="fila-doble">
              <div className="grupo-input">
                <label>Caducidad</label>
                <input type="text" name="expiracion" placeholder="MM/AA" maxLength={5} onChange={manejarCambioInput} required />
              </div>
              <div className="grupo-input">
                <label>CVV</label>
                <input type="text" name="cvv" placeholder="000" maxLength={3} onChange={manejarCambioInput} required />
              </div>
            </div>
            <button type="submit" className="boton-pagar" disabled={procesando}>
              {procesando ? 'Procesando...' : `Confirmar Pago de ${total.toFixed(2)}€`}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default PantallaPago;