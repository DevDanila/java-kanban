package com.practicum.java_kanban;

import com.practicum.java_kanban.manager.*;
import com.practicum.java_kanban.model.Epic;
import com.practicum.java_kanban.model.Subtask;
import com.practicum.java_kanban.model.Task;

import java.io.File;


public class Main {

	public static void main(String[] args) {

		TaskManager taskManager = Managers.getDefault();

		Task task1 = new Task("Прочитать книгу", "Описание 1");
		taskManager.addTask(task1);

		Task task2 = new Task("Смотреть фильм", "Описание 2");
		taskManager.addTask(task2);

		Task task3 = new Task("Убраться", "Описание 3");
		taskManager.addTask(task3);

		Epic ep1 = new Epic("Переезд", "Описание эпика 1");
		taskManager.addEpic(ep1);

		Epic ep2 = new Epic("Переезд2", "Описание эпика 2");
		taskManager.addEpic(ep2);

		Subtask subtask1 = new Subtask("собрать вещи", "описание подзадачи 1", 4);
		taskManager.addSubTask(subtask1);

		Subtask subtask2 = new Subtask("упаковать вещи", "описание подзадачи 2", 4);
		taskManager.addSubTask(subtask2);

		Subtask subtask3 = new Subtask("вынести вещи", "описание подзадачи 3", 4);
		taskManager.addSubTask(subtask3);

		Subtask subtask4 = new Subtask("вынести мусор", "описание подзадачи 4", 5);
		taskManager.addSubTask(subtask4);

		System.out.println("--------------------------");
		System.out.println("Задача 1:");
		System.out.println(task1);

		System.out.println("--------------------------");
		System.out.println("Задача 2:");
		System.out.println(task2);

		System.out.println("--------------------------");
		System.out.println("Задача 3:");
		System.out.println(task3);

		System.out.println("--------------------------");
		System.out.println("Подзадача 1:");
		System.out.println(subtask1);

		System.out.println("--------------------------");
		System.out.println("Подзадача 2:");
		System.out.println(subtask2);

		System.out.println("--------------------------");
		System.out.println("Подзадача 3:");
		System.out.println(subtask3);

		System.out.println("--------------------------");
		System.out.println("Подзадача 4:");
		System.out.println(subtask4);

		System.out.println("--------------------------");
		System.out.println("Эпик 1:");
		System.out.println(ep1);

		System.out.println("--------------------------");
		System.out.println("Эпик 2:");
		System.out.println(ep2);

		System.out.println("--------------------------");
		System.out.println("Статус первого эпика:");
		System.out.println(ep1.getStatus());

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

		System.out.println("Обновление статуса эпика:");
		System.out.println(ep1.getStatus());
		System.out.println("Обновление статуса второго эпика:");
		System.out.println(ep2.getStatus());

		System.out.println("--------------------------");
		taskManager.getTaskById(1);
		taskManager.getTaskById(2);
		taskManager.getTaskById(3);
		taskManager.getEpicById(5);
		taskManager.getEpicById(5);
		taskManager.getSubtaskById(6);
		taskManager.getSubtaskById(6);
		//taskManager.deleteAllSubtask();
		System.out.println("История просмотров:");
		System.out.println(taskManager.getHistory());

		TaskManager file = new FileBackedTaskManager(new File("dataFile.csv"));
		Task task4 = new Task("Прочитать книгу", "Описание 1");
		file.addTask(task4);
		Task task44 = new Task("Прочитать книгу", "Описание 1");
		file.addTask(task44);
		Epic ep = new Epic("Переезд2", "Описание эпика 2");
		file.addEpic(ep);
		Subtask subtask11 = new Subtask("собрать вещи", "описание подзадачи 1", 3);
		file.addSubTask(subtask11);
		Subtask subtask22 = new Subtask("упаковать вещи", "описание подзадачи 2", 3);
		file.addSubTask(subtask22);

		System.out.println(file);
	}
}
