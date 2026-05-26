package com.chronoaegis.combate_service.state;

import com.chronoaegis.combate_service.command.ComandoBatalha;
import com.chronoaegis.combate_service.dto.ResultadoTurnoDTO;
import com.chronoaegis.combate_service.model.BatalhaSession;

public class FimDeCombateState implements EstadoCombate {

    @Override
    public ResultadoTurnoDTO processar(BatalhaSession sessao, ComandoBatalha comando) {
        // Guarda: não reprocessar se sessão já foi encerrada (ex.: chamada duplicada do cliente)
        if (sessao.isEncerrada()) {
            throw new IllegalStateException("Batalha já encerrada");
        }

        // Notifica observers — XPObserver age apenas em "VITORIA"; LogObserver registra ambos
        boolean vitoria = sessao.getMonstro().getHpAtual() <= 0;
        sessao.notificarObservers(vitoria ? "VITORIA" : "DERROTA");

        // Encerra sessão
        sessao.setEncerrada(true);

        ResultadoTurnoDTO resultado = new ResultadoTurnoDTO();
        resultado.setFimDeBatalha(true);
        resultado.setVitoria(vitoria);
        resultado.setHpJogador(sessao.getHpAtualPersonagem());
        resultado.setHpMonstro(sessao.getMonstro().getHpAtual());
        resultado.setLog(sessao.getLog());
        resultado.setTurnoAtual(getClass().getSimpleName());
        return resultado;
    }
}
