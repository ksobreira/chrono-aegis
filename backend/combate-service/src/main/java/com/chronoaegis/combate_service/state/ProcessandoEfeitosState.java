package com.chronoaegis.combate_service.state;

import com.chronoaegis.combate_service.command.ComandoBatalha;
import com.chronoaegis.combate_service.dto.ResultadoTurnoDTO;
import com.chronoaegis.combate_service.model.BatalhaSession;

public class ProcessandoEfeitosState implements EstadoCombate {

    @Override
    public ResultadoTurnoDTO processar(BatalhaSession sessao, ComandoBatalha comando) {
        // Aplica efeitos de veneno (-5 HP/turno) ou atordoamento (decrementa contador)
        int novoHp = sessao.getEstadoPersonagem().aplicarEfeito(sessao.getHpAtualPersonagem());
        sessao.setHpAtualPersonagem(Math.max(0, novoHp));

        // Se veneno reduziu HP a 0, registra morte
        if (sessao.getHpAtualPersonagem() <= 0) {
            sessao.setEstadoPersonagem(new EstadoMorto());
        }

        // Verifica condições de fim de batalha e delega ao FimDeCombateState
        if (sessao.getMonstro().getHpAtual() <= 0 || sessao.getHpAtualPersonagem() <= 0) {
            sessao.setTurnoAtual(new FimDeCombateState());
            return sessao.getTurnoAtual().processar(sessao, null);
        }

        // Batalha continua: retorna turno ao jogador
        sessao.setTurnoAtual(new TurnoJogadorState());

        ResultadoTurnoDTO resultado = new ResultadoTurnoDTO();
        resultado.setFimDeBatalha(false);
        resultado.setVitoria(false);
        resultado.setHpJogador(sessao.getHpAtualPersonagem());
        resultado.setHpMonstro(sessao.getMonstro().getHpAtual());
        resultado.setLog(sessao.getLog());
        resultado.setTurnoAtual(sessao.getTurnoAtual().getClass().getSimpleName());
        return resultado;
    }
}
