package com.chronoaegis.combate_service.dto;

import lombok.Data;
import com.chronoaegis.combate_service.enums.TipoAcao;

import java.util.UUID;

@Data
public class AcaoDTO {
    private UUID sessionId;
    private TipoAcao tipoAcao;
}
