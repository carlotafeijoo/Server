package jdbc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCManager {
	
	private Connection c=null;

	public JDBCManager(){
	    try {            
	        // Open the DB connection
	        Class.forName("org.sqlite.JDBC");
	        String dbPath = "/db/ResidencialArea.db";
	        c = DriverManager.getConnection("jdbc:sqlite:." +dbPath );
	        c.createStatement().execute("PRAGMA foreign_keys=ON");
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


	public void disconnect(){		
		try {
			c.close();
		}
		catch(SQLException e){
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
			
			
			//TABLE ELDERLY
			String sql = "CREATE TABLE Elderly ("
			+ "	elderly_id	    INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "	name			TEXT NOT NULL,"
			+ "	age				INTEGER NOT NULL"
			+ ");";
			stmt.executeUpdate(sql);
			
			//TABLE FAMILY CONTACT
			sql = "CREATE TABLE FamilyContact ("
			+ "	family_id	INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "	elderly_id	INTEGER REFERENCES Elderly(elderly_id),"
			+ "	name		TEXT NOT NULL,"
			+ "	address		TEXT NOT NULL,"
			+ "	phone		INTEGER NOT NULL,"
			+ "	email		TEXT NOT NULL UNIQUE "
			+ ");";
			stmt.executeUpdate(sql);
			
			
			//
			
			sql = "CREATE TABLE Staff ("
			+ "	staff_id	INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "	name		TEXT NOT NULL,"
			+ "	DOB 		DATE,"
			+ "	address		TEXT NOT NULL,"
			+ "	phone		INTEGER NOT NULL"
			+ ");";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE Tasks ("
			+ "	task_id			INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "	description		TEXT NOT NULL"
			+ ");";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE Performs ("
			+ "	performs_id		INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "	staff_id		INTEGER NOT NULL REFERENCES Staff(staff_id) ON DELETE RESTRICT,"
			+ " task_id			INTEGER NOT NULL REFERENCES Tasks(task_id) ON DELETE RESTRICT"
			+ ");";
			stmt.executeUpdate(sql);
			
			
		} catch (SQLException e) {
			// Do not compile if tables already exist
			if (!e.getMessage().contains("already exists")) {
				e.printStackTrace();
			}
		}
	}
	
	

}