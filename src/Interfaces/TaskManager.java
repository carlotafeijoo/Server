package Interfaces;

import java.sql.SQLException;
import java.util.List;

import POJOS.Task;

public interface TaskManager {
	/**
	 * Adds a new task to the database.
	 * 
	 * @param task the Task object representing the task to be added
	 * @throws SQLException if a database access error occurs
	 */
	public void addTask(Task task) throws SQLException;
	/**
	 * Retrieves a list of all tasks from the database.
	 * 
	 * @return a list of Task objects
	 */
	public List<Task> getListOfTasks();
	/**
	 * Retrieves a list of tasks assigned by a specific doctor to a specific elderly person.
	 * 
	 * @param doctor_id the ID of the doctor
	 * @param elderly_id the ID of the elderly person
	 * @return a list of Task objects
	 */
	public List<Task> getListOfTasksByDoctorFromElderly(int doctor_id, int elderly_id);
	/**
	 * Retrieves a task by its ID.
	 * 
	 * @param id_task the ID of the task
	 * @return the Task object
	 */
	public Task getTask(int id_task);

}
