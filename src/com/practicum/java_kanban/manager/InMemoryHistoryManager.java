package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
	private final static List<Task> browsingHistory = new ArrayList<>();
	private static final int LIST_HISTORY_LIMIT = 10;


	@Override
	public void add(Task task) {
		if (browsingHistory.size() == LIST_HISTORY_LIMIT) {
			browsingHistory.removeFirst();
		}
		browsingHistory.add(task);
	}

	@Override
	public List<Task> getHistory() {
		ArrayList<Task> newHistory = new ArrayList<>(browsingHistory);
        return newHistory;
    }
}
