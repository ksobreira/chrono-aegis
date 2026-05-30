package com.chronoaegis.combate_service.state;

import com.chronoaegis.combate_service.command.ComandoBatalha;
import com.chronoaegis.combate_service.dto.ResultadoTurnoDTO;
import com.chronoaegis.combate_service.model.BatalhaSession;

import java.util.List;

public class TurnoJogadorState implements EstadoCombate {

    @Override
    public ResultadoTurnoDTO processar(BatalhaSession sessao, ComandoBatalha comando) {
        if (sessao.isEncerrada()) {
            throw new IllegalStateException("Batalha já encerrada");
        }

        // Personagem atordoado: perde o turno
        if (!sessao.getEstadoPersonagem().podeAgir()) {
            // Aplica efeito de atordoamento (decrementa contador)
            sessao.getEstadoPersonagem().aplicarEfeito(sessao.getHpAtualPersonagem());

            // Encadeia direto para turno do inimigo
            sessao.setTurnoAtual(new TurnoInimigoState());
            ResultadoTurnoDTO r = sessao.getTurnoAtual().processar(sessao, null);
            // Adiciona aviso de atordoamento no início do log
            List<String> logsComAviso = new java.util.ArrayList<>();
            logsComAviso.add("💫 Atordoado! Turno perdido.");
            logsComAviso.addAll(r.getLog());
            r.setLog(logsComAviso);
            return r;
        }

        // Executa ação do jogador
        ResultadoTurnoDTO resultadoAcao = comando.executar(sessao);

        // Fuga bem-sucedida
        if (sessao.isEncerrada()) {
            resultadoAcao.setLog(List.of("💨 Você fugiu da batalha!"));
            resultadoAcao.setFimDeBatalha(true);
            resultadoAcao.setVitoria(false);
            sessao.setTurnoAtual(new FimDeCombateState());
            return resultadoAcao;
        }

        // Monstro morreu
        if (sessao.getMonstro().getHpAtual() <= 0) {
            sessao.setTurnoAtual(new ProcessandoEfeitosState());
            return sessao.getTurnoAtual().processar(sessao, null);
        }

        // Turno normal: encadeia inimigo
        sessao.setTurnoAtual(new TurnoInimigoState());
        return sessao.getTurnoAtual().processar(sessao, null);
    }
}