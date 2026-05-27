package com.chronoaegis.combate_service.command;

import com.chronoaegis.combate_service.dto.ResultadoTurnoDTO;
import com.chronoaegis.combate_service.model.BatalhaSession;

public class UsarItemCommand implements ComandoBatalha {

    @Override
    public ResultadoTurnoDTO executar(BatalhaSession sessao) {
        sessao.getLog().add("Uso de itens ainda não implementado.");

        ResultadoTurnoDTO resultado = new ResultadoTurnoDTO();
        resultado.setFimDeBatalha(false);
        resultado.setVitoria(false);
        resultado.setLog(sessao.getLog());
        resultado.setHpJogador(sessao.getHpAtualPersonagem());
        resultado.setHpMonstro(sessao.getMonstro().getHpAtual());

        return resultado;
    }
}
