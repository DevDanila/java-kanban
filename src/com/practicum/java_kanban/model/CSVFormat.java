package com.practicum.java_kanban.model;

import com.practicum.java_kanban.exceptions.ManagerSaveException;

public class CSVFormat {

	private CSVFormat() {

	}

	public static String toStringCSV(Task task) {

		return new StringBuilder().append(task.getId()).append(",").append(TaskType.TASK).append(",")
				.append(task.getTitle()).append(",").append(task.getStatus()).append(",")
				.append(task.getDescription()).append(",").toString();
	}

	public static String toStringCSV(Subtask task) {

		return new StringBuilder().append(task.getId()).append(",").append(TaskType.SUBTASK).append(",")
				.append(task.getTitle()).append(",").append(task.getStatus()).append(",").append(task.getDescription())
				.append(",").append(task.getEpicId()).toString();
	}

	public static String toStringCSV(Epic task) {

		return new StringBuilder().append(task.getId()).append(",").append(TaskType.EPIC).append(",")
				.append(task.getTitle()).append(",").append(task.getStatus()).append(",")
				.append(task.getDescription()).append(",").toString();
	}


	public static Task fromString(String value) {
		Task task;
		String[] values = value.split(",");
		int taskId = Integer.parseInt(values[0]);
		TaskType taskType = TaskType.valueOf(values[1]);
		String title = values[2];
		String description = values[4];
		int epicId = Integer.parseInt(values[5]);

		if (values[1].equals(TaskType.TASK.toString())) {
			task = new Task(title, description);
		} else if (values[1].equals(TaskType.SUBTASK.toString())) {
			task = new Subtask(title, description, epicId);
		} else if (values[1].equals(TaskType.EPIC.toString())) {
			task = new Epic(title, description);
		} else {
			throw new ManagerSaveException("Нет такого типа задачи: " + taskType);
		}
		return task;
	}
}
