package com.chronoaegis.personagens_service.controller;

import com.chronoaegis.personagens_service.dto.PersonagemDTO;
import com.chronoaegis.personagens_service.model.Personagem;
import com.chronoaegis.personagens_service.service.PersonagemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personagens")
public class PersonagemController {

    private final PersonagemService service;

    public PersonagemController(PersonagemService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Personagem> criar(@RequestBody PersonagemDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarPersonagem(dto));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Personagem>> buscarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @PostMapping("/{id}/ganhar-xp")
    public ResponseEntity<Personagem> ganharXp(@PathVariable Long id, @RequestParam int xp) {
        return ResponseEntity.ok(service.ganharXp(id, xp));
    }

    @PostMapping("/{id}/evoluir")
    public ResponseEntity<Personagem> evoluir(@PathVariable Long id, @RequestParam String arquetipo) {
        return ResponseEntity.ok(service.evoluirParaArquetipo(id, arquetipo));
    }
}