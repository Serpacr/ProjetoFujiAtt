package com.exemplo.todo.dto;

import lombok.Data;
@Data
public class JsonPlaceholderTarefaDTO {
    private Long userId;
    private Long Id;
    private String title;
    private Boolean completed;
}
