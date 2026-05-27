package com.chronoaegis.combate_service.controller;

import com.chronoaegis.combate_service.dto.AcaoDTO;
import com.chronoaegis.combate_service.dto.BatalhaResponseDTO;
import com.chronoaegis.combate_service.dto.IniciarBatalhaDTO;
import com.chronoaegis.combate_service.dto.ResultadoTurnoDTO;
import com.chronoaegis.combate_service.service.BatalhaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/combate")
public class BatalhaController {

    private final BatalhaService batalhaService;

    public BatalhaController(BatalhaService batalhaService) {
        this.batalhaService = batalhaService;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<BatalhaResponseDTO> iniciar(@RequestBody IniciarBatalhaDTO dto) {
        return ResponseEntity.ok(batalhaService.iniciarBatalha(dto));
    }

    @PostMapping("/acao")
    public ResponseEntity<ResultadoTurnoDTO> acao(@RequestBody AcaoDTO dto) {
        return ResponseEntity.ok(batalhaService.processarAcao(dto));
    }
}
