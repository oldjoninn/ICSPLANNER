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
    
    private Main mainApp;
    private Student student;
    private Stage stage;
    private Set<String> newlyAddedCourses;
    
    // Time slots from 7 AM to 7 PM in 30-minute intervals
    private static final String[] TIME_SLOTS = {
        "7:00", "7:30", "8:00", "8:30", "9:00", "9:30", "10:00", "10:30",
        "11:00", "11:30", "12:00", "12:30", "1:00", "1:30", "2:00", "2:30",
        "3:00", "3:30", "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00"
    };
    
    // Time labels for display (hourly ranges)
    private static final String[] TIME_LABELS = {
        "7:00-8:00", "8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-1:00", 
        "1:00-2:00", "2:00-3:00", "3:00-4:00", "4:00-5:00", "5:00-6:00", "6:00-7:00","7:00-8:00" };
    
    private static final String[] DAYS = {"Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};
    
    public CalendarView(Main mainApp, Student student, Stage stage) {
        this.mainApp = mainApp;
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
        
        HBox topBar = createTopBar();
        root.setTop(topBar);
        
        ScrollPane scrollPane = new ScrollPane(createCalendarGrid());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: #f5f5f5;");
        root.setCenter(scrollPane);
        
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
            // Create LandingPage with application context so dashboard and other menus work
            LandingPage landingPage = new LandingPage(mainApp, student);
            landingPage.displayLandingPage(mainRoot, stage);
            Scene scene = new Scene(mainRoot, 1200, 700);
            try {
                scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
            } catch (Exception ex) {
                System.out.println("CSS not found");
            }
            stage.setScene(scene);
        });
        
        Label titleLabel = new Label("Weekly Schedule Calendar (7 AM - 7 PM)");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Button enlistButton = new Button("Add/Remove Courses");
        enlistButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                             "-fx-font-weight: bold; -fx-padding: 8px 15px;");
        enlistButton.setOnAction(e -> {
            EnlistmentUI enlistment = new EnlistmentUI(mainApp, student, Main.courseOfferings, stage);
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
        grid.setHgap(3);
        grid.setVgap(0);
        grid.setPadding(new Insets(10));
        grid.setStyle("-fx-background-color: #ffffff;");
        
        // Header row
        Label cornerLabel = new Label("Time");
        cornerLabel.setPrefSize(100, 50);
        cornerLabel.setAlignment(Pos.CENTER);
        cornerLabel.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold;");
        grid.add(cornerLabel, 0, 0);
        
        for (int i = 0; i < DAYS.length; i++) {
            Label dayLabel = new Label(DAYS[i]);
            dayLabel.setPrefSize(220, 50);
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; " +
                            "-fx-font-weight: bold; -fx-font-size: 14px;");
            grid.add(dayLabel, i + 1, 0);
        }
        
        // Organize schedule and detect collisions
        Map<String, CourseTimeSlot> schedule = organizeSchedule();
        Set<String> collisions = detectCollisions(schedule);
        
        // Display collision warnings
        if (!collisions.isEmpty()) {
            System.out.println("⚠️ SCHEDULE CONFLICTS DETECTED:");
            for (String collision : collisions) {
                System.out.println("  " + collision);
            }
        }
        
        // Track which cells have been filled (by day and time slot index)
        Map<String, Set<Integer>> filledCells = new HashMap<>();
        for (String day : DAYS) {
            filledCells.put(day, new HashSet<>());
        }
        
      
        int gridRow = 1; // Start at row 1 (after header)
        
        for (int timeIdx = 0; timeIdx < TIME_SLOTS.length; timeIdx++) {
            // Add time label for every hour (every 2 slots) - but DON'T increment gridRow for label
            if (timeIdx % 2 == 0) {
                int hourIndex = timeIdx / 2;
                if (hourIndex < TIME_LABELS.length) {
                    Label timeLabel = new Label(TIME_LABELS[hourIndex]);
                    timeLabel.setPrefSize(100, 80);
                    timeLabel.setAlignment(Pos.CENTER);
                    timeLabel.setStyle("-fx-background-color: #ecf0f1; -fx-font-weight: bold; " +
                                     "-fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-font-size: 11px;");
                    grid.add(timeLabel, 0, gridRow);
                    GridPane.setRowSpan(timeLabel, 2); // Span 2 grid rows for the hour
                }
            }
            
            // Process each day column
            for (int dayIdx = 0; dayIdx < DAYS.length; dayIdx++) {
                String day = DAYS[dayIdx];
                
                // Skip if this time slot is already filled
                if (filledCells.get(day).contains(timeIdx)) {
                    continue;
                }
                
                // Check if a course starts at this time slot
                String dayTimeKey = day + "-" + timeIdx;
                if (schedule.containsKey(dayTimeKey)) {
                	CourseTimeSlot slot = schedule.get(dayTimeKey);
                    System.out.println("DEBUG: Placing " + slot.course.getCourseID() + 
                                      " at gridRow=" + gridRow + ", timeIdx=" + timeIdx + 
                                      ", TIME_SLOTS[" + timeIdx + "]=" + TIME_SLOTS[timeIdx]);
                    
                    boolean hasCollision = collisions.contains(dayTimeKey);
                    VBox cell = createMergedCell(slot, hasCollision);
                    
                    // Add the cell to the grid at the CORRECT row
                    grid.add(cell, dayIdx + 1, gridRow);
                    
                    // Set row span based on duration
                    GridPane.setRowSpan(cell, slot.durationSlots);
                    
                    // Mark all time slots this course occupies as filled
                    for (int i = 0; i < slot.durationSlots; i++) {
                        filledCells.get(day).add(timeIdx + i);
                    }
                } else {
                    // Empty cell for this 30-min slot
                    VBox emptyCell = createEmptyCell(30);
                    grid.add(emptyCell, dayIdx + 1, gridRow);
                }
            }
            
            gridRow++; // Increment grid row for EACH time slot
        }
        
        return grid;
    }
    


    // Inner class to hold course with its duration
    private class CourseTimeSlot {
        Course course;
        int durationSlots;
        
        CourseTimeSlot(Course course, int durationSlots) {
            this.course = course;
            this.durationSlots = durationSlots;
        }
    }
    
    private Map<String, CourseTimeSlot> organizeSchedule() {
        Map<String, CourseTimeSlot> schedule = new HashMap<>();
        
        for (Course course : student.getCoursesTaken()) {
            String days = course.getDays();
            String time = course.getTime();
            
            if (days == null || time == null || days.equals("TBA") || time.equals("TBA")) {
                continue;
            }
            
            // Parse days
            List<String> daysList = parseDays(days);
            
            // Parse time to get start time and duration
            String startTime = parseStartTime(time);
            int duration = calculateDuration(time);
            
            if (startTime != null) {
                // Find the index of the start time
                int startIndex = -1;
                for (int i = 0; i < TIME_SLOTS.length; i++) {
                    if (TIME_SLOTS[i].equals(startTime)) {
                        startIndex = i;
                        break;
                    }
                }
                
                if (startIndex >= 0) {
                    // Store course only at its start time with duration
                    for (String day : daysList) {
                        String key = day + "-" + startIndex;
                        
                        // Safety check: ensure duration is positive
                        if (duration > 0 && duration <= TIME_SLOTS.length) {
                            schedule.put(key, new CourseTimeSlot(course, duration));
                        } else {
                            System.out.println("WARNING: Invalid duration " + duration + " for course " + 
                                             course.getCourseID() + " at " + startTime);
                        }
                    }
                }
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
        // Extract start time from formats like "10:00-11:00" or "8:30-9:30"
        if (time.contains("-")) {
            String start = time.split("-")[0].trim();
            return start;
        }
        return time;
    }
    
    private int calculateDuration(String time) {
        if (!time.contains("-")) return 2;
        
        try {
            String[] parts = time.split("-");
            if (parts.length != 2) {
                System.out.println("WARNING: Invalid time format: " + time);
                return 2;
            }
            
            String startStr = parts[0].trim();
            String endStr = parts[1].trim();
            
            int startMinutes = parseTimeToMinutes(startStr, false); // false = start time
            int endMinutes = parseTimeToMinutes(endStr, true);      // true = end time
            
            int durationMinutes = endMinutes - startMinutes;
            
            if (durationMinutes <= 0) {
                System.out.println("WARNING: Invalid duration for time " + time + 
                                 " (start=" + startMinutes + " min, end=" + endMinutes + " min)");
                return 2;
            }
            
            int slots = durationMinutes / 30;
            
            if (durationMinutes % 30 != 0) {
                slots = (durationMinutes + 29) / 30; // Round up
            }
            
            if (slots <= 0 || slots > TIME_SLOTS.length) {
                System.out.println("WARNING: Calculated slots " + slots + " out of range for " + time);
                return 2;
            }
            
            return slots;
            
        } catch (Exception e) {
            System.out.println("ERROR parsing time: " + time);
            e.printStackTrace();
            return 2;
        }
    }

    
    private int parseTimeToMinutes(String timeStr, boolean isEndTime) {
        // Parse time like "8:30" or "10:00" to minutes since midnight
        String[] parts = timeStr.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        
        if (hour == 12) hour = 12; // Noon
        else if (hour >= 1 && hour <= 6) hour += 12; // PM times after noon
        else if (hour == 7) {
			if (isEndTime) hour += 12; // 7 PM is 19:00
		}
        
        return hour * 60 + minute;
    }
    
    private Set<String> detectCollisions(Map<String, CourseTimeSlot> schedule) {
        Set<String> collisions = new HashSet<>();
        
        // For each day, check for overlapping courses
        for (String day : DAYS) {
            List<CourseTimeSlot> dayCourses = new ArrayList<>();
            List<Integer> startIndices = new ArrayList<>();
            
            // Collect all courses for this day
            for (Map.Entry<String, CourseTimeSlot> entry : schedule.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith(day + "-")) {
                    int startIdx = Integer.parseInt(key.substring(key.indexOf("-") + 1));
                    dayCourses.add(entry.getValue());
                    startIndices.add(startIdx);
                }
            }
            
            // Check for overlaps between all pairs of courses on this day
            for (int i = 0; i < dayCourses.size(); i++) {
                int start1 = startIndices.get(i);
                int end1 = start1 + dayCourses.get(i).durationSlots;
                
                for (int j = i + 1; j < dayCourses.size(); j++) {
                    int start2 = startIndices.get(j);
                    int end2 = start2 + dayCourses.get(j).durationSlots;
                    

                    boolean overlaps = (start1 < end2) && (start2 < end1);
                    
                    if (overlaps) {
                        Course c1 = dayCourses.get(i).course;
                        Course c2 = dayCourses.get(j).course;
                        
                        String collision = "⚠️ CONFLICT on " + day + ": " +
                                         c1.getCourseID() + " " + c1.getSection() + " (" + c1.getTime() + ") " +
                                         "overlaps with " +
                                         c2.getCourseID() + " " + c2.getSection() + " (" + c2.getTime() + ")";
                        
                        System.out.println(collision);
                        collisions.add(day + "-" + start1);
                        collisions.add(day + "-" + start2);               
                    }
                }
            }
        }
        
        return collisions;
    }
    
    public boolean hasTimeConflict(Course newCourse) {
        String newDays = newCourse.getDays();
        String newTime = newCourse.getTime();
        
        if (newDays == null || newTime == null || newDays.equals("TBA") || newTime.equals("TBA")) {
            return false; // TBA courses don't conflict
        }
        
        List<String> newDaysList = parseDays(newDays);
        String newStartTime = parseStartTime(newTime);
        int newDuration = calculateDuration(newTime);
        
        int newStartIndex = -1;
        for (int i = 0; i < TIME_SLOTS.length; i++) {
            if (TIME_SLOTS[i].equals(newStartTime)) {
                newStartIndex = i;
                break;
            }
        }
        
        if (newStartIndex < 0) return false;
        
        int newEndIndex = newStartIndex + newDuration;
        
        // Check against all existing courses
        for (Course existing : student.getCoursesTaken()) {
            String existingDays = existing.getDays();
            String existingTime = existing.getTime();
            
            if (existingDays == null || existingTime == null || 
                existingDays.equals("TBA") || existingTime.equals("TBA")) {
                continue;
            }
            
            List<String> existingDaysList = parseDays(existingDays);
            String existingStartTime = parseStartTime(existingTime);
            int existingDuration = calculateDuration(existingTime);
            
            int existingStartIndex = -1;
            for (int i = 0; i < TIME_SLOTS.length; i++) {
                if (TIME_SLOTS[i].equals(existingStartTime)) {
                    existingStartIndex = i;
                    break;
                }
            }
            
            if (existingStartIndex < 0) continue;
            
            int existingEndIndex = existingStartIndex + existingDuration;
            
            // Check if they share any day
            for (String newDay : newDaysList) {
                if (existingDaysList.contains(newDay)) {
                    // Same day - check time overlap
                    if (newStartIndex < existingEndIndex && existingStartIndex < newEndIndex) {
                        return true; // CONFLICT FOUND
                    }
                }
            }
        }
        
        return false; // No conflicts
    }


    
    private VBox createMergedCell(CourseTimeSlot slot, boolean hasCollision) {
        Course course = slot.course;
        String courseKey = course.getCourseID() + "-" + course.getSection();
        boolean isNew = newlyAddedCourses.contains(courseKey);
        
        VBox cell = new VBox(5);
        cell.setPrefSize(220, 40 * slot.durationSlots);
        cell.setAlignment(Pos.TOP_LEFT);
        cell.setPadding(new Insets(8));
        
        // Different styling for collisions
        String borderColor = hasCollision ? "#e74c3c" : (isNew ? "#2ecc71" : "#3498db");
        String bgColor = hasCollision ? "#fadbd8" : (isNew ? "#d5f4e6" : "#ecf9ff");
        
        cell.setStyle(
            "-fx-border-color: " + borderColor + "; " +
            "-fx-border-width: " + (hasCollision ? "3px" : "2px") + "; " +
            "-fx-background-color: " + bgColor + ";"
        );
        
        // Collision warning badge
        if (hasCollision) {
            Label collisionBadge = new Label("⚠️ CONFLICT");
            collisionBadge.setStyle(
                "-fx-font-size: 9px; " +
                "-fx-text-fill: white; " +
                "-fx-background-color: #e74c3c; " +
                "-fx-padding: 2px 5px; " +
                "-fx-background-radius: 3px; " +
                "-fx-font-weight: bold;"
            );
            cell.getChildren().add(collisionBadge);
        }
        
        // Course Code (large and bold)
        Label courseLabel = new Label(course.getCourseID());
        courseLabel.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: " + (isNew ? "#27ae60" : "#2980b9") + ";"
        );
        
        
        

        // Add visual indicator for newly added courses
        if (isNew) {
            Label newBadge = new Label("★ NEW");
            newBadge.setStyle(
                "-fx-font-size: 9px; " +
                "-fx-text-fill: white; " +
                "-fx-background-color: #2ecc71; " +
                "-fx-padding: 2px 5px; " +
                "-fx-background-radius: 3px;"
            );
            cell.getChildren().add(newBadge);
        }
        
        cell.getChildren().addAll(courseLabel);
        
        // Tooltip with full details
        Tooltip tooltip = new Tooltip(
            "Course: " + course.getTitle() + "\n" +
            "Code: " + course.getCourseID() + "\n" +
            "Section: " + course.getSection() + "\n" +
            "Time: " + course.getTime() + "\n" +
            "Room: " + course.getRoom() + "\n" +
            "Duration: " + (slot.durationSlots * 30) + " minutes" +
            (isNew ? "\n★ NEWLY ADDED" : "")
        );
        Tooltip.install(cell, tooltip);
        
        return cell;
    }
    
    private VBox createEmptyCell(int height) {
        VBox cell = new VBox();
        cell.setPrefSize(220, height);
        cell.setStyle("-fx-background-color: white; -fx-border-color: #dfe6e9; -fx-border-width: 0.5px;");
        return cell;
    }
    
    private HBox createLegend() {
        HBox legend = new HBox(30);
        legend.setPadding(new Insets(15));
        legend.setAlignment(Pos.CENTER);
        legend.setStyle("-fx-background-color: #ecf0f1;");
        
        Label legendTitle = new Label("Legend:");
        legendTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        HBox regularItem = createLegendItem("#ecf9ff", "Regular Course");
        HBox newItem = createLegendItem("#d5f4e6", "Newly Added Course");
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