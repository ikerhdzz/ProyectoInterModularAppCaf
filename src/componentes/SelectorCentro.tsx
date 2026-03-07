import { useEffect, useState } from "react";

export default function SelectorCentro({ centroId, onChange }) {
  const [centros, setCentros] = useState([]);

  const BASE = (import.meta as any).env?.VITE_API_BASE || "";

  useEffect(() => {
    const cargarCentros = async () => {
      const res = await fetch(`${BASE}/api/centros`);
      const data = await res.json();
      setCentros(data);
    };
    cargarCentros();
  }, []);

  return (
    <div className="selector-centro">
      <label>Centro actual:</label>
      <select
        value={centroId}
        onChange={(e) => onChange(Number(e.target.value))}
      >
        {centros.map((c) => (
          <option key={c.id} value={c.id}>
            {c.nombre}
          </option>
        ))}
      </select>
    </div>
  );
}
