package Interfaces;

import java.sql.SQLException;

import POJOS.Report;

public interface ReportManager {

	Report seeBitalinoReportByID(int report_id) throws SQLException;

	
}
