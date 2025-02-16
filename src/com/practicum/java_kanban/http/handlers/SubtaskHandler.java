package com.practicum.java_kanban.http.handlers;

import com.google.gson.Gson;
import com.practicum.java_kanban.manager.TaskManager;
import com.practicum.java_kanban.model.Subtask;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SubtaskHandler extends BaseHttpHandler {
	private final TaskManager taskManager;
	private final Gson gson;

	public SubtaskHandler(TaskManager taskManager, Gson gson) {
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

		if (path.matches("^/subtasks/\\d+$")) {
			String[] parts = path.split("/");
			int subtaskId = Integer.parseInt(parts[2]);
			Subtask subtask = taskManager.getSubtaskById(subtaskId);

			if (subtask == null) {
				sendResponse(exchange, "Подзадача не найдена", 404);
			} else {
				sendText(exchange, gson.toJson(subtask), 200);
			}
		} else if (path.equals("/subtasks")) {
			sendText(exchange, gson.toJson(taskManager.getAllSubtasks()), 200);
		} else {
			sendNotFound(exchange);
		}
	}

	private void handlePost(HttpExchange exchange) throws IOException {
		String path = exchange.getRequestURI().getPath();
		InputStream body = exchange.getRequestBody();
		String requestBody = new String(body.readAllBytes(), StandardCharsets.UTF_8);
		Subtask subtask = gson.fromJson(requestBody, Subtask.class);

		if (path.matches("^/subtasks/\\d+$")) {
			String[] parts = path.split("/");
			int subtaskId = Integer.parseInt(parts[2]);

			if (taskManager.getSubtaskById(subtaskId) == null) {
				sendResponse(exchange, "Подзадача не найдена", 404);
				return;
			}

			subtask.setId(subtaskId);
			taskManager.updateSubTask(subtask);
			sendResponse(exchange, gson.toJson(subtask), 200);
		} else {
			if (subtask.getId() != 0) {
				sendResponse(exchange, "Для новой подзадачи ID должен быть 0", 400);
				return;
			}
			taskManager.addSubTask(subtask);
			sendResponse(exchange, gson.toJson(subtask), 201);
		}
	}

	private void handleDelete(HttpExchange exchange) throws IOException {
		String path = exchange.getRequestURI().getPath();
		if (path.matches("^/subtasks/\\d+$")) {
			String[] parts = path.split("/");
			int subtaskId = Integer.parseInt(parts[2]);

			if (taskManager.getSubtaskById(subtaskId) == null) {
				sendResponse(exchange, "Подзадача не найдена", 404);
				return;
			}
			taskManager.deleteSubtask(subtaskId);
			sendResponse(exchange, "Подзадача удалена", 200);
		} else {
			sendNotFound(exchange);
		}
	}
}