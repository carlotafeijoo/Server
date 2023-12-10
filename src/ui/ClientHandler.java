package ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import Interfaces.DoctorManager;
import Interfaces.ElderlyManager;
import Interfaces.TaskManager;
import Interfaces.UserManager;
import Interfaces.ReportManager;
import POJOS.Doctor;
import POJOS.Elderly;
import POJOS.Report;
import POJOS.Role;
import POJOS.Task;
import POJOS.User;
import exceptions.InputException;

public class ClientHandler implements Runnable {

	private Socket so;
	private UserManager userManager;
	private DoctorManager doctorManager;
	private ElderlyManager elderlyManager;
	private TaskManager tasksManager;
	private ReportManager reportManager;

	/**
	 * The ClientHandler class is responsible for handling client requests and interacting with the different managers to process the requests.
	 */
	public ClientHandler(Socket so, UserManager userManager, DoctorManager doctorManager, 
			ElderlyManager elderlyManager, TaskManager tasksManager, ReportManager reportManager) {

		this.so = so;
		this.userManager = userManager;
		this.doctorManager = doctorManager;
		this.elderlyManager = elderlyManager;
		this.tasksManager = tasksManager;
		this.reportManager = reportManager;
	}

	/**
	 * Runs the client handler, handling the client's connection and processing the requests.
	 */
	@Override
	public void run() {

		try {

			handleClientConnection();
		} catch (IOException | NoSuchAlgorithmException | ParseException e) {
			e.printStackTrace();
		}
	}

