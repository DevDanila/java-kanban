package com.practicum.java_kanban.http.handlers;

import com.google.gson.Gson;
import com.practicum.java_kanban.manager.TaskManager;
import com.practicum.java_kanban.model.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {
	private final TaskManager taskManager;
	private final Gson gson;

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

	private void handleGet(HttpExchange exchange) throws IOException {
		String path = exchange.getRequestURI().getPath();

		if (path.matches("^/tasks/\\d+$")) {
			String[] parts = path.split("/");
			int taskId = Integer.parseInt(parts[2]);
			Task task = taskManager.getTaskById(taskId);

			if (task == null) {
				sendResponse(exchange, "Задача не найдена", 404);
			} else {
				sendText(exchange, gson.toJson(task), 200);
			}
		} else if (path.equals("/tasks")) {
			List<Task> tasks = taskManager.getAllTasks();
			sendText(exchange, gson.toJson(tasks), 200);
		} else {
			sendNotFound(exchange);
		}
	}

	private void handlePost(HttpExchange exchange) throws IOException {
		String path = exchange.getRequestURI().getPath();
		InputStream body = exchange.getRequestBody();
		String requestBody = new String(body.readAllBytes(), StandardCharsets.UTF_8);
		Task task = gson.fromJson(requestBody, Task.class);

		if (path.matches("^/tasks/\\d+$")) {
			String[] parts = path.split("/");
			int taskId = Integer.parseInt(parts[2]);

			if (taskManager.getTaskById(taskId) == null) {
				sendResponse(exchange, "Задача не найдена", 404);
				return;
			}

			task.setId(taskId);
			taskManager.updateTask(task);
			sendText(exchange, gson.toJson(task), 200);
		} else {
			if (task.getId() != 0) {
				sendResponse(exchange, "Для новой задачи ID должен быть 0", 400);
				return;
			}
			taskManager.addTask(task);
			sendText(exchange, gson.toJson(task), 201);
		}
	}

	private void handleDelete(HttpExchange exchange) throws IOException {
		String path = exchange.getRequestURI().getPath();
		if (path.matches("^/tasks/\\d+$")) {
			String[] parts = path.split("/");
			int taskId = Integer.parseInt(parts[2]);

			if (taskManager.getTaskById(taskId) == null) {
				sendResponse(exchange, "Задача не найдена", 404);
				return;
			}

			taskManager.deleteTaskById(taskId);
			sendResponse(exchange, "Задача удалена", 200);
		} else {
			sendNotFound(exchange);
		}
	}
}