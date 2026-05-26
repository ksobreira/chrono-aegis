package com.chronoaegis.combate_service.state;

import com.chronoaegis.combate_service.command.ComandoBatalha;
import com.chronoaegis.combate_service.dto.ResultadoTurnoDTO;
import com.chronoaegis.combate_service.model.BatalhaSession;

public class TurnoInimigoState implements EstadoCombate {

    @Override
    public ResultadoTurnoDTO processar(BatalhaSession sessao, ComandoBatalha comando) {
        // Delega o ataque automático do inimigo ao TurnoManager (stateless collaborator)
        sessao.getTurnoManager().executarTurnoInimigo(sessao);

        // Encadeia para processamento de efeitos de status
        sessao.setTurnoAtual(new ProcessandoEfeitosState());
        return sessao.getTurnoAtual().processar(sessao, null);
    }
}
