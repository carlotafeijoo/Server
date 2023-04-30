package jdbc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCManager {
	
	private Connection c = null;

	public JDBCManager(){
		try {			
			// Open the DB connection
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:./db/ResidentialArea.db");
			System.out.println("Database connection opened.");
			
			//create tables
			this.createTables();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		catch (ClassNotFoundException e){
			System.out.print("Libraries not loaded");
		}
	}
	
	private void createTables() {
		// Create Tables
		try {
		Statement stmt = c.createStatement();
		String sql = "CREATE TABLE Elderly ("
		+ "	elderly_id	    INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "	name			TEXT NOT NULL,"
		+ "	age				INTEGER NOT NULL,"
		+ ");";
		stmt.executeUpdate(sql);
		sql = "CREATE TABLE Family_contact ("
		+ "	family_id	INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "	elderly_id	INTEGER FOREIGN KEY AUTOINCREMENT,"
		+ "	name		TEXT NOT NULL,"
		+ "	adress		TEXT NOT NULL,"
		+ "	phone		INTEGER NOT NULL,"
		+ "	email		TEXT NOT NULL,"
		+ "	elderly_id	INTEGER NOT NULL REFERENCES Elderly(elderly_id) ON DELETE RESTRICT"
		+ ");";
		stmt.executeUpdate(sql);
		sql = "CREATE TABLE Staff ("
		+ "	staff_id	INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "	name		TEXT NOT NULL,"
		+ "	DOB 		DATE,"
		+ "	adress		TEXT NOT NULL,"
		+ "	phone		INTEGER NOT NULL,"
		+ ");";
		stmt.executeUpdate(sql);
		sql = "CREATE TABLE Tasks ("
		+ "	task_id			INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "	description		TEXT NOT NULL,"
		+ ");";
		stmt.executeUpdate(sql);
		sql = "CREATE TABLE Performs ("
		+ "	performs_id		INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "	task_id			INTEGER AUTOINCREMENT,"
		+ "	staff_id	 	INTEGER AUTOINCREMENT,"
		+ "	staff_id		INTEGER NOT NULL REFERENCES Staff(staff_id) ON DELETE RESTRICT\""
		+ " task_id			INTEGER NOT NULL REFERENCES Tasks(task_id) ON DELETE RESTRICT\""
		+ ");";
		stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// Do not complain if tables already exist
			if (!e.getMessage().contains("already exists")) {
				e.printStackTrace();
			}
		}
	}
	
	public Connection getConnection() {
		return c;
	}
	
	public void disconnect(){		
		try {
			c.close();
		}
		catch(SQLException e){
			e.printStackTrace();			
		}
	}

}