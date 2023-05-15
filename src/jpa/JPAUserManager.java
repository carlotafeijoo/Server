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
		
		if(this.getRoles().isEmpty()) {
			Role elderly = new Role("Elderly");
			Role staff= new Role("Staff");
			Role familycontact = new Role ("FamilyContact");
			Role schedule = new Role ("Schedule");
			this.newRole(elderly);
			this.newRole(staff);
			this.newRole(familycontact);
			this.newRole(schedule);
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
			Query q = em.createNativeQuery("SELECT * FROM User WHERE email = ? AND password = ?", User.class);
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
		Query q = em.createNativeQuery("SELECT * FROM Role WHERE name LIKE ?", Role.class);
		q.setParameter(1, name); 
		Role r= (Role) q.getSingleResult();
		return r;
	}

	@Override
	public List<Role> getRoles() {
		Query q = em.createNativeQuery("SELECT * FROM Role", Role.class);

		List<Role> roles = (List<Role>) q.getResultList();

		return roles;
	}

	@Override
	public User checkPassword(String username, byte[] passwd) {
		User u = null;
		Query q = em.createNativeQuery("SELECT * FROM User WHERE username =? AND password = ?", User.class);
		q.setParameter(1, username);
		q.setParameter(2, passwd);
		
		try {
			u= (User) q.getSingleResult();
		}catch (NoResultException e) {}
		
		return u;
	}
	
	@Override
	public boolean checkUsername(String username) {
	    try {
	        Query q = em.createNativeQuery("SELECT * FROM User WHERE username = ?", User.class);
	        q.setParameter(1, username);
	        return !q.getResultList().isEmpty();
	    } catch (Exception e) {
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
			Query q = em.createNativeQuery("SELECT * FROM User WHERE email = ? ", User.class);
			q.setParameter(1, oldEmail);
			q.setParameter(2, hash);
			User u = (User) q.getSingleResult();
			em.getTransaction().begin();
			u.setUsername(newEmail);
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
 		}catch(Exception e) {
 			e.printStackTrace();
 		}
	}

	
	
	
}
