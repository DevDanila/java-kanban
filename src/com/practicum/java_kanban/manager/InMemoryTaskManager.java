package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.model.Epic;
import com.practicum.java_kanban.model.Subtask;
import com.practicum.java_kanban.model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

	protected final Map<Integer, Task> tasks = new HashMap<>();
	protected final Map<Integer, Epic> epics = new HashMap<>();
	protected final Map<Integer, Subtask> subtasks = new HashMap<>();
	private final HistoryManager historyManager = Managers.getDefaultHistory();
	private final Set<Task> prioritizedTask = new TreeSet<>(Comparator.comparing(Task::getStartTime));
	protected int nextId = 0;

	private int generatedId() {
		return ++nextId;
	}

	private boolean overlapped(Task task1, Task task2) {
		LocalDateTime start1 = task1.getStartTime();
		LocalDateTime end1 = task1.getEndTime();
		LocalDateTime start2 = task2.getStartTime();
		LocalDateTime end2 = task2.getEndTime();

		return (start1.isBefore(end2) && start2.isBefore(end1));
	}

	@Override
	public Task addTask(Task task) {
		if (task.getStartTime() != null) {
			for (Task existingTask : tasks.values()) {
				if (overlapped(existingTask, task)) {
					throw new IllegalArgumentException("Задача пересекается с существующими задачами.");
				}
			}
			task.setId(generatedId());
			tasks.put(task.getId(), task);
			prioritizedTask.add(task);
		}
		return task;
	}

	@Override
	public Epic addEpic(Epic epic) {
		epic.setId(generatedId());
		epics.put(epic.getId(), epic);
		return epic;
	}

	@Override
	public Subtask addSubTask(Subtask subtask) {
		Epic epic = epics.get(subtask.getEpicId());
		if (epic == null) {
			throw new IllegalArgumentException("Эпик с ID " + subtask.getEpicId() + " не найден.");
		}
		if (subtask.getStartTime() != null) {
			List<Task> allTasks = getAllTasks();
			allTasks.addAll(epic.getSubTasks());
			for (Task existingTask : allTasks) {
				if (overlapped(existingTask, subtask)) {
					throw new IllegalArgumentException("Подзадача пересекается с существующими задачами или подзадачами.");
				}
			}
			subtask.setId(generatedId());
			subtasks.put(subtask.getId(), subtask);
			epic.addSubTask(subtask);
			prioritizedTask.add(subtask);
			updateEpicStatus(epic);
		}
		return subtask;
	}

	@Override
	public List<Subtask> getAllSubtasks() {
		return new ArrayList<>(subtasks.values());
	}

	@Override
	public List<Task> getAllTasks() {
		return new ArrayList<>(tasks.values());
	}

	@Override
	public List<Epic> getAllEpics() {
		return new ArrayList<>(epics.values());
	}

	@Override
	public void deleteTaskById(int nextId) {
		Task removedTask = tasks.remove(nextId);
		if (removedTask != null) {
			prioritizedTask.remove(removedTask);
		}
		historyManager.remove(nextId);
	}

	@Override
	public void deleteEpic(int nextId) {
		Epic epic = epics.remove(nextId);
		if (epic != null) {
			for (Subtask subTask : epic.getSubTasks()) {
				subtasks.remove(subTask.getId());
			}
		}
	}

	@Override
	public void deleteSubtask(int id) {
		Subtask subTask = subtasks.remove(id);
		if (subTask != null) {
			Epic epic = epics.get(subTask.getEpicId());
			if (epic != null) {
				epic.removeSubTask(subTask);
			}
			prioritizedTask.remove(subTask);
			updateEpicStatus(epic);
		}
	}

	@Override
	public void deleteAllTasks() {
		for (Integer idTask : tasks.keySet()) {
			historyManager.remove(idTask);
		}
		tasks.clear();
	}

	@Override
	public void deleteAllEpics() {
		for (Integer idEpic : epics.keySet()) {
			historyManager.remove(idEpic);
		}
		for (Integer idSubtask : subtasks.keySet()) {
			historyManager.remove(idSubtask);
		}
		subtasks.clear();
		epics.clear();
	}

	@Override
	public void deleteAllSubtask() {
		for (Subtask subtask : subtasks.values()) {
			prioritizedTask.remove(subtask);
		}
		subtasks.clear();
		for (Integer epicId : epics.keySet()) {
			updateEpic(getEpicById(epicId));
		}
	}

	@Override
	public Task getTaskById(int nextId) {
		if (tasks.get(nextId) != null) {
			historyManager.add(tasks.get(nextId));
		}
		return tasks.get(nextId);
	}

	@Override
	public Epic getEpicById(int nextId) {
		Epic epic = epics.get(nextId);
		if (epic != null) {
			historyManager.add(epic);
		} else {
			System.out.println("Epic с таким id не найден");
		}
		return epic;
	}

	@Override
	public Subtask getSubtaskById(int nextId) {
		Subtask subtask = subtasks.get(nextId);
		if (subtask != null) {
			historyManager.add(subtask);
		} else {
			System.out.println("Subtask с таким id не найден");
		}
		return subtask;
	}

	@Override
	public List<Task> getHistory() {
		return historyManager.getHistory();
	}

	@Override
	public List<Task> getPrioritizedTasks() {
		return (List<Task>) new TreeSet<>(prioritizedTask);
	}

	@Override
	public void updateTask(Task task) {
		if (task == null || !tasks.containsKey(task.getId())) {
			throw new IllegalArgumentException("Задача с ID " + task.getId() + " не найдена");
		}


		if (task.getStartTime() != null) {
			for (Task existingTask : tasks.values()) {
				if (existingTask.getId() != task.getId() && overlapped(existingTask, task)) {
					throw new IllegalArgumentException("Возникла ошибка");
				}
			}
		}

		tasks.put(task.getId(), task);
		prioritizedTask.add(task);
	}

	@Override
	public void updateEpic(Epic epic) {
		if (epic == null || !epics.containsKey(epic.getId())) {
			throw new IllegalArgumentException("Эпик с ID " + epic.getId() + " не найден");
		}

		epics.put(epic.getId(), epic);
		Managers.updatedEpicStatus(epic);
	}

	@Override
	public void updateSubTask(Subtask subTask) {
		if (subTask == null || !subtasks.containsKey(subTask.getId())) {
			throw new IllegalArgumentException("Подзадача с ID " + subTask.getId() + " не найден");
		}

		Epic epic = epics.get(subTask.getEpicId());
		if (epic == null) {
			throw new IllegalArgumentException("Эпик с ID " + subTask.getEpicId() + " не найден");
		}


		if (subTask.getStartTime() != null) {
			List<Task> allTasks = getAllTasks();
			allTasks.addAll(epic.getSubTasks());
			for (Task existingTask : allTasks) {
				if (existingTask.getId() != subTask.getId() && overlapped(existingTask, subTask)) {
					throw new IllegalArgumentException("Возникла ошибка");
				}
			}
		}

		subtasks.put(subTask.getId(), subTask);
		epic.removeSubTask(subTask);
		epic.addSubTask(subTask);
		Managers.updatedEpicStatus(epic);
	}

	private void updateEpicStatus(Epic epic) {
		Managers.updatedEpicStatus(epic);
	}
}
