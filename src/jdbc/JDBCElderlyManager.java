package jdbc;

import POJOS.Elderly;

import java.sql.PreparedStatement;
import java.util.List;


import Interfaces.ElderlyManager;

public class JDBCElderlyManager implements ElderlyManager{
	private JDBCManager manager;
	
	public JDBCElderlyManager(JDBCManager m)
	{
		this.manager = m;
	}
	

	@Override
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
		
	

	@Override
	public List<Elderly> getListOfElderlies() {
		// TODO Auto-generated method stub
		return null;
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


	@Override
	public void deleteElderly(Elderly e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int counElderlies(List<Elderly> elderlies) {
		// TODO Auto-generated method stub
		return 0;
	}
		
	}

	
	

	
	
	
