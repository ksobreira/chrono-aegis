package com.chronoaegis.combate_service.state;

public class EstadoMorto implements EstadoPersonagem {
    @Override
    public boolean podeAgir() {
        return false;
    }

    @Override
    public int aplicarEfeito(int hpAtual) {
        return 0;
    }

    @Override
    public String getDescricao() {
        return "Morto";
    }
}
