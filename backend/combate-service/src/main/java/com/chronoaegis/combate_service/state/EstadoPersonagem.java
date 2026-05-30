package com.chronoaegis.combate_service.state;

public interface EstadoPersonagem {
    boolean podeAgir();

    int aplicarEfeito(int hpAtual);

    String getDescricao();
}
