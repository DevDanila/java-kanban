package com.practicum.java_kanban.manager;


import com.practicum.java_kanban.model.Epic;
import com.practicum.java_kanban.model.Status;
import com.practicum.java_kanban.model.Subtask;
import com.practicum.java_kanban.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class TaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }


    @Test
    public void testCreateTask() {
        Task task = new Task("Task 1", "Description 1", Duration.ofMinutes(30), LocalDateTime.now());
        Task createdTask = taskManager.addTask(task);
        assertNotNull(createdTask);
        assertEquals("Task 1", createdTask.getTitle());
        assertEquals(Status.NEW, createdTask.getStatus());
    }

    @Test
    public void testCreateEpic() {
        Epic epic = new Epic("Epic 1", "Description 1");
        Epic createdEpic = taskManager.addEpic(epic);
        assertNotNull(createdEpic, "Задача не найдена.");
        assertEquals(epic, createdEpic);

    }

    @Test
    public void testCreateSubTask() {
        Epic epic = new Epic("Epic 1", "Description 1");
        Epic createdEpic = taskManager.addEpic(epic);
        Subtask subTask = new Subtask("SubTask 1", "Description 1", createdEpic.getId());
        Subtask createdSubTask = taskManager.addSubTask(subTask);

        assertEquals(subTask, createdSubTask);
    }

    @Test
    public void testGetEpicById() {
        Epic epic = new Epic("Epic 1", "Description 1");
        Epic createdEpic = taskManager.addEpic(epic);
        Epic retrievedEpic = taskManager.getEpicById(createdEpic.getId());
        assertEquals(createdEpic, retrievedEpic);
    }



    @Test
    public void testGetAllEpics() {
        List<Epic> epics = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            epics.add(new Epic("Epic 1", "Description 1" + i));
            taskManager.addEpic(epics.get(i));
        }
        List<Epic> allEpics = taskManager.getAllEpics();
        assertEquals(5, allEpics.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(epics.get(i), allEpics.get(i));
        }
    }


    @Test
    public void testUpdateEpic() {
        Epic epic = new Epic("Epic 1", "Description 1");
        Epic createdEpic = taskManager.addEpic(epic);
        Epic updatedEpic = new Epic("Updated Epic 1", "Updated Description 1");
        updatedEpic.setId(createdEpic.getId());
        taskManager.updateEpic(updatedEpic);
        assertEquals(updatedEpic, taskManager.getEpicById(createdEpic.getId()));
    }



    @Test
    public void testDeleteEpic() {
        Epic epic = new Epic("Epic 1", "Description 1");
        Epic createdEpic = taskManager.addEpic(epic);
        assertNotNull(taskManager.getEpicById(createdEpic.getId()));

        taskManager.deleteEpic(createdEpic.getId());
        assertNull(taskManager.getTaskById(createdEpic.getId()));
    }

    @Test
    public void testEpicStatusAllNew() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.addEpic(epic);

        // Установим время начала для подзадач, чтобы они не пересекались
        LocalDateTime now = LocalDateTime.now();
        Subtask subTask1 = new Subtask("SubTask 1", "Description 1", epic.getId(), Duration.ofMinutes(10), now);
        Subtask subTask2 = new Subtask("SubTask 2", "Description 2", epic.getId(), Duration.ofMinutes(15), now.plusMinutes(10));

        subTask1.setStatus(Status.NEW);
        subTask2.setStatus(Status.NEW);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void testEpicStatusNewAndDone() {
        Epic epic = new Epic("Epic Test", "Description");
        taskManager.addEpic(epic);

        // Установим время начала для подзадач, чтобы они не пересекались
        LocalDateTime now = LocalDateTime.now();
        Subtask subTask1 = new Subtask("SubTask 1", "Description 1", epic.getId(), Duration.ofMinutes(10), now);
        Subtask subTask2 = new Subtask("SubTask 2", "Description 2", epic.getId(), Duration.ofMinutes(15), now.plusMinutes(10));

        subTask1.setStatus(Status.NEW);
        subTask2.setStatus(Status.DONE);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void testEpicStatusInProgress() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.addEpic(epic);

        Subtask subTask1 = new Subtask("SubTask 1", "Description", epic.getId(), Duration.ofHours(1), LocalDateTime.now());
        subTask1.setStatus(Status.IN_PROGRESS);
        taskManager.addSubTask(subTask1);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void testEpicStatusAllDone() {
        Epic epic = new Epic("Epic Test", "Description");
        taskManager.addEpic(epic);

        // Установим время начала для подзадач, чтобы они не пересекались
        LocalDateTime now = LocalDateTime.now();
        Subtask subTask1 = new Subtask("SubTask 1", "Description 1", epic.getId(), Duration.ofMinutes(10), now);
        Subtask subTask2 = new Subtask("SubTask 2", "Description 2", epic.getId(), Duration.ofMinutes(15), now.plusMinutes(10));

        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        assertEquals(Status.DONE, epic.getStatus());
    }

    // Тесты на создание подзадач с проверкой связей с эпику
    @Test
    public void testCreateSubTaskShouldAssociateWithEpic() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.addEpic(epic);

        Subtask subTask = new Subtask("SubTask 1", "Description 1", epic.getId(), Duration.ofMinutes(30), LocalDateTime.now());
        Subtask createdSubTask = taskManager.addSubTask(subTask);

        assertEquals(epic.getId(), createdSubTask.getEpicId(), "Подзадача должна быть связана с эпиком.");
        assertTrue(epic.getSubTasks().contains(createdSubTask), "Эпик должен содержать подзадачу.");
    }
    @Test
    public void testEpicStatusUpdateOnSubTaskCreation() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.addEpic(epic);

        LocalDateTime now = LocalDateTime.now();
        Subtask subTask1 = new Subtask("SubTask 1", "Description 1", epic.getId(), Duration.ofMinutes(10), now);
        Subtask subTask2 = new Subtask("SubTask 2", "Description 2", epic.getId(), Duration.ofMinutes(15), now.plusMinutes(10));

        subTask1.setStatus(Status.NEW);
        subTask2.setStatus(Status.DONE);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика должен быть IN_PROGRESS после добавления подзадачи NEW.");
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика должен оставаться IN_PROGRESS если есть подзадачи NEW и DONE.");
    }

    // Тест на пересечения интервалов
    @Test
    public void testCreateTaskWithOverlappingTime() {
        Task task1 = new Task("Task 1", "Description 1", Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task1);

        Task task2 = new Task("Task 2", "Description 2", Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(15)); // Пересечение
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskManager.addTask(task2);
        });

        assertEquals("Задача пересекается с существующими задачами.", exception.getMessage());
    }

    @Test
    public void testCreateSubTaskWithOverlappingTime() {
        Epic epic = new Epic("Epic 1", "Description");
        taskManager.addEpic(epic);

        Task task1 = new Task("Task 1", "Description 1", Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task1);

        Subtask subTask = new Subtask("SubTask 1", "Description 1", epic.getId(), Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(15)); // Пересечение
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskManager.addSubTask(subTask);
        });

        assertEquals("Подзадача пересекается с существующими задачами или подзадачами.", exception.getMessage());
    }
}