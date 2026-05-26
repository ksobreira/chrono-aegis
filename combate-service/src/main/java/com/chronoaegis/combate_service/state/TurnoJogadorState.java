package com.chronoaegis.combate_service.state;

import com.chronoaegis.combate_service.command.ComandoBatalha;
import com.chronoaegis.combate_service.dto.ResultadoTurnoDTO;
import com.chronoaegis.combate_service.model.BatalhaSession;

public class TurnoJogadorState implements EstadoCombate {

    @Override
    public ResultadoTurnoDTO processar(BatalhaSession sessao, ComandoBatalha comando) {
        if (sessao.isEncerrada()) {
            throw new IllegalStateException("Batalha já encerrada");
        }

        // Personagem atordoado: perde o turno mas o inimigo ainda age
        if (!sessao.getEstadoPersonagem().podeAgir()) {
            sessao.getLog().add("Personagem atordoado, turno perdido!");
        } else {
            comando.executar(sessao);

            // Fuga: encerra imediatamente, sem turno do inimigo
            if (sessao.isEncerrada()) {
                ResultadoTurnoDTO r = new ResultadoTurnoDTO();
                r.setLog(sessao.getLog());
                r.setHpJogador(sessao.getHpAtualPersonagem());
                r.setHpMonstro(sessao.getMonstro().getHpAtual());
                r.setFimDeBatalha(true);
                r.setVitoria(false);
                sessao.setTurnoAtual(new FimDeCombateState());
                r.setTurnoAtual(sessao.getTurnoAtual().getClass().getSimpleName());
                return r;
            }

            // Monstro morreu pelo ataque do jogador: vai direto para efeitos
            if (sessao.getMonstro().getHpAtual() <= 0) {
                sessao.setTurnoAtual(new ProcessandoEfeitosState());
                return sessao.getTurnoAtual().processar(sessao, null);
            }
        }

        // Caso normal: encadeia turno do inimigo → processando efeitos automaticamente
        sessao.setTurnoAtual(new TurnoInimigoState());
        return sessao.getTurnoAtual().processar(sessao, null);
    }
}
