package com.practicum.java_kanban.model;

public class Subtask extends Task {
	private final int epicId;


	public Subtask(String title, String description, int epicId) {
		super(title, description);
		this.epicId = epicId;
	}

	public int getEpicId() {
		return epicId;
	}

	public TaskType getTaskType() {
		return TaskType.SUBTASK;
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