package components;
	
import java.util.ArrayList;

public class Student extends User {
	private static final long serialVersionUID = 1L;
    private String studentNumber;
    private DegreeProgram degree;  
    private ArrayList<Course> coursesTaken;

    public Student(String username, String password, String firstName, String lastName, String email,
                   String studentNumber, DegreeProgram degree) { 
        super(username, password, firstName, lastName, email);
        this.studentNumber = studentNumber;
        this.degree = degree;
        this.coursesTaken = new ArrayList<>();
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public DegreeProgram getDegree() {  
        return degree;
    }

    public ArrayList<Course> getCoursesTaken() {
        return coursesTaken;
    }

    public String getUserType() {
        return "Student";
    }
    
    public void addCourse(Course course) {
        this.coursesTaken.add(course);
    }

    public void removeCourse(Course course) {
        this.coursesTaken.remove(course);
    }
}

