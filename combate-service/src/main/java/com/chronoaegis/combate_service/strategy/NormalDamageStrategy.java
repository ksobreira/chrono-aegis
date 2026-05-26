package com.chronoaegis.combate_service.strategy;

public class NormalDamageStrategy implements DamageCalculationStrategy {
    private final double multiplicador;

    public NormalDamageStrategy(double multiplicador) {
        this.multiplicador = multiplicador;
    }

    @Override
    public int calcular(int ataque, int danoArma, int defesaAlvo) {
        int dano = (int) ((ataque + danoArma) * multiplicador) - defesaAlvo;
        return Math.max(1, dano);
    }
}
