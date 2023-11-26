package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import Interfaces.DoctorManager;
import Interfaces.ElderlyManager;
import Interfaces.TaskManager;
import Interfaces.UserManager;
import POJOS.Doctor;
import POJOS.Elderly;
import POJOS.Role;
import POJOS.Task;
import POJOS.User;
import jdbc.JDBCDoctorManager;
import jdbc.JDBCElderlyManager;
import jdbc.JDBCManager;
import jdbc.JDBCTasksManager;
import jpa.JPAUserManager;

public class ServerMain {
	static OutputStream os = null;
	static PrintWriter pw = null;

	static BufferedReader br = null;
	static ServerSocket sso = null;
	static Socket so = null;

	private static ElderlyManager elderlyManager;

	private static DoctorManager doctorManager;

	private static UserManager userManager;

	private static TaskManager tasksManager;

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ParseException {
		System.out.println("SERVER");
		// el server es el que se conecta con la base de datos
		JDBCManager jdbcManager = new JDBCManager();

		// initialize database JDBC
		elderlyManager = new JDBCElderlyManager(jdbcManager);
		doctorManager = new JDBCDoctorManager(jdbcManager);
		tasksManager = new JDBCTasksManager(jdbcManager);
		// initialize database JPA
		userManager = new JPAUserManager();

		/*
		 * EJEMPLO DIAPO 27 ServerSocket sso = new ServerSocket (9009); Socket so =
		 * sso.accept();
		 *
		 *
		 * OutputStream os = so.getOutputStream(); PrintWriter pw = new PrintWriter(os,
		 * true); pw.println("mi linea mandada"); //releaseResources(pw, os, so);
		 *
		 * BufferedReader br = new BufferedReader(new
		 * InputStreamReader(so.getInputStream()));
		 *
		 * String line; while((line = br.readLine()) != null) {
		 * if(line.contains("stop")) { releaseResources(pw, os, so); break; }else {
		 * System.out.println(line); } }
		 */

		sso = new ServerSocket(9009);
		while (true) {
			so = sso.accept();
			System.out.println("cliente conectado");
			// el server lee lineas pero tambien manda
			br = new BufferedReader(new InputStreamReader(so.getInputStream()));
			os = so.getOutputStream();
			pw = new PrintWriter(os, true);

			String line;

			while ((line = br.readLine()) != null) {
				System.out.println(line);
				if (line.contains("stop")) {
					releaseResources(pw, br, os, so);
					System.out.println("conexion with the client ended");

					break;
				}

				//DOCTOR
				else if (line.contains("addDoctor")) {// si el cliente dice que quiere añadir un doctor

					System.out.println(line);

					String username = br.readLine();
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

					// recibir un doctor

					try {

						// pasar de doctor texto a doctor objeto
						Doctor doctor;
						doctor = new Doctor(doctor_text);

						// añadir el doctor a la base de datos
						doctorManager.addDoctorMember(doctor);

					} catch (ParseException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					pw.println("doctor added");


				}else if(line.contains("checkPassword")) {
					String username = br.readLine();
					String password = br.readLine();
					MessageDigest md = MessageDigest.getInstance("MD5");
					md.update(password.getBytes());
					byte[] digest = md.digest();
					User u = userManager.checkPassword(username, digest);
					try {
						pw.println(u.getRole().toString());
						pw.println(u.toString());
					}catch(Exception e) {
						System.out.println("no existe el usuario");
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

				}else if(line.contains("getListOfElderlyByDoctorID")) {
					//HACER
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
					String password = br.readLine();
					String elderly_text = br.readLine();
					System.out.println(elderly_text);

					MessageDigest md = MessageDigest.getInstance("MD5");
					md.update(password.getBytes());
					byte[] digest = md.digest();

					// CREATE elderly AND ADD TO JPA
					User u = new User(username, digest);

					Role role = userManager.getRole("Elderly");
					u.setRole(role);
					role.addUser(u);
					userManager.newUser(u);
					
					// recibir un elderly
					try {

						// pasar de elderly texto a elderly objeto
						Elderly elderly;
						elderly = new Elderly(elderly_text);

						// añadir el elderly a la base de datos
						elderlyManager.addElderly(elderly);

					} catch (ParseException e) {
						e.printStackTrace();
					}
					pw.println("elderly added");


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
					System.out.println("dddd");

					String id_text = br.readLine();
					System.out.println("dddd");

					int id = Integer.parseInt(id_text);
					System.out.println("dddd");

					Elderly elderly = null;
					try {
						elderly = elderlyManager.searchElderlyById(id);
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("elderly"+elderly.toString());
					pw.println(elderly.toString());


				}else if(line.contains("seeTasks")) {
					String id_text = br.readLine();
					int id_elder = Integer.parseInt(id_text);
					List<Task> listtasks = elderlyManager.seeTasksbyElderly(id_elder);
					pw.println(""+listtasks.size());
					for (Task listtask : listtasks) {
						pw.println(listtask);

					}


				}
			}
		}
	}

	private static void releaseResources(PrintWriter printWriter, BufferedReader br, OutputStream outputStream,
			Socket socket) {
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
