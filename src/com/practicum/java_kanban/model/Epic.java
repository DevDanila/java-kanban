package com.practicum.java_kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

	protected LocalDateTime endTime;
	private List<Subtask> subtasks = new ArrayList<>();

	public Epic(String name, String description, Duration duration, LocalDateTime startTime) {
		super(name, description, duration, startTime);

	}

	public Epic(String name, String description) {
		super(name, description, Duration.ZERO, LocalDateTime.now());
	}

	public Epic(int id, String name, Status status, String description, Duration duration, LocalDateTime startTime) {
		super(id, name, status, description, duration, startTime);
	}

	public Epic() {
		super("", "", Duration.ZERO, null);
		this.subtasks = new ArrayList<>();
	}

	public List<Subtask> getSubTasks() {
		return subtasks;
	}

	public void addSubTask(Subtask subtask) {
		subtasks.add(subtask);
		updateEpicDetails();
	}

	public void removeSubTask(Subtask subtask) {
		subtasks.remove(subtask);
		updateEpicDetails();
	}

	private void updateEpicDetails() {
		if (subtasks.isEmpty()) {
			this.duration = Duration.ZERO;
			this.startTime = null;
			this.endTime = null;
			return;
		}

		// Проверяем, что все подзадачи имеют startTime
		boolean hasValidTime = subtasks.stream()
				.allMatch(sub -> sub.getStartTime() != null && sub.getDuration() != null);

		if (!hasValidTime) {
			this.duration = Duration.ZERO;
			this.startTime = null;
			this.endTime = null;
			return;
		}

		this.duration = subtasks.stream()
				.map(Subtask::getDuration)
				.reduce(Duration.ZERO, Duration::plus);

		this.startTime = subtasks.stream()
				.map(Subtask::getStartTime)
				.min(LocalDateTime::compareTo)
				.orElse(null);

		this.endTime = subtasks.stream()
				.map(Subtask::getEndTime)
				.max(LocalDateTime::compareTo)
				.orElse(null);
	}


	@Override
	public LocalDateTime getEndTime() {
		if (subtasks.isEmpty()) {
			return startTime;
		}
		return subtasks.stream()
				.map(Subtask::getEndTime)
				.max(LocalDateTime::compareTo)
				.orElse(startTime);
	}


	@Override
	public Status getStatus() {
		boolean allNew = subtasks.stream().allMatch(subTask -> subTask.getStatus() == Status.NEW);
		boolean allDone = subtasks.stream().allMatch(subTask -> subTask.getStatus() == Status.DONE);

		if (subtasks.isEmpty() || allNew) {
			return Status.NEW;
		} else if (allDone) {
			return Status.DONE;
		} else {
			return Status.IN_PROGRESS;
		}
	}
}