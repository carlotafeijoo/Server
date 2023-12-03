package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//connects w/DB, creates tables
public class JDBCManager {

	private Connection c = null;

	public JDBCManager() {
		try {
			// Open the DB connection
			Class.forName("org.sqlite.JDBC");
			String dbPath = "/db/ResidencialArea.db";

			c = DriverManager.getConnection("jdbc:sqlite:." + dbPath);
			c.createStatement().execute("PRAGMA foreign_keys=ON");

			//System.out.println("Database connection opened.");
			// create tables
			this.createTables();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.print("Libraries not loaded");
		}
	}

	public void disconnect() {
		try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return c;
	}

	private void createTables() {
		// Create Tables
		try {
			Statement stmt = c.createStatement();

			// TABLE ELDERLY (OK)
			String sql = "CREATE TABLE Elderly (" + "	elderly_id	    INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "	name			TEXT NOT NULL," + "	dob			DATE," + "DNI          INTEGER NOT NULL,"
					+ " doctor_id		INTEGER REFERENCES Doctor(doctor_id)" + ");";
			stmt.executeUpdate(sql);

			// TABLE DOCTOR (OK)
			sql = "CREATE TABLE Doctor (" + " doctor_id	INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " name		TEXT NOT NULL," + " dob 		DATE," + " address 	TEXT NOT NULL,"
					+ " phone 		INTEGER NOT NULL," + " email 		TEXT" + ");";
			stmt.executeUpdate(sql);
			// TABLE TASK AÑADIR ELDERLY ID, AÑADIR DE DECORACION UN COLUMNA DE HORARIO SI
			// QUEREIS
			sql = "CREATE TABLE Task (" + "	task_id			INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "	description		TEXT NOT NULL," + " doctor_id		INTEGER REFERENCES Doctor(doctor_id),"
					+ " duration		INTEGER NOT NULL," + " elderly_id 		INTEGER REFERENCES Elderly(elderly_id)"
					+ ");";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE Report (" + " report_id	    INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " file_name		TEXT NOT NULL," + " task_id		INTEGER REFERENCES Task(task_id),"
					+ " elderly_id 		INTEGER REFERENCES Elderly(elderly_id)" + ");";
			stmt.executeUpdate(sql);

			/*
			 * //TABLE SCHEDULE ELIMINAR TABLA Y ELIINAR TODAS LAS REFERENCIAS A ESTA TABLA
			 * EN TODOS LOS METODOS DE JDBC sql= "CREATE TABLE Schedule (" +
			 * " schedule_id		INTEGER PRIMARY KEY AUTOINCREMENT, " +
			 * " weekDay			TEXT NOT NULL, " +
			 * " doctor_id	 	INTEGER REFERENCES Doctor(doctor_id), " +
			 * " task_id	 		INTEGER REFERENCES Task(task_id), " +
			 * " elderly_id	 	INTEGER REFERENCES Elderly(elderly_id) " + ");";
			 * stmt.executeUpdate(sql);
			 */

		} catch (SQLException e) {
			// Do not compile if tables already exist
			if (!e.getMessage().contains("already exists")) {
				e.printStackTrace();
			}
		}
	}

}