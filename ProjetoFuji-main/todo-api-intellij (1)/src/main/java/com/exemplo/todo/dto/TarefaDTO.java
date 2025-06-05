package com.exemplo.todo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TarefaDTO {
    @NotBlank
    private String titulo;

    private String descricao;

    private Boolean concluida = false;

    @NotNull
    private Long usuarioId;
}
