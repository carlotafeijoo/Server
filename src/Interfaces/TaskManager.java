package Interfaces;

import POJOS.*;
import java.util.List;

public interface TaskManager {
	
	public void addTask(Task t);
	public void deleteTask(Task t);
	public List<Task> getListOfTasks();
	public void assign(int task_id, int staff_id);
	public void unassign(int task_id, int staff_id);
	public Task getTask (int task_id);
	
	
	

}
