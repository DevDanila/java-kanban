package com.practicum.java_kanban.manager;


import com.practicum.java_kanban.model.Status;
import com.practicum.java_kanban.model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
	TaskManager taskManager;
	HistoryManager historyManager;

	@BeforeEach
	void beforeEach() {
		taskManager = Managers.getDefault();
		historyManager = Managers.getDefaultHistory();
	}

	@Test
	void addHistory() {
		Task task = new Task("Тест1", "Тест 1");
		historyManager.add(task);
		final List<Task> history = historyManager.getHistory();
		assertNotNull(history, "История не пустая");
		assertEquals(1, history.size(), "История не пустая");
	}

	@Test
	void canDeleteRepeatsFromHistory() {
		Task task = new Task("Тест2", "Тест 2");
		taskManager.addTask(task);
		final int taskId = task.getId();
		taskManager.getTaskById(taskId);
		taskManager.getTaskById(taskId);
		taskManager.getTaskById(taskId);

		final List<Task> history = taskManager.getHistory();
		assertNotNull(history, "История не пустая");
		assertEquals(1, history.size(), "История не пустая");
	}
}
