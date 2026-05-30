package com.chronoaegis.combate_service.command;

import com.chronoaegis.combate_service.dto.ResultadoTurnoDTO;
import com.chronoaegis.combate_service.model.BatalhaSession;

public interface ComandoBatalha {
    ResultadoTurnoDTO executar(BatalhaSession sessao);
}
