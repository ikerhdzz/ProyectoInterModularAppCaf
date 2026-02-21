import React, { useState } from 'react';
import { registrar } from '../api/auth';

interface Props {
  alRegistroExitoso: () => void;
  irALogin: () => void;
}

export const PantallaRegistro: React.FC<Props> = ({ alRegistroExitoso, irALogin }) => {
  const [dni, setDni] = useState('');
  const [clase, setClase] = useState('');
  const [nombre, setNombre] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const manejarSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setCargando(true);
    try {
      await registrar({
        dni,
        clase,
        nombre,
        email,
        password,
        rolId: 1, // por ahora, rol alumno/usuario normal
      });
      alRegistroExitoso();
    } catch (err) {
      setError('No se pudo completar el registro');
    } finally {
      setCargando(false);
    }
  };

  return (
    <div className="aplicacion">
      <header className="encabezado">
        <h1>CaféApp - Registro</h1>
      </header>

      <div className="aplicacion__contenedor contenedor-login">
        <form className="form-login" onSubmit={manejarSubmit}>
          <h2>Crear cuenta</h2>

          {error && <p className="mensaje-error">{error}</p>}

          <label>
            DNI
            <input value={dni} onChange={e => setDni(e.target.value)} required />
          </label>

          <label>
            Clase (opcional)
            <input value={clase} onChange={e => setClase(e.target.value)} />
          </label>

          <label>
            Nombre
            <input value={nombre} onChange={e => setNombre(e.target.value)} required />
          </label>

          <label>
            Correo electrónico
            <input
              type="email"
              value={email}
              onChange={e => setEmail(e.target.value)}
              required
            />
          </label>

          <label>
            Contraseña
            <input
              type="password"
              value={password}
              onChange={e => setPassword(e.target.value)}
              required
            />
          </label>

          <button type="submit" disabled={cargando}>
            {cargando ? 'Registrando...' : 'Registrarse'}
          </button>

          <button type="button" className="btn-secundario" onClick={irALogin}>
            Ya tengo cuenta
          </button>
        </form>
      </div>
    </div>
  );
};

export default PantallaRegistro;
