package com.chronoaegis.combate_service.state;

import com.chronoaegis.combate_service.command.ComandoBatalha;
import com.chronoaegis.combate_service.dto.ResultadoTurnoDTO;
import com.chronoaegis.combate_service.model.BatalhaSession;

import java.util.List;

public class FimDeCombateState implements EstadoCombate {

    @Override
    public ResultadoTurnoDTO processar(BatalhaSession sessao, ComandoBatalha comando) {
        if (sessao.isEncerrada()) {
            throw new IllegalStateException("Batalha já encerrada");
        }

        boolean vitoria = sessao.getMonstro().getHpAtual() <= 0;
        sessao.notificarObservers(vitoria ? "VITORIA" : "DERROTA");
        sessao.setEncerrada(true);

        String msgFim = vitoria
                ? "🏆 " + sessao.getMonstro().getNome() + " foi derrotado! Vitória!"
                : "💀 " + sessao.getNomePersonagem() + " foi derrotado...";

        ResultadoTurnoDTO resultado = new ResultadoTurnoDTO();
        resultado.setFimDeBatalha(true);
        resultado.setVitoria(vitoria);
        resultado.setHpJogador(sessao.getHpAtualPersonagem());
        resultado.setHpMonstro(sessao.getMonstro().getHpAtual());
        resultado.setLog(List.of(msgFim));
        resultado.setTurnoAtual("FimDeCombateState");
        return resultado;
    }
}