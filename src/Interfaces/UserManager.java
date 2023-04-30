package Interfaces;

import java.util.List;
import POJOS.Role;
import POJOS.User;

public interface UserManager {
	
	public void connect();
	public void disconnect();
	public void newUser(User u);
	public void newRole(Role r);
	public Role getRole(String email);
	public List<Role> getRoles();
	public User checkPassword(String email, String passwd);
}
