package com.chronoaegis.combate_service.client;

import com.chronoaegis.combate_service.dto.PersonagemResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "personagens-service")
public interface PersonagemClient {

    @GetMapping("/personagens/interno/{id}")
    Map<String, Object> buscarPorIdMap(@PathVariable Long id);

    @PostMapping("/personagens/{id}/ganhar-xp")
    void ganharXp(@PathVariable Long id, @RequestParam int xp);

    default PersonagemResponseDTO buscarPorId(Long id) {
        Map<String, Object> map = buscarPorIdMap(id);
        PersonagemResponseDTO dto = new PersonagemResponseDTO();
        dto.setId(((Number) map.get("id")).longValue());
        dto.setNome((String) map.get("nome"));
        dto.setNivel(((Number) map.get("nivel")).intValue());
        dto.setVidaMax(((Number) map.get("vidaMax")).intValue());
        dto.setAtaque(((Number) map.get("ataque")).intValue());
        dto.setDefesa(((Number) map.get("defesa")).intValue());
        dto.setPosicao((String) map.get("posicao"));
        return dto;
    }
}