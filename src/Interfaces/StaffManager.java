package Interfaces;

import java.util.List;
import POJOS.*;

public interface StaffManager {


	// add a staff member
	public void addStaffMember (Staff staffmember);
	
	//delete a staff member
	public void deleteStaffMember(int id);
	
	//list all the staff members
	public List<Staff> listStaffMembers();
	
	//Get the list of staff members by field
	public Staff searchStaffmemberBYField (String field);
	
	//search in the list of staff members by id
	public static Staff searchStaffmemberByID (int id) {
		return null;
	}
	
	//get the staff members assigned to a specific elderly
	public List<Staff> getAssignedStaffMembers(int elderlyId);
		
	// show info of a staff member
	public static Staff showStaffInfo (int id) {
		// TODO Auto-generated method stub
		return null;
	}
		
	//update info from a staff member
	public void updateStaffMemberInfo(Staff staffmember);
	
	//Count the staff members 
	public int CountStaffMembers (String staff_field);
		
	

}
