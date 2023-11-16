package jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Interfaces.ReportManager;
import POJOS.Report;


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
	
	
	

}


