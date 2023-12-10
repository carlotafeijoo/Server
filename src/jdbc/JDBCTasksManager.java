package jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Interfaces.TaskManager;
import POJOS.Elderly;
import POJOS.Task;

public class JDBCTasksManager implements TaskManager {

	private JDBCManager manager;
	/**
	 * Constructs a new JDBCTasksManager with the specified JDBCManager.
	 * 
	 * @param m the JDBCManager to be used
	 */
	public JDBCTasksManager(JDBCManager m) {
		this.manager = m;
	}

	/**
	 * Adds a new task to the database.
	 * 
	 * @param task the Task object representing the task to be added
	 * @throws SQLException if a database access error occurs
	 */
	@Override
	public void addTask(Task task) throws SQLException {
		try {
			String sql = "INSERT INTO Task (description, doctor_id,duration,elderly_id) VALUES (?,?,?,?) ";

			PreparedStatement prep = manager.getConnection().prepareStatement(sql);

			prep.setString(1, task.getDescription());
			prep.setInt(2, task.getDoctor_id());
			prep.setInt(3, task.getDuration());
			prep.setInt(4, task.getElderly_id());

			prep.executeUpdate();
			prep.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Retrieves a list of all tasks from the database.
	 * 
	 * @return a list of Task objects
	 */
	@Override
	public List<Task> getListOfTasks() {
		List<Task> tasks = new ArrayList<>();
		try {
			String sql = "SELECT * FROM Task ";
			PreparedStatement pr = manager.getConnection().prepareStatement(sql);
			ResultSet rs = pr.executeQuery();

			while (rs.next()) {
				Integer id = rs.getInt("task_id");
				String description = rs.getString("description");
				Integer doctor_id = rs.getInt("doctor_id");
				Task task = new Task(id, description, doctor_id);
				tasks.add(task);
			}
			rs.close();
			pr.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	/**
	 * Retrieves a list of tasks assigned by a specific doctor to a specific elderly person.
	 * 
	 * @param doctor_id the ID of the doctor
	 * @param elderly_id the ID of the elderly person
	 * @return a list of Task objects
	 */
	@Override
	public List<Task> getListOfTasksByDoctorFromElderly(int doctor_id, int elderly_id) {
		List<Task> tasks = new ArrayList<>();
		try {
			String sql = "SELECT * FROM Task WHERE doctor_id = ? AND elderly_id = ?" ;
			PreparedStatement pr = manager.getConnection().prepareStatement(sql);
			pr.setInt(1, doctor_id);
			pr.setInt(2, elderly_id);
			ResultSet rs = pr.executeQuery();

			while (rs.next()) {
				Integer id = rs.getInt("task_id");
				String description = rs.getString("description");
				Integer doc_id = rs.getInt("doctor_id");
				Integer elder_id = rs.getInt("elderly_id");
				Integer dur = rs.getInt("duration");
				Task task = new Task(id,description,doc_id,dur,elder_id);
				tasks.add(task);
			}
			rs.close();
			pr.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	/**
	 * Retrieves a task by its ID.
	 * 
	 * @param id_task the ID of the task
	 * @return the Task object
	 */
	@Override
	public Task getTask(int id_task) {

		Task task = null;

		try {
			String sql = "SELECT * FROM Task WHERE task_id = ?" ;
			PreparedStatement pr = manager.getConnection().prepareStatement(sql);
			pr.setInt(1, id_task);
			ResultSet rs = pr.executeQuery();

			while (rs.next()) {
				//int task_id = rs.getInt("task_id");
				String description = rs.getString("description");
				Integer doctor_id = rs.getInt("doctor_id");
				Integer duration = rs.getInt("duration");
				Integer elderly_id = rs.getInt("elderly_id");

				task = new Task(id_task, description, doctor_id, duration, elderly_id);

			}
			rs.close();
			pr.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return task;
	}


}
