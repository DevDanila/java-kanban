public class Main {

	public static void main(String[] args) {
		TaskManager taskManager = new TaskManager();

		Task task1 = new Task("Прочитать книгу", "Описание 1", Status.NEW);
		taskManager.addTask(task1);

		task1.setTitle("прочитать новую книгу");
		taskManager.updateTask(task1);

		Epic ep = new Epic("Переезд", "Описание эпика 1");
		taskManager.addEpic(ep);


		Epic ep2 = new Epic("Переезд2", "Описание эпика 2");
		taskManager.addEpic(ep2);

		Subtask subtask1 = new Subtask("собрать вещи", "описание подзадачи 1", Status.DONE, 2);
		subtask1.setEpicId(ep.getId());
		taskManager.addSubTask(subtask1);

		Subtask subtask2 = new Subtask("упаковать вещи", "описание подзадачи 2", Status.DONE, 2);
		subtask2.setEpicId(ep.getId());
		taskManager.addSubTask(subtask2);

		Subtask subtask3 = new Subtask("вынести вещи", "описание подзадачи 3", Status.DONE, 2);
		subtask3.setEpicId(ep.getId());
		taskManager.addSubTask(subtask3);

		System.out.println(task1);
		System.out.println(ep);
	}
}