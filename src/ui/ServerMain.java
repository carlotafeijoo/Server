package ui;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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
import java.util.Date;
import java.util.List;
import java.util.Timer;

import Interfaces.DoctorManager;
import Interfaces.ElderlyManager;
import Interfaces.ReportManager;
import Interfaces.TaskManager;
import Interfaces.UserManager;
import POJOS.Doctor;
import POJOS.Elderly;
import POJOS.Role;
import POJOS.Task;
import POJOS.User;
import POJOS.Report;
import exceptions.InputException;
import jdbc.JDBCDoctorManager;
import jdbc.JDBCElderlyManager;
import jdbc.JDBCManager;
import jdbc.JDBCTasksManager;
import jdbc.JDBCReportManager;
import jpa.JPAUserManager;


public class ServerMain {

	public static int clientCounter = 0;

	static DataInputStream dis = null;
	static FileInputStream fis = null;
	static ServerSocket sso = null;


	private static ElderlyManager elderlyManager;

	private static DoctorManager doctorManager;

	private static UserManager userManager;

	private static TaskManager tasksManager;

	private static ReportManager reportManager;


	static int socketPort = 9009;
	/**
	 * The main method to start the server application.
	 * 
	 * @param args the command-line arguments
	 */

	public static void main(String[] args) {

		try {

			int option;
			do {
				System.out.println("\nSERVER MENU ");
				System.out.println("1. Start server ");
				System.out.println("2. Switch off application  ");
				option = InputException.getInt("Introduce the number choice:  ");
				System.out.println("\n--------------------------------------------------------------------------------");

				int clientCounter = 0;

				switch (option) {

				case 1:
					sso = new ServerSocket(socketPort);
					serverMainON();
					break;

				case 2:
					System.out.println("YOU HAVE SWITCHED OFF SERVER APPLICATION\n");
					System.exit(0);
					break;

				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			System.out.println(" ");
		}

	}
	/**
	 * Starts the server and initializes database connections and the server socket.
	 * 
	 * @throws IOException if an I/O error occurs when waiting for a connection
	 * @throws NoSuchAlgorithmException if a requested cryptographic algorithm is not available
	 * @throws ParseException if an error occurs during parsing
	 */

	public static void serverMainON()throws IOException, NoSuchAlgorithmException, ParseException{

		System.out.println("\nSERVER ACTIVE");
		System.out.println("Database connection openned.\n");

		//Server connects to DB
		JDBCManager jdbcManager = new JDBCManager();

		// initialize database JDBC
		elderlyManager = new JDBCElderlyManager(jdbcManager);
		doctorManager = new JDBCDoctorManager(jdbcManager);
		tasksManager = new JDBCTasksManager(jdbcManager);
		reportManager = new JDBCReportManager(jdbcManager);

		// initialize database JPA
		userManager = new JPAUserManager();


		while (true) {

			Socket so = sso.accept();

			ServerMain.clientCounter++;

			System.out.println("\nClient connected");
			System.out.println("\n--------------------------------------------------------------------------------");


			// Server: reads and sends lines
			ClientHandler clientHandler = new ClientHandler(so, userManager, doctorManager, elderlyManager, tasksManager, reportManager);
			Thread clientThread = new Thread(clientHandler);
			clientThread.start();

		}

	}

	/**
	 * Shuts down the server and provides a menu for further actions.
	 * 
	 * @throws IOException if an I/O error occurs when closing the server socket
	 * @throws NoSuchAlgorithmException if a requested cryptographic algorithm is not available
	 * @throws ParseException if an error occurs during parsing
	 */
	public static void switchServerOFF()throws IOException, NoSuchAlgorithmException, ParseException{


		try {

			sso.close();

			int option = 0;


			do {
				System.out.println("\nSWITCH OFF MENU ");
				System.out.println("\nThere are no clients connected");
				System.out.println("1. Turn off server application");
				System.out.println("2. Continue operating server application");

				option = InputException.getInt("\nIntroduce the number choice:  ");
				System.out.println("\n--------------------------------------------------------------------------------");

				switch (option) {

				case 1:
					System.out.println("\nYOU HAVE SWITCHED OFF SERVER APPLICATION");
					//sso.close();
					System.exit(0);
					break;

				case 2:
					//sso.close();
					sso = new ServerSocket(socketPort);
					ServerMain.clientCounter--;					
					serverMainON();

					break;

				default:
					System.out.println("\nPlease introduce option 1 or 2.\n");
					switchServerOFF();


				}
			} while (true);

		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("\nThe socket is correctly closed");
		}

	}



}


