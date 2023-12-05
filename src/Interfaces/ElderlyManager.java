package Interfaces;

import java.util.List;

import POJOS.Elderly;
import POJOS.Task;

public interface ElderlyManager {

	// add an elderly
	public void addElderly(Elderly e);

	public Elderly searchElderlyById(int id) throws Exception;

	// Update info from elderly
	public void updateInfo(Elderly e);

	public List<Elderly> getListOfElderly();

	int searchElderlyIdfromUId(int User_id);

	public List<Elderly> getListOfElderlyByDoctorID(int doctor_id);

	public List<Task> seeTasksbyElderly(int user_id);

	public List<Task> seeTaskANDidbyElderly(int id_elderly);
	
	public boolean checkAlreadyUsedDNI(int username);

	public void addSymptoms(int eld_id, String symptoms);

	public String seeSymptoms(int eld_id);

}