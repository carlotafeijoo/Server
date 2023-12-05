package Interfaces;

import java.sql.SQLException;
import java.util.List;

import POJOS.Report;

public interface ReportManager {

	Report seeBitalinoReportByID(int report_id) throws SQLException;

	void addReport(Report r);
	
	public List<Report> getListOfReportsByDoctorFromElderly(int elderly_id, int doctor_id);

}
