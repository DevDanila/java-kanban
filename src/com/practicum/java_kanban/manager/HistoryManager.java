package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.model.Task;

import java.util.List;

public interface HistoryManager {

	void add(Task task);

	List<Task> getHistory();
}
