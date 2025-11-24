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
        
        // Title
        Label titleLabel = new Label("Academic Course Catalogue - Admin View");
        titleLabel.setLayoutX(50);
        titleLabel.setLayoutY(20);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label subtitleLabel = new Label("1S 2025-2026");
        subtitleLabel.setLayoutX(50);
        subtitleLabel.setLayoutY(55);
        subtitleLabel.setStyle("-fx-font-size: 14px;");
        
        // Total courses label
        totalLabel = new Label("Total Courses: " + allCourses.size());
        totalLabel.setLayoutX(50);
        totalLabel.setLayoutY(90);
        totalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                           "-fx-background-color: #e3f2fd; -fx-padding: 10;");
        totalLabel.setPrefWidth(1300);
        
        // Create table
        courseTable = new TableView<>();
        courseTable.setLayoutX(50);
        courseTable.setLayoutY(140);
        courseTable.setPrefWidth(1300);
        courseTable.setPrefHeight(520);
        
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
        
        // Add all components to root
        root.getChildren().addAll(titleLabel, subtitleLabel, totalLabel, courseTable);
        
        Scene scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setTitle("REGICS - Admin Dashboard");
        stage.show();
    }
    
    private void loadCourseData() {
        ObservableList<CourseRow> data = FXCollections.observableArrayList();
        
        for (Course course : allCourses) {
            // Generate random enrollment count (10-40 students)
            int enrolledCount = (int)(Math.random() * 31) + 10;
            
            String schedule = course.getDays() + " " + course.getTime();
            
            CourseRow row = new CourseRow(
                course.getCourseID(),
                course.getTitle(),
                course.getUnits(),
                course.getSection(),
                schedule,
                course.getRoom(),
                String.valueOf(enrolledCount)
            );
            data.add(row);
        }
        
        courseTable.setItems(data);
    }
}