package com.chronoaegis.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AtualizarUsuarioDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nomeJogador;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    private String email;
}