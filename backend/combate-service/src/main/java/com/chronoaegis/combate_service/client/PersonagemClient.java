package com.chronoaegis.combate_service.client;

import com.chronoaegis.combate_service.dto.PersonagemResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "personagens-service")
public interface PersonagemClient {

    @GetMapping("/personagens/interno/{id}")
    PersonagemResponseDTO buscarPorId(@PathVariable Long id);

    @PostMapping("/personagens/{id}/ganhar-xp")
    void ganharXp(@PathVariable Long id, @RequestParam int xp);
}
