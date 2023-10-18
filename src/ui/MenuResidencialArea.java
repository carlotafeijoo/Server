package ui;

import java.io.*;


import java.security.MessageDigest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Interfaces.*;
import exceptions.InputException;
import jdbc.*;
import jpa.JPAUserManager;
import POJOS.*;

public class MenuResidencialArea {

	private static BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

	private static ElderlyManager elderlyManager;

	private static DoctorManager DoctorManager;

	private static UserManager userManager;

	private static TaskManager tasksManager;
	public static void main(String[] args) {

		System.out.println("WELCOME TO THE RESIDENCIAL AREA DATA BASE");
		JDBCManager jdbcManager = new JDBCManager();

		// initialize database JDBC
		elderlyManager = new JDBCElderlyManager(jdbcManager);
		DoctorManager = new JDBCDoctorManager(jdbcManager);
		tasksManager = new JDBCTasksManager(jdbcManager);
		// initialize database JPA
		userManager = new JPAUserManager();
		mainMenu();

	}

	public static void mainMenu() {
		try {

			int option;
			do {
				System.out.println("MAIN MENU ");
				System.out.println("1. I am an elderly ");
				System.out.println("2. I am a doctor  ");
				System.out.println("3. Exit ");
				option = InputException.getInt("Introduce the number choice:  ");

				switch (option) {

				case 1:
					loginElderly();
					break;

				case 2:
					logindoctor();
					break;

				case 3:
					System.out.println("YOU HAVE EXIT THE RESIDENCIAL AREA DATA BASE");
					System.exit(3);
					break;

				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private static void elderlyMenu(int User_id) {

		try {
			int choice;
			do {

				System.out.println("1. Record signal.  ");
				System.out.println("2. See my tasks");
				System.out.println("3. Back");

				choice = InputException.getInt("Introduce your choice: ");

				switch (choice) {

				case 1:
					//AQUI: FUNCION DE LLAMAR AL BITALINO
					break;
				case 2:
					int elderly_id= elderlyManager.searchElderlyIdfromUId(User_id);
                 List <Task> tasks=elderlyManager.seeTasks(elderly_id);
                 for (int i=0;i<tasks.size();i++) {
                	 System.out.println(tasks.get(i).toStringtoElderly());
                 }
					break;				
					
				case 3:
						mainMenu();
						break;

				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void registerElderly() throws Exception {

		System.out.println("Input the information of the new elderly: ");

		String name = InputException.getString(" Name: ");

		int day = InputException.getInt("Day of birth:  ");
		int month = InputException.getInt("Month of birth:  ");
		int year = InputException.getInt("Year of birth:  ");
		int dni = InputException.getInt("DNI without letter:  ");

		ArrayList<Doctor> doctores = DoctorManager.searchAllDoctors();

		for (int i = 0; i < doctores.size(); i++) {
			System.out.println(doctores.get(i).toStringForPatients());
		}
		int doctor_id = InputException.getInt("put the id of your doctor:  ");

		System.out.println(" info: your username will be your dni ");
		Date dob = new Date(year, month, day);

		String username = "" + dni;
		String password = InputException.getString("Password: ");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();

		User u = new User(username, digest);
		Role role = userManager.getRole("Elderly");
		u.setRole(role);
		role.addUser(u);
		userManager.newUser(u);

		Elderly elderly = new Elderly(name, dni, dob, doctor_id);

		elderlyManager.addElderly(elderly);
		elderlyMenu(u.getId());

	}



	public static void logIn() throws Exception {

		System.out.println("Username or dni without letter:");
		String username = read.readLine();
		String password = InputException.getString("Password: ");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();

		User u = userManager.checkPassword(username, digest);

		if (u == null) {
			System.out.println("User not found");
			mainMenu();
		}

		// depending on the type of user we open a different menu
		if (u != null && u.getRole().getName().equals("Doctor")) {
			Integer id = u.getId();

			int doctor_id = DoctorManager.searchDoctorIdfromUId(id);
			Doctor doctor = DoctorManager.searchDoctorbyId(doctor_id);
			System.out.println(doctor);
			System.out.println("Login successful!");
			doctorMenu(u.getId());

		}

		if (u != null && u.getRole().getName().equals("Elderly")) {
			Integer id = u.getId();

			int elderly_id = elderlyManager.searchElderlyIdfromUId(id);

			Elderly elderly = elderlyManager.searchElderlyById(elderly_id);

			System.out.println(elderly);
			System.out.println("Login successful!");
			elderlyMenu(u.getId());

		}

	}

	private static void doctorMenu(int User_id) {
		try {

			int choice;
			do {
				System.out.println("1.Update information. ");
				System.out.println("2.Register new task. ");
				System.out.println("3.List all the tasks. ");
				System.out.println("4.Back.  ");

				choice = InputException.getInt("Introduce your choice: ");

				switch (choice) {

				case 1:
					int doctor_id = DoctorManager.searchDoctorIdfromUId(User_id);
					Doctor doctorToUpdate = DoctorManager.searchDoctorbyId(doctor_id);
					if (doctorToUpdate != null) {
						int newPhone = InputException.getInt("Enter your new phone number: ");
						doctorToUpdate.setPhone(newPhone);
						String newAddress = InputException.getString("Enter your new address: ");
						doctorToUpdate.setAddress(newAddress);
						DoctorManager.updateDoctorMemberInfo(doctorToUpdate);
						System.out.println("Information updated successfully! ");
					} else {
						System.out.println("doctor update fail.");
					}
					break;

				case 2:
					int doctorToAssignNewTask_id = DoctorManager.searchDoctorIdfromUId(User_id);
					addTask(doctorToAssignNewTask_id);
					System.out.println("Task added sucessfully!");
					break;

				case 3:
					int doctorAllTask_id = DoctorManager.searchDoctorIdfromUId(User_id);
					List<Task> tasksList = tasksManager.getListOfTasks(doctorAllTask_id);
					System.out.println("List of tasks: " + tasksList);
					break;

				case 4:
					mainMenu();
					break;

				default:
					break;

				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addTask(int doctorToAssignNewTask_id) throws Exception {

		System.out.println("Input the information of the new task: ");

		String description = InputException.getString("Description: ");

		// print all elderlies of this doctor
		List<Elderly> elderlies = elderlyManager.getListOfElderlyByDoctorID(doctorToAssignNewTask_id);
		for (int i = 0; i < elderlies.size(); i++) {
			System.out.println(elderlies.get(i).toString() + "\n");
		}
		int elderly_id = InputException.getInt("Elderly id: ");
		int duration = InputException.getInt("Duration: ");
		Task task = new Task(description, doctorToAssignNewTask_id, duration, elderly_id);
		tasksManager.addTask(task);
	}

	private static void loginElderly() throws Exception {
		System.out.println("1. Register");
		System.out.println("2. Log in ");
		System.out.println("3. Exit");
		int choice = InputException.getInt(" Introduce the number of your choice: ");

		switch (choice) {
		case 1:
			// Call method REGISTER
			registerElderly();
			break;

		case 2:
			// LOG IN as doctor member
			logIn();
			break;

		case 3:
			// EXIT
			mainMenu();
			break;

		default:
			break;
		}
	}

	private static void logindoctor() throws Exception {

		System.out.println("1. Register");
		System.out.println("2. Log in ");
		System.out.println("3. Exit");
		int choice = InputException.getInt(" Introduce the number of your choice: ");

		switch (choice) {
		case 1:
			// Call method REGISTER
			registerdoctor();
			logindoctor();
			break;

		case 2:
			// LOG IN as doctor member
			logIn();
			break;

		case 3:
			// EXIT
			mainMenu();
			break;

		default:
			break;
		}

	}

	public static void registerdoctor() throws Exception {

		System.out.println("Input information: ");

		String name = InputException.getString("Name: ");

		String field = null;
		int choice;
		do {

			System.out.println("1.Carer");
			System.out.println("2.Cleaner");
			System.out.println("3.Chef");
			System.out.println("4.Animator");

			choice = InputException.getInt("Chose field: ");
			switch (choice) {

			case 1:
				field = "Carer";
				break;

			case 2:
				field = "Cleaner";
				break;

			case 3:
				field = "Chef";
				break;

			case 4:
				field = "Animator";
				break;

			default:
				break;
			}
		} while (choice < 1 || choice > 4);

		String address = InputException.getString("Address: ");
		String email = InputException.getString("Email: ");
		System.out.println("Your email adress will be used as your username");
		Integer phone = InputException.getInt("Phone: ");
		System.out.println("Enter the year of birth:");
		int year = Integer.parseInt(read.readLine());

		System.out.println("Enter the month of birth:");
		int month = Integer.parseInt(read.readLine());

		System.out.println("Enter the day of birth:");
		int day = Integer.parseInt(read.readLine());

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String dobStr = String.format("%04d/%02d/%02d", year, month, day);
		java.util.Date utilDate = dateFormat.parse(dobStr);
		java.sql.Date dob = new java.sql.Date(utilDate.getTime());

		String username = email;
		String password = InputException.getString("Password: ");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();

		// CREATE doctor AND ADD TO JPA
		User u = new User(username, digest);
		Role role = userManager.getRole("Doctor");
		u.setRole(role);
		role.addUser(u);
		userManager.newUser(u);

		// CREATE doctor AND ADD TO JDBD
		Doctor doctor = new Doctor(name, phone, dob, address, email);
		// doctor.setField(field);

		DoctorManager.addDoctorMember(doctor);
		System.out.println("Register sucessfull!");

	}

}
