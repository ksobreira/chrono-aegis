package com.chronoaegis.combate_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResultadoTurnoDTO {
    private List<String> log;
    private int hpJogador;
    private int hpMonstro;
    private String turnoAtual;
    private boolean fimDeBatalha;
    private boolean vitoria;
}
