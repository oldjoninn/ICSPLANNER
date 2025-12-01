package application;
import components.Course;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
   public static ArrayList<Course> readProgramCourses(String csvPath, String programName) {
       ArrayList<Course> courses = new ArrayList<>();
       try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
           String line;
           br.readLine(); // Skip header
           while ((line = br.readLine()) != null) {
               String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Handle commas in quotes
               if (parts.length >= 4) {
                   String courseCode = parts[0].trim();
                   String courseName = parts[1].trim();
                   String units = parts[2].trim();
                   String description = parts[3].trim();
                   
                   Course course = new Course(courseCode, courseName, units, "", "", "", "");
                   course.setDescription(description);
                   courses.add(course);
               }
           }
       } catch (IOException e) {
           System.err.println("Error reading course file: " + csvPath);
           e.printStackTrace();
       }
      
       return courses;
   }
   
   public static void readCourseOfferings(String csvPath, List<Course> courseOfferings) {
       try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
           String line;
           br.readLine(); // Skip "1S 2025-2026"
           br.readLine(); // Skip header row
           
           while ((line = br.readLine()) != null) {
               String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Handle commas in quotes
               if (parts.length >= 7) {
                   String courseCode = parts[0].trim();
                   String courseTitle = parts[1].trim();
                   String units = parts[2].trim();
                   String section = parts[3].trim();
                   String time = parts[4].trim();
                   String days = parts[5].trim();
                   String room = parts[6].trim();
                   
                   Course course = new Course(courseCode, courseTitle, units, section, days, time, room);
                   courseOfferings.add(course);
               }
           }
           System.out.println("Loaded " + courseOfferings.size() + " course offerings from CSV");
       } catch (IOException e) {
           System.err.println("Error reading course offerings file: " + csvPath);
           e.printStackTrace();
       }
   }
}