package com.practicum.java_kanban.http.handlers;

import com.google.gson.Gson;
import com.practicum.java_kanban.manager.TaskManager;
import com.practicum.java_kanban.model.Epic;
import com.sun.net.httpserver.HttpExchange;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class EpicHandler extends BaseHttpHandler {
	private final TaskManager taskManager;
	private final Gson gson = new Gson();

	public EpicHandler(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		switch (exchange.getRequestMethod()) {
			case "GET":
				if (exchange.getRequestURI().getPath().endsWith("/epics")) {
					sendText(exchange, gson.toJson(taskManager.getAllEpics()), 200);
				} else {
					int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
					Epic epic = taskManager.getEpicById(id);
					if (epic != null) {
						sendText(exchange, gson.toJson(epic), 200);
					} else {
						sendNotFound(exchange);
					}
				}
				break;
			case "POST":
				InputStream body = exchange.getRequestBody();
				String requestBody = new String(body.readAllBytes(), StandardCharsets.UTF_8);
				Epic epic = gson.fromJson(requestBody, Epic.class);
				try {
					if (epic.getId() == 0) {
						taskManager.addEpic(epic);
						sendText(exchange, gson.toJson(epic), 201);
					} else {
						taskManager.updateEpic(epic);
						sendText(exchange, gson.toJson(epic), 200);
					}
				} catch (IllegalArgumentException e) {
					sendNotAcceptable(exchange);
				}
				break;

			case "DELETE":
				int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
				taskManager.deleteEpic(id);
				sendText(exchange, "Epic deleted", 200);
				break;
			default:
				sendNotFound(exchange);
				break;
		}
	}
}