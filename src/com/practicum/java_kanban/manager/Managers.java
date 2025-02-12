package com.practicum.java_kanban.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.practicum.java_kanban.http.adapters.DurationAdapter;
import com.practicum.java_kanban.http.adapters.LocalDateTimeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {


	public static TaskManager getDefault() {
		return new InMemoryTaskManager();
	}

	public static HistoryManager getDefaultHistory() {
		return new InMemoryHistoryManager();
	}

	public static Gson getGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
		gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
		return gsonBuilder.create();
	}
}