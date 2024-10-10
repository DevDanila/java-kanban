package com.practicum.java_kanban.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void testTasksEqualIfSameId() {
        Task task1 = new Task("Task 1", "task_1", Status.NEW );
        task1.setId(1);
        Task task2 = new Task("Task 1", "task_1", Status.NEW);
        task2.setId(1);
        assertEquals(task1, task2);
    }

    @Test
    public void testTaskNotEqualIfNotSameId() {
        Task task1 = new Task("Task 1", "task_1", Status.NEW);
        task1.setId(1);
        Task task2 = new Task("Task 2", "task_2",Status.NEW);
        task2.setId(2);
        assertNotEquals(task1, task2);
    }
}