package com.chronoaegis.combate_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BatalhaResponseDTO {
    private UUID sessionId;
    private String nomePersonagem;
    private int hpPersonagem;
    private String nomeMonstro;
    private int hpMonstro;
    private String turnoAtual;
}
