package com.chronoaegis.combate_service.client;

import com.chronoaegis.combate_service.dto.InventarioResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventario-service", url = "http://localhost:8084")
public interface InventarioClient {

    @GetMapping("/inventario/{personagemId}")
    InventarioResponseDTO buscarPorPersonagem(@PathVariable Long personagemId);
}
