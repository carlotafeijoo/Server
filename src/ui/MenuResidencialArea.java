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
			userManager.connect();
			
		userManager.disconnect();
		
			int option;
			do {
				System.out.println("MAIN MENU ");
				System.out.println("\n 1. Elderlies info");
				System.out.println("2. Family Contacts info ");
				System.out.println("3. Staff info ");
				System.out.println("4. Exit ");
				option = InputException.getInt("Introduce your choice:  ");

				switch (option) {

				case 1:
					//ElderlyMenu(); ARREGLAR
					break;
					
				case 2:
					familyContactMenu();
					break;
					
				case 3:
					int id= InputException.getInt("Introduce the id of the elderly you want to modify or see");
					StaffMenu(id);
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

		FamilyContact familyContact = new FamilyContact(name, phone);

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
		ElderlyMenu(id);
	}
	
	
	
	private static void ElderlyMenu(Integer id) {
		
		try {
			int choice;
			do {
				System.out.println("ELDERLY MENU");
				System.out.println("\n1. Add an elderly to the data base.  ");
				System.out.println("2.Update the information of an elderly. ");
				System.out.println("3.Get the list of elderlies. ");
				System.out.println("4.Load new elderlies. ");
				System.out.println("5. Print me");
				System.out.println("6. Exit");
				

				choice = InputException.getInt("Introduce your choice: ");

				switch (choice) {

				case 1:
					addElderly();
					break;
				case 2:
					 id=InputException.getInt("Introduce the id of the elderly that is going to be updated:" );
					Elderly elderlyToUpdate=elderlyManager.searchElderlyById(id);
					if(elderlyToUpdate!=null) {
						updateInfo(id);
					} else {
						System.out.println("Elderly not found with the provided id.");
					}
					updateInfo(id);
					break;
				case 3:
					getListOfElderlies(id);
					break;	
				case 4: 
					loadElderly(); //xml
				
				case 5:
					printMe(id); //xml
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

	private static void printMe(Integer id) {
		xmlmanager.staff2xml(id);
		
	}

	public static void addElderly() throws Exception {

		System.out.println("Input the information of the new elderly: ");
		int id=InputException.getInt("\n ID: ");
 
		String name = InputException.getString("\n Name: ");
		
		int age=InputException.getInt("Age:  ");

		String password = InputException.getString("Password: ");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();

		Elderly elderly = new Elderly(name, age);
 
		User user = new User(name, digest);
		userManager.connect();

		Role role = userManager.getRole("elderly");

		user.setRole(role);
		System.out.println("aaaaa"+user.toString());
		userManager.newUser(user);
		userManager.disconnect();
		elderlyManager.addElderly(elderly);
		ElderlyMenu(id);

	}
	
	private static void updateInfo(int id) throws IOException {
	    try {
	        // Show the elderly's information
	        Elderly elderly = ElderlyManager.showElderlyInfo(id);
	        System.out.println(elderly.toString());

	        // Search the elderly by its id
	        Elderly e = elderlyManager.searchElderlyById(id);

	        if (e != null) {
	            System.out.println("Update your information:");
	            // Ask for info, if empty keeps the one existing before
	            System.out.println("Elderly new age:");
	            String ageString = read.readLine();
	            int age = Integer.parseInt(ageString);
	            if (age != 0) {
	                e.setAge(age);
	            }

	            elderlyManager.updateInfo(e);
	        } else {
	            System.out.println("Elderly not found with the provided id.");
	        }

	        ElderlyMenu(id);
	    } catch (NullPointerException e) {
	        System.out.println("Error: Unable to update elderly information. Please try again.");
	        e.printStackTrace();
	    }
	}

	
	private static void getListOfElderlies(int id) throws IOException {
		
		System.out.println("The list of elderlies is: ");
		elderlyManager.getListOfElderlies();
		ElderlyMenu(id);
		
	}
	
	
    private static void StaffMenu( Integer id) {
		
		try {
			int choice;
			do {
				System.out.println("STAFF MENU");
				System.out.println("\n1. Add a staff member.  ");
				System.out.println("2.List all the staff members. ");
				System.out.println("3.Update the information of a staff member. ");
				System.out.println("4.Print me  ");
				System.out.println("5.Load new staff members ");
				System.out.println("6.Exit  ");
			

				choice = InputException.getInt("Introduce your choice: ");

				switch (choice) {

				case 1:
					addStaff(id);
					break;
					
				case 2:
					getlistOfStaff(id);
					break;
					
				case 3:
					 id=InputException.getInt("Introduce the id of the staff member that is going to be updated:" );
					updateInfoStaff(id);
					break;
				case 4: printMe(id);
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

	public static void addStaff(int id) throws Exception {

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

		Staff staff = new Staff();
		// Create elderly and add it to JPA  
		User user = new User(name, digest);
		userManager.connect();

		Role role = userManager.getRole("staff");

		user.setRole(role);
		role.addUser(user);
		userManager.newUser(user);
		userManager.disconnect();

		staffManager.addStaffMember(staff);
		StaffMenu(id);

	}
    
    private static void getlistOfStaff(int id) throws IOException {
		
		System.out.println("The list of staff member is: ");
		staffManager.listStaffMembers();
		StaffMenu(id );
		
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
