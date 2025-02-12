package com.practicum.java_kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
	protected int id;
	protected TaskType typeTask;
	protected String title;
	protected Status status;
	protected String description;
	protected Duration duration;
	protected LocalDateTime startTime;

	public Task(int id, String title, Status status, String description, Duration duration, LocalDateTime startTime) {
		this.id = id;
		this.title = title;
		this.status = status;
		this.description = description;
		this.duration = duration;
		this.startTime = startTime;
	}

	public Task(String name, Status status) {
		this.title = name;
		this.status = Status.NEW;

	}

	public Task(int id, String name, String description, Status status) {

		this.id = id;
		this.title = name;
		this.description = description;
		this.status = Status.NEW;
	}

	public Task(int id, String name, String description) {

		this.id = id;
		this.title = name;
		this.description = description;
	}


	public Task(int id, String description, Status status) {
		this.id = id;
		this.description = description;
		this.status = Status.NEW;
	}


	public Task(int id, String name, Status status, String description, int epicId) {

		this.id = id;
		this.title = name;
		this.status = Status.NEW;
		this.description = description;

	}

	public Task(String name, String description) {
		this.title = name;
		this.status = Status.NEW;
		this.description = description;
	}

	public Task(int id, String name, Status status, String description, int epicId, Duration duration, LocalDateTime startTime) {

		this.title = name;
		this.status = Status.NEW;
		this.description = description;
		this.duration = duration;
		this.startTime = startTime;

	}

	public Task(String name, String description, Duration duration, LocalDateTime startTime) {
		this.title = name;
		this.status = Status.NEW;
		this.description = description;
		this.duration = duration;
		this.startTime = startTime;

	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public LocalDateTime getStartTime() {
		return this.startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return startTime.plus(duration);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Task task = (Task) o;
		return id == task.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Task{" +
				"id=" + id +
				", title='" + title + '\'' +
				", status=" + status +
				", description='" + description + '\'' +
				", start time='" + startTime + '\'' +
				", duration='" + duration + '\'' +
				'}';
	}

	public TaskType getType() {
		return TaskType.TASK;
	}
}



