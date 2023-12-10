package jpa;

import java.security.MessageDigest;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import Interfaces.UserManager;
import POJOS.Role;
import POJOS.User;

public class JPAUserManager implements UserManager {

	private EntityManager em;
	/**
	 * Constructs a new JPAUserManager and initializes the database connection.
	 */
	public JPAUserManager() {
		this.connect();
	}
	/**
	 * Establishes a connection to the JPA entity manager and initializes the database with default roles if necessary.
	 */
	@Override
	public void connect() {
		em = Persistence.createEntityManagerFactory("ResidencialArea-provider").createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("PRAGMA foreign_keys=ON").executeUpdate();
		em.getTransaction().commit();

		if (this.getRoles().isEmpty()) {
			Role elderly = new Role("Elderly");
			Role doctor = new Role("Doctor");
			this.newRole(elderly);
			this.newRole(doctor);

		}
	}
	/**
	 * Closes the connection to the JPA entity manager.
	 */
	@Override
	public void disconnect() {
		em.close();
	}
	/**
	 * Adds a new user to the database.
	 * 
	 * @param u the User object representing the user to be added
	 */
	@Override
	public void newUser(User u) {
		em.getTransaction().begin();
		em.persist(u);
		em.getTransaction().commit();
	}
	/**
	 * Deletes a user from the database based on the email and password.
	 * 
	 * @param email the email of the user to be deleted
	 * @param password the password of the user to be deleted
	 */
	@Override
	public void deleteUser(String email, String password) {
		try {
			System.out.println("The user");
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] hash = md.digest();
			Query q = em.createNativeQuery("SELECT * FROM User WHERE email = ? AND password = ?", User.class);
			q.setParameter(1, email);
			q.setParameter(2, hash);
			if (!q.getResultList().isEmpty()) {
				User u = (User) q.getSingleResult();
				em.getTransaction().begin();
				em.remove(u);
				em.getTransaction().commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Adds a new role to the database.
	 * 
	 * @param r the Role object representing the role to be added
	 */
	@Override
	public void newRole(Role r) {
		em.getTransaction().begin();
		em.persist(r);
		em.getTransaction().commit();
	}
	/**
	 * Retrieves a role by its name.
	 * 
	 * @param name the name of the role
	 * @return the Role object
	 */
	@Override
	public Role getRole(String name) {
		//System.out.println("nameee" + name);
		Query q = em.createNativeQuery("SELECT * FROM Role WHERE name LIKE ?", Role.class);
		q.setParameter(1, name);
		Role r = (Role) q.getSingleResult();
		return r;
	}
	/**
	 * Retrieves a list of all roles from the database.
	 * 
	 * @return a list of Role objects
	 */
	@Override
	public List<Role> getRoles() {
		Query q = em.createNativeQuery("SELECT * FROM Role", Role.class);

		List<Role> roles = q.getResultList();

		return roles;
	}
	/**
	 * Checks the password of a user.
	 * 
	 * @param username the username of the user
	 * @param passwd the password digest of the user
	 * @return the User object if the password is correct, otherwise null
	 */
	@Override
	public User checkPassword(String username, byte[] passwd) {
		User u = null;
		Query q = em.createNativeQuery("SELECT * FROM User WHERE username =? AND password = ?", User.class);
		q.setParameter(1, username);
		q.setParameter(2, passwd);

		try {
			u = (User) q.getSingleResult();
		} catch (NoResultException e) {
		}

		return u;
	}
	/**
	 * Checks if a username is already in use.
	 * 
	 * @param username the username to be checked
	 * @return true if the username is already in use, otherwise false
	 */
	@Override
	public boolean checkUsername(String username) {
		try {
			//Query q = em.createNativeQuery("SELECT * FROM User WHERE username = ?", User.class);
			Query q = em.createNativeQuery("SELECT * FROM User WHERE username = ?"+username);
			q.setParameter(1, username);
			return !q.getResultList().isEmpty();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * Updates the email of a user in the database.
	 * 
	 * @param newEmail the new email
	 * @param oldEmail the old email
	 * @param password the password of the user
	 */
	@Override
	public void updateUserEmail(String newEmail, String oldEmail, String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] hash = md.digest();
			Query q = em.createNativeQuery("SELECT * FROM User WHERE email = ? ", User.class);
			q.setParameter(1, oldEmail);
			q.setParameter(2, hash);
			User u = (User) q.getSingleResult();
			em.getTransaction().begin();
			u.setUsername(newEmail);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Updates the password of a user in the database.
	 * 
	 * @param email the email of the user
	 * @param newPassword the new password
	 * @param oldPassword the old password
	 */
	@Override
	public void updateUserPassword(String email, String newPassword, String oldPassword) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(oldPassword.getBytes());
			byte[] hash = md.digest();
			Query q = em.createNativeQuery("SELECT * FROM User WHERE email = ? AND password = ?", User.class);
			q.setParameter(1, email);
			q.setParameter(2, hash);
			User u = (User) q.getSingleResult();
			MessageDigest md2 = MessageDigest.getInstance("MD5");
			md2.update(newPassword.getBytes());
			byte[] hash2 = md2.digest();
			em.getTransaction().begin();
			u.setPassword(hash2);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
