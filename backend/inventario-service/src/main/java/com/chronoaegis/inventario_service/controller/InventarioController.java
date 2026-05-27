package com.chronoaegis.inventario_service.controller;

import com.chronoaegis.inventario_service.dto.EquiparDTO;
import com.chronoaegis.inventario_service.model.Inventario;
import com.chronoaegis.inventario_service.service.InventarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @PostMapping("/equipar")
    public ResponseEntity<Inventario> equipar(@RequestBody EquiparDTO dto) {
        return ResponseEntity.ok(inventarioService.equipar(dto));
    }

    @GetMapping("/{personagemId}")
    public ResponseEntity<Inventario> buscar(@PathVariable Long personagemId) {
        return ResponseEntity.ok(inventarioService.buscarPorPersonagem(personagemId));
    }
}