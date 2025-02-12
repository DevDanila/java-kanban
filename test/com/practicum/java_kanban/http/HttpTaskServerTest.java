
package com.practicum.java_kanban.http;

import com.google.gson.reflect.TypeToken;
import com.practicum.java_kanban.manager.InMemoryTaskManager;
import com.practicum.java_kanban.model.Task;
import com.practicum.java_kanban.model.Epic;
import com.practicum.java_kanban.model.Subtask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class HttpTaskServerTest {

	private HttpTaskServer taskServer;
	private InMemoryTaskManager manager;

	@BeforeEach
	public void setUp() throws IOException {
		manager = new InMemoryTaskManager();
		taskServer = new HttpTaskServer(manager);
		taskServer.start();
	}

	@AfterEach
	public void shutDown() {
		taskServer.stop();
	}

	@Test
void testGetHistory() throws IOException, InterruptedException {
    Task task = new Task("History Task", "Desc", Duration.ofMinutes(30), LocalDateTime.now());
    Task createdTask = manager.addTask(task);
    manager.getTaskById(createdTask.getId()); // Добавляем в историю

    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create("http://localhost:8080/history");
    HttpRequest request = HttpRequest.newBuilder()
            .uri(url)
            .GET()
            .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    assertEquals(200, response.statusCode());

    List<Task> history = HttpTaskServer.getGson().fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
    assertEquals(1, history.size());
    assertEquals("History Task", history.getFirst().getTitle());
}


	@Test
	void testAddTask() throws IOException, InterruptedException {
		Task task = new Task("Test Task", "Testing task description", Duration.ofMinutes(30), LocalDateTime.now());
		String taskJson = HttpTaskServer.getGson().toJson(task);

		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/tasks");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.POST(HttpRequest.BodyPublishers.ofString(taskJson))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(201, response.statusCode());

		List<Task> tasksFromManager = manager.getAllTasks();
		assertEquals(1, tasksFromManager.size());
		assertEquals("Test Task", tasksFromManager.getFirst().getTitle());
	}

	@Test
	void testAddTaskWithExistingTime() throws IOException, InterruptedException {
		Task task1 = new Task("Task 1", "Task 1 description", Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(1));
		manager.addTask(task1);

		Task task2 = new Task("Task 2", "Task 2 description", Duration.ofMinutes(30), LocalDateTime.now());
		String taskJson = HttpTaskServer.getGson().toJson(task2);

		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/tasks");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.POST(HttpRequest.BodyPublishers.ofString(taskJson))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(406, response.statusCode()); //
	}

	//
	@Test
	void testGetAllTasks() throws IOException, InterruptedException {
		Task task1 = new Task("Task 1", "Task 1 description", Duration.ofMinutes(30), LocalDateTime.now());
		manager.addTask(task1);

		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/tasks");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.GET()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode());

		List<Task> returnedTasks = HttpTaskServer.getGson().fromJson(response.body(), new TypeToken<List<Task>>() {
		}.getType());
		assertEquals(1, returnedTasks.size());
		assertEquals("Task 1", returnedTasks.getFirst().getTitle());
	}

	//
	@Test
	void testDeleteTask() throws IOException, InterruptedException {
		Task task = new Task("Task to delete", "Task description", Duration.ofMinutes(30), LocalDateTime.now());
		Task createdTask = manager.addTask(task);

		assertNotNull(manager.getTaskById(createdTask.getId()), "Task should exist before deletion");

		URI url = URI.create("http://localhost:8080/tasks/" + createdTask.getId());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.DELETE()
				.build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode(), "Task should be deleted successfully");

		assertNull(manager.getTaskById(createdTask.getId()), "Task should not exist after deletion");
	}


	@Test
	void testGetTaskById() throws IOException, InterruptedException {
		Task task = new Task("Fetch me", "I am to be fetched", Duration.ofMinutes(30), LocalDateTime.now());
		Task createdTask = manager.addTask(task);

		URI url = URI.create("http://localhost:8080/tasks/" + createdTask.getId());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.GET()
				.build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(200, response.statusCode());
		Task fetchedTask = HttpTaskServer.getGson().fromJson(response.body(), Task.class);
		assertEquals(createdTask.getTitle(), fetchedTask.getTitle());
	}
}

class HttpTaskServerEpicTest {

	private HttpTaskServer taskServer;
	private InMemoryTaskManager manager;

	@BeforeEach
	public void setUp() throws IOException {
		manager = new InMemoryTaskManager();
		taskServer = new HttpTaskServer(manager);
		taskServer.start();
	}

	@AfterEach
	public void shutDown() {
		taskServer.stop();
	}

