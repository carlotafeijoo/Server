package jdbc;
import POJOS.Elderly;
import POJOS.Schedule;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Interfaces.ScheduleManager;

public class JDBCScheduleManager implements ScheduleManager {
	private JDBCManager manager;
	
	
	public JDBCScheduleManager (JDBCManager m) {
		this.manager=m;
	}

	@Override
	public void addSchedule (Schedule s) {
		try {
			String sql = "INSERT INTO Schedule (weekDay, staff_id, task_id, elderly_id) VALUES (?,?,?,?)";
			// use preparedStmt so nothing damages the database
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, s.getWeekDay());
			prep.setInt(2, s.getStaff_id());
			prep.setInt(3, s.getTask_id());
			prep.setInt(4, s.getElderly_id());
			prep.executeUpdate();
			prep.close();
		}catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	
	
	@Override 
	public List<Schedule> getDaySchedule (String weekDay){
		 List<Schedule> schedules = new ArrayList<Schedule>();
		    try {
		    	String sql = "SELECT * FROM Schedule WHERE weekDay = ? ";
				PreparedStatement pr = manager.getConnection().prepareStatement(sql);
				pr.setString(1, weekDay);
				ResultSet rs = pr.executeQuery();
			
		        while (rs.next()) {
		            Integer staff_id = rs.getInt("staff_id");
		            Integer task_id = rs.getInt("task_id");
		            Integer elderly_id = rs.getInt("elderly_id");
		            Schedule schedule = new Schedule (weekDay, staff_id, task_id, elderly_id);
		            schedules.add(schedule);
		           
		        }
		        rs.close();
		        pr.close();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    System.out.println(schedules);
		    return schedules;
	}
	
	@Override
	public Schedule searchScheduleById(int schedule_id) throws Exception {
	    Schedule schedule = null;
	    try {
	        String sql = "SELECT * FROM Schedule WHERE schedule_id = ?";
	        PreparedStatement pr = manager.getConnection().prepareStatement(sql);
	        pr.setInt(1, schedule_id);
	        ResultSet rs = pr.executeQuery();

	        while (rs.next()) {
	        	String weekDay = rs.getString("weekDay");
	        	Integer staff_id = rs.getInt("staff_id");
	            Integer task_id = rs.getInt("task_id");
	            Integer elderly_id = rs.getInt("elderly_id");
	            schedule = new Schedule (weekDay, staff_id, task_id, elderly_id);
	        }

	        rs.close();
	        pr.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return schedule;
	}
	
	
	

	@Override
	public void scheduleUpdate (Schedule schedule) {
		try {
			String sql = "UPDATE Schedule SET weekDay=? WHERE schedule_id = ?;";
			PreparedStatement pr = manager.getConnection().prepareStatement(sql);

	        pr.setString(1, schedule.getWeekDay());
	        
	        pr.executeUpdate();
	        pr.close();
		}catch (Exception e) {
	        e.printStackTrace();
	    }
		
	}
	
}





