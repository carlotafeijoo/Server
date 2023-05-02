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
		
		if( this.getRoles().isEmpty()) {
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


	@Override
	public void newRole(Role r) {
	    em.getTransaction().begin();
		em.persist(r);
		em.getTransaction().commit();
		
	}


	@Override
	public Role getRole(String email) { //DUDAS CON EMAIL, NO SERÍA MEJOR NAME???
		Query q = em.createNativeQuery("SELECT * FROM roles WHERE email = ?", Role.class);
		q.setParameter(1, email);
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
		
		Query q = em.createNativeQuery("Select * from users where email =? AND password = ?", User.class);
		q.setParameter(1, email);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(passwd.getBytes());
			byte[] digest = md.digest();
			q.setParameter(2,digest);
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		
		try {
			u = (User) q.getSingleResult();
			
		}catch(NoResultException e) {}
		
		return u;
	
	}


	
}
