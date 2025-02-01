package com.practicum.java_kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

	protected LocalDateTime endTime;
	private final List<Subtask> subtasks = new ArrayList<>();

	public Epic(String name, String description, Duration duration, LocalDateTime startTime) {
		super(name, description, duration, startTime);

	}

	public Epic(String name, String description) {
		super(name, description, Duration.ZERO, LocalDateTime.now());
	}

	public Epic(int id, String name, Status status, String description, Duration duration, LocalDateTime startTime) {
		super(id, name, status, description, duration, startTime);
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
		} else {
			this.duration = subtasks.stream()
					.map(Subtask::getDuration)
					.reduce(Duration.ZERO, Duration::plus);

			this.startTime = subtasks.stream()
					.map(Subtask::getStartTime)
					.filter(Objects::nonNull)
					.min(LocalDateTime::compareTo)
					.orElse(null);

			this.endTime = subtasks.stream()
					.map(Subtask::getEndTime)
					.filter(Objects::nonNull)
					.max(LocalDateTime::compareTo)
					.orElse(null);
		}
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