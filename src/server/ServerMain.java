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

import Interfaces.DoctorManager;
import Interfaces.ElderlyManager;
import Interfaces.TaskManager;
import Interfaces.UserManager;
import jdbc.JDBCDoctorManager;
import jdbc.JDBCElderlyManager;
import jdbc.JDBCManager;
import jdbc.JDBCTasksManager;
import jpa.JPAUserManager;
import POJOS.*;

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

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
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

		ServerSocket sso = new ServerSocket(9009);
		while (true) {
			Socket so = sso.accept();
			System.out.println("cliente conectado");
			// el server lee lineas pero tambien manda
			br = new BufferedReader(new InputStreamReader(so.getInputStream()));
			OutputStream os = so.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);

			String line;

			while ((line = br.readLine()) != null) {
				if (line.contains("stop")) {
					releaseResources(pw, br, os, so);
					System.out.println("conexion with the client ended");

					break;
				} else if (line.contains("addDoctor")) {// si el cliente dice que quiere añadir un doctor

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
					
	
				}else if(line.contains("logInDoctor")) {
					
				}else if(line.contains("searchDoctorIdfromUId")) {
					
				}else if(line.contains("searchDoctorbyId")) {
					
				}else if(line.contains("updateDoctorMemberInfo")) {
					
				}else if(line.contains("searchAllDoctors")) {
					
				}else if(line.contains("addElderly")) {
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
