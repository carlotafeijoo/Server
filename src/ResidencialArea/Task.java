package ResidencialArea;

import java.util.Objects;

public class Task {

	String description;
	int task_id;
	
	
	public Task() {
		super();
	}
	
	public Task(String description, int task_id) {
		super();
		this.description = description;
		this.task_id = task_id;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getTask_id() {
		return task_id;
	}
	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, task_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		return Objects.equals(description, other.description) && task_id == other.task_id;
	}

	@Override
	public String toString() {
		return "Task [description=" + description + ", task_id=" + task_id + "]";
	}
	
	
}