	private void handleClientConnection() throws IOException, NoSuchAlgorithmException, ParseException {

		BufferedReader br = new BufferedReader(new InputStreamReader(so.getInputStream()));
		OutputStream os = so.getOutputStream();
		PrintWriter pw = new PrintWriter(os, true);
		DataInputStream dis = new DataInputStream(so.getInputStream());
		BufferedInputStream bis = new BufferedInputStream(so.getInputStream());
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;


		System.out.println("Number of clients: " + ServerMain.clientCounter);
		System.out.println("\n--------------------------------------------------------------------------------");
		
		String line;

		while ((line = br.readLine()) != null) {
			/**
			Checks if the provided line contains the word "stop" and takes appropriate actions.
			If the line contains "stop":
			Prints a message indicating that the connection with the client has ended.
			Decrements the client counter.
			Prints the updated number of connected clients.
			Releases the specified resources.
			Exits the loop.
			@param line the String to be checked for the presence of the word "stop"
			 */
			if (line.contains("stop")) {

				System.out.println("\nConexion with the client ended");
				ServerMain.clientCounter--;
				System.out.println("There are " + ServerMain.clientCounter + " clients connected");
				System.out.println("\n--------------------------------------------------------------------------------");
				releaseResources(pw, br, os, so);
				break;
			}
			//SERVER

			/**
			Checks if the provided line contains the phrase "killServer" and takes appropriate actions based on the client count.
			If the line contains "killServer" and there is only one client connected:
			Sends an "exit admin client" message to the PrintWriter.
			Prints a message indicating that the server is in standby mode.
			Switches the server off.
			Releases the specified resources.
			Exits the loop.
			If the line contains "killServer" and there are multiple clients connected, it sends an empty message to the PrintWriter.
			@param line the String to be checked for the presence of the phrase "killServer"
			 */
			else if(line.contains("killServer")) {	

				if(ServerMain.clientCounter==1){

					pw.println("exit admin client");

					System.out.println("\nServer in standby mode");
					System.out.println("\n--------------------------------------------------------------------------------");
					ServerMain.switchServerOFF();
					releaseResources(pw, br, os, so);
					break;
				}
				else {
					pw.println("");
				}

			}
			//DOCTOR

			/**
			Processes the client request to add a doctor.
			If the provided line contains "addDoctor":
			Prints the received line.
			Reads the username from the BufferedReader.
			Checks if the username is already in use.
			If the username is not in use, reads the password and doctor information, then performs the following:
			a. Hashes the password using MD5.
			b. Creates a new User and adds it to the JPA.
			c. Retrieves the "Doctor" role and assigns it to the user.
			d. Adds the user to the role and persists the user.
			e. Converts the doctor information to a Doctor object and adds it to the database.
			If an exception occurs during the process, it is caught and printed.
			Sends a response to the PrintWriter indicating the success or failure of the doctor addition.
			@param line the String to be checked for the presence of the phrase "addDoctor"
			@throws IOException if an I/O error occurs
			@throws NoSuchAlgorithmException if the MD5 algorithm is not available
			 */
			else if (line.contains("addDoctor")) {// Client wants to add a doctor

				System.out.println(line);

				String username = br.readLine();
				boolean check = doctorManager.checkAlreadyUsedDNIDoc(username);
				if (check == true){
					pw.println("\nRegister uncompleted: email already in use, try to log in instead");
					System.out.println("\n--------------------------------------------------------------------------------");
					
				} else {
					String password = br.readLine();
					String doctor_text = br.readLine();
					System.out.println(doctor_text);

					MessageDigest md = MessageDigest.getInstance("MD5");
					md.update(password.getBytes());
					byte[] digest = md.digest();

					// CREATE doctor AND ADD TO JPA
					User u = new User(username, digest);
					Role role = userManager.getRole("Doctor");
					u.setRole(role);
					role.addUser(u);
					userManager.newUser(u);

					//Receive Doctor
					try {

						//From Doctor TEXT to Doctor OBJECT
						Doctor doctor;
						doctor = new Doctor(doctor_text);

						//Add doctor to db
						doctorManager.addDoctorMember(doctor);

					} catch (ParseException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					pw.println("doctor added");
					System.out.println("\n--------------------------------------------------------------------------------");

				}

			}
			/**
			Processes the client request to check the password for a specific username.
			If the provided line contains "checkPassword":
			Reads the username and password from the BufferedReader.
			Hashes the provided password using the MD5 algorithm.
			Calls the userManager to check the password for the given username and hashed password, which returns a User object or null.
			If the returned User object is null, it retrieves the "Doctor" role, sends an error message, and prints the role information.
			If the returned User object is not null, it sends the user's role and information to the PrintWriter.
			If an exception occurs during the process, it is caught, and an error message is printed.
			@param line the String to be checked for the presence of the phrase "checkPassword"
			@throws IOException if an I/O error occurs
			@throws NoSuchAlgorithmException if the MD5 algorithm is not available
			 */
			else if(line.contains("checkPassword")) {
				String username = br.readLine();
				String password = br.readLine();
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(password.getBytes());
				byte[] digest = md.digest();


				User u = userManager.checkPassword(username, digest); //returns a user null
				try {
					if(u == null) { 
						Role role = userManager.getRole("Doctor");
						pw.println(role.toString());
						pw.println("error");

					}else{
						pw.println(u.getRole().toString());
						pw.println(u.toString());
					}

				}catch(Exception e) {
					System.out.println("\nUser does not exist");
					System.out.println("\n--------------------------------------------------------------------------------");
					e.printStackTrace();
				}

			}
			/**
			Processes the client request to search for a doctor's ID based on a user ID.
			If the provided line contains "searchDoctorIdfromUId":
			Reads the user ID as a string from the BufferedReader and parses it to an integer.
			Calls the doctorManager to search for the doctor's ID associated with the provided user ID.
			Sends the obtained doctor ID to the PrintWriter.
			@param line the String to be checked for the presence of the phrase "searchDoctorIdfromUId"
			 */
			else if(line.contains("searchDoctorIdfromUId")) {
				String id_text=br.readLine();
				int id=Integer.parseInt(id_text);
				int doctor_id =  doctorManager.searchDoctorIdfromUId(id);
				pw.println(""+doctor_id);
				System.out.println("\n--------------------------------------------------------------------------------");

			}

			/**
			Processes the client request to search for a doctor by ID and send the doctor's information to the client.
			If the provided line contains "searchDoctorbyId":
			Reads the doctor's ID as a string from the BufferedReader and parses it to an integer.
			Calls the doctorManager to search for the doctor based on the provided ID.
			Sends the obtained doctor's information to the PrintWriter.
			@param line the String to be checked for the presence of the phrase "searchDoctorbyId"
			 */
			else if(line.contains("searchDoctorbyId")) {
				String id_text = br.readLine();
				int id = Integer.parseInt(id_text);
				Doctor doctor = doctorManager.searchDoctorbyId(id);
				pw.println(doctor.toString());
				System.out.println("\n--------------------------------------------------------------------------------");

			}
			/**
			Processes the client request to update a doctor's information.
			If the provided line contains "updateDoctorMemberInfo":
			Reads the doctor's information as a string from the BufferedReader.
			Creates a new Doctor object based on the received doctor's information.
			Calls the doctorManager to update the doctor's information with the newly created Doctor object.
			@param line the String to be checked for the presence of the phrase "updateDoctorMemberInfo"
			@throws IOException if an I/O error occurs
			 */
			else if(line.contains("updateDoctorMemberInfo")) {
				String doctorObject_string = br.readLine();
				System.out.println(doctorObject_string);
				Doctor doctorToUpdate = new Doctor(doctorObject_string);
				doctorManager.updateDoctorMemberInfo(doctorToUpdate);
				System.out.println("\n--------------------------------------------------------------------------------");
			}
			/**
			Processes the client request to retrieve and send the symptoms of an elderly person.
			If the provided line contains "seeSymptoms":
			Reads the elderly person's ID as a string from the BufferedReader and parses it to an integer.
			Calls the elderlyManager to retrieve the symptoms of the elderly person based on the provided ID.
			Sends the obtained symptoms to the PrintWriter.
			@param line the String to be checked for the presence of the phrase "seeSymptoms"
			 */
			else if(line.contains("seeSymptoms")) {
				String eld_id_txt = br.readLine();
				int eld_id = Integer.parseInt(eld_id_txt);
				String symp = elderlyManager.seeSymptoms(eld_id);
				pw.println(symp);
				System.out.println("\n--------------------------------------------------------------------------------");
			}
			/**
			Processes the client request to retrieve a list of tasks by a doctor from a specific elderly person and send the tasks to the client.
			If the provided line contains "getListOfTasksByDoctorFromElder":
			Reads the doctor's ID and the elderly person's ID as strings from the BufferedReader and parses them to integers.
			Calls the tasksManager to retrieve the list of tasks by the doctor from the specific elderly person based on the provided IDs.
			Sends the size of the obtained task list to the PrintWriter.
			Sends each task in the obtained list to the PrintWriter.
			@param line the String to be checked for the presence of the phrase "getListOfTasksByDoctorFromElder"
			 */
			else if(line.contains("getListOfTasksByDoctorFromElder")) {
				String id_text = br.readLine();
				int id_doc = Integer.parseInt(id_text);
				String id_text2 = br.readLine();
				int id_elder = Integer.parseInt(id_text2);
				List<Task> listtasks = tasksManager.getListOfTasksByDoctorFromElderly(id_doc,id_elder);
				pw.println(""+listtasks.size());
				for (Task listtask : listtasks) {
					pw.println(listtask);
				}
				System.out.println("\n--------------------------------------------------------------------------------");
			}
			/**
			Processes the client request to print a report and send its content to the client.
			If the provided line contains "printReport":
			Reads the report ID as a string from the BufferedReader and parses it to an integer.
			Retrieves the report using the reportManager based on the provided ID.
			Constructs the file path for the report.
			Reads the content of the report file and sends it to the PrintWriter.
			@param line the String to be checked for the presence of the phrase "printReport"
			@throws IOException if an I/O error occurs
			 */
			else if(line.contains("printReport")) {
				String id_text = br.readLine();
				int id_report = Integer.parseInt(id_text);
				Report rep= reportManager.seeBitalinoReportByID(id_report);
				String diract = System.getProperty("user.dir"); // find where the program is executing
				String dirfolder = diract +"\\recordstxt";

				File filetxt = new File(dirfolder, rep.getFile_name());
				pw.println(rep.getTask_id());
				pw.println(rep.getFile_name());
				pw.println(rep.getElderly_id());				        
				FileInputStream fileinputstream = null;
				InputStreamReader inputstreamreader = null;
				BufferedReader bufferedreader = null;
				try {
					fileinputstream = new FileInputStream(filetxt);
					inputstreamreader = new InputStreamReader(fileinputstream);
					bufferedreader = new BufferedReader(inputstreamreader);
					String texto = "";
					String stringleido;
					while (true) {
						stringleido = bufferedreader.readLine();
						if (stringleido == null) {
							break;
						}
						texto = texto + stringleido + ",";
					}
					pw.println(texto);
					System.out.println("\n--------------------------------------------------------------------------------");


				} catch (IOException ioe) {
					System.out.println("\nError durante el proceso\t" + ioe);
				} finally {
					try {  //se cierran en sentido contrario al que se han abierto
						if (bufferedreader != null) {
							bufferedreader.close();
						}
					} catch (IOException ioe) {
						System.out.println("\nError durante el proceso\t" + ioe);
						System.out.println("\n--------------------------------------------------------------------------------");
					}
					try {
						if (inputstreamreader != null) {
							inputstreamreader.close();
						}
					} catch (IOException ioe) {
						System.out.println("\nError durante el proceso\t" + ioe);
						System.out.println("\n--------------------------------------------------------------------------------");
					}
					try {
						if (fileinputstream != null) {
							fileinputstream.close();
						}
					} catch (IOException ioe) {
						System.out.println("\nError durante el proceso\t" + ioe);
						System.out.println("\n--------------------------------------------------------------------------------");
					}
				}
			}/**
			Processes the client request to retrieve a list of reports by a doctor from a specific elderly person and send the reports to the client.
			If the provided line contains "getListOfReportsByDoctorFromElder":
			Reads the doctor's ID and the elderly person's ID as strings from the BufferedReader and parses them to integers.
			Calls the reportManager to retrieve the list of reports by the doctor from the specific elderly person based on the provided IDs.
			Sends the size of the obtained report list to the PrintWriter.
			Sends each report in the obtained list to the PrintWriter.
			@param line the String to be checked for the presence of the phrase "getListOfReportsByDoctorFromElder"
			 */
			else if(line.contains("getListOfReportsByDoctorFromElder")) {

				String id_text = br.readLine();
				int id_doc = Integer.parseInt(id_text);
				String id_text2 = br.readLine();
				int id_elder = Integer.parseInt(id_text2);
				List<Report> listreports = reportManager.getListOfReportsByDoctorFromElderly(id_elder,id_doc);
				pw.println(""+listreports.size());
				for (Report listreport : listreports) {
					pw.println(listreport);
				}
				System.out.println("\n--------------------------------------------------------------------------------");
			}
			/**
			Processes the client request to retrieve a list of elderly individuals associated with a specific doctor and sends the list to the client.
			If the provided line contains "getListOfElderlyByDoctorID":
			Reads the doctor's ID as a string from the BufferedReader and parses it to an integer.
			Calls the elderlyManager to retrieve the list of elderly individuals associated with the specific doctor based on the provided ID.
			Sends the size of the obtained list of elderly individuals to the PrintWriter.
			Sends each elderly individual in the obtained list to the PrintWriter.
			@param line the String to be checked for the presence of the phrase "getListOfElderlyByDoctorID"
			 */
			else if(line.contains("getListOfElderlyByDoctorID")) {
				int idDoctor = Integer.parseInt(br.readLine());
				List<Elderly> elderlys = elderlyManager.getListOfElderlyByDoctorID(idDoctor);
				pw.println(""+elderlys.size());
				for (Elderly elderly : elderlys) {
					pw.println(elderly);
				}
				System.out.println("\n--------------------------------------------------------------------------------");
			}
			/**
			Processes the client request to add a new task.
			If the provided line contains "addTask":
			Reads the task information as a string from the BufferedReader and creates a new Task object.
			Attempts to add the new task using the tasksManager.
			Sends a message to the PrintWriter indicating the success or failure of the task addition.
			@param line the String to be checked for the presence of the phrase "addTask"
			 */
			else if(line.contains("addTask")) {
				String taskObject_string = br.readLine();
				Task newTask = new Task (taskObject_string);
				try {
					tasksManager.addTask(newTask);
					pw.println("task added");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("\n--------------------------------------------------------------------------------");
			}


			//ELDERLY

			/**
			Processes the client request to add a new elderly individual.
			If the provided line contains "addElderly":
			Reads the username as a string from the BufferedReader and parses it to an integer.
			Checks if the username (DNI) is already in use.
			If the username is not in use, reads the password and elderly information, then performs the following:
			a. Hashes the password using MD5.
			b. Creates a new User and adds it to the JPA.
			c. Retrieves the "Elderly" role and assigns it to the user.
			d. Adds the user to the role and persists the user.
			e. Converts the elderly information to an Elderly object and adds it to the database.
			If an exception occurs during the process, it is caught and printed.
			Sends a response to the PrintWriter indicating the success or failure of the elderly addition.
			@param line the String to be checked for the presence of the phrase "addElderly"
			 */
			else if(line.contains("addElderly")) {
				System.out.println(line);
				String username = br.readLine();
				int user = Integer.parseInt(username);
				boolean check = elderlyManager.checkAlreadyUsedDNI(user);
				System.out.println(check);
				if (check == true){
					pw.println("\nRegister uncompleted: DNI already in use, try to log in instead");
					System.out.println("\n--------------------------------------------------------------------------------");
				
				} else {
					String password = br.readLine();
					String elderly_text = br.readLine();
					MessageDigest md = MessageDigest.getInstance("MD5");
					md.update(password.getBytes());
					byte[] digest = md.digest();
					// CREATE elderly AND ADD TO JPA
					User u = new User(username, digest);

					Role role = userManager.getRole("Elderly");
					u.setRole(role);
					role.addUser(u);
					userManager.newUser(u);

					try {

						Elderly elderly;
						elderly = new Elderly(elderly_text);
						elderlyManager.addElderly(elderly);

					} catch (ParseException e) {
						e.printStackTrace();
					}
					pw.println("elderly added");
					System.out.println("\n--------------------------------------------------------------------------------");
				}
			}
			/**
			Processes the client request to retrieve a list of all doctors and send the list to the client.
			If the provided line contains "searchAllDoctors":
			Calls the doctorManager to retrieve a list of all doctors.
			Sends the size of the obtained list of doctors to the PrintWriter.
			Sends each doctor in the obtained list to the PrintWriter.
			@param line the String to be checked for the presence of the phrase "searchAllDoctors"
			 */
			else if(line.contains("searchAllDoctors")) {
				ArrayList<Doctor> doctores = doctorManager.searchAllDoctors();
				pw.println(""+doctores.size());

				for (Doctor doctore : doctores) {
					pw.println(doctore);
				}
				System.out.println("\n--------------------------------------------------------------------------------");
			}
			/**
			Processes the client request to search for an elderly person's ID based on a user ID and sends the ID to the client.
			If the provided line contains "searchElderlyIdfromUId":
			Reads the user ID as a string from the BufferedReader and parses it to an integer.
			Calls the elderlyManager to search for the elderly person's ID associated with the provided user ID.
			Sends the obtained elderly person's ID to the PrintWriter.
			@param line the String to be checked for the presence of the phrase "searchElderlyIdfromUId"
			 */
			else if(line.contains("searchElderlyIdfromUId")) {
				String id_text=br.readLine();
				int id=Integer.parseInt(id_text);
				int elderly_id =  elderlyManager.searchElderlyIdfromUId(id);
				pw.println(""+elderly_id);
				System.out.println("\n--------------------------------------------------------------------------------");
			}
			/**
			Processes the client request to search for an elderly person by ID and sends the information to the client.
			If the provided line contains "searchElderlyById":
			Reads the elderly person's ID as a string from the BufferedReader and parses it to an integer.
			Calls the elderlyManager to search for the elderly person based on the provided ID.
			Sends the obtained elderly person's information to the PrintWriter.
			@param line the String to be checked for the presence of the phrase "searchElderlyById"
			 */
			else if(line.contains("searchElderlyById")) {
				String id_text = br.readLine();
				int id = Integer.parseInt(id_text);
				Elderly elderly = null;
				try {
					elderly = elderlyManager.searchElderlyById(id);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("elderly"+elderly.toString());
				pw.println(elderly.toString());
				System.out.println("\n--------------------------------------------------------------------------------");
			}
			/**
			Processes the client request to search for an elderly person's name by ID and sends the name to the client.
			If the provided line contains "searchElderlyNameById":
			Reads the elderly person's ID as a string from the BufferedReader and parses it to an integer.
			Calls the elderlyManager to search for the elderly person based on the provided ID.
			Sends the obtained elderly person's name to the PrintWriter.
			@param line the String to be checked for the presence of the phrase "searchElderlyNameById"
			 */
			else if(line.contains("searchElderlyNameById")) {
				String eld_id_text = br.readLine();
				int eld_id = Integer.parseInt(eld_id_text);
				Elderly elderly = null;
				try {
					elderly = elderlyManager.searchElderlyById(eld_id);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("\n" + elderly.getName());
				pw.println(elderly.getName());
				System.out.println("\n--------------------------------------------------------------------------------");
			}
			/**
			Processes the client request to retrieve a list of tasks and their IDs associated with a specific elderly person and sends the list to the client.
			If the provided line contains "seeTasksandId":
			Reads the elderly person's ID as a string from the BufferedReader and parses it to an integer.
			Calls the elderlyManager to retrieve the list of tasks and their IDs associated with the specific elderly person based on the provided ID.
			Sends the size of the obtained list of tasks to the PrintWriter.
			Sends each task in the obtained list to the PrintWriter.
			@param line the String to be checked for the presence of the phrase "seeTasksandId"
			 */
			else if(line.contains("seeTasksandId")) {
				String eld_id_text = br.readLine();
				int eld_id = Integer.parseInt(eld_id_text);
				List<Task> listtasks = elderlyManager.seeTaskANDidbyElderly(eld_id);

				pw.println(""+listtasks.size());

				for (Task listtask : listtasks) {
					pw.println(listtask.toString());
				}
				System.out.println("\n--------------------------------------------------------------------------------");
			}
			/**
			Processes the client request to search for a task duration by task ID associated with a specific elderly person and sends the duration to the client.
			If the provided line contains "searchTaskDurationByELDid":
			Reads the task ID as a string from the BufferedReader and parses it to an integer.
			Retrieves the task using the tasksManager based on the provided ID.
			Obtains the duration of the task.
			Sends the obtained task duration to the PrintWriter.
			@param line the String to be checked for the presence of the phrase "searchTaskDurationByELDid"
			 */
			else if (line.contains("searchTaskDurationByELDid")){
				String task_id_text = br.readLine();
				int task_id = Integer.parseInt(task_id_text);
				System.out.println(task_id);
				Task task = tasksManager.getTask(task_id);
				int recordDuration = task.getDuration();
				System.out.println("\nRecord duration " +recordDuration);
				pw.println(recordDuration);
				System.out.println("\n--------------------------------------------------------------------------------");
			}
			/**
			Processes the client request to store a record and associated data.
			If the provided line contains "storeRecord":
			Reads the file name, task ID, and elderly ID from the BufferedReader.
			Creates a new Report object with the provided file name, task ID, and elderly ID, and adds it using the reportManager.
			Retrieves the current directory path and constructs the directory for the records.
			Creates a new file in the records directory with the provided file name.
			Writes the signal data to the file, replacing commas with new lines.
			@param line the String to be checked for the presence of the phrase "storeRecord"
			 */
			else if(line.contains("storeRecord")) {
				
				System.out.println(line);
				//Read filename
				String file_name = br.readLine();
				String task_id_text = br.readLine();
				int task_id = Integer.parseInt(task_id_text);
				String id_elderly_text = br.readLine();
				int elderly_id = Integer.parseInt(id_elderly_text);
				
				Report rep =new Report(file_name, task_id, elderly_id);
				reportManager.addReport(rep);
				
				String diract = System.getProperty("user.dir"); 
				//String dirfolder = diract +"\\recordstxt";
				String dirfolder = diract +"//recordstxt";
				File archivo = new File(dirfolder, file_name);
				// Using Java to write in a file
				PrintWriter printwriter = null;
				
				try {
					printwriter = new PrintWriter(archivo);
					String stringleido;
					
					//Read the whole signal (Remind: separated by commas)
					stringleido = br.readLine();
					
					//We want data to be written with \n
					//(THAT IS HOW TXT IS GENERATED IN BITALINO.JAVA)
					//Replace commas by \n
					
					String signal = convertCommaIntoLines(stringleido);
					//Write signal in file
					printwriter.println(signal);
					System.out.println("\n--------------------------------------------------------------------------------");
				} catch (IOException ioe) {
					System.out.println("Error" + ioe);
					System.out.println("\n--------------------------------------------------------------------------------");
					
				} finally {
					if (printwriter != null) {
						printwriter.close();
					}
				}
			}
			/**
			Processes the client request to retrieve and send a list of tasks associated with a specific elderly person.
			If the provided line contains "seeTasks":
			Reads the elderly person's ID as a string from the BufferedReader and parses it to an integer.
			Calls the elderlyManager to retrieve the list of tasks associated with the specific elderly person based on the provided ID.
			Sends the size of the obtained list of tasks to the PrintWriter.
			Sends each task in the obtained list to the PrintWriter.
			@param line the String to be checked for the presence of the phrase "seeTasks"
			 */
			else if(line.contains("seeTasks")) {
				String id_text = br.readLine();
				int id_elder = Integer.parseInt(id_text);
				List<Task> list_tasks = elderlyManager.seeTasksbyElderly(id_elder);
				pw.println(""+list_tasks.size());
				for (Task listtask : list_tasks) {
					pw.println(listtask);
				}
				System.out.println("\n--------------------------------------------------------------------------------");
			}
			/**
			Processes the client request to add symptoms for a specific elderly person.
			If the provided line contains "addSymptoms":
			Reads the elderly person's ID as a string from the BufferedReader and parses it to an integer.
			Reads the symptoms from the BufferedReader.
			Calls the elderlyManager to add the symptoms for the specific elderly person.
			@param line the String to be checked for the presence of the phrase "addSymptoms"
			 */
			else if(line.contains("addSymptoms")) {
				
				int e_id = Integer.parseInt(br.readLine());
				String symptom = br.readLine();
				elderlyManager.addSymptoms(e_id, symptom);
				System.out.println(e_id + ", " + symptom);
				System.out.println("\n--------------------------------------------------------------------------------");
			}

		}

	}

	/**
	 * Replaces commas with new lines in the input string.
	 * 
	 * @param stringleido the input string
	 * @return the modified string with commas replaced by new lines
	 */
	private static String convertCommaIntoLines(String stringleido) {
		String signal = stringleido.replace(",", "\n");
		return signal;
	}

	/**
	 * Closes the provided PrintWriter, BufferedReader, OutputStream, and Socket, handling any potential IOExceptions.
	 * 
	 * @param printWriter the PrintWriter to be closed
	 * @param br the BufferedReader to be closed
	 * @param outputStream the OutputStream to be closed
	 * @param socket the Socket to be closed
	 */

	private static void releaseResources(PrintWriter printWriter, BufferedReader br, OutputStream outputStream, Socket socket) {
		printWriter.close();
		try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}