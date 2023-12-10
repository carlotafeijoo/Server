package Interfaces;

import java.sql.SQLException;
import java.util.List;

import POJOS.Report;

public interface ReportManager {
	/**
	 * Retrieves a Bitalino report by its ID.
	 * 
	 * @param report_id the ID of the report
	 * @return the Report object
	 */
	Report seeBitalinoReportByID(int report_id);
	/**
	 * Adds a new report to the database.
	 * 
	 * @param r the Report object representing the report to be added
	 */
	void addReport(Report r);
	/**
	 * Retrieves a list of reports created by a specific doctor for a specific elderly person.
	 * 
	 * @param elderly_id the ID of the elderly person
	 * @param doctor_id the ID of the doctor
	 * @return a list of Report objects
	 */
	public List<Report> getListOfReportsByDoctorFromElderly(int elderly_id, int doctor_id);

}
