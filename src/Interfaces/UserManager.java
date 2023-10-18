package Interfaces;

import java.util.List;
import POJOS.Role;
import POJOS.User;

public interface UserManager {

	public void connect();

	public void disconnect();

	public void newUser(User u);

	public void deleteUser(String email, String password);

	public void newRole(Role r);

	public Role getRole(String email);

	public List<Role> getRoles();

	public User checkPassword(String username, byte[] digest);

	public boolean checkUsername(String username);

	public void updateUserEmail(String newEmail, String oldEmail, String password);

	public void updateUserPassword(String email, String newPassword, String oldPassword);
}
