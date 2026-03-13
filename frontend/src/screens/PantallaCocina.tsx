import { useEffect, useState } from "react";
import type { Pedido } from "../datos/tipos";

interface Props {
  centroId: number;
  alSalir: () => void;
}

export default function PantallaCocina({ centroId, alSalir }: Props) {
    const [pedidos, setPedidos] = useState<Pedido[]>([]);
    const [cargando, setCargando] = useState(true);

    const BASE = (import.meta as any).env?.VITE_API_BASE || "";

    const cargarPedidos = async () => {
        try {
            const res = await fetch(`${BASE}/api/pedidos/centro/${centroId}`);
            if (!res.ok) throw new Error("Error al cargar pedidos");

            const data = await res.json();
            setPedidos(data);
        } catch (e) {
            console.error("Error cargando pedidos:", e);
        } finally {
            setCargando(false);
        }
    };

    const marcarPreparado = async (idPedido: number) => {
        try {
            const res = await fetch(`${BASE}/api/pedidos/${idPedido}/preparado`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" }
            });

            if (!res.ok) throw new Error("Error al actualizar pedido");

            cargarPedidos();
        } catch (e) {
            console.error("Error marcando preparado:", e);
        }
    };

    useEffect(() => {
        cargarPedidos();
        const intervalo = setInterval(cargarPedidos, 3000);
        return () => clearInterval(intervalo);
    }, []);

    if (cargando) {
        return <div className="cocina-cargando">Cargando pedidos...</div>;
    }

    return (
        <div className="cocina-contenedor">
            <h1 className="cocina-titulo">Pedidos en Cocina</h1>

            <button className="btn-volver" onClick={alSalir}>
                Volver
            </button>

            {pedidos.length === 0 && (
                <p className="cocina-vacio">No hay pedidos pendientes.</p>
            )}

            {pedidos.map((pedido) => (
                <div key={pedido.id} className="pedido-card">
                    <h3 className="pedido-titulo">Pedido #{pedido.id}</h3>
                    <p className="pedido-cliente">
                        <strong>Cliente:</strong> {pedido.usuario?.nombre}
                    </p>

                    <ul className="pedido-lista">
                        {pedido.items?.map((item) => (
                            <li key={item.id} className="pedido-item">
                                {item.cantidad} × {item.nombre}
                            </li>
                        ))}
                    </ul>

                    <button
                        className="btn-preparado"
                        onClick={() => marcarPreparado(pedido.id)}
                    >
                        Marcar como preparado
                    </button>
                </div>
            ))}
        </div>
    );
}
