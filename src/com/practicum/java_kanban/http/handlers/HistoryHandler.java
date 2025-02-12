package com.practicum.java_kanban.http.handlers;

import com.google.gson.Gson;
import com.practicum.java_kanban.manager.TaskManager;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;


public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
	    if (exchange.getRequestMethod().equals("GET")) {
		    sendText(exchange, gson.toJson(taskManager.getHistory()), 200);
	    } else {
		    sendNotFound(exchange);
	    }
    }
}