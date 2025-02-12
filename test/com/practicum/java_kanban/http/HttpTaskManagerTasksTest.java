package com.practicum.java_kanban.http;

import com.google.gson.Gson;
import com.practicum.java_kanban.manager.Managers;
import com.practicum.java_kanban.manager.TaskManager;
import com.practicum.java_kanban.model.Status;
import com.practicum.java_kanban.model.Task;
import com.practicum.java_kanban.model.Epic;
import com.practicum.java_kanban.model.Subtask;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {

	private static final TaskManager manager = Managers.getDefault();
	private static HttpTaskServer taskServer;
	private final Gson gson = Managers.getGson();

	@BeforeAll
	public static void beforeAll() throws IOException {
		taskServer = new HttpTaskServer(manager);
		taskServer.start();
	}

	@BeforeEach
	void init() {
		manager.deleteAllTasks();
		manager.deleteAllSubtask();
		manager.deleteAllEpics();
	}

	@AfterAll
	public static void afterAll() {
		taskServer.stop();
	}

	@Test
	public void testDeleteEpic() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic to delete", "Epic description");
		manager.addEpic(epic);

		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/epics/" + epic.getId());

		HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(200, response.statusCode());


		assertNull(manager.getEpicById(epic.getId()), "Epic should be deleted and return null");
	}


	@Test
	public void testDeleteSubTask() throws IOException, InterruptedException {
		Epic dummyEpic = new Epic("Epic for delete", "Description");
		int epicId = manager.addEpic(dummyEpic).getId();

		Subtask subtask = new Subtask("Subtask to delete", "Description", epicId);
		manager.addSubTask(subtask);

		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/subtasks/" + subtask.getId());

		HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(200, response.statusCode());


		assertNull(manager.getSubtaskById(subtask.getId()), "SubTask should be deleted and return null");
	}

	@Test
	public void testAddTask() {
		Task task = new Task("New Task", "Task description", Duration.ofMinutes(30), LocalDateTime.now());
		Task createdTask = manager.addTask(task);

		assertNotNull(createdTask, "Task should be created");
		assertEquals("New Task", createdTask.getTitle(), "Task name should match");
		assertEquals(Status.NEW, createdTask.getStatus(), "Task status should be NEW");
	}

	@Test
	public void testAddSubtask() {
		Epic epic = new Epic("New Epic", "Epic description");
		Epic createdEpic = manager.addEpic(epic);
		Subtask subtask = new Subtask("New SubTask", "SubTask description", createdEpic.getId(), Duration.ofMinutes(15), LocalDateTime.now());
		Subtask createdSubTask = manager.addSubTask(subtask);

		assertNotNull(createdSubTask, "SubTask should be created");
		assertEquals(createdEpic.getId(), createdSubTask.getEpicId(), "SubTask should be associated with the correct Epic");
		assertTrue(createdEpic.getSubTasks().contains(createdSubTask), "Epic should contain the created SubTask");
	}


	@Test
	public void testAddEpic() {
		Epic epic = new Epic("New Epic", "Epic description");
		Epic createdEpic = manager.addEpic(epic);

		assertNotNull(createdEpic, "Epic should be created");
		assertEquals("New Epic", createdEpic.getTitle(), "Epic name should match");
		assertEquals(Status.NEW, createdEpic.getStatus(), "Epic status should be NEW");
	}

	@Test
	public void testGetSubtaskById() {
		Epic epic = new Epic("Get SubTask Epic", "Epic description");
		Epic createdEpic = manager.addEpic(epic);
		Subtask subtask = new Subtask("Get SubTask", "SubTask description", createdEpic.getId(), Duration.ofMinutes(15), LocalDateTime.now());
		Subtask createdSubTask = manager.addSubTask(subtask);

		Subtask fetchedSubTask = manager.getSubtaskById(createdSubTask.getId());
		assertNotNull(fetchedSubTask, "SubTask should be fetched");
		assertEquals(createdSubTask.getId(), fetchedSubTask.getId(), "Fetched SubTask ID should match");
	}
}