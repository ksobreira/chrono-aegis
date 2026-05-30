package com.chronoaegis.combate_service.dto;

import com.chronoaegis.combate_service.enums.TipoAcao;
import lombok.Data;

import java.util.UUID;

@Data
public class AcaoDTO {
    private UUID sessionId;
    private TipoAcao tipoAcao;
}
