package com.practicum.java_kanban.http.handlers;

import com.practicum.java_kanban.manager.*;
import com.sun.net.httpserver.HttpExchange;
import com.practicum.java_kanban.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler {
	private final TaskManager taskManager;

	public TaskHandler(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		switch (exchange.getRequestMethod()) {
			case "GET":
				if (exchange.getRequestURI().getPath().endsWith("/tasks")) {
					sendText(exchange, gson.toJson(taskManager.getAllTasks()), 200);
				} else {
					int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
					Task task = taskManager.getTaskById(id);
					if (task != null) {
						sendText(exchange, gson.toJson(task), 200);
					} else {
						sendNotFound(exchange);
					}
				}
				break;
			case "POST":
				InputStream body = exchange.getRequestBody();
				String requestBody = new String(body.readAllBytes(), StandardCharsets.UTF_8);
				Task task = gson.fromJson(requestBody, Task.class);
				try {
					if (task.getId() == 0) {
						taskManager.addTask(task);
						sendText(exchange, gson.toJson(task), 201);
					} else {
						taskManager.updateTask(task);
						sendText(exchange, gson.toJson(task), 200);
					}
				} catch (IllegalArgumentException e) {
					sendNotAcceptable(exchange);
				}
				break;
			case "DELETE":
				int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
				taskManager.deleteTaskById(id);
				sendText(exchange, "Task deleted", 200);
				break;
			default:
				sendNotFound(exchange);
				break;
		}
	}
}