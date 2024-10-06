package com.practicum.java_kanban;

import com.practicum.java_kanban.manager.TaskManager;
import com.practicum.java_kanban.model.Epic;
import com.practicum.java_kanban.model.Subtask;
import com.practicum.java_kanban.model.Task;
import com.practicum.java_kanban.model.Status;


public class Main {

	public static void main(String[] args) {
		TaskManager taskManager = new TaskManager();

		Task task1 = new Task("Прочитать книгу", "Описание 1", Status.NEW);
		taskManager.addTask(task1);

		Task task2 = new Task("Смотреть фильм", "Описание 2", Status.NEW);
		taskManager.addTask(task2);

		Task task3 = new Task("Убраться", "Описание 3", Status.NEW);
		taskManager.addTask(task3);

		task1.setTitle("прочитать новую книгу");
		taskManager.updateTask(task1);

		Epic ep = new Epic("Переезд", "Описание эпика 1");
		taskManager.addEpic(ep);

		Epic ep2 = new Epic("Переезд2", "Описание эпика 2");
		taskManager.addEpic(ep2);

		Subtask subtask1 = new Subtask("собрать вещи", "описание подзадачи 1", Status.DONE, 4);
		taskManager.addSubTask(subtask1);

		Subtask subtask2 = new Subtask("упаковать вещи", "описание подзадачи 2", Status.DONE, 4);
		taskManager.addSubTask(subtask2);

		Subtask subtask3 = new Subtask("вынести вещи", "описание подзадачи 3", Status.DONE, 4);
		taskManager.addSubTask(subtask3);

		Subtask subtask4 = new Subtask("вынести мусор", "описание подзадачи 4", Status.NEW, 5);
		taskManager.addSubTask(subtask4);

		System.out.println(task1);
		System.out.println(ep);
		System.out.println(ep.getStatus());
		System.out.println(ep2.getStatus());
		System.out.println(taskManager.getAllTasks());
		System.out.println(taskManager.getAllEpics());
		System.out.println(taskManager.getAllSubtasks());
		System.out.println("-------------");
		System.out.println(taskManager.getSubtaskByEpicId(4));

		System.out.println("-------------");
		System.out.println(taskManager.getSubtaskByEpicId(5));
		System.out.println("-------------");
		taskManager.deleteAllSubtask();
		System.out.println(taskManager.getAllSubtasks());
		System.out.println(ep.getStatus());
		System.out.println(ep2.getStatus());
//        taskManager.deleteSubtask(6);
//        System.out.println(taskManager.getAllEpics());
//        System.out.println(taskManager.getAllSubtasks());
//        System.out.println(ep.getSubtaskIds());
//        System.out.println(ep.getStatus());
//        taskManager.deleteEpic(4);
//        System.out.println(taskManager.getAllEpics());
//        System.out.println(taskManager.getAllSubtasks());
	}
}