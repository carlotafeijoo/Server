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
			String sql = "INSERT INTO elderly (name, id, age) VALUES (?,?,?)";
			// use preparedStmt so nothing damages the database
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, e.getName());
			prep.setInt(2, e.getElderly_id()); 
			prep.setInt(3, e.getAge());
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
	public Elderly searchElderlyById( int id) {
		Elderly elderly = null;
		try {
			java.sql.Statement stmt = ((java.sql.Statement) elderly).getConnection().createStatement();
			String sql = "SELECT name,age FROM elderly WHERE id = " + id;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("name");
				int age = rs.getInt("age");

				elderly = new Elderly(id,name, age);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return elderly;
	}
	
	
	@Override
	public List<Elderly> getListOfElderlies() {
	    List<Elderly> elderlies = new ArrayList<Elderly>();
	    elderlies=null;
	    try {
	        Connection connection = ((java.sql.Statement) elderlies).getConnection();
	        Statement stmt = (Statement) connection.createStatement();
	        String sql = "SELECT * FROM elderly";
	        ResultSet rs = ((java.sql.Statement) stmt).executeQuery(sql);
	        while (rs.next()) {
	            Integer id = rs.getInt("id");
	            String name = rs.getString("name");
	            Integer age = rs.getInt("age");
	            Elderly elderly = new Elderly(id, name, age);
	            elderlies.add(elderly);
	        }
	        rs.close();
	        ((Connection) stmt).close();
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
	public void updateInfo(Elderly e) {
		try {
			String sql = "UPDATE patient SET name = ?, age = ? WHERE id = ?";
			PreparedStatement pr = manager.getConnection().prepareStatement(sql);

			pr.setString(1, e.getName());
			pr.setInt(2, e.getAge());
			pr.setInt(3, e.getElderly_id());
			
			pr.executeUpdate();
			pr.close();
		} catch (Exception p) {
			p.printStackTrace();

		}
	}

	
}

	
	

	
	
	
