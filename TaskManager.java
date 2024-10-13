package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.model.Epic;
import com.practicum.java_kanban.model.Subtask;
import com.practicum.java_kanban.model.Task;

import java.util.List;

public interface TaskManager {
	void addTask(Task task);

	void addEpic(Epic epic);

	void addSubTask(Subtask subtask);

	void updateTask(Task task);

	void updateEpic(Epic epic);

	void updateSubTask(Subtask subtask);

	List<Subtask> getAllSubtasks();

	List<Task> getAllTasks();

	List<Epic> getAllEpics();

	void deleteTaskById(int nextId);

	void deleteEpic(int nextId);

	void deleteSubtask(int id);

	void deleteAllTasks();

	void deleteAllTEpics();

	void deleteAllSubtask();

	Task getTaskById(int nextId);

	Epic getEpicById(int nextId);

	Subtask getSubtaskById(int nextId);

	List<Subtask> getSubtaskByEpicId(int epicId);

	List<Task> getHistory();
}
