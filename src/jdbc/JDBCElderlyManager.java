package jdbc;

import POJOS.Elderly;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import Interfaces.ElderlyManager;

public class JDBCElderlyManager implements ElderlyManager{
	private JDBCManager manager;
	
	public JDBCElderlyManager(JDBCManager m){
		this.manager = m;
	}
	
	public void addElderly(Elderly e) {
		try {
			String sql = "INSERT INTO Elderly (name, age) VALUES (?,?)";
			// use preparedStmt so nothing damages the database
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, e.getName());
			prep.setInt(2, e.getAge());
			prep.executeUpdate();
			prep.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
		
	public Elderly showElderlyInfo (int id) {
	    Elderly elderly = null;
	    try (java.sql.Statement statement = ((java.sql.Statement) elderly).getConnection().createStatement()) {
	        String sql = "SELECT * FROM elderly WHERE id = " + id;
	        ResultSet rs = statement.executeQuery(sql);
	        while (rs.next()) {
	            String name = rs.getString("name");
	            int phone = rs.getInt("phone");

	            elderly = new Elderly(id, name, phone);
	        }
	        rs.close();
	        statement.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return elderly;
	}
	
	
	@Override
	public Elderly searchElderlyById(int id) throws Exception {
	    Elderly elderly = null;
	    try {
	        String sql = "SELECT * FROM Elderly WHERE elderly_id = ?";
	        PreparedStatement pr = manager.getConnection().prepareStatement(sql);
	        pr.setInt(1, id);
	        ResultSet rs = pr.executeQuery();

	        while (rs.next()) {
	            Integer elderly_id = rs.getInt("elderly_id");
	            String name = rs.getString("name");
	            Integer age = rs.getInt("age");
	            elderly = new Elderly(elderly_id, name, age);
	        }

	        rs.close();
	        pr.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return elderly;
	}


	
	
	@Override
	public List<Elderly> getListOfElderliesFromStaff (int staff_id) {
	    List<Elderly> elderlies = new ArrayList<Elderly>();
	    try {
	    	String sql = "SELECT * FROM elderly WHERE staff_id = ?";
			PreparedStatement pr = manager.getConnection().prepareStatement(sql);
			pr.setInt(1,staff_id);
			ResultSet rs = pr.executeQuery();
		
	        while (rs.next()) {
	            Integer id = rs.getInt("id");
	            String name = rs.getString("name");
	            Integer age = rs.getInt("age");
	            Elderly elderly = new Elderly(id, name, age);
	            elderlies.add(elderly);
	        }
	        rs.close();
	        pr.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return elderlies;
	}
	
	
	@Override 
	public List<Elderly> getListOfElderly(){
		 List<Elderly> elderlies = new ArrayList<Elderly>();
		    try {
		    	String sql = "SELECT * FROM Elderly ";
				PreparedStatement pr = manager.getConnection().prepareStatement(sql);
				ResultSet rs = pr.executeQuery();
			
		        while (rs.next()) {
		            Integer id = rs.getInt("elderly_id");
		            String name = rs.getString("name");
		            Integer age = rs.getInt("age");
		            Elderly elderly = new Elderly(id, name, age);
		            elderlies.add(elderly);
		        }
		        rs.close();
		        pr.close();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return elderlies;
	}
	

	@Override
	public void assign(int staff_ID, int elderly_ID) {
		try{
			String sql = "INSERT INTO examines (staffID,elderlyID) VALUES (?,?)";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setInt(1, staff_ID);
			prep.setInt(2, elderly_ID);		
			
			prep.executeUpdate();			
					
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
		
	

	@Override
	public void updateInfo(Elderly elderly) {
	    try {
	        String sql = "UPDATE Elderly SET age=? WHERE elderly_id = ?;";
	        PreparedStatement pr = manager.getConnection().prepareStatement(sql);

	        pr.setInt(1, elderly.getAge());
	        pr.setInt(2, elderly.getElderly_id());

	        pr.executeUpdate();
	        pr.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	
}

	
	

	
	
	
