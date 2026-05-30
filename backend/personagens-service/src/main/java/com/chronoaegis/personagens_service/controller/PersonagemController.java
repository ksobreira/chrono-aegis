package com.chronoaegis.personagens_service.controller;

import com.chronoaegis.personagens_service.dto.PersonagemDTO;
import com.chronoaegis.personagens_service.model.Personagem;
import com.chronoaegis.personagens_service.service.PersonagemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/personagens")
@CrossOrigin(origins = "*")
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

    @GetMapping("/interno/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        Personagem p = service.buscarPorId(id);
        Map<String, Object> map = new HashMap<>();
        map.put("id", p.getId());
        map.put("nome", p.getNome());
        map.put("nivel", p.getNivel());
        map.put("vidaMax", p.getVidaMax());
        map.put("ataque", p.getAtaque());
        map.put("defesa", p.getDefesa());
        map.put("posicao", p.getPosicao() != null ? p.getPosicao().name() : "FRENTE");
        return ResponseEntity.ok(map);
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
