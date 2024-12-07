package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

	@Test
	public void testSave() throws IOException {
		File testFile = File.createTempFile("testCSV", ".csv");
		FileBackedTaskManager testManager = new FileBackedTaskManager(testFile);
		testManager.save();

		List<Task> tasks = testManager.getAllTasks();
		assertEquals(0, tasks.size());
	}

	@Test
	public void testSaveOneTask() throws IOException {
		File testFile = File.createTempFile("testCSV", ".csv");
		FileBackedTaskManager testManager = new FileBackedTaskManager(testFile);
		Task task = new Task("1", "1");
		testManager.addTask(task);
		testManager.save();

		List<Task> testTasks = testManager.getAllTasks();
		assertEquals(1, testTasks.size());
		assertEquals(task, testTasks.getFirst());
	}
}