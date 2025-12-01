package components;
import java.io.*;
import java.util.ArrayList;
public class Save_Load {
   private static final String USERS = "src/storage";
   private static final String ALL_USERS = USERS + "/allUsers.txt";
   // Save list of students
   public static void saveAllStudents(ArrayList<Student> allStudents) {
       new File(USERS).mkdirs();
       try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ALL_USERS))) {
           oos.writeObject(allStudents);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   //Load list of students
 
   public static ArrayList<Student> loadAllStudents() {
       File file = new File(ALL_USERS);
       if (!file.exists()) {
           return new ArrayList<>();
       }
       try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
           return (ArrayList<Student>) ois.readObject();
       } catch (IOException | ClassNotFoundException e) {
           System.err.println("Error loading users: " + e.getMessage());
           return new ArrayList<>();
       }
   }
   //Save student class
   public static void saveStudent(Student student) {
       ArrayList<Student> allStudents = loadAllStudents();
       // Remove duplicates
       allStudents.removeIf(s -> s.getEmail().equals(student.getEmail()));
       allStudents.add(student);
    
       saveAllStudents(allStudents);
   }
   //Load student class
   public static Student loadStudent(String email) {
       ArrayList<Student> allStudents = loadAllStudents();
     
       for (Student s : allStudents) {
           if (s.getEmail().equals(email)) {
               return s;
           }
       }
       return null;
   }
}

