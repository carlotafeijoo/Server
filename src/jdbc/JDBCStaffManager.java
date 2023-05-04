package jdbc;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import Interfaces.StaffManager;
import POJOS.Elderly;
import POJOS.Staff;


public abstract class JDBCStaffManager implements StaffManager {
	
	
	private JDBCManager staffmanager;
	
	public JDBCStaffManager(JDBCManager jdbcManager) {
		this.staffmanager = jdbcManager;
	}

	//Add a staff member
	@Override
	public void addStaffMember (Staff staffmember) {
		try {
			String sql="INSERT INTO staff (name, field, phone, dob, adress) VALUES (?,?,?,?,?,?) ";
			PreparedStatement prep= ((java.sql.Statement) staffmanager).getConnection().prepareStatement(sql);
			prep.setString(1, staffmember.getName());
			prep.setString(2, staffmember.getField());
			prep.setInt(3, staffmember.getPhone);
			prep.setDate(4, (Date) staffmember.getDob());
			prep.setString(5, staffmember.getAddress());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Delete a staff member by ID
	@Override
	public void deleteStaffMember(int id) {
	    try {
	        String sql = "DELETE FROM staff WHERE id = ?";
	        PreparedStatement prep = ((java.sql.Statement) staffmanager).getConnection().prepareStatement(sql);
	        prep.setInt(1, id);
	        prep.executeUpdate();
	        prep.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	
	//List all the staff members
	@Override
	public List<Staff> listStaffMembers() {
	    List<Staff> staffmembers = new ArrayList<Staff>();
	    try {
	        Connection connection = ((java.sql.Statement) staffmanager).getConnection();
	        Statement stmt = (Statement) connection.createStatement();
	        String sql = "SELECT * FROM staff";
	        ResultSet rs = ((java.sql.Statement) stmt).executeQuery(sql);
	        while (rs.next()) {
	            Integer id = rs.getInt("id");
	            String field = rs.getString("field");
	            Integer phone = rs.getInt("phone");
	            Date dob = rs.getDate("dob");
	            Staff staffmember = new Staff(id, field, phone, dob);
	            staffmembers.add(staffmember);
	        }
	        rs.close();
	        ((Connection) stmt).close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return staffmembers;
	}


	//Search in all the staff members list a member with a specific id
	@Override
	public Staff searchStaffmemberByID (int id){
	    Staff staffmember = null;
	    try {
	        Connection connection = ((java.sql.Statement) staffmanager).getConnection();
	        java.sql.Statement stmt = connection.createStatement();
	        String sql = "SELECT * FROM staff WHERE id = " + id;
	        ResultSet rs = stmt.executeQuery(sql);
	        if (rs.next()) {
	        	String name = rs.getString("name");
	            String field = rs.getString("field");
	            int phone = rs.getInt("phone");
	            Date dob = rs.getDate("dob");
	            String address = rs.getString("address");

	            staffmember = new Staff(id, name, field, phone, dob, address);
	        }
	        rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return staffmember;
	}

	
	//get the staff members assigned to a specific elderly
	@Override
	public List<Staff> getAssignedStaffMembers(int elderlyId) {
	    List<Staff> staffMembers = new ArrayList<>();
	    try {
	        Connection connection = ((java.sql.Statement) staffmanager).getConnection();
	        String sql = "SELECT s.id, s.name, s.field, s.phone, s.dob, s.address " +
	                     "FROM staff s " +
	                     "JOIN assignment a ON s.id = a.staff_id " +
	                     "WHERE a.elderly_id = ?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setInt(1, elderlyId);
	        ResultSet rs = statement.executeQuery();
	        while (rs.next()) {
	            int id = rs.getInt("id");
	            String name = rs.getString("name");
	            String field = rs.getString("field");
	            int phone = rs.getInt("phone");
	            Date dob = rs.getDate("dob");
	            String address = rs.getString("address");

	            Staff staffMember = new Staff(id, name, field, phone, dob, address);
	            staffMembers.add(staffMember);
	        }
	        rs.close();
	        statement.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return staffMembers;
	}
	
	//show info of a staff member before updating 
	@Override
	public Staff showStaffInfo(int id) {
	    Staff staffMember = null;
	    try {
	        java.sql.Statement statement = ((java.sql.Statement) staffmanager).getConnection().createStatement();
	        String sql = "SELECT * FROM staff WHERE id = " + id;
	        ResultSet rs = statement.executeQuery(sql);
	        while (rs.next()) {
	            String name = rs.getString("name"); 
	            String field = rs.getString("field");
	            int phone = rs.getInt("phone"); 
	            Date dob = rs.getDate("dob"); 
	            String address = rs.getString("address"); 

	            staffMember = new Staff(id, name, field, phone, dob, address);
	        }
	        rs.close();
	        statement.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return staffMember;
	}

	
	//update info from a staff member
	@Override
	public void updateStaffMemberInfo(Staff staffmember) {
	    try {
	        String sql = "UPDATE staff SET phone = ?, address = ? WHERE id = ?";
	        PreparedStatement pr = ((java.sql.Statement) staffmanager).getConnection().prepareStatement(sql);

	        if (staffmember.getPhone() != null) {
	            pr.setInt(1, (int) staffmember.getPhone());
	        }
	        if (staffmember.getAddress() != null) {
	            pr.setString(2, staffmember.getAddress());
	        }
	        pr.setInt(3, staffmember.getId());
	        pr.executeUpdate();
	        pr.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	//count the staff members by field
	@Override
	public int CountStaffMembers (String field) {
		int staffmembers = 0;
		
		try {
			
			String sql = "SELECT COUNT(id) FROM Staff WHERE field LIKE ?" ;
			
			PreparedStatement pr = ((java.sql.Statement) staffmanager).getConnection().prepareStatement(sql);
			pr.setString(1,field);
			ResultSet rs = pr.executeQuery();
			while (rs.next()) {
				staffmembers = (rs.getInt("COUNT(id)"));
			}
			pr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return staffmembers;
	}

	
	public List<POJOS.Staff> StaffMembersFromElderlyList(Elderly elderly_id) {
		return null;
	}

	public POJOS.Staff ShowStaffInfo(POJOS.Staff staff_id) {
		return null;
	}

	public int CounterStaffMembers(String staff_field) {
		return 0;
	}
	

}

