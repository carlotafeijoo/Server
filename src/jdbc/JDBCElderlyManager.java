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
	public void addElderly(Elderly d) {
		try {
			String sql = "INSERT INTO elderly (name, age, staffmember) VALUES (?,?,?)";
			// use preparedStmt so nothing damages the database
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, p.getName());
			prep.setString(2, p.getMedstat());
			prep.setString(3, p.getEmail());
			prep.setDate(4, p.getDob());
			prep.setString(5, p.getSex());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	
	

	
	
}
