package com.practicum.java_kanban.manager;

import org.junit.jupiter.api.Test;
import com.practicum.java_kanban.model.*;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

	@Test
	void testGetDefaultTaskManager() {
		TaskManager taskManager = Managers.getDefault();
		assertNotNull(taskManager);
	}

	@Test
	void testGetDefaultHistoryManager() {
		HistoryManager historyManager = Managers.getDefaultHistory();
		assertNotNull(historyManager);
	}

	@Test
	void testCreateCopyTaskManager() {
		TaskManager taskManager = Managers.getDefault();
		Task task1 = new Task("Task 1", "task_1");
		task1.setTitle("Task new");
		taskManager.updateTask(task1);
		assertEquals("Task new", task1.getTitle());
	}
}