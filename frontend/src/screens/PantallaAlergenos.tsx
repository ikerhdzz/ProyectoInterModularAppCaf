import React, { useState, useEffect } from 'react';

interface AlergenoDTO {
  id: number;
  nombre: string;
}

interface Props {
  onContinuar: (alergenosSeleccionados: string[]) => void;
}

export default function PantallaAlergenos({ onContinuar }: Props) {
  const [alergenosBD, setAlergenosBD] = useState<AlergenoDTO[]>([]);
  const [seleccionados, setSeleccionados] = useState<string[]>([]);
  const [cargando, setCargando] = useState<boolean>(true);

  useEffect(() => {
    const fetchAlergenos = async () => {
      try {
        const base = (import.meta as any).env?.VITE_API_BASE || 'http://localhost:8080';
        const token = localStorage.getItem('token');
        const respuesta = await fetch(`${base}/alergenos`, {
          headers: { 'Authorization': `Bearer ${token}` }
        });

        if (respuesta.ok) {
          const datos = await respuesta.json();
          setAlergenosBD(datos);
        }
      } catch (error) {
        console.error('Error de conexión:', error);
      } finally {
        setCargando(false);
      }
    };

    fetchAlergenos();
  }, []);

  const toggleAlergeno = (nombreAlergeno: string) => {
    if (seleccionados.includes(nombreAlergeno)) {
      setSeleccionados(seleccionados.filter(a => a !== nombreAlergeno));
    } else {
      setSeleccionados([...seleccionados, nombreAlergeno]);
    }
  };

  if (cargando) {
    return (
      <div className="aplicacion flex-centrado">
        <h2 style={{ color: 'white', opacity: 0.8 }}>Cargando datos...</h2>
      </div>
    );
  }

  return (
    <div className="aplicacion flex-centrado">
      <div className="alergenos-contenedor">
        
        <h2 className="alergenos-titulo">Tus Alérgenos</h2>
        <p className="alergenos-subtitulo">
          Por tu seguridad, marca los alérgenos que te afectan. 
          Solo te mostraremos los productos seguros para ti.
        </p>

        <div className="alergenos-grid">
        {alergenosBD.map(alergeno => {
            const activo = seleccionados.includes(alergeno.nombre);
            return (
            <button
                key={alergeno.id}
                onClick={() => toggleAlergeno(alergeno.nombre)}
                className={`alergeno-btn ${activo ? 'alergeno-btn-activo' : ''}`}
            >
                {alergeno.nombre}
            </button>
            );
        })}
        </div>

        <button
          className="alergenos-btn-continuar"
          onClick={() => onContinuar(seleccionados)}
        >
          Continuar al Menú
        </button>

      </div>
    </div>
  );
}