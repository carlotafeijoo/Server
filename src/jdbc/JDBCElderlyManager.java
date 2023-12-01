package jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Interfaces.ElderlyManager;
import POJOS.Elderly;
import POJOS.Task;

public class JDBCElderlyManager implements ElderlyManager {
	private JDBCManager manager;

	public JDBCElderlyManager(JDBCManager m) {
		this.manager = m;
	}

	@Override
	public void addElderly(Elderly e) {
		try {
			String sql = "INSERT INTO Elderly (name, dob,DNI,doctor_id) VALUES (?,?,?,?)";
			// use preparedStmt so nothing damages the database
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setString(1, e.getName());
			java.sql.Date sqlDate = new java.sql.Date(e.getDob().getTime());

			prep.setDate(2, sqlDate);
			prep.setInt(3, e.getDni());
			prep.setInt(4, e.getDoctor_id());
			//System.out.println("mi id of the doctor" + e.getDoctor_id());
			prep.executeUpdate();
			prep.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
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
				Date dob = rs.getDate("dob");
				Integer dni = rs.getInt("DNI");
				elderly = new Elderly(id, name, dni, dob);
			}

			rs.close();
			pr.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return elderly;
	}

	@Override
	public List<Elderly> getListOfElderly() {
		List<Elderly> elderlies = new ArrayList<>();
		try {
			String sql = "SELECT * FROM Elderly ";
			PreparedStatement pr = manager.getConnection().prepareStatement(sql);

			ResultSet rs = pr.executeQuery();

			while (rs.next()) {
				Integer id = rs.getInt("elderly_id");
				String name = rs.getString("name");
				Date dob = rs.getDate("dob");
				Integer dni = rs.getInt("DNI");
				Elderly elderly = new Elderly(id, name, dni, dob);
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
	public List<Elderly> getListOfElderlyByDoctorID(int doctor_id) {
		List<Elderly> elderlies = new ArrayList<>();
		try {
			String sql = "SELECT * FROM Elderly WHERE doctor_id = ?";
			PreparedStatement pr = manager.getConnection().prepareStatement(sql);
			pr.setInt(1, doctor_id);

			ResultSet rs = pr.executeQuery();

			while (rs.next()) {
				Integer id = rs.getInt("elderly_id");
				String name = rs.getString("name");
				Date dob = rs.getDate("dob");
				Integer dni = rs.getInt("DNI");
				Elderly elderly = new Elderly(id, name, dni, dob);
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
	public void assign(int doctor_ID, int elderly_ID) {
		try {
			String sql = "INSERT INTO examines (doctorID,elderlyID) VALUES (?,?)";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);
			prep.setInt(1, doctor_ID);
			prep.setInt(2, elderly_ID);

			prep.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	// get Doctor_id from user id -> for login
	public int searchElderlyIdfromUId(int User_id) {
		int doctor_id = 0;
		try {
			Statement stmt = manager.getConnection().createStatement();
			String sql = "SELECT Elderly.elderly_id FROM Elderly JOIN User ON Elderly.DNI=User.username WHERE User.id= "
					+ User_id;
			ResultSet rs = stmt.executeQuery(sql);

			doctor_id = rs.getInt("elderly_id");

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return doctor_id;

	}

	@Override
	public void updateInfo(Elderly elderly) {
		try {
			String sql = "UPDATE Elderly SET dob=? WHERE elderly_id = ?;";
			PreparedStatement pr = manager.getConnection().prepareStatement(sql);

			pr.setDate(1, (Date) elderly.getDob());
			pr.setInt(2, elderly.getElderly_id());

			pr.executeUpdate();
			pr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Task> seeTasksbyElderly(int user_id) {
		List<Task> tasks = new ArrayList<>();
		try {
			String sql = "SELECT * FROM Task WHERE elderly_id = ?";
			PreparedStatement pr = manager.getConnection().prepareStatement(sql);
			pr.setInt(1, user_id);

			ResultSet rs = pr.executeQuery();

			while (rs.next()) {
				String description = rs.getString("description");
				int duration = rs.getInt("duration");
				Task t = new Task(description, duration);
				tasks.add(t);
			}
			rs.close();
			pr.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	@Override
	public List<Task> seeTaskANDidbyElderly(int user_id){
		
		List<Task> tasks = new ArrayList<>();
		try {
			String sql = "SELECT * FROM Task WHERE elderly_id = ?";
			PreparedStatement pr = manager.getConnection().prepareStatement(sql);
			pr.setInt(1, user_id);

			ResultSet rs = pr.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String description = rs.getString("description");
				int duration = rs.getInt("duration");
				Task t = new Task(id, description, duration);
				tasks.add(t);
			}
			rs.close();
			pr.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tasks;
		
		
	}
	
	
}
