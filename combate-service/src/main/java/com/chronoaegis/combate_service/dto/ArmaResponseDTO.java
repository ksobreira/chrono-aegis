package com.chronoaegis.combate_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArmaResponseDTO {
    private String nome;
    private int danoBase;
    private List<String> tags;
}
