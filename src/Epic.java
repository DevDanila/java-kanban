import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
	private final ArrayList<Integer> subtaskIds = new ArrayList<>();

	public ArrayList<Integer> getSubtaskIds() {
		return subtaskIds;
	}

	public Epic(String title, String description) {
		super(title, description, Status.NEW);
	}
}


