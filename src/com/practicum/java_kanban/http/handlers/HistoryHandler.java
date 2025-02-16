package com.practicum.java_kanban.http.handlers;

import com.google.gson.Gson;
import com.practicum.java_kanban.manager.TaskManager;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                String path = exchange.getRequestURI().getPath();

                if (path.equals("/history")) {
                    sendText(exchange, gson.toJson(taskManager.getHistory()), 200);
                } else {
                    sendNotFound(exchange);
                }
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendError(exchange, e.getMessage());
        }
    }
}