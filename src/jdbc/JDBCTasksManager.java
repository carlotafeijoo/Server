package jdbc;

import java.sql.PreparedStatement;
import java.util.List;

import Interfaces.TaskManager;
import POJOS.Task;

public class JDBCTasksManager implements TaskManager{

	private JDBCManager manager;
	
	public JDBCTasksManager(JDBCManager m){
		this.manager = m;
	}
	
	//add a new task
	public void addTask(Task t) {
		try {
			String sql = "INSERT INTO task (task_id, description) VALUES (?,?)";
			// use preparedStmt so nothing damages the database
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, t.getDescription());
			prep.setInt(2, t.getTask_id()); 
			prep.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	//delete a task
	public void deleteTask(Task t) {
		try {
			String sql = "DELETE FROM t.task (task_id, description) VALUES (?,?)";
			// use preparedStmt so nothing damages the database
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, t.getDescription());
			prep.setInt(2, t.getTask_id()); 
			prep.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	//list the tasks of a staff member
	public List<Task> getListOfTasks(){
		return null;
	}
	
	//assign a task to a staff
	public void assign(int task_id, int staff_id){
		try{
			String sql = "INSERT INTO task (task_id,staff_id) VALUES (?,?)";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setInt(1, task_id);
			prep.setInt(2, staff_id);		
			prep.executeUpdate();				
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	//unassign a task from a staff member
	public void unassign(int task_id, int staff_id){
		try{
			String sql = "DELETE FROM t.task (task_id, description) VALUES (?,?)";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setInt(1, task_id);
			prep.setInt(2, staff_id);		
			prep.executeUpdate();				
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	//get info (description) of a task by taskid
	public Task getTask (int task_id) {
		Task t = null;
		String description = "";
		try{			
			String sql = "SELECT * FROM task (task_id,description) VALUES (?)";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, description);		
			prep.executeUpdate();	
			t = new Task(task_id, description);
		}catch(Exception e) {
			e.printStackTrace();
		}	
		return t;
	}
	
	
}
