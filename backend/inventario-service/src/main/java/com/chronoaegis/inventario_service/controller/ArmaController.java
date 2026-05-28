package com.chronoaegis.inventario_service.controller;

import com.chronoaegis.inventario_service.dto.ArmaDTO;
import com.chronoaegis.inventario_service.model.Arma;
import com.chronoaegis.inventario_service.service.ArmaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens/armas")
@CrossOrigin(origins = "*")
public class ArmaController {

    private final ArmaService armaService;

    public ArmaController(ArmaService armaService) {
        this.armaService = armaService;
    }

    @PostMapping
    public ResponseEntity<Arma> criar(@RequestBody ArmaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(armaService.criar(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Arma> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(armaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Arma>> listarTodas() {
        return ResponseEntity.ok(armaService.listarTodas());
    }
}
