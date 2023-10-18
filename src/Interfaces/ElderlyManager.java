package Interfaces;

import java.util.List;
import POJOS.Elderly;

public interface ElderlyManager {

	// add an elderly
	public void addElderly(Elderly e);

	public Elderly searchElderlyById(int id) throws Exception;

	// Assign a elderly to a staff member to a elderly
	public void assign(int staff_ID, int elderly_ID);

	// Update info from elderly
	public void updateInfo(Elderly e);

	public List<Elderly> getListOfElderly();

	int searchElderlyIdfromUId(int User_id);

	List<Elderly> getListOfElderlyByDoctorID(int doctor_id);

}
