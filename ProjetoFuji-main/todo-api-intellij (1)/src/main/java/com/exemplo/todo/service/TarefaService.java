package com.exemplo.todo.service;

import com.exemplo.todo.dto.JsonPlaceholderTarefaDTO;
import com.exemplo.todo.dto.TarefaDTO;
import com.exemplo.todo.model.Tarefa;
import com.exemplo.todo.model.Usuario;
import com.exemplo.todo.repository.TarefaRepository;
import com.exemplo.todo.repository.UsuarioRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private static final Logger logger = LoggerFactory.getLogger(TarefaService.class);

    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;
    private final JsonPlaceholderService jsonPlaceholderService;
    private final Validator validator;

    // ✅ Importação de API externa
    public Tarefa importarTarefaDaApi(Long idTarefa) throws IOException, InterruptedException {
        logger.info("Iniciando importação da tarefa externa ID: {}", idTarefa);

        JsonPlaceholderTarefaDTO jsonDto = jsonPlaceholderService.buscarTarefaPorId(idTarefa);

        logger.debug("Dados recebidos da API externa: {}", jsonDto);

        if (jsonDto == null || jsonDto.getTitle() == null || jsonDto.getUserId() == null) {
            logger.warn("Dados incompletos vindos da API externa para o ID: {}", idTarefa);
            throw new IllegalArgumentException("Dados incompletos vindos da API externa");
        }

        TarefaDTO dto = ConversorDtoService.converterJsonParaTarefaDTO(jsonDto);

        Set<ConstraintViolation<TarefaDTO>> violacoes = validator.validate(dto);
        if (!violacoes.isEmpty()) {
            logger.error("Erro de validação: {}", violacoes);
            throw new IllegalArgumentException("Erro de validação: " + violacoes);
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> {
                    logger.error("Usuário com ID {} não encontrado", dto.getUsuarioId());
                    return new RuntimeException("Usuário com ID " + dto.getUsuarioId() + " não encontrado");
                });

        Tarefa tarefa = Tarefa.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .concluida(dto.getConcluida())
                .usuario(usuario)
                .build();

        tarefa = tarefaRepository.save(tarefa);

        logger.info("Tarefa importada com sucesso. ID salvo: {}", tarefa.getId());
        return tarefa;
    }

    // ✅ Métodos padrões
    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    public Tarefa buscarPorId(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com ID: " + id));
    }

    public Tarefa criar(TarefaDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + dto.getUsuarioId()));

        Tarefa tarefa = Tarefa.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .concluida(dto.getConcluida())
                .usuario(usuario)
                .build();

        return tarefaRepository.save(tarefa);
    }

    public Tarefa atualizar(Long id, TarefaDTO dto) {
        Tarefa existente = buscarPorId(id);

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + dto.getUsuarioId()));

        existente.setTitulo(dto.getTitulo());
        existente.setDescricao(dto.getDescricao());
        existente.setConcluida(dto.getConcluida());
        existente.setUsuario(usuario);

        return tarefaRepository.save(existente);
    }

    public void deletar(Long id) {
        Tarefa tarefa = buscarPorId(id);
        tarefaRepository.delete(tarefa);
    }
}