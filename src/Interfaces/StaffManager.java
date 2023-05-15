package Interfaces;

import java.sql.SQLException;
import POJOS.*;

public interface StaffManager {

	
	public void addStaffMember (Staff staffmember) throws SQLException;
	
	
	public int searchStaffIdfromUId(int id);
	
	public Staff searchStaffbyId ( int id);
	
	public void updateStaffMemberInfo(Staff staffmember);
	

}
