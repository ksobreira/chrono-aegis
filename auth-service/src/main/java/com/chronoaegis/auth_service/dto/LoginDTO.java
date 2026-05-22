package com.chronoaegis.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @Email(message = "Email inválido")
    @NotBlank(message = "Email obrigatório")
    private String email;

    @NotBlank(message = "Senha obrigatória")
    private String senha;

}
