const API_URL = "http://localhost:8080/api/usuarios";

export const getUsuarios = async (token: string) => {
  const res = await fetch(API_URL, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error("Error obteniendo usuarios");
  return res.json();
};

export const crearUsuario = async (usuario: any, token: string) => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(usuario),
  });

  if (!res.ok) throw new Error("Error creando usuario");
  return res.json();
};

export const eliminarUsuario = async (id: number, token: string) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error("Error eliminando usuario");
};

export const editarUsuario = async (id: number, usuario: any, token: string) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(usuario),
  });

  if (!res.ok) throw new Error("Error editando usuario");
  return res.json();
};
