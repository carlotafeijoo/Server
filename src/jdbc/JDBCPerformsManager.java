package jdbc;

import Interfaces.PerformsManager;
import POJOS.FamilyContact;
import POJOS.Performs;
import POJOS.Shift;
import POJOS.Staff;
import POJOS.Task;

public class JDBCPerformsManager implements PerformsManager {
private JDBCManager manager;
	
	public JDBCPerformsManager(JDBCManager m){
		this.manager = m;
	}
	
	//assign a new shift to a staff
	public void assignShift (Shift shift, Staff staff) {//TODO
		
	}
	
	//unassign a new shift to a staff
	public void unassignShift (Shift shift, Staff staff) { //TODO
		
	}
	
	//show the schedule from a staff
	public void schedule (Performs performs, Staff staff, Task task) {//TODO
			
	}

}
