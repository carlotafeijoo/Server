package Interfaces;
import java.util.List;
import POJOS.Elderly;
import POJOS.Staff;

public interface ElderlyManager {
	    //Adding a new elderly
		public void deleteElderly(Elderly e);
		
		//get the list of the elderlies
		public List<Elderly> getListOfElderlies();
		//Delete an elderly
		public void addElderly(Elderly e);
		
		//Assign a dog to a staff member to a elderly
		public void assign(int staff_ID, int elderly_ID);
		
		//Update info from elderly
		public void updateInfo(Elderly e);
		
		//count number of elderlies
		public int counElderlies (List<Elderly> elderlies);
		

}
