const BASE = (import.meta as any).env?.VITE_API_BASE || '';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegistroRequest {
  dni: string;
  clase?: string;
  nombre: string;
  email: string;
  password: string;
  cursoId?: number;
  rolId: number;
}

// ===============================================
// LOGIN — ahora devuelve token + usuario completo
// ===============================================
export async function login(req: LoginRequest): Promise<{
  token: string;
  usuario: {
    id: number;
    nombre: string;
    email: string;
    rol: { id: number; nombre: string };
    centro: { id: number; nombre: string } | null;
  };
}> {
  const res = await fetch(BASE + '/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(req),
  });

  if (!res.ok) {
    let mensajeError = 'Credenciales inválidas';
    try {
      const errorData = await res.json();
      mensajeError = errorData.message || errorData.error || mensajeError;
    } catch {
      mensajeError = `Error ${res.status}: ${res.statusText}`;
    }
    throw new Error(mensajeError);
  }

  return res.json(); // devuelve token + usuario
}

// ===============================================
//  REGISTRO — igual que antes
// ===============================================
export async function registrar(req: RegistroRequest) {
  const res = await fetch(BASE + '/api/auth/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(req),
  });

  if (!res.ok) {
    let mensajeError = 'Error en el registro';
    try {
      const errorData = await res.json();
      mensajeError = errorData.message || errorData.error || mensajeError;
    } catch {
      mensajeError = `Error ${res.status}: ${res.statusText}`;
    }
    throw new Error(mensajeError);
  }

  return await res.json();
}
