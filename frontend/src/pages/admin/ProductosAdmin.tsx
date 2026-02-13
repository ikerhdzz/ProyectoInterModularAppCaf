import { useEffect, useState } from "react";
import { getProductos, crearProducto, eliminarProducto, editarProducto } from "../../services/productosService";
import { useAuth } from "../../context/AuthContext";

export const ProductosAdmin = () => {
  const [productos, setProductos] = useState([]);
  const [nuevoProducto, setNuevoProducto] = useState({ nombre: "", precio: "" });
  const [productoEditando, setProductoEditando] = useState<any | null>(null);

  const { token } = useAuth();

  useEffect(() => {
    cargarProductos();
  }, []);

  const cargarProductos = async () => {
    try {
      const data = await getProductos();
      setProductos(data);
    } catch (error) {
      console.error("Error cargando productos:", error);
    }
  };

  const handleCrearProducto = async () => {
    if (!nuevoProducto.nombre || !nuevoProducto.precio) {
      alert("Todos los campos son obligatorios");
      return;
    }

    try {
      await crearProducto(
        {
          nombre: nuevoProducto.nombre,
          precio: parseFloat(nuevoProducto.precio),
        },
        token
      );

      setNuevoProducto({ nombre: "", precio: "" });
      cargarProductos();
    } catch (error) {
      console.error("Error creando producto:", error);
    }
  };

  const handleEliminar = async (id: number) => {
    if (!confirm("¿Seguro que deseas eliminar este producto?")) return;

    try {
      await eliminarProducto(id, token);
      cargarProductos();
    } catch (error) {
      console.error("Error eliminando producto:", error);
    }
  };

  const handleGuardarEdicion = async () => {
    if (!productoEditando.nombre || !productoEditando.precio) {
      alert("Todos los campos son obligatorios");
      return;
    }

    try {
      await editarProducto(
        productoEditando.id,
        {
          nombre: productoEditando.nombre,
          precio: parseFloat(productoEditando.precio),
        },
        token
      );

      setProductoEditando(null);
      cargarProductos();
    } catch (error) {
      console.error("Error editando producto:", error);
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h1>Gestión de Productos</h1>

      {/* FORMULARIO DE CREACIÓN */}
      <div
        style={{
          marginBottom: "20px",
          padding: "15px",
          border: "1px solid #ccc",
          borderRadius: "10px",
          width: "350px",
        }}
      >
        <h3>Crear nuevo producto</h3>

        <input
          type="text"
          placeholder="Nombre"
          value={nuevoProducto.nombre}
          onChange={(e) =>
            setNuevoProducto({ ...nuevoProducto, nombre: e.target.value })
          }
          style={inputStyle}
        />

        <input
          type="number"
          placeholder="Precio"
          value={nuevoProducto.precio}
          onChange={(e) =>
            setNuevoProducto({ ...nuevoProducto, precio: e.target.value })
          }
          style={inputStyle}
        />

        <button onClick={handleCrearProducto} style={btnCrear}>
          Crear Producto
        </button>
      </div>

      {/* FORMULARIO DE EDICIÓN */}
      {productoEditando && (
        <div
          style={{
            marginBottom: "20px",
            padding: "15px",
            border: "2px solid #2196F3",
            borderRadius: "10px",
            width: "350px",
            background: "#E3F2FD",
          }}
        >
          <h3>Editando producto ID {productoEditando.id}</h3>

          <input
            type="text"
            value={productoEditando.nombre}
            onChange={(e) =>
              setProductoEditando({ ...productoEditando, nombre: e.target.value })
            }
            style={inputStyle}
          />

          <input
            type="number"
            value={productoEditando.precio}
            onChange={(e) =>
              setProductoEditando({ ...productoEditando, precio: e.target.value })
            }
            style={inputStyle}
          />

          <button onClick={handleGuardarEdicion} style={btnEdit}>
            Guardar cambios
          </button>

          <button
            onClick={() => setProductoEditando(null)}
            style={{ ...btnDelete, marginLeft: "10px" }}
          >
            Cancelar
          </button>
        </div>
      )}

      {/* TABLA DE PRODUCTOS */}
      <table style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr>
            <th style={thStyle}>ID</th>
            <th style={thStyle}>Nombre</th>
            <th style={thStyle}>Precio</th>
            <th style={thStyle}>Acciones</th>
          </tr>
        </thead>

        <tbody>
          {productos.map((p: any) => (
            <tr key={p.id}>
              <td style={tdStyle}>{p.id}</td>
              <td style={tdStyle}>{p.nombre}</td>
              <td style={tdStyle}>{p.precio} €</td>
              <td style={tdStyle}>
                <button style={btnEdit} onClick={() => setProductoEditando(p)}>
                  Editar
                </button>
                <button style={btnDelete} onClick={() => handleEliminar(p.id)}>
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
