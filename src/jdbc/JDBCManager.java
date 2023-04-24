package jdbc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCManager {
	
	
	private Connection c = null;

	public JDBCManager() {
		
		try 
		{			
			// Open the DB connection
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:./db/hospital.db");
			System.out.println("Database connection opened.");
			
			//create tables
			this.createTables();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			System.out.print("Libraries not loaded");
		}
	}
	
	private void createTables() {
		// Create Tables
		try {
		Statement stmt = c.createStatement();
		String sql = "CREATE TABLE owners ("
		+ "	id	    INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "	name	TEXT NOT NULL,"
		+ "	phone	INTEGER NOT NULL,"
		+ "	email	TEXT NOT NULL,"
		+ "	cardNumber	INTEGER NOT NULL"
		+ ");";
		stmt.executeUpdate(sql);
		sql = "CREATE TABLE dogs ("
		+ "	id	    INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "	name	TEXT NOT NULL,"
		+ "	breed	TEXT NOT NULL,"
		+ "	coat	TEXT,"
		+ "	dob	    DATE,"
		+ "	cured	BOOLEAN,"
		+ "	ownerId	INTEGER NOT NULL REFERENCES owners(id) ON DELETE RESTRICT"
		+ ");";
		stmt.executeUpdate(sql);
		sql = "CREATE TABLE vets ("
		+ "	id	    INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "	name	TEXT NOT NULL,"
		+ "	speciality	TEXT"
		+ ");";
		stmt.executeUpdate(sql);
		sql = "CREATE TABLE examines ("
		+ "	dogId	INTEGER,"
		+ "	vetId	INTEGER,"
		+ "	FOREIGN KEY(dogId) REFERENCES dogs(id) ON DELETE CASCADE,"
		+ "	FOREIGN KEY(vetId) REFERENCES vets(id) ON DELETE CASCADE,"
		+ "	PRIMARY KEY(dogId,vetId)\r\n"
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
	
	public void disconnect()
	{		
		try {
			c.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}
	}

}