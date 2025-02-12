package com.practicum.java_kanban.http.handlers;

import com.practicum.java_kanban.manager.TaskManager;
import com.practicum.java_kanban.model.Subtask;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class SubtaskHandler extends BaseHttpHandler {
	private final TaskManager taskManager;

	public SubtaskHandler(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		switch (exchange.getRequestMethod()) {
			case "GET":
				if (exchange.getRequestURI().getPath().endsWith("/subtasks")) {
					sendText(exchange, gson.toJson(taskManager.getAllSubtasks()), 200);
				} else {
					int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
					Subtask subTask = taskManager.getSubtaskById(id);
					if (subTask != null) {
						sendText(exchange, gson.toJson(subTask), 200);
					} else {
						sendNotFound(exchange);
					}
				}
				break;
			case "POST":
				InputStream body = exchange.getRequestBody();
				String requestBody = new String(body.readAllBytes(), StandardCharsets.UTF_8);
				Subtask subtask = gson.fromJson(requestBody, Subtask.class);
				try {
					if (subtask.getId() == 0) {
						taskManager.addSubTask(subtask);
						sendText(exchange, gson.toJson(subtask), 201);
					} else {
						taskManager.updateSubTask(subtask);
						sendText(exchange, gson.toJson(subtask), 200);
					}
				} catch (IllegalArgumentException e) {
					sendNotAcceptable(exchange);
				}
				break;
			case "DELETE":
				int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
				taskManager.deleteSubtask(id);
				sendText(exchange, "SubTask deleted", 200);
				break;
			default:
				sendNotFound(exchange);
				break;
		}
	}
}