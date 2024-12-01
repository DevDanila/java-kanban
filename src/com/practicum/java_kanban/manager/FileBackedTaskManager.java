package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.exceptions.ManagerSaveException;
import com.practicum.java_kanban.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTaskManager extends InMemoryTaskManager {
	private final File file;


	public FileBackedTaskManager(File file) {
		this.file = file;
		String fileName = ".src\\com\\practicum\\java_kanban\\manager\\dataFile.csv";
		file = new File(fileName);
		if (!file.isFile()) {
			try {
				Path path = Files.createFile(Paths.get(fileName));
			} catch (IOException e) {
				throw new ManagerSaveException("Ошибка создания файла.");
			}
		}
	}

	public void save() throws ManagerSaveException {
		try (Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
			fileWriter.write(this.toString());
		} catch (IOException e) {
			throw new ManagerSaveException("Ошибка сохранения");
		}
	}


	public static FileBackedTaskManager loadFromFile(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

			while (br.ready()) {
				String line = br.readLine();
				if (!line.startsWith("ID")) {
					Task task = fromString(line);
					String taskClass = task.getClass().getName().toUpperCase().substring(6);
					if (taskClass.equals(TaskType.TASK.toString())) {
						taskManager.tasks.put(task.getId(), task);
					} else if (taskClass.equals(TaskType.SUBTASK.toString())) {
						taskManager.subtasks.put(task.getId(), (Subtask) task);
					} else if (taskClass.equals(TaskType.EPIC.toString())) {
						taskManager.epics.put(task.getId(), (Epic) task);
					}
				}
			}
			return taskManager;
		} catch (IOException e) {
			throw new ManagerSaveException("Ошибка при чтении файла: " + e.getMessage());
		}
	}

	private String toString(Task task) {
		if (task == null) {
			return null;
		}

		String taskClass = task.getClass().getName().toUpperCase().substring(6);
		String taskString = task.getId() + "," + taskClass + "," + task.getId() + "," + task.getStatus() +
				"," + task.getDescription();

		if (taskClass.equals(TaskType.TASK.toString()) || taskClass.equals(TaskType.EPIC.toString())) {
			taskString = taskString + "\n";
		} else if (taskClass.equals(TaskType.SUBTASK.toString())) {
			Subtask subtask = (Subtask) task;
			taskString = taskString + "," + subtask.getEpicId() + "\n";
		}
		return taskString;
	}

	private static Task fromString(String value) {
		String[] values = value.split(",");
		Task task;

		if (values[1].equals(TaskType.TASK.toString())) {
			task = new Task(values[2], values[4], Status.valueOf(values[3]));
		} else if (values[1].equals(TaskType.SUBTASK.toString())) {
			task = new Subtask(values[2], values[4], Status.valueOf(values[3]),
					Integer.parseInt(values[5]));
		} else if (values[1].equals(TaskType.EPIC.toString())) {
			task = new Epic(values[2], values[4]);
		} else {
			throw new ManagerSaveException("Нет такого типа задачи: " + values[1]);
		}
		return task;
	}


	@Override
	public void addTask(Task task) {
		super.addTask(task);
		save();

	}

	@Override
	public void addEpic(Epic epic) {
		super.addEpic(epic);
		save();
	}

	@Override
	public void addSubTask(Subtask subtask) {
		super.addSubTask(subtask);
		save();
	}

	@Override
	public void updateTask(Task task) {
		super.updateTask(task);
		save();
	}

	@Override
	public void updateEpic(Epic epic) {
		super.updateEpic(epic);
		save();
	}

	@Override
	public void updateSubTask(Subtask subtask) {
		super.updateSubTask(subtask);
		save();
	}

	@Override
	public void deleteTaskById(int nextId) {
		super.deleteTaskById(nextId);
		save();
	}

	@Override
	public void deleteEpic(int nextId) {
		super.deleteEpic(nextId);
		save();
	}

	@Override
	public void deleteSubtask(int id) {
		super.deleteSubtask(id);
		save();
	}

	@Override
	public void deleteAllTasks() {
		super.deleteAllTasks();
		save();
	}

	@Override
	public void deleteAllTEpics() {
		super.deleteAllTEpics();
		save();
	}

	@Override
	public void deleteAllSubtask() {
		super.deleteAllSubtask();
		save();
	}
}
