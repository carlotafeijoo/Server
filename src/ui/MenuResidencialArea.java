package ui;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import Interfaces.*;
import exceptions.InputException;
import jdbc.*;
import jpa.JPAUserManager;
import POJOS.*;
import ResidencialAreaXML.XMLManagerImpl;

public class MenuResidencialArea {

	private static BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
	
	private static ElderlyManager elderlyManager;
	
	private static FamilyContactManager familyContactManager;
	
	private static PerformsManager performsManager;
	
	private static StaffManager staffManager;
	
	private static UserManager userManager;
	
	private static TaskManager tasksManager;
	
	private static XMLManager xmlmanager;
	
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
		xmlmanager= new XMLManagerImpl();
		

		mainMenu();
	}
	
	public static void mainMenu() {
		try {
			
		
			int option;
			do {
				System.out.println("MAIN MENU ");
				System.out.println("1. Elderly management ");
				System.out.println("2. I am family contact ");
				System.out.println("3. I am staff member  ");
				System.out.println("4. Exit ");
				option = InputException.getInt("Introduce the number choice:  ");

				switch (option) {

				case 1:
					elderlyMenu();
					break;
					
				case 2:
					loginFC();
					
					break;
					
				case 3:
					loginStaff();
					break;
					
				case 4:
					
					//JDBCManager.disconnect();
					System.exit(4);

				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private static void loginFC() throws Exception {
		

		System.out.println("1. Register");
		System.out.println("2. Log in ");
		System.out.println("3. Exit");
		int choice = InputException.getInt(" Introduce the number of your choice: ");
	
		switch (choice) {
			case 1:
				// Call method REGISTER
				registerFamilyContact();
				loginFC();
	
				break;
			case 2:
				// LOG IN as family contact
				logIn();
	
				break;
	
			case 3:
				// EXIT
				mainMenu();
			default:
				break;
		}
		
	}

	private static void loginStaff() throws Exception {
	

		System.out.println("1. Register");
		System.out.println("2. Log in ");
		System.out.println("3. Exit");
		int choice = InputException.getInt(" Introduce the number of your choice: ");
	
		switch (choice) {
			case 1:
				// Call method REGISTER
				registerStaff();
	
				break;
			case 2:
				// LOG IN as staff member
				logIn();
	
				break;
	
			case 3:
				// EXIT
				mainMenu();
			default:
				break;
		}
	
	}



	public static void registerFamilyContact() throws Exception {
	
		System.out.println("Input information: ");
	
		String name = InputException.getString("Name: ");
		String email = InputException.getString("Email: ");
		Integer phone = InputException.getInt("Phone: ");
		String address = InputException.getString("Address: ");
		
		String username = InputException.getString("Username: ");
		String password = InputException.getString("Password: ");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();
	
	
		// CREATE FC AND ADD TO JPA
		User u = new User(username, digest);
		Role role = userManager.getRole("FamilyContact");
		u.setRole(role);
		role.addUser(u);
		userManager.newUser(u);
	
		// CREATE FAMILY CONTACT AND ADD TO JDBD
		FamilyContact fc = new FamilyContact(name, phone, email, address);
	
		familyContactManager.addFamilyContact(fc);
		System.out.println("REGISTER SUCESSFULL!");
	}
	
	public static void registerStaff() throws Exception {
		
		System.out.println("Input information: ");
	
		String name = InputException.getString("Name: ");
		
		String field = null;
		int choice;
		do {

			System.out.println("1.Carer");
			System.out.println("2.Cleaner");
			System.out.println("3.Chef");
			System.out.println("4.Animator");

			choice = InputException.getInt(" Chose field: ");
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
		
		String address = InputException.getString("Adress: ");
		Integer phone = InputException.getInt("Phone: ");
		System.out.println("Type the date of birth:");
		String dob_str = read.readLine();
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date dob = (Date) df.parse(dob_str);
		
		
		String email = InputException.getString("Email: ");
		String password = InputException.getString("Password: ");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();
	
		// CREATE STAFF AND ADD TO JPA
		User u = new User(email, digest);
		Role role = userManager.getRole("staff");
		u.setRole(role);
		role.addUser(u);
		userManager.newUser(u);
	
		// CREATE STAFF AND ADD TO JDBD
		Staff staff = new Staff(name, phone,dob, address, field);

		
		staffManager.addStaffMember(staff);
		
	}

	public static void logIn() throws Exception {
	
		System.out.println("Email :");
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
		
		//depending on the type of user we open a different menu
		if(u!= null && u.getRole().getName().equals("staff")) {
			Integer id=u.getId();
			
			int staff_id = staffManager.searchStaffIdfromUId(id);
			Staff staff = staffManager.searchStaffbyId(staff_id);
			System.out.println(staff);
			System.out.println("Logic successful!");
			StaffMenu(u.getId());
			
		}
		if (u != null && u.getRole().getName().equals("FamilyContact")) {
			Integer id = u.getId();
			
			int familycontact_id = familyContactManager.searchFamilycontacIdfromUId(id);
			System.out.println(familycontact_id);
			FamilyContact fc = familyContactManager.searchFamilyContactbyId(familycontact_id);
			System.out.println(fc);
			System.out.println("Logic successful!");
			familyContactMenu(u.getId());
	
		}
	}


	
	private static void familyContactMenu(int User_id) {
		
		try {
			int choice;
			do {
				System.out.println("1. Update your information. ");
				System.out.println("2. Exit");
				choice = InputException.getInt("Introduce your choice: ");

				switch (choice) {

				case 1:
					
					int family_id = familyContactManager.searchFamilycontacIdfromUId(User_id);
					System.out.println(family_id);
					FamilyContact familyToUpdate = familyContactManager.searchFamilyContactbyId(family_id);
					System.out.println(familyToUpdate);
					if (familyToUpdate != null) {
					    int newPhone = InputException.getInt("Enter your new phone number: ");
					    familyToUpdate.setPhone(newPhone);
					    String newAddress = InputException.getString("Enter your new address: ");
					    familyToUpdate.setAddress(newAddress);
					    familyContactManager.updateFamilyContactInfo(familyToUpdate);
					    System.out.println("Information updated successfully.");
					} else {
					    System.out.println("Family contact");
					}
				    break;
					
				case 2:
					mainMenu();
				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	private static void elderlyMenu() {
		
		try {
			int choice;
			do {
				
				System.out.println("1. Add an elderly.  ");
				System.out.println("2. Update the information of an elderly. ");
				System.out.println("3. Get the list of all elderlies. ");
				System.out.println("4. Load new elderlies. ");
				System.out.println("5. Print me");
				System.out.println("6. Back");
				

				choice = InputException.getInt("Introduce your choice: ");

				switch (choice) {

				case 1:
					
					addElderly();
					break;
					
				case 2:
					int elderly_id = InputException.getInt("Introduce the id of the elderly that is going to be updated:");
					Elderly elderlyToUpdate = elderlyManager.searchElderlyById(elderly_id);
					System.out.println(elderlyToUpdate);
					if (elderlyToUpdate != null) {
					    int newAge = InputException.getInt("Enter the new age for the elderly: ");
					    elderlyToUpdate.setAge(newAge);
					    elderlyManager.updateInfo(elderlyToUpdate);
					    System.out.println("Elderly information updated successfully.");
					} else {
					    System.out.println("Elderly not found with the provided id.");
					}
				    break;
				case 3:
					
					getListOfElderlies();
					break;	
					
				case 4: 
					loadElderly(); //xml
				
				case 5:
					printMe(); //xml
				case 6:
					mainMenu();
				default:
					break;
					
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static void loadElderly() {
		File file = new File("./xmls/External-Elderly.xml");
		System.out.println(xmlmanager.xml2Elderly(file));
		
	}

	private static void printMe() {
		int id=InputException.getInt("Introduce the id of the elderly wanted to be printed:" );
		xmlmanager.staff2xml(id);
		
	}

	public static void addElderly() throws Exception {

		System.out.println("Input the information of the new elderly: ");
 
		String name = InputException.getString(" Name: ");
		
		int age=InputException.getInt("Age:  ");

		Elderly elderly = new Elderly(name, age);
 
		
		elderlyManager.addElderly(elderly);
		elderlyMenu();

	}
	
	

	
	private static void getListOfElderlies() throws IOException, Exception {
		
		System.out.println("The list of elderlies is: ");
		List<Elderly> resultado = elderlyManager.getListOfElderly();
		System.out.println(resultado);
		elderlyMenu();
		
	}
	
	
    private static void StaffMenu( Integer id) {
    
		
		try {
			
			
			int choice;
			do {
			
				System.out.println("1.List all the elderlies associated to the staff member . ");
				System.out.println("2.List all the task associated to a staff member . ");
				System.out.println("3.Update the information of a staff member. ");
				System.out.println("4.Print me.  ");
				System.out.println("5.Load new staff members. ");
				System.out.println("6.Back.  ");
			

				choice = InputException.getInt("Introduce your choice: ");

				switch (choice) {

				
				case 1:
					
					getlistOfEderliesFromStaff(id);
					break;
				case 2:
					getlistOfTasksFromStaff(id);
					break;
					
				case 3:
					updateInfoStaff(id);
					break;
				case 4: 
					printMe();
				break;
				case 5:
					loadStaff();
					
				case 6:
					mainMenu();
					
				default:
					break;
					
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    private static void loadStaff() {
    	File file = new File("./xmls/External-Staff.xml");
		System.out.println(xmlmanager.xml2Staff(file));
		
	}

	
    
    private static void getlistOfEderliesFromStaff(int id) throws IOException {
		
    	
		System.out.println("The list of elderlies associated to this staff member is: ");
		elderlyManager.getListOfElderliesFromStaff(id);
		StaffMenu(id);
		
	}
    private static void getlistOfTasksFromStaff(int id) throws IOException {
    	System.out.println("The list of tasks associated to this staff member is: ");
    	
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
		StaffMenu(id);
	}
    
    
	
	


	
	
}
