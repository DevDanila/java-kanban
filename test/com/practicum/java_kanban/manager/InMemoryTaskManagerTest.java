package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.model.Epic;
import com.practicum.java_kanban.model.Status;
import com.practicum.java_kanban.model.Subtask;
import com.practicum.java_kanban.model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
	private static TaskManager taskManager;
	private static HistoryManager historyManager;
	private static Task task1;
	private static Epic epic1;
	private static Subtask subtask1;
	private static Subtask subtask2;


	@BeforeEach
	void beforeEach() {
		historyManager = Managers.getDefaultHistory();
		taskManager = Managers.getDefault();
		task1 = new Task("1", "task_1");
		epic1 = new Epic("2", "Epic_1");
		subtask1 = new Subtask("3", "Subtask_1", 2);
		subtask2 = new Subtask("4", "Subtask_2", 2);

	}


	@Test
	void testTaskManagerAddsTasksOfDifferentTypesAndFindThem() { // InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
		taskManager.addTask(task1);
		taskManager.addEpic(epic1);
		taskManager.addSubTask(subtask1);
		taskManager.addSubTask(subtask2);
		assertEquals(taskManager.getTaskById(1), task1);
		assertEquals(taskManager.getSubtaskById(3), subtask1);
		assertEquals(taskManager.getSubtaskById(4), subtask2);
		assertEquals(taskManager.getEpicById(2), epic1);
	}

	@Test
	void testImmutabilityOfTasks() { // тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
		taskManager.addTask(task1);
		assertEquals(taskManager.getTaskById(1).getTitle(), "1");
		assertEquals(taskManager.getTaskById(1).getDescription(), "task_1");
		assertEquals(taskManager.getTaskById(1).getStatus(), Status.NEW);
	}

	@Test
	public void testNoConflictWithSpecifiedIdAndGeneratedId() { // проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
		Task task1 = new Task("1", "1");
		Task task2 = new Task("2", "1");
		task1.setId(1);
		task2.setId(1);

		taskManager.addTask(task1);
		taskManager.addTask(task2);

		assertNotEquals(task1, taskManager.getTaskById(2));
	}

	@Test
	void testAddHistoryManager() { // убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
		ArrayList<Task> historyTest = new ArrayList<>();
		taskManager.addTask(task1);
		taskManager.addEpic(epic1);
		taskManager.addSubTask(subtask1);

		taskManager.getEpicById(2);
		historyTest.add(epic1);

		taskManager.getTaskById(1);
		historyTest.add(task1);

		taskManager.getSubtaskById(3);
		historyTest.add(subtask1);

		assertEquals(historyTest, taskManager.getHistory());//вернули и сравнили
	}
}
