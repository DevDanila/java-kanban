package com.practicum.java_kanban.model;

import java.util.Objects;

public class Task {
	protected int id;
	protected String title;
	protected String description;
	protected Status status;
	protected TaskType typeTask;

	public Task(String title, String description) {
		this.title = title;
		this.description = description;
		this.status = Status.NEW;
		this.typeTask = TaskType.TASK;
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

	public TaskType  getTypeTask() {
		return TaskType.TASK;
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
				", name='" + title + '\'' +
				", status=" + status +
				", description='" + description + '\'' +
				'}';
	}
}



