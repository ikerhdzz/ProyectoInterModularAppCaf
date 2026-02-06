import React from 'react';

interface PropsBotonesAccion {
  alAceptar: () => void;
  alLimpiar: () => void;
  alSalir: () => void;
}

export const BotonesAccion: React.FC<PropsBotonesAccion> = ({
  alAceptar,
  alLimpiar,
  alSalir
}) => {
  return (
    <div className="botones-accion">
      <button className="botones-accion__aceptar" onClick={alAceptar}>
        Aceptar
      </button>
      <button className="botones-accion__limpiar" onClick={alLimpiar}>
        Limpiar
      </button>
      <button className="botones-accion__salir" onClick={alSalir}>
        Salir
      </button>
    </div>
  );
};