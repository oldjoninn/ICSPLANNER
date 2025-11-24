package components;

public abstract class User {
	private String email; 
	private String password;
	private String firstName;
	private String lastName;
	private String userType; 


	public User(String username, String password, String firstName, String lastName, String emaill) {
		 this.email = username;
	     this.password = password;
	     this.firstName = firstName;
	     this.lastName = lastName;
	     this.userType = email;
		}

	public String getEmail() { return email; } 
	public String getPassword() { return password; }
	public String getFirstName() { return firstName; }
	public String getLastName() { return lastName; }
	public String getUserType() { return userType; }		
	
}




 	
