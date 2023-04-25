package jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import POJOS.*;

public class JDBCFamilyContactManager {
	
	private JDBCFamilyContactManager familyContactManager;
	private int getPhone;


	//add a family contact 
	public void addFamilyContact (FamilyContact familycontact) {
		try {
			String sql="INSERT INTO FamilyContact (name, address, phone, email) VALUES (?,?,?,?,?) ";
			PreparedStatement prep= ((java.sql.Statement) familyContactManager).getConnection().prepareStatement(sql);
			prep.setString(1, familyContactManager.getName());
			prep.setString(2, familyContactManager.getAddress());
			prep.setInt(3, familyContactManager.getPhone);
			prep.setDate(4, (Date) familyContactManager.getEmail());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//show info of a family contact before updating 
	public FamilyContact showFamilyContactInfo(int id) {
	    FamilyContact familycontact= null;
	    try {
	        java.sql.Statement statement = ((java.sql.Statement) familycontact).getConnection().createStatement();
	        String sql = "SELECT * FROM familyContact WHERE id = " + id;
	        ResultSet rs = statement.executeQuery(sql);
	        while (rs.next()) {
	            String name = rs.getString("name");
	            String email= rs.getString("email"); 
	            Date dob = rs.getDate("phone");
	            String address = rs.getString("address");
	            int elderly_id = rs.getInt("elderly_id"); 

	            familycontact = new FamilyContact(id, name, email, dob, address, elderly_id);
	        }
	        rs.close();
	        statement.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return familycontact;
	}
	
	//update the info of a family contact
	public void updateFamilyContactInfo(FamilyContact familycontact) {
	    try {
	        String sql = "UPDATE familycontact SET email = ?, phone = ?, address = ?  WHERE id = ?";
	        PreparedStatement pr = ((java.sql.Statement) familycontact).getConnection().prepareStatement(sql);
	        
	        if (familycontact.getEmail() != null) {
	            pr.setString(1, familycontact.getEmail());
	        }
	        if(familycontact.getPhone()!=0) {
	        	pr.setInt(2, familycontact.getPhone());
	        }
	        if (familycontact.getAddress() != null) {
	            pr.setString(3, familycontact.getAddress());
	        }
	        pr.setInt(4, familycontact.getFamily_id());
	        pr.executeUpdate();
	        pr.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	private String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	private Date getEmail() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
