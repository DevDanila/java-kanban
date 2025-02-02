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
}
