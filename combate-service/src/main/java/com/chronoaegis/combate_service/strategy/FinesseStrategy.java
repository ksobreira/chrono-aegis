package com.chronoaegis.combate_service.strategy;

public class FinesseStrategy implements DamageCalculationStrategy {

    @Override
    public int calcular(int ataque, int danoArma, int defesaAlvo) {
        int dano = (ataque + danoArma) - defesaAlvo;
        return Math.max(1, dano);
    }
}
