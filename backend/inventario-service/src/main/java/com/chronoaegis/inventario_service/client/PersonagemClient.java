package com.chronoaegis.inventario_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PersonagemClient {

    private final RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:8083/personagens";

    public PersonagemClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public int buscarForcaDoPersonagem(Long personagemId) {
        try {
            var personagem = restTemplate.getForObject(
                    BASE_URL + "/" + personagemId,
                    java.util.Map.class
            );
            if (personagem != null && personagem.containsKey("ataque")) {
                return (int) personagem.get("ataque");
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }
}