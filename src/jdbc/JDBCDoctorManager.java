package jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Interfaces.DoctorManager;
import POJOS.Doctor;

public class JDBCDoctorManager implements DoctorManager {

	private JDBCManager Doctormanager;

	public JDBCDoctorManager(JDBCManager jdbcManager) {
		this.Doctormanager = jdbcManager;
	}

	@Override
	public void addDoctorMember(Doctor Doctormember) throws SQLException {
		try {
			String sql = "INSERT INTO Doctor (name, dob, address, phone, email) VALUES (?,?,?,?,?)";
			PreparedStatement prep = Doctormanager.getConnection().prepareStatement(sql);
			prep.setString(1, Doctormember.getName());

			java.sql.Date sqlDate = new java.sql.Date(Doctormember.getDob().getTime());
			prep.setDate(2, sqlDate);
			prep.setString(3, Doctormember.getAddress());
			prep.setInt(4, Doctormember.getPhone());
			prep.setString(5, Doctormember.getEmail());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// update info from a Doctor member
	@Override
	public void updateDoctorMemberInfo(Doctor Doctormember) {
		try {
			String sql = "UPDATE Doctor SET phone = ?, address = ?, dob = ? WHERE doctor_id = ?";
			PreparedStatement pr = Doctormanager.getConnection().prepareStatement(sql);

			pr.setInt(1, Doctormember.getPhone());
			pr.setString(2, Doctormember.getAddress());
			java.sql.Date sqlDate = new java.sql.Date(Doctormember.getDob().getTime());
			pr.setDate(3, sqlDate);
			pr.setInt(4, Doctormember.getdoctor_id());
			pr.executeUpdate();
			pr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	// get Doctor_id from user id -> for login
	public int searchDoctorIdfromUId(int User_id) {
		int doctor_id = 0;
		try {
			Statement stmt = Doctormanager.getConnection().createStatement();
			String sql = "SELECT Doctor.doctor_id FROM Doctor JOIN User ON Doctor.email=User.username WHERE User.id= "
					+ User_id;
			ResultSet rs = stmt.executeQuery(sql);

			doctor_id = rs.getInt("doctor_id");

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return doctor_id;

	}

	@Override
	public Doctor searchDoctorbyId(int doctor_id) {
		Doctor Doctor = null;
		try {
			String sql = "SELECT * FROM Doctor WHERE doctor_id = " + doctor_id;
			PreparedStatement pr = Doctormanager.getConnection().prepareStatement(sql);
			ResultSet rs = pr.executeQuery();
			if (rs.next()) {

				String name = rs.getString("name");
				// String field= rs.getString("field");
				Date dob = rs.getDate("dob");
				String address = rs.getString("address");
				int phone = rs.getInt("phone");
				String email = rs.getString("email");

				Doctor = new Doctor(doctor_id, name, dob, address, phone, email);
			}
			rs.close();
			pr.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Doctor;
	}

	@Override
	public ArrayList<Doctor> searchAllDoctors() {
		ArrayList<Doctor> listaDoctores = new ArrayList<>();
		try {
			String sql = "SELECT * FROM Doctor ";
			PreparedStatement pr = Doctormanager.getConnection().prepareStatement(sql);
			ResultSet rs = pr.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("doctor_id");

				String name = rs.getString("name");
				// String field= rs.getString("field");
				Date dob = rs.getDate("dob");
				String address = rs.getString("address");
				int phone = rs.getInt("phone");
				String email = rs.getString("email");
				listaDoctores.add(new Doctor(id, name, dob, address, phone, email));

			}
			rs.close();
			pr.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listaDoctores;
	}
	public boolean checkAlreadyUsedDNIDoc(String username) {
		boolean result = false;
		try {
			String sql = "SELECT * FROM  Doctor WHERE email LIKE ?";
			PreparedStatement pr = Doctormanager.getConnection().prepareStatement(sql);
			pr.setString(1, "%" + username + "%");
			ResultSet rs = pr.executeQuery();
			String registered = rs.getString("email");
			if (registered.equalsIgnoreCase(username)) {

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
