package Interfaces;

import java.util.List;
import POJOS.Elderly;


public interface ElderlyManager {
	    
	//get the list of the elderlies
	public List<Elderly> getListOfElderliesFromStaff (int staff_id);
	
	//add an elderly
	public void addElderly(Elderly e);
	
	public static Elderly showElderlyInfo (int id) {
		// TODO Auto-generated method stub
		return null;
	}


	public Elderly searchElderlyById( int id) throws Exception;
	
	//Assign a elderly to a staff member to a elderly
	public void assign(int staff_ID, int elderly_ID);
	
	//Update info from elderly
	public void updateInfo(Elderly e) ;
	
	public List<Elderly> getListOfElderly();
	
	

}
