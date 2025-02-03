package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.model.Status;
import com.practicum.java_kanban.model.Epic;
import com.practicum.java_kanban.model.Subtask;

public class Managers {


	public static TaskManager getDefault() {
		return new InMemoryTaskManager();
	}

	public static HistoryManager getDefaultHistory() {
		return new InMemoryHistoryManager();
	}
}