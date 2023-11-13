package jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Interfaces.TaskManager;

import POJOS.Task;

public class JDBCTasksManager implements TaskManager {

	private JDBCManager manager;

	public JDBCTasksManager(JDBCManager m) {
		this.manager = m;
	}

	// add a new task
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

	//BORRARLA O NO?
	@Override
	public List<Task> getListOfTasks() {
		List<Task> tasks = new ArrayList<Task>();
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
	
	@Override
	public List<Task> getListOfTasksByDoctorFromElderly(int doctor_id, int elderly_id) {
		List<Task> tasks = new ArrayList<Task>();
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

}
