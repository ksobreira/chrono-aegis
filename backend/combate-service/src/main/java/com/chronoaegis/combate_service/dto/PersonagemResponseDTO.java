package com.chronoaegis.combate_service.dto;

import lombok.Data;

@Data
public class PersonagemResponseDTO {
    private Long id;
    private String nome;
    private int nivel;
    private int vidaMax;
    private int ataque;
    private int defesa;
    private String posicao;
}
