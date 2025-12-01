package GUIDisplays;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import components.Course;
import components.Student;
import components.Save_Load;
import components.Admin;
import application.Main;
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
    private Main mainApp;
    
    public AdminDashboard(Stage primaryStage, List<Course> courses, Main mainApp) {
        this.stage = primaryStage;
        this.allCourses = (courses == null) ? new ArrayList<>() : courses;
        this.enrollmentCounts = new HashMap<>();
        this.mainApp = mainApp;
        
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
        // Main container with dark background matching the app theme
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #050816;");
        
        // Top Bar
        HBox topBar = createTopBar();
        root.setTop(topBar);
        
        // Center content with table
        VBox centerContent = createCenterContent();
        root.setCenter(centerContent);
        
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
        stage.setMaximized(true);
        stage.show();
    }
    
    private HBox createTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(20, 30, 20, 30));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #050816;");
        
        // Back/Logout Button
        Button backButton = new Button("← Logout");
        backButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #73deff;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10px 20px;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: #73deff;" +
            "-fx-border-radius: 25px;" +
            "-fx-background-radius: 25px;" +
            "-fx-border-width: 2px;"
        );
        
        backButton.setOnMouseEntered(e -> {
            backButton.setStyle(
                "-fx-background-color: rgba(115, 222, 255, 0.1);" +
                "-fx-text-fill: #73deff;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 10px 20px;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: #73deff;" +
                "-fx-border-radius: 25px;" +
                "-fx-background-radius: 25px;" +
                "-fx-border-width: 2px;"
            );
        });
        
        backButton.setOnMouseExited(e -> {
            backButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #73deff;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 10px 20px;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: #73deff;" +
                "-fx-border-radius: 25px;" +
                "-fx-background-radius: 25px;" +
                "-fx-border-width: 2px;"
            );
        });
        
        backButton.setOnAction(e -> {
            // Navigate back to landing page
            if (mainApp != null) {
                // Create admin user for landing page
                Admin admin = new Admin();
                mainApp.setLogin(true, admin);
                // This will trigger displayLandingPage in Main
            } else {
                // Fallback: restart application
                try {
                    application.Main newApp = new application.Main();
                    newApp.start(stage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        // Title
        Label titleLabel = new Label("Academic Course Catalogue");
        titleLabel.setStyle(
            "-fx-text-fill: white;" +
            "-fx-font-size: 32px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Refresh Button
        Button refreshButton = new Button("🔄 Refresh");
        refreshButton.setStyle(
            "-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #73deff, #4973f1);" +
            "-fx-text-fill: #1d223a;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10px 20px;" +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 8px;"
        );
        
        refreshButton.setOnAction(e -> {
            calculateEnrollmentCounts();
            loadCourseData();
            System.out.println("Data refreshed!");
        });
        
        topBar.getChildren().addAll(backButton, titleLabel, spacer, refreshButton);
        return topBar;
    }
    
    private VBox createCenterContent() {
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20, 30, 30, 30));
        centerContent.setStyle("-fx-background-color: #050816;");
        
        // Subtitle and stats
        Label subtitleLabel = new Label("1S 2025-2026");
        subtitleLabel.setStyle(
            "-fx-text-fill: #94a3b8;" +
            "-fx-font-size: 16px;" +
            "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );
        
        // Stats card
        HBox statsCard = createStatsCard();
        
        // Table
        courseTable = createStyledTable();
        VBox.setVgrow(courseTable, Priority.ALWAYS);
        
        // Load data
        loadCourseData();
        
        centerContent.getChildren().addAll(subtitleLabel, statsCard, courseTable);
        return centerContent;
    }
    
    private HBox createStatsCard() {
        HBox statsCard = new HBox(40);
        statsCard.setPadding(new Insets(20));
        statsCard.setAlignment(Pos.CENTER_LEFT);
        statsCard.setStyle(
            "-fx-background-color: #12182B;" +
            "-fx-background-radius: 15px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 20, 0.3, 0, 5);"
        );
        
        int totalEnrolled = enrollmentCounts.values().stream().mapToInt(Integer::intValue).sum();
        
        // Total Courses
        VBox coursesBox = createStatBox("Total Courses", String.valueOf(allCourses.size()), "#73deff");
        
        // Total Enrollments
        VBox enrollmentsBox = createStatBox("Total Enrollments", String.valueOf(totalEnrolled), "#2ecc71");
        
        // Average per course
        double avgEnrollment = allCourses.isEmpty() ? 0 : (double) totalEnrolled / allCourses.size();
        VBox avgBox = createStatBox("Avg. per Course", String.format("%.1f", avgEnrollment), "#f39c12");
        
        statsCard.getChildren().addAll(coursesBox, enrollmentsBox, avgBox);
        return statsCard;
    }
    
    private VBox createStatBox(String label, String value, String color) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER_LEFT);
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-text-fill: " + color + ";" +
            "-fx-font-size: 36px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );
        
        Label nameLabel = new Label(label);
        nameLabel.setStyle(
            "-fx-text-fill: #94a3b8;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );
        
        box.getChildren().addAll(valueLabel, nameLabel);
        return box;
    }
    
    private TableView<CourseRow> createStyledTable() {
        TableView<CourseRow> table = new TableView<>();
        table.setStyle(
            "-fx-background-color: #12182B;" +
            "-fx-background-radius: 15px;" +
            "-fx-border-radius: 15px;"
        );
        
        // Define columns
        TableColumn<CourseRow, String> codeCol = new TableColumn<>("Course Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        codeCol.setPrefWidth(120);
        styleColumn(codeCol);
        
        TableColumn<CourseRow, String> titleCol = new TableColumn<>("Course Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(350);
        styleColumn(titleCol);
        
        TableColumn<CourseRow, String> unitsCol = new TableColumn<>("Units");
        unitsCol.setCellValueFactory(new PropertyValueFactory<>("units"));
        unitsCol.setPrefWidth(60);
        styleColumn(unitsCol);
        
        TableColumn<CourseRow, String> sectionCol = new TableColumn<>("Section");
        sectionCol.setCellValueFactory(new PropertyValueFactory<>("section"));
        sectionCol.setPrefWidth(100);
        styleColumn(sectionCol);
        
        TableColumn<CourseRow, String> scheduleCol = new TableColumn<>("Schedule");
        scheduleCol.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        scheduleCol.setPrefWidth(200);
        styleColumn(scheduleCol);
        
        TableColumn<CourseRow, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
        roomCol.setPrefWidth(180);
        styleColumn(roomCol);
        
        TableColumn<CourseRow, String> countCol = new TableColumn<>("Enrolled");
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        countCol.setPrefWidth(100);
        styleColumn(countCol);
        
        // Style enrollment column with color coding
        countCol.setCellFactory(column -> new TableCell<CourseRow, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText(item);
                    try {
                        int count = Integer.parseInt(item);
                        if (count >= 30) {
                            setStyle(
                                "-fx-background-color: #2d1a1e;" +
                                "-fx-text-fill: #ff6b6b;" +
                                "-fx-font-weight: bold;" +
                                "-fx-alignment: center;"
                            );
                        } else if (count >= 20) {
                            setStyle(
                                "-fx-background-color: #2d2419;" +
                                "-fx-text-fill: #f39c12;" +
                                "-fx-font-weight: bold;" +
                                "-fx-alignment: center;"
                            );
                        } else if (count >= 10) {
                            setStyle(
                                "-fx-background-color: #1a2d1e;" +
                                "-fx-text-fill: #2ecc71;" +
                                "-fx-alignment: center;"
                            );
                        } else {
                            setStyle(
                                "-fx-text-fill: #94a3b8;" +
                                "-fx-alignment: center;"
                            );
                        }
                    } catch (NumberFormatException e) {
                        setStyle("-fx-text-fill: #94a3b8; -fx-alignment: center;");
                    }
                }
            }
        });
        
        table.getColumns().addAll(codeCol, titleCol, unitsCol, sectionCol, 
                                  scheduleCol, roomCol, countCol);
        
        return table;
    }
    
    private void styleColumn(TableColumn<CourseRow, String> column) {
        column.setStyle(
            "-fx-background-color: #1e293b;" +
            "-fx-text-fill: white;" +
            "-fx-alignment: CENTER_LEFT;"
        );
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
    }
}