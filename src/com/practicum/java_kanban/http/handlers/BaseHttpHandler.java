package com.practicum.java_kanban.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {

	protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
		byte[] resp = text.getBytes(StandardCharsets.UTF_8);
		exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
		exchange.sendResponseHeaders(statusCode, resp.length);
		exchange.getResponseBody().write(resp);
		exchange.close();
	}

	protected void sendResponse(HttpExchange exchange, String message, int statusCode) throws IOException {
		byte[] response = message.getBytes();
		exchange.getResponseHeaders().add("Content-Type", "text/plain");
		exchange.sendResponseHeaders(statusCode, response.length);
		try (OutputStream os = exchange.getResponseBody()) {
			os.write(response);
		}
	}

	protected void sendError(HttpExchange exchange, String errorMessage) throws IOException {
		sendResponse(exchange, "Ошибка: " + errorMessage, 500);
	}

	protected void sendNotFound(HttpExchange exchange) throws IOException {
		sendResponse(exchange, "Ресурс не найден", 404);
	}
}