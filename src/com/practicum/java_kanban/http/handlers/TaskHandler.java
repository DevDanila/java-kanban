package com.practicum.java_kanban.http.handlers;

import com.google.gson.Gson;
import com.practicum.java_kanban.manager.*;
import com.sun.net.httpserver.HttpExchange;
import com.practicum.java_kanban.model.*;

import java.io.IOException;
import java.util.List;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private  Gson gson = new Gson();

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    sendNotFound(exchange);
                    break;
            }
        } catch (Exception e) {
            sendError(exchange, e.getMessage());
        }
    }


    public void handleGet(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            List<Task> tasks = taskManager.getAllTasks();
            sendText(exchange, gson.toJson(tasks), 200);
        } else {
            sendNotFound(exchange);
        }
    }


    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream body = exchange.getRequestBody();
        String requestBody = new String(body.readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(requestBody, Task.class);

        if (task.getId() == 0) {
            taskManager.addTask(task);
            sendResponse(exchange, gson.toJson(task), 201);
        } else {
            taskManager.updateTask(task);
            sendResponse(exchange, gson.toJson(task), 200);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
        if (taskManager.getTaskById(id) != null) {
            taskManager.deleteTaskById(id);
            sendResponse(exchange, "Task deleted", 200);
        } else {
            sendNotFound(exchange);
        }
    }


    private void sendError(HttpExchange exchange, String errorMessage) throws IOException {
        sendResponse(exchange, errorMessage, 500);

    }

    private void sendResponse(HttpExchange exchange, String json, int statusCode) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        try (var outputStream = exchange.getResponseBody()) {
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        }
    }
}