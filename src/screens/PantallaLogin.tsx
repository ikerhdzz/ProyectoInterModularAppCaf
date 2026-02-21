import React, { useState } from 'react';
import { login } from '../api/auth';

interface Props {
  alLoginExitoso: (token: string) => void;
  irARegistro: () => void;
}

export const PantallaLogin: React.FC<Props> = ({ alLoginExitoso, irARegistro }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const manejarSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setCargando(true);
    try {
      const token = await login({ email, password });
      alLoginExitoso(token);
    } catch (err) {
      setError('Email o contraseña incorrectos');
    } finally {
      setCargando(false);
    }
  };

  return (
    <div className="aplicacion">
      <header className="encabezado">
        <h1>CaféApp - Acceso</h1>
      </header>

      <div className="contenedor-login">
        <form className="form-login" onSubmit={manejarSubmit}>
          <h2>Iniciar sesión</h2>

          {error && <p className="mensaje-error">{error}</p>}

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
            {cargando ? 'Entrando...' : 'Entrar'}
          </button>

          <button type="button" className="btn-secundario" onClick={irARegistro}>
            Crear cuenta
          </button>
        </form>
      </div>
    </div>
  );
};

export default PantallaLogin;
