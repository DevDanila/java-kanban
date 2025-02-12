package com.practicum.java_kanban.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.practicum.java_kanban.http.handlers.*;
import com.sun.net.httpserver.HttpServer;
import com.practicum.java_kanban.manager.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson = new Gson();

	public HttpTaskServer(TaskManager taskManager) throws IOException {
		server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/tasks", new TaskHandler(taskManager));
        server.createContext("/subtasks", new SubtaskHandler(taskManager));
        server.createContext("/epics", new EpicHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на порту " + PORT);
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен");
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
    }
}