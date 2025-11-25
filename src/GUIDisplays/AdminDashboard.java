package GUIDisplays;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import components.Course;
import java.util.List;

public class AdminDashboard {
    
    private Stage stage;
    private List<Course> allCourses;
    private TableView<CourseRow> courseTable;
    private Label totalLabel;
    
    public AdminDashboard(Stage primaryStage, List<Course> courses) {
        this.stage = primaryStage;
        this.allCourses = courses;
    }
    
    // Inner class for TableView data
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
        
        // Back to Login Button
        Button backButton = new Button("← Logout");
        backButton.setLayoutX(10);
        backButton.setLayoutY(10);
        backButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                           "-fx-font-weight: bold; -fx-padding: 8px 15px; -fx-cursor: hand;");
        backButton.setOnAction(e -> {
            // Return to main login screen
            try {
                application.Main mainApp = new application.Main();
                mainApp.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        // Title
        Label titleLabel = new Label("Academic Course Catalogue - Admin View");
        titleLabel.setLayoutX(50);
        titleLabel.setLayoutY(50);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label subtitleLabel = new Label("1S 2025-2026");
        subtitleLabel.setLayoutX(50);
        subtitleLabel.setLayoutY(85);
        subtitleLabel.setStyle("-fx-font-size: 14px;");
        
        // Total courses label
        totalLabel = new Label("Total Courses: " + allCourses.size());
        totalLabel.setLayoutX(50);
        totalLabel.setLayoutY(120);
        totalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                           "-fx-background-color: #e3f2fd; -fx-padding: 10;");
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
        
        courseTable.getColumns().addAll(codeCol, titleCol, unitsCol, sectionCol, 
                                        scheduleCol, roomCol, countCol);
        
        // Populate table
        loadCourseData();
        
        // Debug: Print course count
        System.out.println("Admin Dashboard - Total courses loaded: " + allCourses.size());
        if (!allCourses.isEmpty()) {
            System.out.println("First course: " + allCourses.get(0).getCourseID() + " - " + allCourses.get(0).getTitle());
        } else {
            System.out.println("WARNING: No courses in the list!");
        }
        
        // Add all components to root
        root.getChildren().addAll(backButton, titleLabel, subtitleLabel, totalLabel, courseTable);
        
        Scene scene = new Scene(root, 1400, 700);
        // Try to load CSS, but don't fail if it doesn't exist
        try {
            String css = getClass().getResource("/application/application.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception ex) {
            System.out.println("CSS file not found, using default styles");
        }
        stage.setScene(scene);
        stage.setTitle("REGICS - Admin Dashboard");
        stage.show();
    }
    
    private void loadCourseData() {
        ObservableList<CourseRow> data = FXCollections.observableArrayList();
        
        for (Course course : allCourses) {
            // Generate random  count between 10 and 40
            int enrolledCount = (int)(Math.random() * 31) + 10;
            
            // Build schedule string, handling null values
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
            
            // Debug: Print first few courses
            if (data.size() <= 3) {
                System.out.println("Added row: " + row.getCourseCode() + " | " + row.getTitle() + " | " + row.getSchedule());
            }
        }
        
        System.out.println("Total rows added to table: " + data.size());
        courseTable.setItems(data);
    }
}