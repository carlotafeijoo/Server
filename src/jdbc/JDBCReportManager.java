package jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Interfaces.ReportManager;
import POJOS.Elderly;
import POJOS.Report;
import POJOS.Task;


public class JDBCReportManager implements ReportManager {
	
	private JDBCManager ReportManager;

	public JDBCReportManager(JDBCManager jdbcManager) {
		this.ReportManager = jdbcManager;
	}
	
	@Override
	public Report seeBitalinoReportByID(int report_id) throws SQLException {
		
		Report report = null;
		
		try {
			String sql = "SELECT * FROM Report WHERE id = ?";
			PreparedStatement prep = ReportManager.getConnection().prepareStatement(sql);
			prep.setInt(1, report_id);
			ResultSet resultSet = prep.executeQuery();
			
			while(resultSet.next()) {
				String file_name = resultSet.getString("file_name");
				Integer task_id = resultSet.getInt("task_id");
				Integer elderly_id = resultSet.getInt("elderly_id");
				
				report = new Report(report_id, file_name, task_id, elderly_id);
		
			prep.close();
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return report;
	}
	
	public void addReport(Report r) {
		try {
			String sql = "INSERT INTO Report (file_name, elderly_id, task_id) VALUES (?,?,?)";
			// use preparedStmt so nothing damages the database
			PreparedStatement prep = ReportManager.getConnection().prepareStatement(sql);
			
			prep.setString(1, r.getFile_name());
			prep.setInt(2, r.getElderly_id());
			prep.setInt(3, r.getTask_id());
			prep.executeUpdate();
			prep.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public List<Report> getListOfReportsByDoctorFromElderly(int elderly_id) {
		List<Task> tasks = new ArrayList<>();
		try {
			String sql = "SELECT * FROM Report WHERE elderly_id = ?" ;
			PreparedStatement pr = ReportManager.getConnection().prepareStatement(sql);
			pr.setInt(1, elderly_id);
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


