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
import exceptions.InputException;
import jdbc.JDBCDoctorManager;
import jdbc.JDBCElderlyManager;
import jdbc.JDBCManager;
import jdbc.JDBCTasksManager;
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
	
	
	public static void main(String[] args) {
		
		try {

			int option;
			do {
				System.out.println("SERVER MENU ");
				System.out.println("1. Start server ");
				System.out.println("2. Switch off server  ");
				option = InputException.getInt("Introduce the number choice:  ");
				
				int clientCounter = 0;

				switch (option) {

				case 1:
					sso = new ServerSocket(9009);
					serverMainON();
					break;

				case 2:
					System.out.println("YOU HAVE SWITCHED OFF SERVER APPLICATION");
					System.exit(0);
					break;

				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

	public static void serverMainON()throws IOException, NoSuchAlgorithmException, ParseException{
			
		System.out.println("SERVER");
		
		//Server connects to DB
		JDBCManager jdbcManager = new JDBCManager();

		// initialize database JDBC
		elderlyManager = new JDBCElderlyManager(jdbcManager);
		doctorManager = new JDBCDoctorManager(jdbcManager);
		tasksManager = new JDBCTasksManager(jdbcManager);
		
		// initialize database JPA
		userManager = new JPAUserManager();

		
		while (true) {
			
		
			Socket so = sso.accept();
			
			ServerMain.clientCounter++;
			//System.out.println(ServerPrueba.clientCounter + "anadido");
				
			System.out.println("Cliente conectado");
			
			// Server: reads and sends lines
			 ClientHandler clientHandler = new ClientHandler(so, userManager, doctorManager, elderlyManager, tasksManager);
	         Thread clientThread = new Thread(clientHandler);
	         clientThread.start();
	          
        }

	}
	
	public static void switchServerOFF()throws IOException, NoSuchAlgorithmException, ParseException{
		
		try {

			int option;
			do {
				System.out.println("\nSWITCH OFF MENU ");
				System.out.println("\nThere are no clients connected");
				System.out.println("1. Turn off server application");
				System.out.println("2. Continue operating server application");
				
				option = InputException.getInt("Introduce the number choice:  ");

				switch (option) {

				case 1:
					System.out.println("YOU HAVE SWITCHED OFF SERVER APPLICATION");
					System.exit(0);
					break;

				case 2:
					serverMainON();
					break;

				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
}


