package Interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import POJOS.*;

public interface DoctorManager {

	public void addDoctorMember(Doctor Doctormember) throws SQLException;

	public int searchDoctorIdfromUId(int id);

	public Doctor searchDoctorbyId(int id);

	public void updateDoctorMemberInfo(Doctor Doctormember);

	public ArrayList<Doctor> searchAllDoctors();

}
