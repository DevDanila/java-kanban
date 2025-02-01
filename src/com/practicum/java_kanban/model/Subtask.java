package com.practicum.java_kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
	private int epicId;


	public Subtask(int id, String name, Status status, String description, int epicId, Duration duration, LocalDateTime startTime) {
		super(name, description, duration, startTime);
		this.epicId = epicId;
	}

	public Subtask(String title, String description, int epicId, Duration duration, LocalDateTime startTime) {
		super(title, description, duration, startTime);
		this.epicId = epicId;
	}

	public Subtask(String title, String description, int epicId) {
		super(title, description);
		this.epicId = epicId;
	}

	@Override
	public TaskType getType() {
		return TaskType.SUBTASK;
	}

	public int getEpic() {
		return epicId;
	}

	public void setEpic(int epicId) {
		this.epicId = epicId;
	}

	public int getEpicId() {

		return epicId;
	}
}