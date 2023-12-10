package Interfaces;

import java.util.List;

import POJOS.Role;
import POJOS.User;

public interface UserManager {
	/**
	 * Establishes a connection to the database.
	 */
	public void connect();
	/**
	 * Closes the connection to the database.
	 */
	public void disconnect();
	/**
	 * Adds a new user to the database.
	 * 
	 * @param u the User object representing the user to be added
	 */
	public void newUser(User u);
	/**
	 * Deletes a user from the database based on the email and password.
	 * 
	 * @param email the email of the user to be deleted
	 * @param password the password of the user to be deleted
	 */
	public void deleteUser(String email, String password);
	/**
	 * Adds a new role to the database.
	 * 
	 * @param r the Role object representing the role to be added
	 */
	public void newRole(Role r);
	/**
	 * Retrieves the role of a user based on the email.
	 * 
	 * @param email the email of the user
	 * @return the Role object
	 */
	public Role getRole(String email);
	/**
	 * Retrieves a list of all roles from the database.
	 * 
	 * @return a list of Role objects
	 */
	public List<Role> getRoles();
	/**
	 * Checks the password of a user.
	 * 
	 * @param username the username of the user
	 * @param digest the password digest of the user
	 * @return the User object if the password is correct, otherwise null
	 */
	public User checkPassword(String username, byte[] digest);
	/**
	 * Checks if a username is already in use.
	 * 
	 * @param username the username to be checked
	 * @return true if the username is already in use, otherwise false
	 */
	public boolean checkUsername(String username);
	/**
	 * Updates the email of a user in the database.
	 * 
	 * @param newEmail the new email
	 * @param oldEmail the old email
	 * @param password the password of the user
	 */
	public void updateUserEmail(String newEmail, String oldEmail, String password);
	/**
	 * Updates the password of a user in the database.
	 * 
	 * @param email the email of the user
	 * @param newPassword the new password
	 * @param oldPassword the old password
	 */
	public void updateUserPassword(String email, String newPassword, String oldPassword);
}
