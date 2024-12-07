package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.exceptions.ManagerSaveException;
import com.practicum.java_kanban.model.*;

public class CSVFormat {

	private static Status status;

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
		String[] values = value.split(",");
		String taskId = values[0];
		String taskType = values[1];
		String title = values[2];
		String status = values[3];
		String description = values[4];
//		String epicId = values[5];

		switch (TaskType.valueOf(taskType)) {
			case TASK-> {
				return new Task(title, description);
			}
			case SUBTASK -> {
				return new Subtask(title, description, Integer.parseInt((values[5])));
			}
			case EPIC -> {
				Epic epic = new Epic(title, description);
				epic.setStatus(Status.valueOf(status));
				epic.setId(Integer.parseInt(taskId));
				return epic;
			}
			default -> {
				throw new ManagerSaveException("Неизвестный тип задачи " + taskType);
			}
		}
	}
}
