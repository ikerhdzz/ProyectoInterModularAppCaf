import React, { useState, useEffect } from 'react';
import { loadStripe } from '@stripe/stripe-js';
import { Elements, PaymentElement, useStripe, useElements } from '@stripe/react-stripe-js';
import type { ElementoPedido } from '../datos/tipos';

// Usar clave pública desde .env (VITE_STRIPE_PK)
const stripePk = (import.meta as any).env?.VITE_STRIPE_PK;
const stripePromise = stripePk ? loadStripe(stripePk) : null;

interface Props {
  pedido: ElementoPedido[];
  total: number;
  alSalir: () => void;
  alConfirmarPago: () => void;
}

// COMPONENTE INTERNO DEL FORMULARIO
const FormularioPago: React.FC<{ total: number, alConfirmarPago: () => void }> = ({ total, alConfirmarPago }) => {
  const stripe = useStripe();
  const elements = useElements();
  const [mensaje, setMensaje] = useState<string | null>(null);
  const [cargando, setCargando] = useState(false);

  const manejarEnvio = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!stripe || !elements) return; // Stripe aún no ha cargado

    setCargando(true);

    // Stripe confirma el pago directamente con sus servidores
    const { error } = await stripe.confirmPayment({
      elements,
      confirmParams: {
        // A dónde redirigir si algo sale mal (opcional en SPA)
        return_url: window.location.origin, 
      },
      redirect: 'if_required' // Evita recargar la página si no es necesario (ej: 3D Secure)
    });

    if (error) {
      setMensaje(error.message || "Error desconocido");
    } else {
      // PAGO EXITOSO
      alConfirmarPago(); // Aquí guardas el pedido en tu BD
    }

    setCargando(false);
  };

  return (
    <form onSubmit={manejarEnvio} className="formulario-stripe">
      {/* ESTO PINTA EL FORMULARIO DE TARJETA SEGURO AUTOMÁTICAMENTE */}
      <PaymentElement />
      
      <button disabled={!stripe || cargando} id="submit" className="boton-pagar">
        {cargando ? "Procesando..." : `Pagar ${total.toFixed(2)}€`}
      </button>
      
      {mensaje && <div id="payment-message" style={{color: 'red', marginTop: '10px'}}>{mensaje}</div>}
    </form>
  );
};

// COMPONENTE PRINCIPAL DE LA PANTALLA
export const PantallaPago: React.FC<Props> = ({ pedido, total, alSalir, alConfirmarPago }) => {
  const [clientSecret, setClientSecret] = useState("");

  // Al entrar, pedimos al backend que prepare el cobro
  useEffect(() => {
    const base = (import.meta as any).env?.VITE_API_BASE || '';
    fetch(base + "/api/pagos/crear-intento", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ items: pedido.map(i => ({ id: i.id, cantidad: i.cantidad })) })
    })
      .then((res) => res.json())
      .then((data) => setClientSecret(data.clientSecret))
      .catch((err) => console.error("Error iniciando pago:", err));
  }, [total, pedido]);

  const opciones = {
    clientSecret,
    appearance: { theme: 'stripe' as const }, // Tema visual
  };

  if (!stripePk) {
    return (
      <div className="aplicacion">
        <header className="encabezado"><h1>Pago Seguro</h1></header>
        <div className="aplicacion__contenedor contenedor-pago">
          <div className="seccion-resumen">
            <h2>Resumen</h2>
            <div className="lista-resumen">
              {pedido.map((item, i) => (
                <div key={i} className="item-resumen">
                  <span>{item.cantidad}x {item.nombre}</span>
                  <span>{(item.precio * item.cantidad).toFixed(2)}€</span>
                </div>
              ))}
            </div>
            <div className="total-final">
              <span>Total a pagar:</span>
              <span>{total.toFixed(2)}€</span>
            </div>
            <button className="boton-volver" onClick={alSalir}>Cancelar</button>
          </div>
          <div className="seccion-pago">
            <p style={{color: 'red'}}>La clave pública de Stripe no está configurada. Defina `VITE_STRIPE_PK` en .env.</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="aplicacion">
      <header className="encabezado">
        <h1>Pago Seguro</h1>
      </header>

      <div className="aplicacion__contenedor contenedor-pago">
        <div className="seccion-resumen">
          <h2>Resumen</h2>
          <div className="lista-resumen">
            {pedido.map((item, i) => (
              <div key={i} className="item-resumen">
                <img src={item.imagen ?? '/img/imagenNoDisponible.jpg'} alt={item.nombre} className="mini-imagen" style={{marginRight:8}} />
                <span>{item.cantidad}x {item.nombre}</span>
                <span>{(item.precio * item.cantidad).toFixed(2)}€</span>
              </div>
            ))}
          </div>
          <div className="total-final">
            <span>Total a pagar:</span>
            <span>{total.toFixed(2)}€</span>
          </div>
          <button className="boton-volver" onClick={alSalir}>Cancelar</button>
        </div>

        <div className="seccion-pago">
          {clientSecret ? (
            <Elements options={opciones} stripe={stripePromise}>
              <FormularioPago total={total} alConfirmarPago={alConfirmarPago} />
            </Elements>
          ) : (
            <p>Cargando pasarela de pago...</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default PantallaPago;