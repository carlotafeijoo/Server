package Interfaces;

import java.sql.SQLException;
import java.util.List;

import POJOS.Task;

public interface TaskManager {

	public void addTask(Task task) throws SQLException;

	public List<Task> getListOfTasks();

	public List<Task> getListOfTasksByDoctorFromElderly(int doctor_id, int elderly_id);

	public Task getTask(int id_task);

}
