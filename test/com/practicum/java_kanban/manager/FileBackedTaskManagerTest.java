package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.model.Epic;
import com.practicum.java_kanban.model.Subtask;
import com.practicum.java_kanban.model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

	@Test
	public void testSaveAndGetEmptyFile() throws IOException {
		File testFile = File.createTempFile("testCSV", ".csv");
		testFile.deleteOnExit();
		FileBackedTaskManager testManager = new FileBackedTaskManager(testFile);


		List<Task> tasks = testManager.getAllTasks();
		assertEquals(0, tasks.size());
	}

	@Test
	public void testSaveAndGetTwoTasks() throws IOException {
		File testFile = File.createTempFile("testCSV", ".csv");
		testFile.deleteOnExit();
		FileBackedTaskManager testManager = new FileBackedTaskManager(testFile);
		Task task = new Task("1", "1");
		Task task2 = new Epic("2", "2");

		testManager.addTask(task);
		testManager.addTask(task2);


		List<Task> testTasks = testManager.getAllTasks();
		assertEquals(2, testTasks.size());
		assertEquals(task, testTasks.getFirst());
	}
}