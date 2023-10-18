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
			String sql = "INSERT INTO Task (description, doctor_id,duration,elderly_id) VALUES (?,?,?,?) ";// añadir
																											// elderly
																											// id que
																											// tiene qu
																											// venir
																											// indicado
																											// por el
																											// doctor

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

	@Override
	public List<Task> getListOfTasks(int task_id) {
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

}
