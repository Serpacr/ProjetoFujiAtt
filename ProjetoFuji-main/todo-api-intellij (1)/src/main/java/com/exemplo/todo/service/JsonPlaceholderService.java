package com.exemplo.todo.service;

import com.exemplo.todo.dto.JsonPlaceholderTarefaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;

@Service
public class JsonPlaceholderService {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/todos";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonPlaceholderTarefaDTO buscarTarefaPorId(long id) throws IOException, InterruptedException {
        String url = BASE_URL + id;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), JsonPlaceholderTarefaDTO.class);
    }
}
