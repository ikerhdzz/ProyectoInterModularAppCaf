package com.cafeapp.backend.controlador;

import com.cafeapp.backend.modelo.Usuario;
import com.cafeapp.backend.servicio.EmpleadoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "*")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping("/centro/{idCentro}")
    public List<Usuario> obtenerPorCentro(@PathVariable Integer idCentro) {
        return empleadoService.obtenerEmpleadosPorCentro(idCentro);
    }

    @PostMapping
    public Usuario crearEmpleado(@RequestBody Usuario empleado) {
        Integer centroId = empleado.getCentro().getId();
        return empleadoService.crearEmpleado(empleado, centroId);
    }

    @PutMapping("/{id}/rol")
    public Usuario cambiarRol(@PathVariable Integer id, @RequestBody Integer nuevoRolId) {
        return empleadoService.cambiarRol(id, nuevoRolId);
    }

    @PutMapping("/{id}/centro")
    public Usuario cambiarCentro(@PathVariable Integer id, @RequestBody Integer nuevoCentroId) {
        return empleadoService.cambiarCentro(id, nuevoCentroId);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        empleadoService.eliminarEmpleado(id);
    }
}
