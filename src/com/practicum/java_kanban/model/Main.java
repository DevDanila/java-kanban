package com.practicum.java_kanban.model;

import com.practicum.java_kanban.manager.*;


public class Main {

	public static void main(String[] args) {

		TaskManager taskManager = Managers.getDefault();
		HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

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

		Subtask subtask1 = new Subtask("собрать вещи", "описание подзадачи 1", Status.NEW, 4);
		taskManager.addSubTask(subtask1);

		Subtask subtask2 = new Subtask("упаковать вещи", "описание подзадачи 2", Status.DONE, 4);
		taskManager.addSubTask(subtask2);

		Subtask subtask3 = new Subtask("вынести вещи", "описание подзадачи 3", Status.DONE, 4);
		taskManager.addSubTask(subtask3);

		Subtask subtask4 = new Subtask("вынести мусор", "описание подзадачи 4", Status.NEW, 5);
		taskManager.addSubTask(subtask4);

		System.out.println("--------------------------");
		System.out.println("Задача 1:");
		System.out.println(task1);

		System.out.println("--------------------------");
		System.out.println("Эпик 1:");
		System.out.println(ep);

		System.out.println("--------------------------");
		System.out.println("Статус эпика ep:");
		System.out.println(ep.getStatus());

		System.out.println("--------------------------");
		System.out.println("Статус второго эпика:");
		System.out.println(ep2.getStatus());

		System.out.println("--------------------------");
		System.out.println("Все задачи:");
		System.out.println(taskManager.getAllTasks());

		System.out.println("--------------------------");
		System.out.println("Все эпики:");
		System.out.println(taskManager.getAllEpics());

		System.out.println("--------------------------");
		System.out.println("Все подзадачи:");
		System.out.println(taskManager.getAllSubtasks());

		System.out.println("--------------------------");
		System.out.println("Подзадачи конкретного эпика:");
		System.out.println(taskManager.getSubtaskByEpicId(4));

		System.out.println("--------------------------");
		System.out.println("Подзадачи конкретного эпика:");
		System.out.println(taskManager.getSubtaskByEpicId(5));

		System.out.println("--------------------------");
		System.out.println("Список подзадач после вызова метода удаления всех подзадач:");
		taskManager.deleteAllSubtask();
		System.out.println(taskManager.getAllSubtasks());
		System.out.println("Обновление статуса эпика:");
		System.out.println(ep.getStatus());
		System.out.println("Обновление статуса второго эпика:");
		System.out.println(ep2.getStatus());

		System.out.println("--------------------------");
		taskManager.getTaskById(1);
		taskManager.getTaskById(2);
		taskManager.getTaskById(3);
		System.out.println("История просмотров:");
		System.out.println(inMemoryHistoryManager.getHistory());
	}
}
//        taskManager.deleteSubtask(6);
//        System.out.println(taskManager.getAllEpics());
//        System.out.println(taskManager.getAllSubtasks());
//