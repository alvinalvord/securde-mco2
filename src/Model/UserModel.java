package Model;

import Controller.*;

public class UserModel {

	public static final int ADMIN = 5;
	public static final int MANAGER = 4;
	public static final int STAFF = 3;
	public static final int CLIENT = 2;
	public static final int DISABLED = 1;

	private User user;

	public UserModel () {
		user = new User (null, null);
	}

	public boolean isAdmin () {
		return user.getRole () == ADMIN;
	}

	public boolean isManager () {
		return user.getRole () == MANAGER;
	}

	public boolean isStaff () {
		return user.getRole () == STAFF;
	}

	public boolean isClient () {
		return user.getRole () == CLIENT;
	}

	public boolean isDisabled () {
		return user.getRole () == DISABLED;
	}

	public User getUser () {
		return user;
	}

	public void setUser (User user) {
		this.user = user;
	}

	public void logout () {
		user = new User (null, null);
	}

	public boolean reauth (String password) {
		String hashedPassword = PasswordEncryption.hash (password);

		return user.getPassword ().equals (hashedPassword);
	}
}
