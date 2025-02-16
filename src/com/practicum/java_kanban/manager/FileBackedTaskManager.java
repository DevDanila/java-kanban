package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.exceptions.ManagerSaveException;
import com.practicum.java_kanban.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {
	private final Path file;

	public FileBackedTaskManager(Path file) {
		this.file = file;
		try {
			if (Files.exists(file)) {
				loadFromFile();
			} else {
				Files.createFile(file);
			}
		} catch (IOException e) {
			throw new ManagerSaveException("Ошибка чтения или сохранения файла" + e.getMessage());
		}
	}

	void save() {
		try (BufferedWriter bw = Files.newBufferedWriter(file)) {
			bw.write(CSVFormat.getHeader());
			bw.newLine();
			for (Task task : tasks.values()) {
				bw.write(CSVFormat.toStringCSV(task));
				bw.newLine();
			}
			for (Epic epic : epics.values()) {
				bw.write(CSVFormat.toStringCSV(epic));
				bw.newLine();
				for (Subtask subtask : epic.getSubTasks()) {
					bw.write(CSVFormat.toStringCSV(subtask));
					bw.newLine();
				}
			}
		} catch (IOException e) {
			throw new ManagerSaveException("Ошибка сохранения" + e.getMessage());
		}
	}


	public static FileBackedTaskManager loadFromFile(Path file) {
		FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
		try (BufferedReader br = Files.newBufferedReader(file)) {
			String line;
			while ((line = br.readLine()) != null) {
				Task task = CSVFormat.fromString(line);
				taskManager.addTask(task);
			}
			for (Epic epic : taskManager.getAllEpics()) {
				for (Subtask subtask : epic.getSubTasks()) {
					taskManager.addSubTask(subtask);
				}
			}

		} catch (IOException e) {
			throw new ManagerSaveException("Ошибка при чтении файла: " + e.getMessage());
		}
		return taskManager;
	}

	private void loadFromFile() {
		try (BufferedReader br = Files.newBufferedReader(file)) {
			String line;
			int maxId = -1;
			while ((line = br.readLine()) != null) {
				Task task = CSVFormat.fromString(line);
				assert task != null;
				TaskType type = task.getTypeTask();

				if (type == TaskType.TASK) {
					tasks.put(task.getId(), task);

				} else if (type == TaskType.EPIC) {
					Epic epic = (Epic) task;
					epics.put(task.getId(), epic);

				} else if (type == TaskType.SUBTASK) {
					Subtask subtask = (Subtask) task;
					subtasks.put(task.getId(), subtask);
					final int epicId = subtask.getEpicId();
					epics.get(epicId).getSubTasks().add(subtask);

				}
				maxId = Math.max(maxId, task.getId());
			}
			nextId = maxId + 1;
		} catch (IOException e) {
			throw new ManagerSaveException("Ошибка при чтении файла: " + e.getMessage());
		}
	}


	@Override
	public Task addTask(Task task) {
		Task addTask = super.addTask(task);
		save();

		return addTask;
	}

	@Override
	public Epic addEpic(Epic epic) {
		Epic addEpic = super.addEpic(epic);
		save();
		return addEpic;
	}

	@Override
	public Subtask addSubTask(Subtask subtask) {
		Subtask addSubtask = super.addSubTask(subtask);
		save();
		return addSubtask;
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
	public void deleteAllEpics() {
		super.deleteAllEpics();
		save();
	}

	@Override
	public void deleteAllSubtask() {
		super.deleteAllSubtask();
		save();
	}
}
