package jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Interfaces.FamilyContactManager;
import POJOS.*;

public class JDBCFamilyContactManager implements FamilyContactManager{
	
	private JDBCManager familyContactManager;
	


	public JDBCFamilyContactManager(JDBCManager jdbcManager) {
		this.familyContactManager=jdbcManager;
	}

	//add a family contact 
	public void addFamilyContact (FamilyContact familycontact) throws SQLException {
		try {
			String sql="INSERT INTO FamilyContact (name, address, phone, email) VALUES (?,?,?,?) ";
			PreparedStatement prep= familyContactManager.getConnection().prepareStatement(sql);
			prep.setString(1, familycontact.getName());
			prep.setString(2, familycontact.getAddress());
			prep.setInt(3, familycontact.getPhone());
			prep.setString(4, familycontact.getEmail());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	//update the info of a family contact
	public void updateFamilyContactInfo(FamilyContact familycontact) {
	    try {
	        String sql = "UPDATE FamilyContact SET phone = ?, address = ?  WHERE family_id = ?";
	        PreparedStatement pr = familyContactManager.getConnection().prepareStatement(sql);;
	        
	        
	        pr.setInt(1, familycontact.getPhone());
	       
	        pr.setString(2, familycontact.getAddress());
	        
	        pr.setInt(3, familycontact.getFamily_id());
	        pr.executeUpdate();
	        pr.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	
	
	@Override
	public FamilyContact searchFamilyContactbyId(int family_id) {
	    FamilyContact fc = null;
	    try {
	        String sql = "SELECT * FROM FamilyContact WHERE family_id = ?";
	        PreparedStatement pr = familyContactManager.getConnection().prepareStatement(sql);
	        pr.setInt(1, family_id);
	        ResultSet rs = pr.executeQuery();
	        
	        if (rs.next()) {
	            String name = rs.getString("name");
	            String email = rs.getString("email");
	            String address = rs.getString("address");
	            int phone = rs.getInt("phone");
	            
	            fc = new FamilyContact(family_id, name, email, phone, address);
	        }
	        
	        rs.close();
	        pr.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return fc;
	}



	
	
	
	@Override
	public int searchFamilycontacIdfromUId(int User_id) {
	    int family_id = 0;
	    try {
	        Statement stmt = familyContactManager.getConnection().createStatement();
	        String sql = "SELECT FamilyContact.family_id FROM FamilyContact JOIN User ON FamilyContact.email = User.username WHERE User.id = " + User_id;
	        ResultSet rs = stmt.executeQuery(sql);

	        if (rs.next()) {
	            family_id = rs.getInt("family_id");
	        }

	        rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return family_id;
	}


	@Override
	public FamilyContact showFamilyContactInfo(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	
	
	
	
	
}
