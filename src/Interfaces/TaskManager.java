package Interfaces;

import POJOS.*;
import java.sql.SQLException;
import java.util.List;


public interface TaskManager {
	
	public void addTask(Task task) throws SQLException;
	
	public List<Task> getListOfTasks (int staffAllTask_id);
	

}
