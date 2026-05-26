package com.chronoaegis.combate_service.state;

public class EstadoEnvenenado implements EstadoPersonagem {
    @Override
    public boolean podeAgir() {
        return true;
    }

    @Override
    public int aplicarEfeito(int hpAtual) {
        return Math.max(0, hpAtual - 5);
    }

    @Override
    public String getDescricao() {
        return "Envenenado";
    }
}
