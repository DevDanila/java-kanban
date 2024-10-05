package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.model.Epic;
import com.practicum.java_kanban.model.Subtask;
import com.practicum.java_kanban.model.Task;
import com.practicum.java_kanban.status.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
	private final HashMap<Integer, Task> tasks = new HashMap<>();
	private final HashMap<Integer, Epic> epics = new HashMap<>();
	private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
	private int nextId = 1;

	public void addTask(Task task) {
		task.setId(nextId++);
		tasks.put(task.getId(), task);
	}

	public void addEpic(Epic epic) {
		epic.setId(nextId++);
		epics.put(epic.getId(), epic);
	}

	public void addSubTask(Subtask subtask) {
		subtask.setId(nextId++);
		subtasks.put(subtask.getId(), subtask);
		Epic ep = epics.get(subtask.getEpicId());
		ep.getSubtaskIds().add(subtask.getId());
		updateStatus(subtask.getEpicId());
	}

	public void updateTask(Task task) {
		tasks.put(task.getId(), task);
	}

	public void updateEpic(Epic epic) {
		final Epic oldEpic = epics.get(epic.getId());
		oldEpic.setTitle(epic.getTitle());
		oldEpic.setDescription(epic.getDescription());
	}

	public void updateSubTask(Subtask subtask) {
		subtasks.put(subtask.getId(), subtask);
		updateStatus(subtask.getEpicId());
	}

	public ArrayList<String> getAllSubtasks() {
		ArrayList<String> subtasksList = new ArrayList<>();
		for (Task subtask : subtasks.values()) {
			subtasksList.add(subtask.getTitle());
		}
		return subtasksList;
	}

	public ArrayList<String> getAllTasks() {
		ArrayList<String> tasksList = new ArrayList<>();
		for (Task task : tasks.values()) {
			tasksList.add(task.getTitle());
		}
		return tasksList;
	}

	public ArrayList<String> getAllEpics() {
		ArrayList<String> epicsList = new ArrayList<>();
		for (Task epic : epics.values()) {
			epicsList.add(epic.getTitle());
		}
		return epicsList;
	}

	public void deleteTaskById(int nextId) {
		tasks.remove(nextId);
	}


	public void deleteEpic(int nextId) {
		Epic epic = epics.get(nextId);
		for (Integer subtaskId : epic.getSubtaskIds()) {
			subtasks.remove(subtaskId);
		}
		epics.remove(nextId);
	}

	public void deleteSubtask(int id) {
		Subtask subtask = subtasks.get(id);
		Epic epic = epics.get(subtask.getEpicId());
		epic.getSubtaskIds().remove((Integer) subtask.getId());
		subtasks.remove(id);
		updateStatus(epic.getId());
	}

	public void deleteAllTasks() {
		tasks.clear();
	}

	public void deleteAllTEpics() {
		subtasks.clear();
		epics.clear();
	}

	public void deleteAllSubtask() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateStatus(epic.getId());
        }
	}

	public void getTaskById(int nextId) {
		tasks.get(nextId);
	}

	public void getEpicById(int nextId) {
		epics.get(nextId);
	}

	public void getSubtaskById(int nextId) {
		subtasks.get(nextId);
	}


	 public ArrayList<Subtask> getSubtaskByEpicId(int epicId) {
		ArrayList<Subtask> subtasksNew = new ArrayList<>();
		Epic epic = epics.get(epicId);
		for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
                subtasksNew.add(subtasks.get(epic.getSubtaskIds().get(i)));
            }
            return subtasksNew;
	}

	private void updateStatus(int epicId) {
		int countDone = 0;
		int countNew = 0;
		Epic epicGetEpicId = epics.get(epicId);

		for (Integer subtaskId : epicGetEpicId.getSubtaskIds()) {
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
		if (countNew == epicGetEpicId.getSubtaskIds().size()) {
			epicGetEpicId.setStatus(Status.NEW);
		} else if (countDone == epicGetEpicId.getSubtaskIds().size()) {
			epicGetEpicId.setStatus(Status.DONE);
		} else {
			epicGetEpicId.setStatus(Status.IN_PROGRESS);
		}
	}
}