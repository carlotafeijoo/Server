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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteElderly(Elderly d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateInfo(Elderly e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int counElderlies(List<Elderly> elderlies) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	

	
	
}
