package com.chronoaegis.combate_service.state;

import com.chronoaegis.combate_service.command.ComandoBatalha;
import com.chronoaegis.combate_service.dto.ResultadoTurnoDTO;
import com.chronoaegis.combate_service.model.BatalhaSession;

import java.util.ArrayList;
import java.util.List;

public class ProcessandoEfeitosState implements EstadoCombate {

    @Override
    public ResultadoTurnoDTO processar(BatalhaSession sessao, ComandoBatalha comando) {
        List<String> logs = new ArrayList<>();

        // Aplica efeito do estado atual (veneno ou atordoamento)
        String descricao = sessao.getEstadoPersonagem().getDescricao();
        int hpAntes = sessao.getHpAtualPersonagem();
        int novoHp = sessao.getEstadoPersonagem().aplicarEfeito(hpAntes);
        sessao.setHpAtualPersonagem(Math.max(0, novoHp));

        if (novoHp < hpAntes) {
            logs.add("☠️ Veneno causou " + (hpAntes - novoHp)
                    + " de dano! [" + descricao + "] HP: " + sessao.getHpAtualPersonagem());
        } else if ("Atordoado".equals(descricao)) {
            logs.add("💫 Efeito de atordoamento diminuiu.");
        }

        // Se veneno matou
        if (sessao.getHpAtualPersonagem() <= 0) {
            sessao.setEstadoPersonagem(new EstadoMorto());
        }

        // Verifica fim de batalha
        if (sessao.getMonstro().getHpAtual() <= 0 || sessao.getHpAtualPersonagem() <= 0) {
            sessao.setTurnoAtual(new FimDeCombateState());
            return sessao.getTurnoAtual().processar(sessao, null);
        }

        // Batalha continua
        sessao.setTurnoAtual(new TurnoJogadorState());
        ResultadoTurnoDTO resultado = new ResultadoTurnoDTO();
        resultado.setFimDeBatalha(false);
        resultado.setVitoria(false);
        resultado.setHpJogador(sessao.getHpAtualPersonagem());
        resultado.setHpMonstro(sessao.getMonstro().getHpAtual());
        resultado.setLog(logs);
        resultado.setTurnoAtual("TurnoJogadorState");
        return resultado;
    }
}