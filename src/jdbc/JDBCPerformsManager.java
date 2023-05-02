package jdbc;

import Interfaces.PerformsManager;
import POJOS.Performs;
import POJOS.Shift;
import POJOS.Staff;
import POJOS.Task;

public class JDBCPerformsManager implements PerformsManager {
	
private JDBCManager manager;
	
	public JDBCPerformsManager(JDBCManager m){
		this.manager = m;
	}
	
	//TO DO
	//assign a new shift to a staff
	public void assignShift (Shift shift, Staff staff) {
		
	}
	
	//TO DO
	//unassign a new shift to a staff
	public void unassignShift (Shift shift, Staff staff) { 
		
	}
	
	//TO DO
	//show the schedule from a staff
	public void schedule (Performs performs, Staff staff, Task task) {
			
	}

}
