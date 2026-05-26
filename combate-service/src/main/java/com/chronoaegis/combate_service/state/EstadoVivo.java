package com.chronoaegis.combate_service.state;

public class EstadoVivo implements EstadoPersonagem {
    @Override
    public boolean podeAgir() {
        return true;
    }

    @Override
    public int aplicarEfeito(int hpAtual) {
        return hpAtual;
    }

    @Override
    public String getDescricao() {
        return "Vivo";
    }
}
