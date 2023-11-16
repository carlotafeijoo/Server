package jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Interfaces.ReportManager;
import POJOS.Bitalino;
//crear proyecto Bitalino 

public class JDBCReportManager implements ReportManager {
	
	private JDBCManager ReportManager;

	public JDBCReportManager(JDBCManager jdbcManager) {
		this.ReportManager = jdbcManager;
	}
	
	
	
	

}
