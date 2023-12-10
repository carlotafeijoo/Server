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
	/**
	 * Constructs a new JDBCElderlyManager with the specified JDBCManager.
	 * 
	 * @param m the JDBCManager to be used
	 */
	public JDBCElderlyManager(JDBCManager m) {
		this.manager = m;
	}
	/**
	 * Adds a new elderly person to the database.
	 * 
	 * @param e the Elderly object representing the elderly person to be added
	 */
	@Override
	public void addElderly(Elderly e) {
		try {
			String sql = "INSERT INTO Elderly (name, dob, DNI, doctor_id, symptoms) VALUES (?,?,?,?,?)";
			// use preparedStmt so nothing damages the database
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);

			prep.setString(1, e.getName());
			java.sql.Date sqlDate = new java.sql.Date(e.getDob().getTime());
			prep.setDate(2, sqlDate);
			prep.setInt(3, e.getDni());
			prep.setInt(4, e.getDoctor_id());
			prep.setString(5, e.getSymptoms());

			//System.out.println("mi id of the doctor" + e.getDoctor_id());
			prep.executeUpdate();
			prep.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	/**
	 * Adds symptoms to an elderly person's record.
	 * 
	 * @param eld_id the ID of the elderly person
	 * @param symp the symptoms to be added
	 */
	@Override
	public void addSymptoms(int eld_id, String symp) {

		try {
			String sql = "UPDATE Elderly SET symptoms=? WHERE elderly_id=?";
			//set @sql_text = concat('insert into tblEvents (AccountId, EventTableId, EventTable, EventBasic, EventFull, EventDate, UserId) values(?, ?, ?, ?, ?, ?,?)');
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);

			prep.setString(1, symp);
			prep.setInt(2, eld_id);

			prep.executeUpdate();
			prep.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Retrieves an elderly person by their ID.
	 * 
	 * @param id the ID of the elderly person
	 * @return the Elderly object
	 * @throws Exception if an error occurs during the search
	 */

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
				Integer doctor_id = rs.getInt("doctor_id");
				String symptoms = rs.getString("symptoms");
				elderly = new Elderly(elderly_id, name, dni, doctor_id ,dob, symptoms);
			}

			rs.close();
			pr.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return elderly;
	}
	/**
	 * Retrieves a list of elderly persons based on the doctor's ID.
	 * 
	 * @param doctor_id the ID of the doctor
	 * @return a list of Elderly objects
	 */

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
				Integer doct_id = rs.getInt("doctor_id");
				String symptoms = rs.getString("symptoms");
				Elderly elderly = new Elderly(id, name, dni, doct_id, dob, symptoms);
				elderlies.add(elderly);
			}
			rs.close();
			pr.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return elderlies;
	}


	/**
	 * Retrieves the ID of an elderly person based on the user ID for login.
	 * 
	 * @param User_id the user ID
	 * @return the ID of the elderly person
	 */
	@Override
	// get Doctor_id from user id -> for login
	public int searchElderlyIdfromUId(int User_id) {
		int elderly_id = 0;
		try {
			Statement stmt = manager.getConnection().createStatement();
			String sql = "SELECT Elderly.elderly_id FROM Elderly JOIN User ON Elderly.DNI=User.username WHERE User.id= "
					+ User_id;
			ResultSet rs = stmt.executeQuery(sql);

			elderly_id = rs.getInt("elderly_id");

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return elderly_id;

	}
	/**
	 * Updates the information of an elderly person in the database.
	 * 
	 * @param elderly the Elderly object representing the updated elderly person information
	 */
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
	/**
	 * Retrieves the symptoms of an elderly person.
	 * 
	 * @param eld_id the ID of the elderly person
	 * @return a string representing the symptoms
	 */
	@Override
	public String seeSymptoms(int eld_id) {

		String symptoms = null;

		try {
			String sql = "SELECT symptoms FROM Elderly WHERE elderly_id=?";
			PreparedStatement prep = manager.getConnection().prepareStatement(sql);

			PreparedStatement pr = manager.getConnection().prepareStatement(sql);
			pr.setInt(1, eld_id);
			ResultSet rs = pr.executeQuery();

			while(rs.next()) {
				symptoms = rs.getString("symptoms");
			}

			rs.close();
			pr.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return symptoms;

	}
	/**
	 * Retrieves a list of tasks assigned to a specific elderly person.
	 * 
	 * @param user_id the ID of the elderly person
	 * @return a list of Task objects
	 */
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
				int doctor_id = rs.getInt("doctor_id");
				int task_id= rs.getInt("task_id");
				int elderly_id = rs.getInt("elderly_id");
				Task t = new Task(task_id, description, doctor_id, duration,elderly_id);
				tasks.add(t);
			}
			rs.close();
			pr.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	/**
	 * Retrieves a list of tasks and their IDs assigned to a specific elderly person.
	 * 
	 * @param user_id the ID of the elderly person
	 * @return a list of Task objects
	 */
	@Override
	public List<Task> seeTaskANDidbyElderly(int user_id){

		List<Task> tasks = new ArrayList<>();
		try {
			String sql = "SELECT * FROM Task WHERE elderly_id = ?";
			PreparedStatement pr = manager.getConnection().prepareStatement(sql);
			pr.setInt(1, user_id);

			ResultSet rs = pr.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("task_id");
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
	/**
	 * Checks if a DNI (username) is already used by an elderly person.
	 * 
	 * @param username the DNI (username) to be checked
	 * @return true if the DNI is already used, otherwise false
	 */
	public boolean checkAlreadyUsedDNI(int username) {
		boolean result = false;
		try {
			String sql = "SELECT * FROM  Elderly WHERE DNI= ?";
			PreparedStatement pr = manager.getConnection().prepareStatement(sql);
			pr.setInt(1, username);
			ResultSet rs = pr.executeQuery();
			int registered = rs.getInt("DNI");

			if (registered == username) {

				result = true;
				pr.close();
				rs.close();
			} else {
				result = false;
				pr.close();
				rs.close();
			}

		} catch (Exception e) {
			System.out.println("Validated username");
		}

		return result;
	}


}



