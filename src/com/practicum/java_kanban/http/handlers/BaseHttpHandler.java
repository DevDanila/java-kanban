package com.practicum.java_kanban.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
	protected final Gson gson = new Gson();

	protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
		byte[] resp = text.getBytes(StandardCharsets.UTF_8);
		exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
		exchange.sendResponseHeaders(statusCode, resp.length);
		exchange.getResponseBody().write(resp);
		exchange.close();
	}

	protected void sendSuccess(HttpExchange exchange) throws IOException {
		sendText(exchange, "", 200);
	}

	protected void sendCreated(HttpExchange exchange) throws IOException {
		sendText(exchange, "", 201);
	}

	protected void sendNotFound(HttpExchange exchange) throws IOException {
		sendText(exchange, "Not Found", 404);
	}

	protected void sendNotAcceptable(HttpExchange exchange) throws IOException {
		sendText(exchange, "Not Acceptable", 406);
	}


	protected void sendInternalError(HttpExchange exchange) throws IOException {
		sendText(exchange, "Internal Server Error", 500);
	}
}