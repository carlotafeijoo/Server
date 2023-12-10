package ui;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import exceptions.InputException;
import POJOS.*;

public class ServerMenuResidencialArea {

	static OutputStream os = null;
	static PrintWriter pw = null;

	static BufferedReader br = null;
	static Socket so = null;
	private static BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * The main method to initialize the client application and connect to the server.
	 * 
	 * @param args the command-line arguments
	 * @throws IOException if an I/O error occurs when creating the socket or getting the input stream
	 */
	public static void main(String[] args) throws IOException {

		System.out.println("\nADMIN! WELCOME TO THE RESIDENCIAL AREA DATA BASE");

		so = new Socket("localhost", 9009);
		br = new BufferedReader(new InputStreamReader(so.getInputStream())); //sockets client
		os = so.getOutputStream();
		pw = new PrintWriter(os, true);

		mainMenu();

	}
	/**
	 * Releases the resources associated with the socket and I/O streams.
	 * 
	 * @param printWriter the PrintWriter to be closed
	 * @param br the BufferedReader to be closed
	 * @param outputStream the OutputStream to be closed
	 * @param socket the Socket to be closed
	 */
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
	/**
	 * Displays the main menu and handles user input.
	 */
	public static void mainMenu() {

		try {

			int option;
			do {
				System.out.println("\nMAIN MENU ");
				System.out.println("1. Stop the server  ");
				System.out.println("2. Exit ");
				option = InputException.getInt("Introduce the number choice:  ");
				pw.println(option);

				switch (option) {

				case 1:
					//CLOSE CLIENT SOCKET AND APP
					stopserver();
					mainMenu();

					break;

				case 2:
					//CLOSE CLIENT
					System.out.println("THE SERVER EXECUTION WILL CONTINUE");
					pw.println("stop");
					releaseResources(pw, br, os, so);
					System.exit(1);
					break;

				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a request to the server to stop and waits for the response. If the response is "exit admin client", 
	 * the client resources are released and the application exits. If the response is not received within 10 minutes, 
	 * a message is displayed, and the method returns.
	 * 
	 * @throws Exception if an error occurs during the process
	 */
	private static void stopserver() throws Exception {


		long start_time = System.currentTimeMillis();
		long current_time = System.currentTimeMillis();

		while(true) {
			pw.println("killServer");
			current_time = System.currentTimeMillis();
			long timer = current_time - start_time;
			if (br.readLine().equalsIgnoreCase("exit admin client")){
				releaseResources(pw, br, os, so);
				System.exit(0);
			}else {
				if (timer >= 600000) { //10 minutes
					System.out.println("Waiting time ended, please try again");
					return;
				}

			}
		}
	}
}


