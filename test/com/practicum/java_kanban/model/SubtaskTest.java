package com.practicum.java_kanban.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

	@Test
	void testSubtaskEqualIfSameId() {
		Subtask subtask1 = new Subtask("собрать вещи", "описание подзадачи 1", 4);
		subtask1.setId(1);

		Subtask subtask2 = new Subtask("упаковать вещи", "описание подзадачи 2", 4);
		subtask2.setId(1);

		assertEquals(subtask1, subtask2);
	}

	@Test
	void testSubtaskNotEqualIfNotSameId() {
		Subtask subtask1 = new Subtask("собрать вещи", "описание подзадачи 1", 4);
		subtask1.setId(1);

		Subtask subtask2 = new Subtask("упаковать вещи", "описание подзадачи 2", 4);
		subtask2.setId(2);

		assertNotEquals(subtask1, subtask2);
	}

	@Test
	void testSubtaskCantBeHisEpic() { //проверяется, что объект Subtask нельзя сделать своим же эпиком;
		Subtask subtask = new Subtask("Subtask", "Subtask 1", 1);
		subtask.setId(1);
		Epic epic = new Epic("Epic 1", "Epic 1");
		epic.setId(1);
		assertFalse(epic.getSubtaskIds().contains(1));
	}
}