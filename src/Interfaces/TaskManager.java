package Interfaces;

import POJOS.*;
import java.sql.SQLException;
import java.util.List;

public interface TaskManager {

	public void addTask(Task task) throws SQLException;

	public List<Task> getListOfTasks();
	
	public List<Task> getListOfTasksByDoctorFromElderly(int doctor_id, int elderly_id);

}
