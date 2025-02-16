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
	private final Gson gson;

	public EpicHandler(TaskManager taskManager, Gson gson) {
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

	private void handleGet(HttpExchange exchange) throws IOException {
		String path = exchange.getRequestURI().getPath();

		if (path.matches("^/epics/\\d+$")) {
			String[] parts = path.split("/");
			int epicId = Integer.parseInt(parts[2]);
			Epic epic = taskManager.getEpicById(epicId);

			if (epic == null) {
				sendResponse(exchange, "Эпик не найден", 404);
			} else {
				sendText(exchange, gson.toJson(epic), 200);
			}
		} else if (path.equals("/epics")) {
			sendText(exchange, gson.toJson(taskManager.getAllEpics()), 200);
		} else {
			sendNotFound(exchange);
		}
	}

	private void handlePost(HttpExchange exchange) throws IOException {
		String path = exchange.getRequestURI().getPath();
		InputStream body = exchange.getRequestBody();
		String requestBody = new String(body.readAllBytes(), StandardCharsets.UTF_8);
		Epic epic = gson.fromJson(requestBody, Epic.class);

		if (path.matches("^/epics/\\d+$")) {
			String[] parts = path.split("/");
			int epicId = Integer.parseInt(parts[2]);

			if (taskManager.getEpicById(epicId) == null) {
				sendResponse(exchange, "Эпик не найден", 404);
				return;
			}

			epic.setId(epicId);
			taskManager.updateEpic(epic);
			sendResponse(exchange, gson.toJson(epic), 200);
		} else {
			if (epic.getId() != 0) {
				sendResponse(exchange, "Для нового эпика ID должен быть 0", 400);
				return;
			}
			taskManager.addEpic(epic);
			sendResponse(exchange, gson.toJson(epic), 201);
		}
	}

	private void handleDelete(HttpExchange exchange) throws IOException {
		String path = exchange.getRequestURI().getPath();
		if (path.matches("^/epics/\\d+$")) {
			String[] parts = path.split("/");
			int epicId = Integer.parseInt(parts[2]);

			if (taskManager.getEpicById(epicId) == null) {
				sendResponse(exchange, "Эпик не найден", 404);
				return;
			}

			taskManager.deleteEpic(epicId);
			sendResponse(exchange, "Эпик удален", 200);
		} else {
			sendNotFound(exchange);
		}
	}
}