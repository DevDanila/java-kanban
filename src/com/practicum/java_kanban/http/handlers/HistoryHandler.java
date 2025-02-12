package com.practicum.java_kanban.http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.practicum.java_kanban.http.adapters.DurationAdapter;
import com.practicum.java_kanban.http.adapters.LocalDateTimeAdapter;
import com.practicum.java_kanban.manager.TaskManager;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;


public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
       protected final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

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