package com.exemplo.todo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioDTO {
    @NotBlank
    private String nome;

    @Email
    private String email;
}
