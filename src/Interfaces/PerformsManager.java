package Interfaces;


import POJOS.Shift;


public interface PerformsManager {
	
	
	public boolean assignShiftToStaffMember(Shift shift, int staff_id);
	
	public boolean unassignShiftFromStaffMember(Shift shift, int staffId);
	
	public Shift getShiftFromStaffMember(int staff_id);
	
}
