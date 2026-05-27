package com.chronoaegis.combate_service.strategy;

public interface DamageCalculationStrategy {
    int calcular(int ataque, int danoArma, int defesaAlvo);
}
