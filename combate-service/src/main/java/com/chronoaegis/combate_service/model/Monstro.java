package com.chronoaegis.combate_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Monstro {
    private String nome;
    private int hpAtual;
    private int hpMax;
    private int ataque;
    private int defesa;
}
