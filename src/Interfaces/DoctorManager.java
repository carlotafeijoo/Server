package Interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import POJOS.Doctor;

public interface DoctorManager {
	/**
	 * Adds a new doctor member to the database.
	 * 
	 * @param Doctormember the Doctor object representing the doctor to be added
	 * @throws SQLException if a database access error occurs
	 */
	public void addDoctorMember(Doctor Doctormember) throws SQLException;
	/**
	 * Searches for a doctor ID based on the user ID.
	 * 
	 * @param id the user ID
	 * @return the doctor ID
	 */
	public int searchDoctorIdfromUId(int id);

	/**
	 * Searches for a doctor by their ID.
	 * 
	 * @param id the doctor ID
	 * @return the Doctor object
	 */
	public Doctor searchDoctorbyId(int id);
	/**
	 * Updates the information of a doctor member in the database.
	 * 
	 * @param Doctormember the Doctor object representing the updated doctor information
	 */
	public void updateDoctorMemberInfo(Doctor Doctormember);
	/**
	 * Retrieves a list of all doctors from the database.
	 * 
	 * @return an ArrayList of Doctor objects representing all the doctors
	 */
	public ArrayList<Doctor> searchAllDoctors();
	/**
	 * Checks if a DNI (username) is already used by a doctor member.
	 * 
	 * @param username the DNI (username) to be checked
	 * @return true if the DNI is already used, otherwise false
	 */
	public boolean checkAlreadyUsedDNIDoc(String username);

}
