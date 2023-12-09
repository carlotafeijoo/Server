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


	public ClientHandler(Socket so, UserManager userManager, DoctorManager doctorManager, 
			ElderlyManager elderlyManager, TaskManager tasksManager, ReportManager reportManager) {

		this.so = so;
		this.userManager = userManager;
		this.doctorManager = doctorManager;
		this.elderlyManager = elderlyManager;
		this.tasksManager = tasksManager;
		this.reportManager = reportManager;
	}


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

		String line;

		while ((line = br.readLine()) != null) {
			//System.out.println(line);

			//System.out.println("Server accessible");
			

			if (line.contains("stop")) {
	
				System.out.println("\nConexion with the client ended");
				ServerMain.clientCounter--;
				System.out.println("There are " + ServerMain.clientCounter + " clients connected");
				releaseResources(pw, br, os, so);
				break;
			}
			//SERVER
			else if(line.contains("killServer")) {	
				
				if(ServerMain.clientCounter==1){
					
					pw.println("exit admin client");
					
					System.out.println("\nServer in standby mode");
					ServerMain.switchServerOFF();
					releaseResources(pw, br, os, so);
					break;
					}
			
			}
			//DOCTOR
			else if (line.contains("addDoctor")) {// Client wants to add a doctor

				System.out.println(line);

				String username = br.readLine();
				boolean check = doctorManager.checkAlreadyUsedDNIDoc(username);
				if (check == true){
					pw.println("\nRegister uncompleted: email already in use, try to log in instead");
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

				}
			}else if(line.contains("checkPassword")) {
				String username = br.readLine();
				String password = br.readLine();
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(password.getBytes());
				byte[] digest = md.digest();

				//User u = userManager.checkUsername(username);

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

					/*ORIGINAL
						pw.println(u.getRole().toString());
						pw.println(u.toString());*/

				}catch(Exception e) {
					System.out.println("\nUser does not exist");
					e.printStackTrace();
				}

			}else if(line.contains("searchDoctorIdfromUId")) {
				String id_text=br.readLine();
				int id=Integer.parseInt(id_text);
				int doctor_id =  doctorManager.searchDoctorIdfromUId(id);
				pw.println(""+doctor_id);


			}else if(line.contains("searchDoctorbyId")) {
				String id_text = br.readLine();
				int id = Integer.parseInt(id_text);
				Doctor doctor = doctorManager.searchDoctorbyId(id);
				pw.println(doctor.toString());


			}else if(line.contains("updateDoctorMemberInfo")) {
				String doctorObject_string = br.readLine();
				System.out.println(doctorObject_string);
				Doctor doctorToUpdate = new Doctor(doctorObject_string);
				doctorManager.updateDoctorMemberInfo(doctorToUpdate);


			}else if(line.contains("seeSymptoms")) {

				String eld_id_txt = br.readLine();
				int eld_id = Integer.parseInt(eld_id_txt);

				String symp = elderlyManager.seeSymptoms(eld_id);
				pw.println(symp);


			}else if(line.contains("getListOfTasksByDoctorFromElder")) {
				//HACER
				String id_text = br.readLine();
				int id_doc = Integer.parseInt(id_text);
				String id_text2 = br.readLine();
				int id_elder = Integer.parseInt(id_text2);
				List<Task> listtasks = tasksManager.getListOfTasksByDoctorFromElderly(id_doc,id_elder);
				pw.println(""+listtasks.size());
				for (Task listtask : listtasks) {
					pw.println(listtask);
				}
			}else if(line.contains("printReport")) {
				//HACER
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


				} catch (IOException ioe) {
					System.out.println("\nError durante el proceso\t" + ioe);
				} finally {
					try {  //se cierran en sentido contrario al que se han abierto
						if (bufferedreader != null) {
							bufferedreader.close();
						}
					} catch (IOException ioe) {
						System.out.println("\nError durante el proceso\t" + ioe);
					}
					try {
						if (inputstreamreader != null) {
							inputstreamreader.close();
						}
					} catch (IOException ioe) {
						System.out.println("\nError durante el proceso\t" + ioe);
					}
					try {
						if (fileinputstream != null) {
							fileinputstream.close();
						}
					} catch (IOException ioe) {
						System.out.println("\nError durante el proceso\t" + ioe);
					}
				}

			}else if(line.contains("getListOfReportsByDoctorFromElder")) {
			
				String id_text = br.readLine();
				int id_doc = Integer.parseInt(id_text);
				String id_text2 = br.readLine();
				int id_elder = Integer.parseInt(id_text2);
				List<Report> listreports = reportManager.getListOfReportsByDoctorFromElderly(id_elder,id_doc);
				pw.println(""+listreports.size());
				for (Report listreport : listreports) {
					pw.println(listreport);
				}


			}else if(line.contains("getListOfElderlyByDoctorID")) {
				
				int idDoctor = Integer.parseInt(br.readLine());
				List<Elderly> elderlys = elderlyManager.getListOfElderlyByDoctorID(idDoctor);
				pw.println(""+elderlys.size());

				for (Elderly elderly : elderlys) {
					pw.println(elderly);
				}


			}else if(line.contains("addTask")) {
				String taskObject_string = br.readLine();
				Task newTask = new Task (taskObject_string);
				try {
					tasksManager.addTask(newTask);
					pw.println("task added");
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}


			//ELDERLY
			else if(line.contains("addElderly")) {
				System.out.println(line);

				String username = br.readLine();
				int user = Integer.parseInt(username);
				boolean check = elderlyManager.checkAlreadyUsedDNI(user);
				System.out.println(check);
				if (check == true){
					pw.println("\nRegister uncompleted: DNI already in use, try to log in instead");
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

					// Receive elderly
					try {

						//From elderly OBJECT to elderly TEXT
						Elderly elderly;
						elderly = new Elderly(elderly_text);

						//ESTO ESTA MAL
						//va a imprimir un elderly_id = 0 porque aqui no esta mandando ningun id al constructor y tampoco pasa por la base de datos antes de imprimirlo
						//System.out.println("Server main" + elderly);
						//mirar en la base de datos que se haya metido bien (pero eso si que funciona)

						//Add elderly to DB
						elderlyManager.addElderly(elderly);

					} catch (ParseException e) {
						e.printStackTrace();
					}
					pw.println("elderly added");
				}

			}else if(line.contains("searchAllDoctors")) {
				ArrayList<Doctor> doctores = doctorManager.searchAllDoctors();
				pw.println(""+doctores.size());

				for (Doctor doctore : doctores) {
					pw.println(doctore);
				}


			}else if(line.contains("searchElderlyIdfromUId")) {
				String id_text=br.readLine();
				int id=Integer.parseInt(id_text);
				int elderly_id =  elderlyManager.searchElderlyIdfromUId(id);
				pw.println(""+elderly_id);


			}else if(line.contains("searchElderlyById")) {
				
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



			}else if(line.contains("searchElderlyNameById")) {

				String eld_id_text = br.readLine();
				int eld_id = Integer.parseInt(eld_id_text);

				Elderly elderly = null;
				try {
					elderly = elderlyManager.searchElderlyById(eld_id);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//System.out.println("elderly"+elderly.toString());
				System.out.println("\n" + elderly.getName());
				pw.println(elderly.getName());

			}else if(line.contains("seeTasksandId")) {
				String eld_id_text = br.readLine();
				int eld_id = Integer.parseInt(eld_id_text);
				List<Task> listtasks = elderlyManager.seeTaskANDidbyElderly(eld_id);
				
				pw.println(""+listtasks.size());
 
				for (Task listtask : listtasks) {
					pw.println(listtask.toString());
				}
			}else if (line.contains("searchTaskDurationByELDid")){
				//TODO: REVISAR QUE SOLO PUEDA INTRODUCIR UNA TASK QUE LE PERTENECE
				String task_id_text = br.readLine();
				int task_id = Integer.parseInt(task_id_text);
				System.out.println(task_id);
				Task task = tasksManager.getTask(task_id);
				int recordDuration = task.getDuration();
				System.out.println("\nRecord duration " +recordDuration);
				pw.println(recordDuration);


			}else if(line.contains("storeRecord")) {

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

				} catch (IOException ioe) {
					System.out.println("Error" + ioe);
				} finally {
					if (printwriter != null) {
						printwriter.close();
					}

				}

			}else if(line.contains("seeTasks")) {

				String id_text = br.readLine();
				int id_elder = Integer.parseInt(id_text);

				List<Task> list_tasks = elderlyManager.seeTasksbyElderly(id_elder);
				pw.println(""+list_tasks.size());


				for (Task listtask : list_tasks) {
					pw.println(listtask);

				}


			}else if(line.contains("addSymptoms")) {

				int e_id = Integer.parseInt(br.readLine());
				String symptom = br.readLine();

				elderlyManager.addSymptoms(e_id, symptom);
			}
			
		}
		
	}
	//}

	private static String convertCommaIntoLines(String stringleido) {
		String signal = stringleido.replace(",", "\n");
		return signal;
	}



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