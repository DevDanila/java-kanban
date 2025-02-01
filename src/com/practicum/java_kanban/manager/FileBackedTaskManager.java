package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.exceptions.ManagerSaveException;
import com.practicum.java_kanban.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {
	private final File file;

	public FileBackedTaskManager(File file) {
		this.file = file;


	}

	public static void main(String[] args) {
		FileBackedTaskManager fileManager = new FileBackedTaskManager(new File("saveTasks2.csv"));
		fileManager.addEpic(new Epic("new Epic1", "Новый Эпик"));
		fileManager.addSubTask(new Subtask("New Subtask", "Подзадача", 2));
		fileManager.addSubTask(new Subtask("New Subtask2", "Подзадача2", 2));
		System.out.println(fileManager.getAllTasks());
		System.out.println(fileManager.getAllEpics());
		System.out.println(fileManager.getAllSubtasks());
		System.out.println("\n\n" + "new" + "\n\n");
		FileBackedTaskManager fileBackedTasksManager = loadFromFile(new File("saveTasks2.csv"));
		System.out.println(fileBackedTasksManager.getAllTasks());
		System.out.println(fileBackedTasksManager.getAllEpics());
		System.out.println(fileBackedTasksManager.getAllSubtasks());
	}

	private void save() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			bw.write("id,type,title,status,description,epic\n");

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
			throw new ManagerSaveException("Ошибка сохранения");
		}
	}


	public static FileBackedTaskManager loadFromFile(File file) {
		FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			br.readLine();
			String line;
			while ((line = br.readLine()) != null) {
				Task task = CSVFormat.fromString(line);
				if (task instanceof Epic) {
					taskManager.addEpic((Epic) task);
				} else if (task instanceof Subtask) {
					taskManager.addSubTask((Subtask) task);
				} else {
					assert task != null;
					taskManager.addTask(task);
				}
			}
		} catch (IOException e) {
			throw new ManagerSaveException("Ошибка при чтении файла: " + e.getMessage());
		}
		return taskManager;
	}


	@Override
	public Task addTask(Task task) {
		super.addTask(task);
		save();

		return task;
	}

	@Override
	public Epic addEpic(Epic epic) {
		super.addEpic(epic);
		save();
		return epic;
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
