package com.chronoaegis.combate_service.command;

import com.chronoaegis.combate_service.dto.ResultadoTurnoDTO;
import com.chronoaegis.combate_service.model.BatalhaSession;
import com.chronoaegis.combate_service.strategy.DamageCalculationStrategy;
import com.chronoaegis.combate_service.strategy.FinesseStrategy;
import com.chronoaegis.combate_service.strategy.NormalDamageStrategy;

import java.util.List;

public class AtacarCommand implements ComandoBatalha {

    @Override
    public ResultadoTurnoDTO executar(BatalhaSession sessao) {
        DamageCalculationStrategy strategy;
        if ("ACUIDADE".equalsIgnoreCase(sessao.getTagArma())) {
            strategy = new FinesseStrategy();
        } else if ("VANGUARDA".equalsIgnoreCase(sessao.getPosicao())) {
            strategy = new NormalDamageStrategy(1.2);
        } else {
            strategy = new NormalDamageStrategy(0.8);
        }

        int dano = strategy.calcular(sessao.getAtaquePersonagem(), sessao.getDanoArma(), sessao.getMonstro().getDefesa());

        sessao.getMonstro().setHpAtual(sessao.getMonstro().getHpAtual() - dano);

        ResultadoTurnoDTO resultado = new ResultadoTurnoDTO();
        resultado.setHpJogador(sessao.getHpAtualPersonagem());
        resultado.setHpMonstro(sessao.getMonstro().getHpAtual());
        resultado.setTurnoAtual("TURNO_INIMIGO");
        // CORRIGIDO: só a mensagem do turno atual, sem acumular
        resultado.setLog(List.of("Jogador atacou! Dano: " + dano + " | HP Monstro: " + sessao.getMonstro().getHpAtual()));
        resultado.setFimDeBatalha(false);
        resultado.setVitoria(false);

        return resultado;
    }
}