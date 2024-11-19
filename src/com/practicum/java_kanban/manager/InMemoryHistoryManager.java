package com.practicum.java_kanban.manager;

import com.practicum.java_kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

	private Node<Task> head;
	private Node<Task> tail;
	private final Map<Integer, Node<Task>> historyMap = new HashMap<>();

	private static class Node<E> { // отдельный класс Node для узла списка

		public E data;
		public Node<E> next;
		public Node<E> prev;

		public Node(Node<E> prev, E data, Node<E> next) {
			this.data = data;
			this.next = next;
			this.prev = prev;
		}
	}

	@Override
	public void add(Task task) {
		if (task != null) {
			remove(task.getId());
			linkLast(task);
		}
	}

	@Override
	public void remove(int id) {
		removeNode(historyMap.get(id));
	}

	@Override
	public List<Task> getHistory() {
		return getTasks();
	}

	private void linkLast(Task task) {
		final Node<Task> oldTail = tail;
		final Node<Task> newNode = new Node<>(tail, task, null);
		tail = newNode;
		if (oldTail == null)
			head = newNode;
		else
			oldTail.next = newNode;

		historyMap.put(task.getId(), newNode);
	}

	private List<Task> getTasks() {
		List<Task> tasks = new ArrayList<>(historyMap.size());
		Node<Task> currentNode = head;
		while (currentNode != null) {
			tasks.add(currentNode.data);
			currentNode = currentNode.next;
		}
		return tasks;
	}

	private void removeNode(Node<Task> node) {
		if (node != null) {
			final Node<Task> next = node.next;
			final Node<Task> prev = node.prev;
			node.data = null;

			if (head == node && tail == node) {
				head = null;
				tail = null;
			} else if (head == node) {
				head = next;
				head.prev = null;
			} else if (tail == node) {
				tail = prev;
				tail.next = null;
			} else {
				prev.next = next;
				next.prev = prev;
			}

		}
	}
}

