package com.practicum.java_kanban.model;

import java.util.ArrayList;

public class Epic extends Task {
	protected final ArrayList<Integer> subtaskIds = new ArrayList<>();

	public ArrayList<Integer> getSubtaskIds() {
		return subtaskIds;
	}

	public Epic(String title, String description) {
		super(title, description, Status.NEW);
	}

	@Override
	public String toString() {
		return "Epic {" +
				"epicId='" + id + '\'' +
				", subtaskId='" + subtaskIds + '\'' +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", status='" + status + '\'' +
				'}';
	}
}


