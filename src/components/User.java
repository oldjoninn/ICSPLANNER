package components;
import java.io.Serializable;
public abstract class User implements Serializable {
	private static final long serialVersionUID = 1L;
    private String email; 
    private String password;
    private String firstName;
    private String lastName;

    public User(String username, String password, String firstName, String lastName, String email) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() { return email; } 
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public abstract String getUserType();
}