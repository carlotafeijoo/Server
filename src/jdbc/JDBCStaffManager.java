package jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import Interfaces.StaffManager;

import POJOS.Staff;


public class JDBCStaffManager implements StaffManager {
	
	
	private JDBCManager staffmanager;
	
	public JDBCStaffManager(JDBCManager jdbcManager) {
		this.staffmanager = jdbcManager;
	}

	@Override
	public void addStaffMember(Staff staffmember) throws SQLException {
	    try {
	        String sql = "INSERT INTO Staff (name, field, dob, address, phone, email) VALUES (?,?,?,?,?,?)";
	        PreparedStatement prep = staffmanager.getConnection().prepareStatement(sql);
	        prep.setString(1, staffmember.getName());
	        prep.setString(2, staffmember.getField());
	        prep.setDate(3, (Date) staffmember.getDob());
	        prep.setString(4, staffmember.getAddress());
	        prep.setInt(5, staffmember.getPhone());
	        prep.setString(6, staffmember.getEmail());
	        prep.executeUpdate();
	        prep.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	//update info from a staff member
	@Override
	public void updateStaffMemberInfo(Staff staffmember) {
	    try {
	        String sql = "UPDATE Staff SET phone = ?, address = ? WHERE staff_id = ?";
	        PreparedStatement pr = staffmanager.getConnection().prepareStatement(sql);;
	        pr.setInt(1, staffmember.getPhone());
	        pr.setString(2, staffmember.getAddress());
	        pr.setInt(3, staffmember.getStaff_id());
	      
	        pr.executeUpdate();
	        pr.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	

	
	@Override
	// get staff_id from user id -> for login
	public int searchStaffIdfromUId(int User_id) {
		int staff_id = 0;
		try {
			Statement stmt = staffmanager.getConnection().createStatement(); 
			String sql = "SELECT Staff.staff_id FROM Staff JOIN User ON Staff.email=User.username WHERE User.id= " + User_id;
			ResultSet rs = stmt.executeQuery(sql);

			staff_id = rs.getInt("staff_id");

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return staff_id;

	}
	
	

	@Override
	public Staff searchStaffbyId (int staff_id) {
		Staff staff=null;
		try {
			String sql = "SELECT * FROM Staff WHERE staff_id = " + staff_id;
			PreparedStatement pr = staffmanager.getConnection().prepareStatement(sql);
			ResultSet rs= pr.executeQuery();
			if (rs.next()) {
				String name = rs.getString("name");
				String field= rs.getString("field");
				Date dob = rs.getDate("dob");
				String address = rs.getString("address");
				int phone = rs.getInt("phone");
				String email = rs.getString("email");

				staff= new Staff(staff_id, name, field, dob, address, phone, email);
			}
			rs.close();
			pr.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return staff;
	}
	
	
	
	
	
	

}

