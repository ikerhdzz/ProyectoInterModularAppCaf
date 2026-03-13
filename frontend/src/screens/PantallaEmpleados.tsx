import { useEffect, useState } from "react";

interface Props {
  centroId: number;
  alSalir: () => void;
}

export default function PantallaEmpleados({ centroId, alSalir }: Props) {
  const [empleados, setEmpleados] = useState<any[]>([]);
  const [nombre, setNombre] = useState("");
  const [email, setEmail] = useState("");
  const [dni, setDni] = useState("");
  const [password, setPassword] = useState("123456");
  const [cargando, setCargando] = useState(false);

  const BASE = (import.meta as any).env?.VITE_API_BASE || "";

  // ============================================================
  // Cargar empleados del centro
  // ============================================================
  const cargarEmpleados = async () => {
    const res = await fetch(`${BASE}/api/empleados/centro/${centroId}`);
    const data = await res.json();
    setEmpleados(data);
  };

  useEffect(() => {
    cargarEmpleados();
  }, [centroId]);

  // ============================================================
  // Crear empleado
  // ============================================================
  const crearEmpleado = async (e: React.FormEvent) => {
    e.preventDefault();
    setCargando(true);

    const nuevo = {
      nombre,
      email,
      dni,
      password,
      rolId: 2,       // empleado
      centroId: centroId
    };

    await fetch(`${BASE}/api/empleados`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(nuevo)
    });

    setNombre("");
    setEmail("");
    setDni("");
    setPassword("123456");

    await cargarEmpleados();
    setCargando(false);
  };

  // ============================================================
  // Eliminar empleado
  // ============================================================
  const eliminarEmpleado = async (id: number) => {
    if (!window.confirm("¿Eliminar empleado?")) return;

    await fetch(`${BASE}/api/empleados/${id}`, { method: "DELETE" });
    cargarEmpleados();
  };

  return (
    <div className="empleados-contenedor">
      <h1>Gestión de Empleados</h1>
      <p>Centro seleccionado: {centroId}</p>

      {/* FORMULARIO CREAR EMPLEADO */}
      <form className="empleados-form" onSubmit={crearEmpleado}>
        <h3>Crear nuevo empleado</h3>

        <input
          placeholder="DNI"
          value={dni}
          onChange={(e) => setDni(e.target.value)}
          required
        />

        <input
          placeholder="Nombre"
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
          required
        />

        <input
          placeholder="Correo"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />

        <input
          placeholder="Contraseña"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        <button type="submit" disabled={cargando}>
          {cargando ? "Creando..." : "Crear empleado"}
        </button>
      </form>

      {/* LISTA DE EMPLEADOS */}
      <h3>Empleados del centro</h3>

      <div className="empleados-lista">
        {empleados.map((emp) => (
          <div key={emp.id} className="empleado-card">
            <p><strong>{emp.nombre}</strong></p>
            <p>{emp.email}</p>
            <p>DNI: {emp.dni}</p>

            <button
              className="btn-eliminar"
              onClick={() => eliminarEmpleado(emp.id)}
            >
              Eliminar
            </button>
          </div>
        ))}
      </div>

      <button className="btn-volver" onClick={alSalir}>
        Volver
      </button>
    </div>
  );
}
