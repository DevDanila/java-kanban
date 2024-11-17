package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.model.Epic;
import com.practicum.java_kanban.model.Subtask;
import com.practicum.java_kanban.model.Task;
import com.practicum.java_kanban.model.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

	private final Map<Integer, Task> tasks = new HashMap<>();
	private final Map<Integer, Epic> epics = new HashMap<>();
	private final Map<Integer, Subtask> subtasks = new HashMap<>();
	private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
	private int nextId = 1;

	@Override
	public void addTask(Task task) {
		task.setId(nextId++);
		tasks.put(task.getId(), task);
	}

	@Override
	public void addEpic(Epic epic) {
		epic.setId(nextId++);
		epics.put(epic.getId(), epic);
	}

	@Override
	public void addSubTask(Subtask subtask) {
		subtask.setId(nextId++);
		subtasks.put(subtask.getId(), subtask);
		Epic ep = epics.get(subtask.getEpicId());
		ep.getSubtaskIds().add(subtask.getId());
		updateStatus(subtask.getEpicId());
	}

	@Override
	public void updateTask(Task task) {
		tasks.put(task.getId(), task);
	}

	@Override
	public void updateEpic(Epic epic) {
		final Epic oldEpic = epics.get(epic.getId());
		oldEpic.setTitle(epic.getTitle());
		oldEpic.setDescription(epic.getDescription());
	}

	@Override
	public void updateSubTask(Subtask subtask) {
		subtasks.put(subtask.getId(), subtask);
		updateStatus(subtask.getEpicId());
	}

	@Override
	public ArrayList<Subtask> getAllSubtasks() {
		return new ArrayList<>(subtasks.values());
	}

	@Override
	public ArrayList<Task> getAllTasks() {
		return new ArrayList<>(tasks.values());
	}

	@Override
	public ArrayList<Epic> getAllEpics() {
		return new ArrayList<>(epics.values());
	}

	@Override
	public void deleteTaskById(int nextId) {
		tasks.remove(nextId);
	}

	@Override
	public void deleteEpic(int nextId) {
		final Epic epic = epics.remove(nextId);
		for (Integer subtaskId : epic.getSubtaskIds()) {
			subtasks.remove(subtaskId);
		}
	}

	@Override
	public void deleteSubtask(int id) {
		Subtask subtask = subtasks.remove(id);
		Epic epic = epics.remove(subtask.getEpicId());
		epic.getSubtaskIds().remove((Integer) subtask.getId());
		updateStatus(epic.getId());
	}

	@Override
	public void deleteAllTasks() {
		tasks.clear();
	}

	@Override
	public void deleteAllTEpics() {
		subtasks.clear();
		epics.clear();
	}

	@Override
	public void deleteAllSubtask() {
		subtasks.clear();
		for (Epic epic : epics.values()) {
			epic.getSubtaskIds().clear();
			updateStatus(epic.getId());
		}
	}

	@Override
	public Task getTaskById(int nextId) {
		if (tasks.get(nextId) != null) {
			inMemoryHistoryManager.add(tasks.get(nextId));
		}
		return tasks.get(nextId);
	}

	@Override
	public Epic getEpicById(int nextId) {
		Epic epic = epics.get(nextId);
		if (epic != null) {
			inMemoryHistoryManager.add(epic);
		} else {
			System.out.println("Epic с таким id не найден");
		}
		return epic;
	}

	@Override
	public Subtask getSubtaskById(int nextId) {
		Subtask subtask = subtasks.get(nextId);
		if (subtask != null) {
			inMemoryHistoryManager.add(subtask);
		} else {
			System.out.println("Subtask с таким id не найден");
		}
		return subtask;
	}

	@Override
	public ArrayList<Subtask> getSubtaskByEpicId(int epicId) {
		ArrayList<Subtask> subtasksNew = new ArrayList<>();
		Epic epic = epics.get(epicId);
		for (Integer subtaskId : epic.getSubtaskIds()) {
			subtasksNew.add(subtasks.get(subtaskId));
		}
		return subtasksNew;
	}

	@Override
	public List<Task> getHistory() {
		return inMemoryHistoryManager.getHistory();
	}

	private void updateStatus(int epicId) {
		int countDone = 0;
		int countNew = 0;
		Epic epicGetEpicId = epics.get(epicId);
		ArrayList<Integer> subtaskIds = epicGetEpicId.getSubtaskIds();

		for (Integer subtaskId : subtaskIds) {
			Subtask anSubtask = subtasks.get(subtaskId);
			if (anSubtask.getStatus().equals(Status.DONE)) {
				countDone++;
			} else if (anSubtask.getStatus().equals(Status.NEW)) {
				countNew++;
			} else if (anSubtask.getStatus().equals(Status.IN_PROGRESS)) {
				epicGetEpicId.setStatus(Status.IN_PROGRESS);
				return;
			}
		}
		if (countNew == subtaskIds.size()) {
			epicGetEpicId.setStatus(Status.NEW);
		} else if (countDone == subtaskIds.size()) {
			epicGetEpicId.setStatus(Status.DONE);
		} else {
			epicGetEpicId.setStatus(Status.IN_PROGRESS);
		}
	}
}
