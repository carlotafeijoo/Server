package ui;

import java.io.*;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import Interfaces.*;
import exceptions.InputException;
import jdbc.*;
import jpa.JPAUserManager;
import POJOS.*;

public class MenuResidencialArea {

	private static BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
	
	private static ElderlyManager elderlyManager;
	
	private static FamilyContactManager familyContactManager;
	
	private static PerformsManager performsManager;
	
	private static StaffManager staffManager;
	
	private static UserManager userManager;
	
	private static TaskManager tasksManager;
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private static DateTimeFormatter formattert = DateTimeFormatter.ofPattern("HH:mm");
	
	public static void main(String[] args) {

		System.out.println("WELCOME TO THE RESIDENCIAL AREA DATA BASE");
		JDBCManager jdbcManager = new JDBCManager();

		//initialize database JDBC
		elderlyManager = new JDBCElderlyManager(jdbcManager);
		familyContactManager = new JDBCFamilyContactManager(jdbcManager);
		performsManager = new JDBCPerformsManager(jdbcManager);
		staffManager = new JDBCStaffManager(jdbcManager);
		tasksManager = new JDBCTasksManager(jdbcManager);
		// initialize database JPA
		userManager = new JPAUserManager();
		

		mainMenu();
	}
	
	public static void mainMenu() {
		try {
			userManager.connect();
			
		userManager.disconnect();
		
			int option;
			do {
				System.out.println("MAIN MENU ");
				System.out.println("\n 1. Elderly ");
				System.out.println("2. Family Contact ");
				System.out.println("3. Staff ");
				System.out.println("4. Exit ");
				option = InputException.getInt("Introduce your choice:  ");

				switch (option) {

				case 1:
					ElderlyMenu();
					break;
					
				case 2:
					familyContactMenu();
					break;
					
				case 3:
					StaffMenu();
					break;
					
				case 4:

					JDBCManager.disconnect();
					System.exit(3);

				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static void familyContactMenu() {
		
		try {
			int choice;
			do {
				System.out.println("FAMILY CONTACT MENU: ");
				System.out.println("\n1. Add a new family contact. ");
				System.out.println("2. Update the family contact´s information. ");
				System.out.println("3. Back");
				choice = InputException.getInt("Introduce your choice: ");

				switch (choice) {

				case 1:
					addFamilyContact();
					break;
				case 2:
					int id = InputException.getInt("Introduce the id of your elderly: ");
					updateFamilyContactInfo(id);
					break;	
				case 3:
					mainMenu();
				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addFamilyContact() throws Exception {

		System.out.println("Input the family contact's information: ");

		String name = InputException.getString("\n Name: ");
		
		int phone=InputException.getInt("Phone:  ");
		
		String email = InputException.getString("Email: ");
		
		String address = InputException.getString("Address: ");
		
		int elderly_id=InputException.getInt("Elderly´s id : ");

		String password = InputException.getString("Password: ");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();

		FamilyContact familyContact = new FamilyContact(name, phone, email, address, elderly_id);

		// CREATE PATIENT AND ADD TO JPA
		User user = new User(email, digest);
		userManager.connect();

		Role role = userManager.getRole("familyContact");
		user.setRole(role);
		role.addUser(user);
		userManager.newUser(user);
		userManager.disconnect();

		FamilyContactManager.addFamilyContact(familyContact);

	}
	
	private static void updateFamilyContactInfo(int id) throws IOException {

		//Show the family contact information
		FamilyContact familyContact = FamilyContactManager.showFamilyContactInfo(id);
		System.out.println(familyContact.toString());
		
		//Search the family contact by its familyContact_id
		FamilyContact fc = familyContactManager.searchFamilyContactbyId(id);

		System.out.println("Update your information: ");
		// Ask for info, if empty keeps the one existing before
		System.out.println("Family contact new email: ");
		String email = read.readLine();
		if (!email.equals("")) {
			fc.setName(email);
		}
		
		System.out.println("New address: ");
		String address = read.readLine();
		if (!address.equals("")) {
			fc.setEmail(address);
		}
		
		System.out.println("New phone: ");
		String phoneString = read.readLine();
		int phone = Integer.parseInt(phoneString);
		if (phone!=0) {
			fc.setPhone(phone);
		}
		
		familyContactManager.updateFamilyContactInfo(fc);
		ElderlyMenu();
	}
	
	
	
	private static void ElderlyMenu() {
		
		try {
			int choice;
			do {
				System.out.println("ELDERLY MENU");
				System.out.println("\n1. Add an elderly to the data base.  ");
				System.out.println("2.Update the information of an elderly. ");
				System.out.println("3.Get the list of elderlies. ");
				System.out.println("4. Exit");

				choice = InputException.getInt("Introduce your choice: ");

				switch (choice) {

				case 1:
					addElderly();
					break;
				case 2:
					int id=InputException.getInt("Introduce the id of the elderly that is going to be updated:" );
					updateInfo(id);
					break;
				case 3:
					getListOfElderlies();
					break;	
				case 4:
					mainMenu();
				default:
					break;
					
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void addElderly() throws Exception {

		System.out.println("Input the information of the new elderly: ");

		String name = InputException.getString("\n Name: ");
		
		int age=InputException.getInt("Age:  ");

		String password = InputException.getString("Password: ");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();

		Elderly elderly = new Elderly(name, age);


		// Create elderly and add it to JPA  
		User user = new User(name, digest);
		userManager.connect();

		Role role = userManager.getRole("elderly");

		user.setRole(role);
		System.out.println("aaaaa"+user.toString());
		userManager.newUser(user);
		userManager.disconnect();
		elderlyManager.addElderly(elderly);
		ElderlyMenu();

	}
	
	private static void updateInfo(int id) throws IOException {

		//Show the elderly´s information
		Elderly elderly = ElderlyManager.showElderlyInfo(id);
		System.out.println(elderly.toString());
		
		//Search the elderly by its id
		Elderly e = elderlyManager.searchElderlyById(id);

		System.out.println("Update your information: ");
		// Ask for info, if empty keeps the one existing before
		System.out.println("Elderly new age: ");
		String ageString = read.readLine();
		int age = Integer.parseInt(ageString);
		if (age!=0) {
			e.setAge(age);
		}
		
		elderlyManager.updateInfo(e);
		ElderlyMenu();
	}
	
	private static void getListOfElderlies() throws IOException {
		
		System.out.println("The list of elderlies is: ");
		elderlyManager.getListOfElderlies();
		ElderlyMenu();
		
	}
	
	
    private static void StaffMenu() {
		
		try {
			int choice;
			do {
				System.out.println("STAFF MENU");
				System.out.println("\n1. Add a staff member.  ");
				System.out.println("2.List all the staff members. ");
				System.out.println("3.Update the information of a staff member. ");
				System.out.println("4.Exit  ");
			

				choice = InputException.getInt("Introduce your choice: ");

				switch (choice) {

				case 1:
					addStaff();
					break;
					
				case 2:
					getlistOfStaff();
					break;
					
				case 3:
					int id=InputException.getInt("Introduce the id of the staff member that is going to be updated:" );
					updateInfoStaff(id);
					break;
					
				case 4:
					mainMenu();
					
				default:
					break;
					
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public static void addStaff() throws Exception {

		System.out.println("Input the information of the new staff member: ");

		String name = InputException.getString("\n Name: ");
		
		int phone=InputException.getInt("Phone:  ");
		
		String field=InputException.getString("Field: ");
		
		System.out.println("Date of birth (yyyy-mm-dd): ");
		String dob = read.readLine();
		LocalDate dobDate = LocalDate.parse(dob, formatter);
		
		String address= InputException.getString("Address: ");
		
		List<Elderly> elderlies = elderlyManager.getListOfElderlies();
		

		String password = InputException.getString("Password: ");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();

		Staff staff = new Staff(name, phone, field, dobDate, address, elderlies);
		// Create elderly and add it to JPA  
		User user = new User(name, digest);
		userManager.connect();

		Role role = userManager.getRole("staff");

		user.setRole(role);
		role.addUser(user);
		userManager.newUser(user);
		userManager.disconnect();

		staffManager.addStaffMember(staff);
		ElderlyMenu();

	}
    
    private static void getlistOfStaff() throws IOException {
		
		System.out.println("The list of staff member is: ");
		staffManager.listStaffMembers();
		StaffMenu();
		
	}
    
    
    private static void updateInfoStaff(int id) throws IOException {

		//Show the staff´s information
		Staff staffmember = StaffManager.showStaffInfo(id);
		System.out.println(staffmember.toString());
		
		//Search the staff member by its id
		Staff s = StaffManager.searchStaffmemberByID(id);

		System.out.println("Update your information: ");
		// Ask for info, if empty keeps the one existing before
		System.out.println("Staff new phone: ");
		String phoneString = read.readLine();
		int phone = Integer.parseInt(phoneString);
		if (phone!=0) {
			s.setPhone(phone);
		}
		System.out.println("Staff new address: ");
		String address=read.readLine();
		if(!address.equals(" ")) {
			s.setAddress(address);
		}
		
		staffManager.updateStaffMemberInfo(s);
		StaffMenu();
	}
    
    
	
	


	
	
}
