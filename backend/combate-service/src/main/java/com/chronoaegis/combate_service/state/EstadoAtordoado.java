package com.chronoaegis.combate_service.state;

public class EstadoAtordoado implements EstadoPersonagem {
    private int turnosRestantes = 1;

    @Override
    public boolean podeAgir() {
        return turnosRestantes <= 0;
    }

    @Override
    public int aplicarEfeito(int hpAtual) {
        turnosRestantes--;
        return hpAtual;
    }

    @Override
    public String getDescricao() {
        return "Atordoado";
    }
}
