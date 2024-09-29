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
		epics.put(epic.getId(), epic);
		updateStatus(epic.getId());
	}

	public void updateSubTask(Subtask subtask) {
		subtasks.put(subtask.getId(), subtask);
		Epic ep = epics.get(subtask.getEpicId());
		ep.getSubtaskIds().add(subtask.getId());
		updateStatus(subtask.getEpicId());
	}


	public HashMap<Integer, Subtask> getAllSubtasks() {
		return subtasks;
	}

	public HashMap<Integer, Task> getAllTasks() {
		return tasks;
	}

	public HashMap<Integer, Epic> getALLEpics() {
		return epics;
	}

	public void deleteTaskById(int nextId) {
		tasks.remove(nextId);
	}

	public void deleteEpic(int nextId) {
		epics.get(nextId).getSubtaskIds().clear();
		epics.remove(nextId);
		updateStatus(nextId);
	}

	public void deleteSubtask(int nextId) {
		subtasks.remove(nextId);
		updateStatus(epics.get(nextId).getId());
	}

	public void deleteAllTasks() {
		tasks.clear();
	}

	public void deleteAllTEpics() {
		epics.clear();
	}

	public void deleteAllSubtask() {
		subtasks.clear();
	}

	public void getTaskById(int nextId) {
		tasks.get(nextId);
	}

	public void getEpicById(int nextId) {
		epics.get(nextId);
	}

	public void getSubTaskById(int nextId) {
		subtasks.get(nextId);
	}

	public ArrayList<Integer> getSubTaskByEpic(int epicId) {
		Epic epic = epics.get(epicId);
		return epic.getSubtaskIds();
	}

	public void updateStatus(int epicId) {
		int countDone = 0;
		int countNew = 0;
		for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {
			Subtask anSubtask = subtasks.get(subtaskId);
			if (anSubtask.equals(Status.DONE))
				countDone++;
			else if (anSubtask.equals(Status.NEW))
				countNew++;
		}
		if (countNew == epics.get(epicId).getSubtaskIds().size()) {
			epics.get(epicId).setStatus(Status.NEW);
		} else if (countDone == epics.get(epicId).getSubtaskIds().size()) {
			epics.get(epicId).setStatus(Status.DONE);
		} else {
			epics.get(epicId).setStatus(Status.IN_PROGRESS);
		}
	}
}