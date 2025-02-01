package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.exceptions.ManagerSaveException;
import com.practicum.java_kanban.model.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class CSVFormat {

	public static String getHeader() {
		return "id,type,name,status,description,epic" + System.lineSeparator();
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
        int id = Integer.parseInt(values[0]);
        TaskType taskType = TaskType.valueOf(values[1]);
        Status status = Status.valueOf(values[3]);
        LocalDateTime startTime = values[5].equals("null") ? null : LocalDateTime.parse(values[5]);
        Duration duration = Duration.ofMinutes(Long.parseLong(values[6]));
        switch (taskType) {
            case TaskType.TASK -> {
                Task task = new Task(values[2], values[4]);
                task.setId(id);
                task.setStatus(status);
                task.setStartTime(startTime);
                task.setDuration(duration);
                return task;
            }
            case TaskType.EPIC -> {
                Epic epic = new Epic(values[2], values[4]);
                epic.setId(id);
                epic.setStatus(status);
                epic.setStartTime(startTime);
                epic.setDuration(duration);
                return epic;
            }
            case TaskType.SUBTASK -> {
                Subtask subtask = new Subtask(values[2], values[4], Integer.parseInt(values[7]));
                subtask.setId(id);
                subtask.setStatus(status);
                subtask.setStartTime(startTime);
                subtask.setDuration(duration);
                return subtask;
            }
        }
        System.err.println("Ошибка в: " + value);
        return null;
    }
}
