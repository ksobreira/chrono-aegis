package com.chronoaegis.combate_service.command;

import com.chronoaegis.combate_service.dto.ResultadoTurnoDTO;
import com.chronoaegis.combate_service.model.BatalhaSession;

public class FugirCommand implements ComandoBatalha {

    @Override
    public ResultadoTurnoDTO executar(BatalhaSession sessao) {
        sessao.setEncerrada(true);
        sessao.getLog().add("Jogador fugiu da batalha!");
        sessao.notificarObservers("FUGA");

        ResultadoTurnoDTO resultado = new ResultadoTurnoDTO();
        resultado.setFimDeBatalha(true);
        resultado.setVitoria(false);
        resultado.setLog(sessao.getLog());

        return resultado;
    }
}
