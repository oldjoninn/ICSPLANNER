package components;

public class Admin extends User{
	private static final long serialVersionUID = 1L;


	public Admin() {
		super("admin", "admin123", "Admin", "User", "admin@up.edu.ph");
	}

	public String getUserType() {
		return "Admin";
	}
	
}

