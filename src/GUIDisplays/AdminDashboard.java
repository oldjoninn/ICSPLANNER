package GUIDisplays;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import components.Course;
import components.Student;
import components.Save_Load;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminDashboard {
    
    private Stage stage;
    private List<Course> allCourses;
    private TableView<CourseRow> courseTable;
    private Label totalLabel;
    private Map<String, Integer> enrollmentCounts;
    
    public AdminDashboard(Stage primaryStage, List<Course> courses) {
        this.stage = primaryStage;
        this.allCourses = (courses == null) ? new ArrayList<>() : courses;
        this.enrollmentCounts = new HashMap<>();
        
        System.out.println("AdminDashboard initialized with " + allCourses.size() + " courses");
        calculateEnrollmentCounts();
    }
    
    private String keyFor(String courseID, String section) {
        String id = (courseID == null || courseID.isEmpty()) ? "N/A" : courseID;
        String sec = (section == null || section.isEmpty()) ? "N/A" : section;
        return id + "-" + sec;
    }
    
    private void calculateEnrollmentCounts() {
        enrollmentCounts.clear();
        
        // Initialize all course sections with 0
        for (Course c : allCourses) {
            String key = keyFor(c.getCourseID(), c.getSection());
            enrollmentCounts.put(key, 0);
        }
        
        // Load all students and count enrollments
        ArrayList<Student> allStudents = Save_Load.loadAllStudents();
        if (allStudents == null) allStudents = new ArrayList<>();
        
        for (Student student : allStudents) {
            for (Course enrolledCourse : student.getCoursesTaken()) {
                String key = keyFor(enrolledCourse.getCourseID(), enrolledCourse.getSection());
                enrollmentCounts.put(key, enrollmentCounts.getOrDefault(key, 0) + 1);
            }
        }
        
        System.out.println("Enrollment counts: " + enrollmentCounts.size() + " sections");
    }
    
    public static class CourseRow {
        private String courseCode;
        private String title;
        private String units;
        private String section;
        private String schedule;
        private String room;
        private String count;
        
        public CourseRow(String courseCode, String title, String units, String section, 
                        String schedule, String room, String count) {
            this.courseCode = courseCode;
            this.title = title;
            this.units = units;
            this.section = section;
            this.schedule = schedule;
            this.room = room;
            this.count = count;
        }
        
        public String getCourseCode() { return courseCode; }
        public String getTitle() { return title; }
        public String getUnits() { return units; }
        public String getSection() { return section; }
        public String getSchedule() { return schedule; }
        public String getRoom() { return room; }
        public String getCount() { return count; }
    }
    
    public void show() {
        Pane root = new Pane();
        root.setPrefSize(1400, 700);
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        // Top Bar
        HBox topBar = new HBox(10);
        topBar.setLayoutX(10);
        topBar.setLayoutY(10);
        
        Button backButton = new Button("← Logout");
        backButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                           "-fx-font-weight: bold; -fx-padding: 8px 15px; -fx-cursor: hand; " +
                           "-fx-font-size: 14px;");
        backButton.setOnAction(e -> {
            try {
                application.Main mainApp = new application.Main();
                mainApp.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        Button refreshButton = new Button("🔄 Refresh Data");
        refreshButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                              "-fx-font-weight: bold; -fx-padding: 8px 15px; -fx-cursor: hand; " +
                              "-fx-font-size: 14px;");
        refreshButton.setOnAction(e -> {
            calculateEnrollmentCounts();
            loadCourseData();
            System.out.println("Data refreshed!");
        });
        
        topBar.getChildren().addAll(backButton, refreshButton);
        
        // Title
        Label titleLabel = new Label("Academic Course Catalogue - Admin View");
        titleLabel.setLayoutX(50);
        titleLabel.setLayoutY(50);
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label subtitleLabel = new Label("1S 2025-2026");
        subtitleLabel.setLayoutX(50);
        subtitleLabel.setLayoutY(85);
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");
        
        // Total stats
        int totalEnrolled = enrollmentCounts.values().stream().mapToInt(Integer::intValue).sum();
        totalLabel = new Label(String.format("Total Courses: %d | Total Enrollments: %d", 
                                              allCourses.size(), totalEnrolled));
        totalLabel.setLayoutX(50);
        totalLabel.setLayoutY(120);
        totalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                           "-fx-background-color: #e3f2fd; -fx-padding: 10px; " +
                           "-fx-background-radius: 5px; -fx-text-fill: #1976d2;");
        totalLabel.setPrefWidth(1300);
        
        // Create table
        courseTable = new TableView<>();
        courseTable.setLayoutX(50);
        courseTable.setLayoutY(170);
        courseTable.setPrefWidth(1300);
        courseTable.setPrefHeight(490);
        
        // Define columns
        TableColumn<CourseRow, String> codeCol = new TableColumn<>("Course Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        codeCol.setPrefWidth(120);
        
        TableColumn<CourseRow, String> titleCol = new TableColumn<>("Course Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(400);
        
        TableColumn<CourseRow, String> unitsCol = new TableColumn<>("Units");
        unitsCol.setCellValueFactory(new PropertyValueFactory<>("units"));
        unitsCol.setPrefWidth(60);
        
        TableColumn<CourseRow, String> sectionCol = new TableColumn<>("Section");
        sectionCol.setCellValueFactory(new PropertyValueFactory<>("section"));
        sectionCol.setPrefWidth(100);
        
        TableColumn<CourseRow, String> scheduleCol = new TableColumn<>("Schedule");
        scheduleCol.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        scheduleCol.setPrefWidth(250);
        
        TableColumn<CourseRow, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
        roomCol.setPrefWidth(200);
        
        TableColumn<CourseRow, String> countCol = new TableColumn<>("Enrolled");
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        countCol.setPrefWidth(100);
        
        // Style enrollment column
        countCol.setCellFactory(column -> new TableCell<CourseRow, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    try {
                        int count = Integer.parseInt(item);
                        if (count >= 30) {
                            setStyle("-fx-background-color: #ffebee; -fx-text-fill: #c62828; -fx-font-weight: bold;");
                        } else if (count >= 20) {
                            setStyle("-fx-background-color: #fff3e0; -fx-text-fill: #ef6c00; -fx-font-weight: bold;");
                        } else if (count >= 10) {
                            setStyle("-fx-background-color: #e8f5e9; -fx-text-fill: #2e7d32;");
                        } else {
                            setStyle("-fx-text-fill: #616161;");
                        }
                    } catch (NumberFormatException e) {
                        setStyle("");
                    }
                }
            }
        });
        
        courseTable.getColumns().addAll(codeCol, titleCol, unitsCol, sectionCol, 
                                        scheduleCol, roomCol, countCol);
        
        // Load data
        loadCourseData();
        
        root.getChildren().addAll(topBar, titleLabel, subtitleLabel, totalLabel, courseTable);
        
        Scene scene = new Scene(root, 1400, 700);
        
        // Load CSS
        try {
            String css = getClass().getResource("/application/application.css").toExternalForm();
            scene.getStylesheets().add(css);
            System.out.println("CSS loaded successfully");
        } catch (Exception ex) {
            System.err.println("CSS file not found: " + ex.getMessage());
        }
        
        stage.setScene(scene);
        stage.setTitle("REGICS - Admin Dashboard");
        stage.show();
    }
    
    private void loadCourseData() {
        ObservableList<CourseRow> data = FXCollections.observableArrayList();
        
        System.out.println("Loading " + allCourses.size() + " courses into table...");
        
        for (Course course : allCourses) {
            String key = keyFor(course.getCourseID(), course.getSection());
            int enrolledCount = enrollmentCounts.getOrDefault(key, 0);
            
            String schedule = "";
            if (course.getDays() != null && !course.getDays().isEmpty()) {
                schedule = course.getDays();
                if (course.getTime() != null && !course.getTime().isEmpty()) {
                    schedule += " " + course.getTime();
                }
            } else if (course.getTime() != null && !course.getTime().isEmpty()) {
                schedule = course.getTime();
            } else {
                schedule = "TBA";
            }
            
            CourseRow row = new CourseRow(
                course.getCourseID() != null ? course.getCourseID() : "N/A",
                course.getTitle() != null ? course.getTitle() : "N/A",
                course.getUnits() != null ? course.getUnits() : "0",
                course.getSection() != null && !course.getSection().isEmpty() ? course.getSection() : "N/A",
                schedule,
                course.getRoom() != null && !course.getRoom().isEmpty() ? course.getRoom() : "TBA",
                String.valueOf(enrolledCount)
            );
            data.add(row);
        }
        
        System.out.println("Table loaded with " + data.size() + " rows");
        courseTable.setItems(data);
        
        int totalEnrolled = enrollmentCounts.values().stream().mapToInt(Integer::intValue).sum();
        totalLabel.setText(String.format("Total Courses: %d | Total Enrollments: %d", 
                                        allCourses.size(), totalEnrolled));
    }
}