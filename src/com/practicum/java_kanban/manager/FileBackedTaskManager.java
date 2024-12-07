package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.exceptions.ManagerSaveException;
import com.practicum.java_kanban.model.*;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
	private final File file;

	public FileBackedTaskManager(File file) {
		this.file = file;

	}


    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("id,type,name,status,description,epic\n");
            bw.newLine();
            for (Task task : tasks.values()) {
                bw.write(CSVFormat.toStringCSV(task));
                bw.newLine();
            }
            for (Epic epic : epics.values()) {
                bw.write(CSVFormat.toStringCSV(epic));
                bw.newLine();
                for (Integer subtask : epic.getSubtaskIds()) {
                    bw.write(CSVFormat.toStringCSV(getSubtaskById(subtask)));
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
            String line;
            while ((line = br.readLine()) != null) {
                Task task = CSVFormat.fromString(line);
                taskManager.addTask(task);
            }
            for (Epic epic : taskManager.getAllEpics()) {
                for (Integer subtask : epic.getSubtaskIds()) {
                    taskManager.addSubTask(taskManager.getSubtaskById(subtask));
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла: " + e.getMessage());
        }
        return taskManager;
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
