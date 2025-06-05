package com.exemplo.todo.service;

import com.exemplo.todo.dto.JsonPlaceholderTarefaDTO;
import com.exemplo.todo.dto.TarefaDTO;

public class ConversorDtoService {
    public static TarefaDTO converterJsonParaTarefaDTO(JsonPlaceholderTarefaDTO externalDto) {
        TarefaDTO dto = new TarefaDTO();
        dto.setTitulo(externalDto.getTitle());
        dto.setConcluida(externalDto.getCompleted());
        dto.setUsuarioId(externalDto.getUserId());
        dto.setDescricao(null);
        return dto;
    }
}
