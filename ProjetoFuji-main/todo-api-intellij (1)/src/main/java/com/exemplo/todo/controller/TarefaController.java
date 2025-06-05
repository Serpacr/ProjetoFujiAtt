package com.exemplo.todo.controller;

import com.exemplo.todo.dto.TarefaDTO;
import com.exemplo.todo.model.Tarefa;
import com.exemplo.todo.service.TarefaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
@RequiredArgsConstructor
public class TarefaController {

    private static final Logger logger = LoggerFactory.getLogger(TarefaController.class);

    private final TarefaService tarefaService;

    // ✅ Importação de tarefa de sistema externo
    @PostMapping("/importar/{id}")
    public ResponseEntity<?> importarTarefa(@PathVariable Long id) {
        logger.info("Recebida requisição para importar tarefa com ID externo: {}", id);
        try {
            Tarefa tarefa = tarefaService.importarTarefaDaApi(id);
            return ResponseEntity.ok(tarefa);
        } catch (IllegalArgumentException e) {
            logger.warn("Erro de validação ao importar tarefa: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro inesperado ao importar tarefa: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro inesperado: " + e.getMessage());
        }
    }

    // ✅ Listar todas
    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTarefas() {
        logger.info("Listando todas as tarefas");
        return ResponseEntity.ok(tarefaService.listarTodas());
    }

    // ✅ Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Long id) {
        logger.info("Buscando tarefa com ID: {}", id);
        return ResponseEntity.ok(tarefaService.buscarPorId(id));
    }

    // ✅ Criar tarefa
    @PostMapping
    public ResponseEntity<Tarefa> criar(@RequestBody @Valid TarefaDTO dto) {
        logger.info("Criando nova tarefa para usuário ID: {}", dto.getUsuarioId());
        return ResponseEntity.ok(tarefaService.criar(dto));
    }

    // ✅ Atualizar tarefa
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizar(@PathVariable Long id, @RequestBody @Valid TarefaDTO dto) {
        logger.info("Atualizando tarefa ID: {}", id);
        return ResponseEntity.ok(tarefaService.atualizar(id, dto));
    }

    // ✅ Deletar tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        logger.info("Deletando tarefa ID: {}", id);
        tarefaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}