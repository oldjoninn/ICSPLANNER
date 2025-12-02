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
    private Main mainApp;
    private Map<String, Integer> enrollmentCounts;
    
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
        // Main container with modern dark gradient background
        BorderPane root = new BorderPane();
        root.getStyleClass().add("calendar-view"); // Use centralized dark gradient theme
        
        // Top Bar
        HBox topBar = createTopBar();
        root.setTop(topBar);
        
        // Center content with table
        VBox centerContent = createCenterContent();
        root.setCenter(centerContent);
        
        // Get screen dimensions for full screen
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        
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
        topBar.setPadding(new Insets(25, 40, 25, 40));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle(
            "-fx-background-color: rgba(18, 24, 43, 0.8);" +
            "-fx-border-color: transparent transparent rgba(115, 222, 255, 0.2) transparent;" +
            "-fx-border-width: 0 0 2 0;"
        );
        
        // Back/Logout Button with improved styling
        Button backButton = new Button("← Back to Home");
        backButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #73deff;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 15px;" +
            "-fx-padding: 12px 24px;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: #73deff;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-border-width: 2px;"
        );
        
        backButton.setOnMouseEntered(e -> {
            backButton.setStyle(
                "-fx-background-color: rgba(115, 222, 255, 0.15);" +
                "-fx-text-fill: #73deff;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 15px;" +
                "-fx-padding: 12px 24px;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: #73deff;" +
                "-fx-border-radius: 8px;" +
                "-fx-background-radius: 8px;" +
                "-fx-border-width: 2px;" +
                "-fx-effect: dropshadow(gaussian, rgba(115, 222, 255, 0.4), 10, 0, 0, 0);"
            );
        });
        
        backButton.setOnMouseExited(e -> {
            backButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #73deff;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 15px;" +
                "-fx-padding: 12px 24px;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: #73deff;" +
                "-fx-border-radius: 8px;" +
                "-fx-background-radius: 8px;" +
                "-fx-border-width: 2px;"
            );
        });
        
        backButton.setOnAction(e -> {
            // Navigate back to landing page
            if (mainApp != null) {
                Pane mainRoot = new Pane();
                // Prefer the current logged-in user; fall back to an Admin wrapper if none available
                components.User currentUser = mainApp.getCurrentUser();
                if (currentUser == null) currentUser = new Admin();
                LandingPage landingPage = new LandingPage(mainApp, currentUser);
                landingPage.displayLandingPage(mainRoot, stage);
                
                // Use screen visual bounds to size the scene so it fills the screen
                javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                Scene scene = new Scene(mainRoot, screenBounds.getWidth(), screenBounds.getHeight());
                try {
                    scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
                } catch (Exception ex) {
                    System.out.println("CSS not found");
                }
                stage.setScene(scene);
                stage.setMaximized(true);
            }
        });
        
        // Title with gradient effect
        Label titleLabel = new Label("📊 Academic Course Catalogue");
        titleLabel.setStyle(
            "-fx-text-fill: white;" +
            "-fx-font-size: 36px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(115, 222, 255, 0.3), 10, 0, 0, 2);"
        );
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Refresh Button with modern styling
        Button refreshButton = new Button("🔄 Refresh Data");
        refreshButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #73deff, #4973f1);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 12px 24px;" +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 8px;" +
            "-fx-effect: dropshadow(gaussian, rgba(73, 115, 241, 0.4), 10, 0, 0, 3);"
        );
        
        refreshButton.setOnMouseEntered(e -> {
            refreshButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #8ae5ff, #6a8ff5);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 12px 24px;" +
                "-fx-cursor: hand;" +
                "-fx-background-radius: 8px;" +
                "-fx-effect: dropshadow(gaussian, rgba(73, 115, 241, 0.6), 15, 0, 0, 4);"
            );
        });
        
        refreshButton.setOnMouseExited(e -> {
            refreshButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #73deff, #4973f1);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 12px 24px;" +
                "-fx-cursor: hand;" +
                "-fx-background-radius: 8px;" +
                "-fx-effect: dropshadow(gaussian, rgba(73, 115, 241, 0.4), 10, 0, 0, 3);"
            );
        });
        
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
        centerContent.setPadding(new Insets(30, 40, 40, 40));
        centerContent.setStyle("-fx-background-color: transparent;");
        
        // Semester badge
        HBox semesterBox = new HBox(10);
        semesterBox.setAlignment(Pos.CENTER_LEFT);
        
        Label semesterBadge = new Label("📅 1S 2025-2026");
        semesterBadge.setStyle(
            "-fx-background-color: rgba(115, 222, 255, 0.15);" +
            "-fx-text-fill: #73deff;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
            "-fx-padding: 10px 20px;" +
            "-fx-background-radius: 20px;" +
            "-fx-border-color: #73deff;" +
            "-fx-border-radius: 20px;" +
            "-fx-border-width: 1px;"
        );
        
        semesterBox.getChildren().add(semesterBadge);
        
        // Stats card with enhanced design
        HBox statsCard = createStatsCard();
        
        // Table
        courseTable = createStyledTable();
        VBox.setVgrow(courseTable, Priority.ALWAYS);
        
        // Load data
        loadCourseData();
        
        centerContent.getChildren().addAll(semesterBox, statsCard, courseTable);
        return centerContent;
    }
    
    private HBox createStatsCard() {
        HBox statsCard = new HBox(40);
        statsCard.setPadding(new Insets(20));
        statsCard.setAlignment(Pos.CENTER);
        statsCard.setStyle(
            "-fx-background-color: rgba(18, 24, 43, 0.6);" +
            "-fx-background-radius: 15px;" +
            "-fx-border-color: rgba(115, 222, 255, 0.3);" +
            "-fx-border-radius: 15px;" +
            "-fx-border-width: 1px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 20, 0.4, 0, 6);"
        );
        
        int totalEnrolled = enrollmentCounts.values().stream().mapToInt(Integer::intValue).sum();
        
        // Total Courses
        VBox coursesBox = createStatBox("Total Courses", String.valueOf(allCourses.size()), "#73deff", "📚");
        
        // Total Enrollments
        VBox enrollmentsBox = createStatBox("Total Enrollments", String.valueOf(totalEnrolled), "#2ecc71", "👥");
        
        // Average per course
        double avgEnrollment = allCourses.isEmpty() ? 0 : (double) totalEnrolled / allCourses.size();
        VBox avgBox = createStatBox("Avg. per Course", String.format("%.1f", avgEnrollment), "#f39c12", "📊");
        
        statsCard.getChildren().addAll(coursesBox, enrollmentsBox, avgBox);
        return statsCard;
    }
    
    private VBox createStatBox(String label, String value, String color, String icon) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(8));
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle(
            "-fx-font-size: 24px;"
        );
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-text-fill: " + color + ";" +
            "-fx-font-size: 32px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
            "-fx-effect: dropshadow(gaussian, " + color + ", 5, 0.3, 0, 0);"
        );
        
        Label nameLabel = new Label(label);
        nameLabel.setStyle(
            "-fx-text-fill: #94a3b8;" +
            "-fx-font-size: 12px;" +
            "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
            "-fx-font-weight: 600;"
        );
        
        box.getChildren().addAll(iconLabel, valueLabel, nameLabel);
        return box;
    }
    
    private TableView<CourseRow> createStyledTable() {
        TableView<CourseRow> table = new TableView<>();
        table.setStyle(
            "-fx-background-color: rgba(18, 24, 43, 0.6);" +
            "-fx-background-radius: 20px;" +
            "-fx-border-color: rgba(115, 222, 255, 0.3);" +
            "-fx-border-radius: 20px;" +
            "-fx-border-width: 1px;"
        );
        
        // Define columns with improved widths
        TableColumn<CourseRow, String> codeCol = new TableColumn<>("Course Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        codeCol.setPrefWidth(130);
        styleColumn(codeCol);
        
        TableColumn<CourseRow, String> titleCol = new TableColumn<>("Course Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(380);
        styleColumn(titleCol);
        
        TableColumn<CourseRow, String> unitsCol = new TableColumn<>("Units");
        unitsCol.setCellValueFactory(new PropertyValueFactory<>("units"));
        unitsCol.setPrefWidth(70);
        styleColumn(unitsCol);
        
        TableColumn<CourseRow, String> sectionCol = new TableColumn<>("Section");
        sectionCol.setCellValueFactory(new PropertyValueFactory<>("section"));
        sectionCol.setPrefWidth(100);
        styleColumn(sectionCol);
        
        TableColumn<CourseRow, String> scheduleCol = new TableColumn<>("Schedule");
        scheduleCol.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        scheduleCol.setPrefWidth(220);
        styleColumn(scheduleCol);
        
        TableColumn<CourseRow, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
        roomCol.setPrefWidth(180);
        styleColumn(roomCol);
        
        TableColumn<CourseRow, String> countCol = new TableColumn<>("Enrolled");
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        countCol.setPrefWidth(110);
        styleColumn(countCol);
        
        // Enhanced enrollment column with gradient backgrounds
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
                                "-fx-background-color: linear-gradient(to right, rgba(255, 107, 107, 0.2), rgba(255, 107, 107, 0.3));" +
                                "-fx-text-fill: #ff6b6b;" +
                                "-fx-font-weight: bold;" +
                                "-fx-alignment: center;" +
                                "-fx-font-size: 13px;"
                            );
                        } else if (count >= 20) {
                            setStyle(
                                "-fx-background-color: linear-gradient(to right, rgba(243, 156, 18, 0.2), rgba(243, 156, 18, 0.3));" +
                                "-fx-text-fill: #f39c12;" +
                                "-fx-font-weight: bold;" +
                                "-fx-alignment: center;" +
                                "-fx-font-size: 13px;"
                            );
                        } else if (count >= 10) {
                            setStyle(
                                "-fx-background-color: linear-gradient(to right, rgba(46, 204, 113, 0.2), rgba(46, 204, 113, 0.3));" +
                                "-fx-text-fill: #2ecc71;" +
                                "-fx-font-weight: bold;" +
                                "-fx-alignment: center;" +
                                "-fx-font-size: 13px;"
                            );
                        } else {
                            setStyle(
                                "-fx-text-fill: #94a3b8;" +
                                "-fx-alignment: center;" +
                                "-fx-font-size: 13px;"
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
            "-fx-background-color: rgba(30, 41, 59, 0.5);" +
            "-fx-text-fill: white;" +
            "-fx-alignment: CENTER_LEFT;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;"
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