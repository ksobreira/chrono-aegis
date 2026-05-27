package com.chronoaegis.inventario_service.controller;

import com.chronoaegis.inventario_service.model.Item;
import com.chronoaegis.inventario_service.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/itens")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<Item> criar(@RequestBody Item item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.criar(item));
    }

    @GetMapping("/personagem/{personagemId}")
    public ResponseEntity<List<Item>> buscarPorPersonagem(@PathVariable Long personagemId) {
        return ResponseEntity.ok(itemService.buscarPorPersonagem(personagemId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        itemService.deletar(id);
        return ResponseEntity.ok("Item deletado com sucesso!");
    }
}