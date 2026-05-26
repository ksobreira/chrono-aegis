package com.chronoaegis.inventario_service.dto;

import com.chronoaegis.inventario_service.enums.WeaponTag;
import lombok.Data;

import java.util.List;

@Data
public class ArmaDTO {
    private String nome;
    private int danoBase;
    private int forcaMinima;
    private List<WeaponTag> tags;
}
