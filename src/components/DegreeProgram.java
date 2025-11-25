package components;

import java.util.ArrayList;
import application.CSVReader;

public class DegreeProgram {
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