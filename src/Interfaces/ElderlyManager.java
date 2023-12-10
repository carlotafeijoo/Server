package Interfaces;

import java.util.List;

import POJOS.Elderly;
import POJOS.Task;

public interface ElderlyManager {
	/**
	 * Adds a new elderly person to the database.
	 * 
	 * @param e the Elderly object representing the elderly person to be added
	 */
	public void addElderly(Elderly e);
	/**
	 * Searches for an elderly person by their ID.
	 * 
	 * @param id the ID of the elderly person
	 * @return the Elderly object
	 * @throws Exception if an error occurs during the search
	 */
	public Elderly searchElderlyById(int id) throws Exception;
	/**
	 * Updates the information of an elderly person in the database.
	 * 
	 * @param e the Elderly object representing the updated elderly person information
	 */
	public void updateInfo(Elderly e);
	/**
	 * Searches for the ID of an elderly person based on the user ID.
	 * 
	 * @param User_id the user ID
	 * @return the ID of the elderly person
	 */
	int searchElderlyIdfromUId(int User_id);
	/**
	 * Retrieves a list of elderly persons based on the doctor's ID.
	 * 
	 * @param doctor_id the ID of the doctor
	 * @return a list of Elderly objects
	 */
	public List<Elderly> getListOfElderlyByDoctorID(int doctor_id);
	/**
	 * Retrieves a list of tasks for a specific elderly person.
	 * 
	 * @param user_id the user ID of the elderly person
	 * @return a list of Task objects
	 */
	public List<Task> seeTasksbyElderly(int user_id);
	/**
	 * Retrieves a list of tasks and their IDs for a specific elderly person.
	 * 
	 * @param id_elderly the ID of the elderly person
	 * @return a list of Task objects
	 */
	public List<Task> seeTaskANDidbyElderly(int id_elderly);
	/**
	 * Checks if a DNI (username) is already used by an elderly person.
	 * 
	 * @param username the DNI (username) to be checked
	 * @return true if the DNI is already used, otherwise false
	 */
	public boolean checkAlreadyUsedDNI(int username);
	/**
	 * Adds symptoms for a specific elderly person.
	 * 
	 * @param eld_id the ID of the elderly person
	 * @param symptoms the symptoms to be added
	 */
	public void addSymptoms(int eld_id, String symptoms);
	/**
	 * Retrieves the symptoms of a specific elderly person.
	 * 
	 * @param eld_id the ID of the elderly person
	 * @return a string representing the symptoms
	 */
	public String seeSymptoms(int eld_id);

}