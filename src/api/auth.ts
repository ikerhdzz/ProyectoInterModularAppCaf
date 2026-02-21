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

export async function login(req: LoginRequest): Promise<string> {
  const res = await fetch(BASE + '/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(req),
  });
  if (!res.ok) throw new Error('Credenciales inválidas');
  const data = await res.json();
  return data.token as string;
}

export async function registrar(req: RegistroRequest) {
  const res = await fetch(BASE + '/api/auth/registro', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(req),
  });
  if (!res.ok) throw new Error('Error en el registro');
  return await res.json();
}
