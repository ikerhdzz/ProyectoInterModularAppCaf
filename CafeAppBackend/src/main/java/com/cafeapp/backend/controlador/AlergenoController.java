package com.cafeapp.backend.controlador;

import com.cafeapp.backend.modelo.Alergeno;
import com.cafeapp.backend.servicio.AlergenoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alergenos")
public class AlergenoController {

    private final AlergenoService alergenoService;

    public AlergenoController(AlergenoService alergenoService) {
        this.alergenoService = alergenoService;
    }

    @GetMapping
    public List<Alergeno> listar() {
        return alergenoService.listar();
    }

    @PostMapping
    public Alergeno crear(@RequestBody Alergeno alergeno) {
        return alergenoService.crear(alergeno);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        alergenoService.eliminar(id);
    }
}