	@Test
	void testAddEpic() throws IOException, InterruptedException {
		Epic epic = new Epic("Test Epic", "Testing epic description");
		String epicJson = HttpTaskServer.getGson().toJson(epic);

		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/epics");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.POST(HttpRequest.BodyPublishers.ofString(epicJson))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(201, response.statusCode());

		List<Epic> epicsFromManager = manager.getAllEpics();
		assertEquals(1, epicsFromManager.size());
		assertEquals("Test Epic", epicsFromManager.getFirst().getTitle());
	}

	@Test
	void testGetAllEpics() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic 1", "Description 1");
		manager.addEpic(epic);

		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/epics");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.GET()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode());

		List<Epic> returnedEpics = HttpTaskServer.getGson().fromJson(response.body(), new TypeToken<List<Epic>>() {
		}.getType());
		assertEquals(1, returnedEpics.size());
		assertEquals("Epic 1", returnedEpics.getFirst().getTitle());
	}

	@Test
	void testDeleteEpic() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic to delete", "Epic description");
		Epic createdEpic = manager.addEpic(epic);

		assertNotNull(manager.getEpicById(createdEpic.getId()), "Epic should exist before deletion");

		URI url = URI.create("http://localhost:8080/epics/" + createdEpic.getId());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.DELETE()
				.build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode(), "Epic should be deleted successfully");

		assertNull(manager.getEpicById(createdEpic.getId()), "Epic should not exist after deletion");
	}

	@Test
	void testGetEpicById() throws IOException, InterruptedException {
		Epic epic = new Epic("Fetch me", "I am to be fetched");
		Epic createdEpic = manager.addEpic(epic);

		URI url = URI.create("http://localhost:8080/epics/" + createdEpic.getId());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.GET()
				.build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(200, response.statusCode());
		Epic fetchedEpic = HttpTaskServer.getGson().fromJson(response.body(), Epic.class);
		assertEquals(createdEpic.getTitle(), fetchedEpic.getTitle());
	}
}

class HttpTaskServerSubTaskTest {

	private HttpTaskServer taskServer;
	private InMemoryTaskManager manager;

	@BeforeEach
	public void setUp() throws IOException {
		manager = new InMemoryTaskManager();
		taskServer = new HttpTaskServer(manager);
		taskServer.start();
	}

	@AfterEach
	public void shutDown() {
		taskServer.stop();
	}

	@Test
	void testAddSubTask() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic for Subtask", "Testing subtask epic");
		manager.addEpic(epic);

		Subtask subtask = new Subtask("Test Subtask", "Testing subtask description", epic.getId());
		String subtaskJson = HttpTaskServer.getGson().toJson(subtask);

		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/subtasks");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(201, response.statusCode());

		List<Subtask> subTasksFromManager = manager.getAllSubtasks();
		assertEquals(1, subTasksFromManager.size());
		assertEquals("Test Subtask", subTasksFromManager.getFirst().getTitle());
	}

	@Test
	void testGetAllSubTasks() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic for Subtasks", "Description for epic");
		manager.addEpic(epic);

		Subtask subtask = new Subtask("Subtask 1", "Description 1", epic.getId());
		manager.addSubTask(subtask);

		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/subtasks");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.GET()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode());

		List<Subtask> returnedSubTasks = HttpTaskServer.getGson().fromJson(response.body(), new TypeToken<List<Subtask>>() {
		}.getType());
		assertEquals(1, returnedSubTasks.size());
		assertEquals("SubTask 1", returnedSubTasks.getFirst().getTitle());
	}

	@Test
	void testDeleteSubTask() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic for SubTask", "Description for epic");
		Epic createdEpic = manager.addEpic(epic);

		Subtask subtask = new Subtask("SubTask to delete", "SubTask description", createdEpic.getId());
		Subtask createdSubTask = manager.addSubTask(subtask);

		assertNotNull(manager.getSubtaskById(createdSubTask.getId()), "SubTask should exist before deletion");

		URI url = URI.create("http://localhost:8080/subtasks/" + createdSubTask.getId());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.DELETE()
				.build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode(), "SubTask should be deleted successfully");

		assertNull(manager.getSubtaskById(createdSubTask.getId()), "SubTask should not exist after deletion");
	}

	@Test
	void testGetSubTaskById() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic for SubTask", "Description for epic");
		Epic createdEpic = manager.addEpic(epic);

		Subtask subtask = new Subtask("Fetch me", "I am to be fetched", createdEpic.getId());
		Subtask createdSubTask = manager.addSubTask(subtask);

		URI url = URI.create("http://localhost:8080/subtasks/" + createdSubTask.getId());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.GET()
				.build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(200, response.statusCode());
		Subtask fetchedSubTask = HttpTaskServer.getGson().fromJson(response.body(), Subtask.class);
		assertEquals(createdSubTask.getTitle(), fetchedSubTask.getTitle());
	}
}
