package com.chronoaegis.inventario_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

// CORRIGIDO: usa Feign + Eureka em vez de RestTemplate com URL hardcoded
@FeignClient(name = "personagens-service")
public interface PersonagemClient {

    @GetMapping("/personagens/interno/{id}")
    Map<String, Object> buscarPersonagem(@PathVariable Long id);

    default int buscarForcaDoPersonagem(Long personagemId) {
        try {
            Map<String, Object> personagem = buscarPersonagem(personagemId);
            if (personagem != null && personagem.containsKey("ataque")) {
                return ((Number) personagem.get("ataque")).intValue();
            }
        } catch (Exception ignored) {}
        return 0;
    }
}
