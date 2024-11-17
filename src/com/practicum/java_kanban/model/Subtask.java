package com.practicum.java_kanban.model;

public class Subtask extends Task {
	protected final int epicId;


	public Subtask(String title, String description, Status status, int epicId) {
		super(title, description, status);
		this.epicId = epicId;
	}

	public int getEpicId() {
		return epicId;
	}

	@Override
	public String toString() {
		return "Subtask {" +
				"id='" + id + '\'' +
				", epicId='" + epicId + '\'' +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", status='" + status + '\'' +
				'}';
	}

}