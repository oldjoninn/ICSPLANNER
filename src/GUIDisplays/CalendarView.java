package GUIDisplays;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import components.Course;
import components.Student;
import application.Main;
import java.util.*;

public class CalendarView {
    
    private Student student;
    private Stage stage;
    private Set<String> newlyAddedCourses; // Track newly added courses
    
    // Time slots from 7 AM to 10 PM
    private static final String[] TIME_SLOTS = {
        "7:00", "8:00", "9:00", "10:00", "11:00", "12:00",
        "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00"
    };
    
    private static final String[] DAYS = {"Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};
    
    public CalendarView(Student student, Stage stage) {
        this.student = student;
        this.stage = stage;
        this.newlyAddedCourses = new HashSet<>();
    }
    
    public void markAsNewlyAdded(Course course) {
        newlyAddedCourses.add(course.getCourseID() + "-" + course.getSection());
    }
    
    public void clearNewlyAdded() {
        newlyAddedCourses.clear();
    }
    
    public Scene createCalendarScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        // Top bar with title and buttons
        HBox topBar = createTopBar();
        root.setTop(topBar);
        
        // Calendar grid
        ScrollPane scrollPane = new ScrollPane(createCalendarGrid());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: #f5f5f5;");
        root.setCenter(scrollPane);
        
        // Legend at bottom
        HBox legend = createLegend();
        root.setBottom(legend);
        
        Scene scene = new Scene(root, 1200, 700);
        return scene;
    }
    
    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(15));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #2c3e50;");
        
        Button backButton = new Button("← Back");
        backButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                           "-fx-font-weight: bold; -fx-padding: 8px 15px;");
        backButton.setOnAction(e -> {
            Pane mainRoot = new Pane();
            LandingPage landingPage = new LandingPage();
            landingPage.displayaDashboard(mainRoot, stage, student);
            Scene scene = new Scene(mainRoot, 1200, 700);
            try {
                scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
            } catch (Exception ex) {
                System.out.println("CSS not found");
            }
            stage.setScene(scene);
        });
        
        Label titleLabel = new Label("Weekly Schedule Calendar");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Button enlistButton = new Button("Add/Remove Courses");
        enlistButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                             "-fx-font-weight: bold; -fx-padding: 8px 15px;");
        enlistButton.setOnAction(e -> {
            EnlistmentUI enlistment = new EnlistmentUI(student, Main.courseOfferings);
            Scene enlistScene = enlistment.EnlistScreen(stage);
            stage.setScene(enlistScene);
        });
        
        Button clearHighlightButton = new Button("Clear Highlights");
        clearHighlightButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; " +
                                     "-fx-font-weight: bold; -fx-padding: 8px 15px;");
        clearHighlightButton.setOnAction(e -> {
            clearNewlyAdded();
            stage.setScene(createCalendarScene());
        });
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        topBar.getChildren().addAll(backButton, titleLabel, spacer, clearHighlightButton, enlistButton);
        return topBar;
    }
    
    private GridPane createCalendarGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setPadding(new Insets(10));
        grid.setStyle("-fx-background-color: #ffffff;");
        
        // Header row (days)
        Label cornerLabel = new Label("Time");
        cornerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-padding: 10px;");
        cornerLabel.setPrefSize(80, 40);
        cornerLabel.setAlignment(Pos.CENTER);
        cornerLabel.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold;");
        grid.add(cornerLabel, 0, 0);
        
        for (int i = 0; i < DAYS.length; i++) {
            Label dayLabel = new Label(DAYS[i]);
            dayLabel.setPrefSize(180, 40);
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; " +
                            "-fx-font-weight: bold; -fx-font-size: 14px;");
            grid.add(dayLabel, i + 1, 0);
        }
        
        // Time slots and cells
        Map<String, Map<String, List<Course>>> schedule = organizeSchedule();
        
        for (int timeIdx = 0; timeIdx < TIME_SLOTS.length; timeIdx++) {
            String time = TIME_SLOTS[timeIdx];
            
            // Time label
            Label timeLabel = new Label(time);
            timeLabel.setPrefSize(80, 60);
            timeLabel.setAlignment(Pos.CENTER);
            timeLabel.setStyle("-fx-background-color: #ecf0f1; -fx-font-weight: bold; " +
                             "-fx-border-color: #bdc3c7; -fx-border-width: 1px;");
            grid.add(timeLabel, 0, timeIdx + 1);
            
            // Day cells
            for (int dayIdx = 0; dayIdx < DAYS.length; dayIdx++) {
                String day = DAYS[dayIdx];
                VBox cell = createCell(day, time, schedule);
                grid.add(cell, dayIdx + 1, timeIdx + 1);
            }
        }
        
        return grid;
    }
    
    private Map<String, Map<String, List<Course>>> organizeSchedule() {
        Map<String, Map<String, List<Course>>> schedule = new HashMap<>();
        
        for (Course course : student.getCoursesTaken()) {
            String days = course.getDays();
            String time = course.getTime();
            
            if (days == null || time == null || days.equals("TBA") || time.equals("TBA")) {
                continue;
            }
            
            // Parse days
            List<String> daysList = parseDays(days);
            
            // Parse time to get start hour
            String startTime = parseStartTime(time);
            
            for (String day : daysList) {
                schedule.putIfAbsent(day, new HashMap<>());
                schedule.get(day).putIfAbsent(startTime, new ArrayList<>());
                schedule.get(day).get(startTime).add(course);
            }
        }
        
        return schedule;
    }
    
    private List<String> parseDays(String days) {
        List<String> result = new ArrayList<>();
        days = days.trim();
        
        if (days.contains("Mon")) result.add("Mon");
        if (days.contains("Tues")) result.add("Tues");
        if (days.contains("Wed")) result.add("Wed");
        if (days.contains("Thurs")) result.add("Thurs");
        if (days.contains("Fri")) result.add("Fri");
        if (days.contains("Sat")) result.add("Sat");
        
        // Handle TTh format
        if (days.contains("TTh") || days.equals("TTh")) {
            if (!result.contains("Tues")) result.add("Tues");
            if (!result.contains("Thurs")) result.add("Thurs");
        }
        
        // Handle WF format
        if (days.contains("WF") || days.equals("WF")) {
            if (!result.contains("Wed")) result.add("Wed");
            if (!result.contains("Fri")) result.add("Fri");
        }
        
        return result;
    }
    
    private String parseStartTime(String time) {
        // Extract start time from formats like "10:00-11:00" or "7:00-10:00"
        if (time.contains("-")) {
            String start = time.split("-")[0].trim();
            // Normalize to just hour
            if (start.contains(":")) {
                String hour = start.split(":")[0];
                return hour + ":00";
            }
        }
        return time;
    }
    
    private VBox createCell(String day, String time, Map<String, Map<String, List<Course>>> schedule) {
        VBox cell = new VBox(3);
        cell.setPrefSize(180, 60);
        cell.setAlignment(Pos.TOP_LEFT);
        cell.setPadding(new Insets(5));
        cell.setStyle("-fx-background-color: white; -fx-border-color: #dfe6e9; -fx-border-width: 1px;");
        
        if (schedule.containsKey(day) && schedule.get(day).containsKey(time)) {
            List<Course> courses = schedule.get(day).get(time);
            
            for (Course course : courses) {
                String courseKey = course.getCourseID() + "-" + course.getSection();
                boolean isNew = newlyAddedCourses.contains(courseKey);
                
                Label courseLabel = new Label(course.getCourseID() + " " + course.getSection());
                courseLabel.setStyle(
                    "-fx-font-size: 11px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-padding: 3px 5px; " +
                    "-fx-background-radius: 3px; " +
                    (isNew ? 
                        "-fx-background-color: #2ecc71; -fx-text-fill: white; " +
                        "-fx-effect: dropshadow(gaussian, rgba(46,204,113,0.6), 5, 0.5, 0, 0);" :
                        "-fx-background-color: #3498db; -fx-text-fill: white;")
                );
                
                Label roomLabel = new Label(course.getRoom());
                roomLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: #7f8c8d;");
                
                Tooltip tooltip = new Tooltip(
                    "Course: " + course.getTitle() + "\n" +
                    "Section: " + course.getSection() + "\n" +
                    "Time: " + course.getTime() + "\n" +
                    "Room: " + course.getRoom() + "\n" +
                    (isNew ? "★ NEWLY ADDED" : "")
                );
                Tooltip.install(courseLabel, tooltip);
                
                cell.getChildren().addAll(courseLabel, roomLabel);
            }
            
            if (!courses.isEmpty()) {
                cell.setStyle("-fx-background-color: #ecf9ff; -fx-border-color: #3498db; -fx-border-width: 1px;");
            }
        }
        
        return cell;
    }
    
    private HBox createLegend() {
        HBox legend = new HBox(30);
        legend.setPadding(new Insets(15));
        legend.setAlignment(Pos.CENTER);
        legend.setStyle("-fx-background-color: #ecf0f1;");
        
        Label legendTitle = new Label("Legend:");
        legendTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        HBox regularItem = createLegendItem("#3498db", "Regular Course");
        HBox newItem = createLegendItem("#2ecc71", "Newly Added Course");
        HBox emptyItem = createLegendItem("white", "Free Time");
        
        legend.getChildren().addAll(legendTitle, regularItem, newItem, emptyItem);
        return legend;
    }
    
    private HBox createLegendItem(String color, String text) {
        HBox item = new HBox(8);
        item.setAlignment(Pos.CENTER_LEFT);
        
        Label colorBox = new Label();
        colorBox.setPrefSize(30, 20);
        colorBox.setStyle("-fx-background-color: " + color + "; -fx-border-color: #95a5a6; -fx-border-width: 1px;");
        
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 12px;");
        
        item.getChildren().addAll(colorBox, label);
        return item;
    }
}