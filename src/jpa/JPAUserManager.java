package jpa;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import Interfaces.UserManager;
import POJOS.Role;
import POJOS.User;

public class JPAUserManager implements UserManager{
	
	private EntityManager em;

	public JPAUserManager() {
		this.connect();
	}

	@Override
	public void connect() {
		em = Persistence.createEntityManagerFactory("ResidencialArea-provider").createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("PRAGMA foreign_keys=ON").executeUpdate();
		em.getTransaction().commit();
		
		
		List<Role> existingRoles = this.getRoles();
		if(existingRoles.size() < 2 || existingRoles.isEmpty()) {
			Role elderly = new Role("elderly");
			Role staff= new Role("staff");
			this.newRole(elderly);
			this.newRole(staff);
		}		
	}
	
	@Override
	public void disconnect() {
		em.close();
	}

	@Override
	public void newUser(User u) {
		em.getTransaction().begin();
		em.persist(u);
		em.getTransaction().commit();
	}
	
	public void deleteUser(String email, String password) {
		try {
			System.out.println("The user");
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] hash = md.digest();
			Query q = em.createNativeQuery("SELECT * FROM users WHERE email = ? AND password = ?", User.class);
			q.setParameter(1, email);
			q.setParameter(2, hash);
			if (!q.getResultList().isEmpty()) {
				User u = (User) q.getSingleResult();
				em.getTransaction().begin();
				em.remove(u);
				em.getTransaction().commit();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void newRole(Role r) {
	    em.getTransaction().begin();
		em.persist(r);
		em.getTransaction().commit();
	}

	@Override
	public Role getRole(String name) { 
		Query q = em.createNativeQuery("SELECT * FROM roles WHERE name = ?", Role.class);
		q.setParameter(1, name);
		return (Role) q.getSingleResult();
	}

	@Override
	public List<Role> getRoles() {
		Query q = em.createNativeQuery("SELECT * FROM roles", Role.class);
		List<Role> roles = (List<Role>) q.getResultList();
		return roles;
	}

	@Override
	public User checkPassword(String email, String passwd) {
		User u = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(passwd.getBytes());
			byte[] digest = md.digest();
			Query q = em.createNativeQuery("Select * FROM users WHERE email =? AND password = ?", User.class);
			q.setParameter(1, email);
			q.setParameter(2,digest);
			return (User) q.getSingleResult();
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return u;
	}
	
	@Override
	public boolean checkEmail(String email) {
		try {
			Query q = em.createNativeQuery("SELECT * FROM users WHERE email = ?", User.class);
			q.setParameter(1, email);
			User temp = (User)q.getSingleResult();
			if(temp.getEmail().equalsIgnoreCase(email)) {
				return true;
			}else {
				return false;
			}
		}catch(NoResultException nre) {
			return false;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public void updateUserEmail(String newEmail, String oldEmail, String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] hash = md.digest();
			Query q = em.createNativeQuery("SELECT * FROM users WHERE email = ? ", User.class);
			q.setParameter(1, oldEmail);
			q.setParameter(2, hash);
			User u = (User) q.getSingleResult();
			em.getTransaction().begin();
			u.setEmail(newEmail);
			em.getTransaction().commit();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
 	public void updateUserPassword(String email, String newPassword, String oldPassword) {
 		try {
 			MessageDigest md = MessageDigest.getInstance("MD5");
 			md.update(oldPassword.getBytes());
 			byte[] hash = md.digest();
 			Query q = em.createNativeQuery("SELECT * FROM users WHERE email = ? AND password = ?", User.class);
 			q.setParameter(1, email);
 			q.setParameter(2, hash);
 			User u = (User) q.getSingleResult();
 			MessageDigest md2 = MessageDigest.getInstance("MD5");
 			md2.update(newPassword.getBytes());
 			byte[] hash2 = md2.digest();
 			em.getTransaction().begin();
 			u.setPassword(hash2);
 			em.getTransaction().commit();
 		}catch(Exception e) {
 			e.printStackTrace();
 		}
	}
	
	
}
