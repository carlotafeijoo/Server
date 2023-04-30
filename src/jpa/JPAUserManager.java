package jpa;

import java.util.List;
import javax.persistence.EntityManager;
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
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void newUser(User u) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void newRole(Role r) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Role getRole(String email) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Role> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public User checkPassword(String email, String passwd) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
