package jdbc;

import Interfaces.PerformsManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import POJOS.Shift;

public class JDBCPerformsManager implements PerformsManager {
	
	private JDBCManager staffmanager;
	
	public JDBCPerformsManager(JDBCManager m){
		this.setManager(m);
	}
	
	private void setManager(JDBCManager m) {
		// TODO Auto-generated method stub
		
	}

	//Assign shifts to a staff member (thereby updating the database's performs table)
	@Override
	public boolean assignShiftToStaffMember(Shift shift, int staff_id) {
	    try {
	        Connection connection = ((java.sql.Statement) staffmanager).getConnection();
	        String sql = "INSERT INTO performs (staff_id, shift ,task_id, performs_id, day ) VALUES (?, ?, ?, ?, ? )";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setInt(1, staff_id);
	        statement.setInt(2, shift.ordinal() + 1);
	        int rowsInserted = statement.executeUpdate();
	        statement.close();
	        return rowsInserted > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	//Unassigns shift to a staff member
	public boolean unassignShiftFromStaffMember(Shift shift, int staffId) {
	    try {
	        Connection connection = ((java.sql.Statement) staffmanager).getConnection();
	        String sql = "DELETE FROM performs WHERE staff_id = ? AND shift_id = ?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setInt(1, staffId);
	        statement.setInt(2, shift.ordinal() + 1);
	        int rowsDeleted = statement.executeUpdate();
	        statement.close();
	        return rowsDeleted > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	//Return the staff member´s assigned shift
	public Shift getShiftFromStaffMember(int staffId) {
		try {
	        Connection connection = ((java.sql.Statement) staffmanager).getConnection();
	        String sql = "SELECT shift_id FROM performs WHERE staff_id = ?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setInt(1, staffId);
	        ResultSet rs = statement.executeQuery();
	        if (rs.next()) {
	            int shiftId = rs.getInt("shift_id");
	            Shift shift = Shift.values()[shiftId - 1];
	            rs.close();
	            statement.close();
	            return shift;
	        }
	        statement.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	        
	 	

}
