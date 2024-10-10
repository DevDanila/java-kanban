package com.practicum.java_kanban.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

	@Test
	void testEpicEqualIfSameId() {
		Epic ep1 = new Epic("Epic 1", "epic_1");
		ep1.setId(1);
		Epic ep2 = new Epic("Epic 1", "epic_1");
		ep2.setId(1);
		assertEquals(ep1, ep2);
	}

	@Test
	public void testEpicNotEqualIfNotSameId() {
		Epic epic1 = new Epic("Epic 1", "task_1");
		epic1.setId(1);
		Epic epic2 = new Epic("Epic 2", "task_2");
		epic2.setId(2);
		assertNotEquals(epic1, epic2);
	}

	@Test
	void testEpicCantBeHisSubtask() { //проверяется, что объект Epic нельзя добавить в самого себя в виде подзадачи;
		Epic epic = new Epic("Epic 1", "Epic 1");
		epic.setId(1);

		Subtask subtask = new Subtask("Subtask 1", "Subtask 1", Status.NEW, 1);
		subtask.setId(1);

		assertFalse(epic.getSubtaskIds().contains(1));
	}
}
