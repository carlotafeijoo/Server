package Interfaces;

import java.sql.SQLException;
import POJOS.*;

public interface StaffManager {

	
	public void addStaffMember (Doctor staffmember) throws SQLException;
	
	
	public int searchStaffIdfromUId(int id);
	
	public Doctor searchStaffbyId ( int id);
	
	public void updateStaffMemberInfo(Doctor staffmember);
	

}
