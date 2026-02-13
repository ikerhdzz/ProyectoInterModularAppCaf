import { useEffect, useState } from "react";
import { getUsuarios, crearUsuario, eliminarUsuario, editarUsuario } from "../../services/usuariosService";
import { useAuth } from "../../context/AuthContext";

export const UsuariosAdmin = () => {
  const [usuarios, setUsuarios] = useState([]);
  const [nuevoUsuario, setNuevoUsuario] = useState({ nombre: "", email: "", password: "", rol: "CLIENTE" });
  const [usuarioEditando, setUsuarioEditando] = useState<any | null>(null);

  const { token } = useAuth();

  useEffect(() => {
    cargarUsuarios();
  }, []);

  const cargarUsuarios = async () => {
    try {
      const data = await getUsuarios(token);
      setUsuarios(data);
    } catch (error) {
      console.error("Error cargando usuarios:", error);
    }
  };

  const handleCrearUsuario = async () => {
    if (!nuevoUsuario.nombre || !nuevoUsuario.email || !nuevoUsuario.password) {
      alert("Todos los campos son obligatorios");
      return;
    }

    try {
      await crearUsuario(nuevoUsuario, token);
      setNuevoUsuario({ nombre: "", email: "", password: "", rol: "CLIENTE" });
      cargarUsuarios();
    } catch (error) {
      console.error("Error creando usuario:", error);
    }
  };

  const handleEliminar = async (id: number) => {
    if (!confirm("¿Seguro que deseas eliminar este usuario?")) return;

    try {
      await eliminarUsuario(id, token);
      cargarUsuarios();
    } catch (error) {
      console.error("Error eliminando usuario:", error);
    }
  };

  const handleGuardarEdicion = async () => {
    if (!usuarioEditando.nombre || !usuarioEditando.email) {
      alert("Todos los campos son obligatorios");
      return;
    }

    try {
      await editarUsuario(usuarioEditando.id, usuarioEditando, token);
      setUsuarioEditando(null);
      cargarUsuarios();
    } catch (error) {
      console.error("Error editando usuario:", error);
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h1>Gestión de Usuarios</h1>

      {/* FORMULARIO DE CREACIÓN */}
      <div style={formBox}>
        <h3>Crear nuevo usuario</h3>

        <input
          type="text"
          placeholder="Nombre"
          value={nuevoUsuario.nombre}
          onChange={(e) => setNuevoUsuario({ ...nuevoUsuario, nombre: e.target.value })}
          style={inputStyle}
        />

        <input
          type="email"
          placeholder="Email"
          value={nuevoUsuario.email}
          onChange={(e) => setNuevoUsuario({ ...nuevoUsuario, email: e.target.value })}
          style={inputStyle}
        />

        <input
          type="password"
          placeholder="Contraseña"
          value={nuevoUsuario.password}
          onChange={(e) => setNuevoUsuario({ ...nuevoUsuario, password: e.target.value })}
          style={inputStyle}
        />

        <select
          value={nuevoUsuario.rol}
          onChange={(e) => setNuevoUsuario({ ...nuevoUsuario, rol: e.target.value })}
          style={inputStyle}
        >
          <option value="CLIENTE">CLIENTE</option>
          <option value="EMPLEADO">EMPLEADO</option>
          <option value="ADMIN">ADMIN</option>
        </select>

        <button onClick={handleCrearUsuario} style={btnCrear}>
          Crear Usuario
        </button>
      </div>

      {/* FORMULARIO DE EDICIÓN */}
      {usuarioEditando && (
        <div style={editBox}>
          <h3>Editando usuario ID {usuarioEditando.id}</h3>

          <input
            type="text"
            value={usuarioEditando.nombre}
            onChange={(e) => setUsuarioEditando({ ...usuarioEditando, nombre: e.target.value })}
            style={inputStyle}
          />

          <input
            type="email"
            value={usuarioEditando.email}
            onChange={(e) => setUsuarioEditando({ ...usuarioEditando, email: e.target.value })}
            style={inputStyle}
          />

          <select
            value={usuarioEditando.rol}
            onChange={(e) => setUsuarioEditando({ ...usuarioEditando, rol: e.target.value })}
            style={inputStyle}
          >
            <option value="CLIENTE">CLIENTE</option>
            <option value="EMPLEADO">EMPLEADO</option>
            <option value="ADMIN">ADMIN</option>
          </select>

          <button onClick={handleGuardarEdicion} style={btnEdit}>
            Guardar cambios
          </button>

          <button onClick={() => setUsuarioEditando(null)} style={{ ...btnDelete, marginLeft: "10px" }}>
            Cancelar
          </button>
        </div>
      )}

      {/* TABLA DE USUARIOS */}
      <table style={{ width: "100%", borderCollapse: "collapse", marginTop: "20px" }}>
        <thead>
          <tr>
            <th style={thStyle}>ID</th>
            <th style={thStyle}>Nombre</th>
            <th style={thStyle}>Email</th>
            <th style={thStyle}>Rol</th>
            <th style={thStyle}>Acciones</th>
          </tr>
        </thead>

        <tbody>
          {usuarios.map((u: any) => (
            <tr key={u.id}>
              <td style={tdStyle}>{u.id}</td>
              <td style={tdStyle}>{u.nombre}</td>
              <td style={tdStyle}>{u.email}</td>
              <td style={tdStyle}>{u.rol}</td>
              <td style={tdStyle}>
                <button style={btnEdit} onClick={() => setUsuarioEditando(u)}>
                  Editar
                </button>
                <button style={btnDelete} onClick={() => handleEliminar(u.id)}>
                  Eliminar
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

const formBox: React.CSSProperties = {
  marginBottom: "20px",
  padding: "15px",
  border: "1px solid #ccc",
  borderRadius: "10px",
  width: "350px",
};

const editBox: React.CSSProperties = {
  marginBottom: "20px",
  padding: "15px",
  border: "2px solid #2196F3",
  borderRadius: "10px",
  width: "350px",
  background: "#E3F2FD",
};

const inputStyle: React.CSSProperties = {
  display: "block",
  width: "100%",
  padding: "8px",
  marginBottom: "10px",
  borderRadius: "5px",
  border: "1px solid #ccc",
};

const btnCrear: React.CSSProperties = {
  padding: "10px 20px",
  background: "#4CAF50",
  color: "white",
  border: "none",
  borderRadius: "5px",
  cursor: "pointer",
};

const thStyle: React.CSSProperties = {
  borderBottom: "2px solid #ccc",
  padding: "10px",
  textAlign: "left",
};

const tdStyle: React.CSSProperties = {
  borderBottom: "1px solid #eee",
  padding: "10px",
};

const btnEdit: React.CSSProperties = {
  marginRight: "10px",
  padding: "5px 10px",
  background: "#2196F3",
  color: "white",
  border: "none",
  borderRadius: "5px",
  cursor: "pointer",
};

const btnDelete: React.CSSProperties = {
  padding: "5px 10px",
  background: "#f44336",
  color: "white",
  border: "none",
  borderRadius: "5px",
  cursor: "pointer",
};
