package com.practicum.java_kanban.model;

import com.practicum.java_kanban.status.Status;

import java.util.ArrayList;

public class Epic extends Task {
	private final ArrayList<Integer> subtaskIds = new ArrayList<>();

	public ArrayList<Integer> getSubtaskIds() {
		return subtaskIds;
	}

	public Epic(String title, String description) {
		super(title, description, Status.NEW);
	}
}


