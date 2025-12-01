package components;

import java.util.ArrayList;
import java.io.Serializable;
import application.CSVReader;

public class DegreeProgram implements Serializable {
	private static final long serialVersionUID = 1L;
    private String name;
    private String csvFile;
    private ArrayList<Course> coursesOffered;

    public DegreeProgram(String name, String csvFile) {
        this.name = name;
        this.csvFile = csvFile;
        this.coursesOffered = new ArrayList<>();
    }

    public String getName() { return name; }
    public String getcsvFile() { return csvFile; }
    public ArrayList<Course> getCoursesOffered() { return coursesOffered; }
    
    public void loadCourses() {
        coursesOffered = CSVReader.readProgramCourses(csvFile, name);
    }
}