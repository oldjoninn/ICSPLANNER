package GUIDisplays;
import javafx.animation.FadeTransition;
import javafx.scene.media.AudioClip;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Insets;
import components.Course;
import components.Student;
import application.Main;
import java.util.*;
import java.io.File;

public class CalendarView {
   private Main mainApp;
   private Student student;
   private Stage stage;
   private Set<String> newlyAddedCourses;
   private AudioClip flashbangSound;
   
   {
      // Load flashbang sound
	   try {
		    File audioFile = new File("src/application/flashbang.mp3");
		    flashbangSound = new AudioClip(audioFile.toURI().toString());
		    System.out.println("Loaded successfully!");
		} catch (Exception e) {
		    e.printStackTrace();
		}

}
   
   // Sidebar specific variables
   private static final double SIDEBAR_WIDTH = 300;
   private Pane dashboardPane;
   private Pane overlay;
   private boolean isSidebarOpen = false;
   
   // Time slots from 7 AM to 7 PM in 30-minute intervals
   private static final String[] TIME_SLOTS = {
      "7:00", "7:30", "8:00", "8:30", "9:00", "9:30", "10:00", "10:30",
      "11:00", "11:30", "12:00", "12:30", "1:00", "1:30", "2:00", "2:30",
      "3:00", "3:30", "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00"
   };
   // Labels for every hour
   private static final String[] TIME_LABELS = {
      "7:00", "8:00", "9:00", "10:00", "11:00", "12:00",
      "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00"
   };
   // Days of the week (Mon to Sat)
   private static final String[] DAYS = {"Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};
   
   // Constructor
   public CalendarView(Main mainApp, Student student, Stage stage) {
      this.mainApp = mainApp;
      this.student = student;
      this.stage = stage;
      this.newlyAddedCourses = new HashSet<>();
   }
   // Mark a course as newly added
   public void markAsNewlyAdded(Course course) {
      newlyAddedCourses.add(course.getCourseID() + "-" + course.getSection());
   }
   // Clear newly added courses
   public void clearNewlyAdded() {
      newlyAddedCourses.clear();
   }
   
   // Create the main calendar scene
   public Scene createCalendarScene() {
      // Root Container for Layers (Content + Overlay + Sidebar)
      Pane mainRoot = new Pane();
      mainRoot.getStyleClass().add("calendar-view"); // Dark gradient background
    
      // DYNAMIC SIZING: Get Screen Dimensions
      Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds(); 
      double screenWidth = screenBounds.getWidth();
      double screenHeight = screenBounds.getHeight();
    
      // Calendar Content Layer
      BorderPane calendarContent = new BorderPane();
      calendarContent.prefWidthProperty().bind(mainRoot.widthProperty());
      calendarContent.prefHeightProperty().bind(mainRoot.heightProperty());
      calendarContent.setStyle("-fx-background-color: transparent;");
    
      // Build Calendar UI 
      HBox topBar = createTopBar(screenWidth); // Top Navigation Bar
      calendarContent.setTop(topBar); 
    
      GridPane calendarGrid = createCalendarGrid(screenWidth); // Calendar Grid
      ScrollPane scrollPane = new ScrollPane(calendarGrid); // Scrollable Grid
      scrollPane.setFitToWidth(true);
      scrollPane.setFitToHeight(false);
      scrollPane.getStyleClass().add("calendar-scroll-pane");
      calendarContent.setCenter(scrollPane);
    
      HBox legend = createLegend(); // Legend at Bottom
      calendarContent.setBottom(legend);
    
      // Sidebar & Overlay Setup
      overlay = new Pane(); // Semi-transparent overlay
      overlay.prefWidthProperty().bind(mainRoot.widthProperty());
      overlay.prefHeightProperty().bind(mainRoot.heightProperty());
      overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);"); // Semi-transparent black
      overlay.setVisible(false); // Hidden initially
      overlay.setOnMouseClicked(e -> closeSidebar());
    
      // Initialize Dashboard Sidebar
      Dashboard dashboard = new Dashboard(SIDEBAR_WIDTH, mainApp, stage, student);
      dashboardPane = dashboard.getView(); // Sidebar Pane
      dashboardPane.prefHeightProperty().bind(mainRoot.heightProperty()); // Full height
      dashboardPane.setTranslateX(-SIDEBAR_WIDTH); // Hide initially because of negative X
    
      // Add all layers to main root
      mainRoot.getChildren().addAll(calendarContent, overlay, dashboardPane);
    
      // ANIMATION: Fade in on load
      FadeTransition fadeIn = new FadeTransition(Duration.millis(800), mainRoot);
      fadeIn.setFromValue(0.0);
      fadeIn.setToValue(1.0);
      fadeIn.play();
    
      // Create Scene with proper dimensions
      Scene scene = new Scene(mainRoot, screenWidth, screenHeight);
    
      // Load CSS
      try {
          java.net.URL cssUrl = getClass().getResource("/application/application.css");
          if (cssUrl != null) {
              scene.getStylesheets().add(cssUrl.toExternalForm());
          }
      } catch(Exception e) {
          e.printStackTrace();
      }
    
      return scene;
   }
   
   // TOP BAR CREATION
   private HBox createTopBar(double screenWidth) {
      HBox topBar = new HBox(20);
      topBar.setPadding(new Insets(15, 30, 15, 30));
      topBar.setAlignment(Pos.CENTER_LEFT);
      topBar.setStyle("-fx-background-color: rgba(5, 11, 20, 0.8); -fx-border-color: rgba(255,255,255,0.1); -fx-border-width: 0 0 1px 0;");
   
      // MENU BUTTON 
      Button menuBtn = new Button("☰");
      menuBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px; -fx-cursor: hand;");
      menuBtn.setOnAction(e -> {
          if (isSidebarOpen) closeSidebar(); else openSidebar();
      });
      
    // TITLE LABEL
      Label titleLabel = new Label("Weekly Schedule");
      titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Arial'; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 1);");
    
      Region spacer = new Region();
      HBox.setHgrow(spacer, Priority.ALWAYS);
    
      Button clearHighlightButton = new Button("Clear Highlights");
      clearHighlightButton.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: #bdc3c7; " +
                                   "-fx-font-weight: bold; -fx-padding: 8px 15px; -fx-background-radius: 20px; -fx-cursor: hand;");
      clearHighlightButton.setOnAction(e -> {
    	  
    	  if (flashbangSound != null) {
              flashbangSound.play();
          }
    	  
          clearNewlyAdded();
          stage.setScene(createCalendarScene());
          stage.setMaximized(true);
      });
      // ENLIST BUTTON
      Button enlistButton = new Button("+ Add/Remove Courses");
      enlistButton.setStyle("-fx-background-color: linear-gradient(to right, #2980b9, #2c3e50); -fx-text-fill: white; " +
                           "-fx-font-weight: bold; -fx-padding: 8px 20px; -fx-background-radius: 20px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 1); -fx-cursor: hand;");
      enlistButton.setOnAction(e -> {
          EnlistmentUI enlistment = new EnlistmentUI(mainApp, student, Main.courseOfferings, stage); // Pass stage
          Scene enlistScene = enlistment.EnlistScreen(stage);
          stage.setScene(enlistScene);
          stage.setMaximized(true);
      });
    
      topBar.getChildren().addAll(menuBtn, titleLabel, spacer, clearHighlightButton, enlistButton);
      return topBar; // End of createTopBar
   }
   
   //SIDEBAR ANIMATION LOGIC 
   private void openSidebar() { // Open sidebar with animation
      if (!isSidebarOpen) {
          overlay.setVisible(true);
          TranslateTransition openAnim = new TranslateTransition(Duration.seconds(0.4), dashboardPane);
          openAnim.setToX(0);
          openAnim.setInterpolator(Interpolator.EASE_OUT);
          openAnim.play();
          isSidebarOpen = true;
      }
   }
   
   private void closeSidebar() { // Close sidebar with animation
      if (isSidebarOpen) {
          TranslateTransition closeAnim = new TranslateTransition(Duration.seconds(0.4), dashboardPane);
          closeAnim.setToX(-SIDEBAR_WIDTH);
          closeAnim.setInterpolator(Interpolator.EASE_IN);
          closeAnim.setOnFinished(e -> overlay.setVisible(false));
          closeAnim.play();
          isSidebarOpen = false;
      }
   }
   
   //  CALENDAR GRID LOGIC 
   private GridPane createCalendarGrid(double screenWidth) {
      GridPane grid = new GridPane();
      grid.setHgap(5);
      grid.setVgap(0);
      grid.setPadding(new Insets(20));
      grid.setStyle("-fx-background-color: transparent;");
    
      double timeColWidth = 80;
      double dayColWidth = (screenWidth - timeColWidth - 80) / 6;
    
      // Corner cell
      Label cornerLabel = new Label("TIME");
      cornerLabel.setPrefSize(timeColWidth, 50);
      cornerLabel.setAlignment(Pos.CENTER);
      cornerLabel.getStyleClass().add("calendar-header-label");
      grid.add(cornerLabel, 0, 0);
    
      // Header Row 
      for (int i = 0; i < DAYS.length; i++) {
          Label dayLabel = new Label(DAYS[i].toUpperCase());
          dayLabel.setPrefSize(dayColWidth, 50);
          dayLabel.setAlignment(Pos.CENTER);
          dayLabel.getStyleClass().add("calendar-header-label");
          grid.add(dayLabel, i + 1, 0);
      }
      // Populate Time Slots and Courses
      Map<String, CourseTimeSlot> schedule = organizeSchedule();
    
      Map<String, Set<Integer>> filledCells = new HashMap<>();
      for (String day : DAYS) filledCells.put(day, new HashSet<>());
    
      int gridRow = 1;
    
      for (int timeIdx = 0; timeIdx < TIME_SLOTS.length; timeIdx++) {
        
          if (timeIdx % 2 == 0) {
              int hourIndex = timeIdx / 2;
              if (hourIndex < TIME_LABELS.length) {
                  Label timeLabel = new Label(TIME_LABELS[hourIndex]);
                  timeLabel.setPrefSize(timeColWidth, 80);
                  timeLabel.setAlignment(Pos.TOP_CENTER);
                  timeLabel.setPadding(new Insets(5, 0, 0, 0));
                  timeLabel.getStyleClass().add("calendar-time-label");
                  grid.add(timeLabel, 0, gridRow);
                  GridPane.setRowSpan(timeLabel, 2);
              }
          }
        
          for (int dayIdx = 0; dayIdx < DAYS.length; dayIdx++) {
              String day = DAYS[dayIdx];
            
              if (filledCells.get(day).contains(timeIdx)) continue;
            
              String dayTimeKey = day + "-" + timeIdx;
            
              if (schedule.containsKey(dayTimeKey)) {
                  CourseTimeSlot slot = schedule.get(dayTimeKey);
                
                  VBox cell = createMergedCell(slot, dayColWidth);
                  grid.add(cell, dayIdx + 1, gridRow);
                  GridPane.setRowSpan(cell, slot.durationSlots);
                
                  for (int i = 0; i < slot.durationSlots; i++) {
                      filledCells.get(day).add(timeIdx + i);
                  }
                
                  addHoverAnimation(cell);
                
              } else {
                  VBox emptyCell = createEmptyCell(40, dayColWidth);
                  grid.add(emptyCell, dayIdx + 1, gridRow);
              }
          }
          gridRow++;
      }
    
      return grid;
   }
   // HOVER ANIMATION FOR CELLS
   private void addHoverAnimation(VBox cell) {
      ScaleTransition st = new ScaleTransition(Duration.millis(150), cell);
      cell.setOnMouseEntered(e -> {
          cell.toFront();
          st.setToX(1.05);
          st.setToY(1.05);
          st.playFromStart();
      });
      cell.setOnMouseExited(e -> {
          st.setToX(1.0);
          st.setToY(1.0);
          st.playFromStart();
      });
   }
   // ORGANIZE SCHEDULE INTO A MAP
   private class CourseTimeSlot {
      Course course;
      int durationSlots;
      // Constructor
      CourseTimeSlot(Course course, int durationSlots) {
          this.course = course;
          this.durationSlots = durationSlots;
      }
   }
   
   private Map<String, CourseTimeSlot> organizeSchedule() { // Key: "Day-StartIndex", Value: CourseTimeSlot
      Map<String, CourseTimeSlot> schedule = new HashMap<>();
    
      for (Course course : student.getCoursesTaken()) { // Iterate through enlisted courses
          String days = course.getDays(); // e.g. "MonWedFri"
          String time = course.getTime();// e.g. "9:00-10:30"
        
          if (days == null || time == null || days.equals("TBA") || time.equals("TBA")) continue; // Skip TBA courses
        
          List<String> daysList = parseDays(days); // Parse days into list
          String startTime = parseStartTime(time); // Get start time
          int duration = calculateDuration(time); // Calculate duration in slots
        
          if (startTime != null) { // Find start index
              int startIndex = -1; // Initialize
              for (int i = 0; i < TIME_SLOTS.length; i++) { // Search for start time index
                  if (TIME_SLOTS[i].equals(startTime)) { // Match found
                      startIndex = i; // Found
                      break;
                  }
              }
              // If valid start index, map course to each day
              if (startIndex >= 0) { // Valid index
                  for (String day : daysList) { // For each day
                      String key = day + "-" + startIndex;
                      if (duration > 0 && duration <= TIME_SLOTS.length) { // Valid duration
                          schedule.put(key, new CourseTimeSlot(course, duration)); // Map course
                      }
                  }
              }
          }
      }
      return schedule;
   }
   // CREATE MERGED CELL FOR A COURSE
   private VBox createMergedCell(CourseTimeSlot slot, double width) {
	   // Check if course is newly added
      Course course = slot.course;
      String courseKey = course.getCourseID() + "-" + course.getSection();
      boolean isNew = newlyAddedCourses.contains(courseKey);
      
      // Create cell
      VBox cell = new VBox(2);
      cell.setPrefSize(width, 40 * slot.durationSlots);
      cell.setAlignment(Pos.TOP_LEFT);
      cell.setPadding(new Insets(6));
      cell.getStyleClass().add("course-card");
    
      String bgStyle;
      String borderStyle;
    
      if (isNew) { // Special styling for newly added courses
          bgStyle = "-fx-background-color: linear-gradient(to bottom right, #27ae60, #2ecc71);";
          borderStyle = "-fx-border-color: #a8eaff; -fx-border-width: 2px; -fx-effect: dropshadow(gaussian, #2ecc71, 10, 0.4, 0, 0);";
          
          Label badge = new Label("★ NEW"); 
          badge.setStyle("-fx-font-size: 9px; -fx-text-fill: #27ae60; -fx-background-color: white; -fx-padding: 1 4; -fx-background-radius: 2; -fx-font-weight: bold;");
          cell.getChildren().add(badge);
      } else { // Regular styling
          bgStyle = "-fx-background-color: linear-gradient(to bottom right, #2980b9, #3498db);";
          borderStyle = "-fx-border-color: rgba(255,255,255,0.2); -fx-border-width: 1px;";
      }
    
      cell.setStyle(bgStyle + borderStyle); // Apply styles
    
      Label idLabel = new Label(course.getCourseID()); // Course ID
      idLabel.getStyleClass().add("course-card-title");
      idLabel.setStyle("-fx-text-fill: white;");
    
      Label secLabel = new Label(course.getSection()); // Section
      secLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.9); -fx-font-size: 11px;");
    
      Label roomLabel = new Label(course.getRoom()); // Room
      roomLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.7); -fx-font-size: 10px;");
    
      cell.getChildren().addAll(idLabel, secLabel, roomLabel);
    
      Tooltip tooltip = new Tooltip( // Detailed info on hover
          "Course: " + course.getTitle() + "\n" +
          "Time: " + course.getTime() + "\n" +
          "Room: " + course.getRoom()
      );
      Tooltip.install(cell, tooltip);
    
      return cell;
   }
   
   // CREATE EMPTY CELL
   private VBox createEmptyCell(int height, double width) {
      VBox cell = new VBox();
      cell.setPrefSize(width, height);
      cell.getStyleClass().add("calendar-empty-cell");
      return cell;
   }
   // CREATE LEGEND AT BOTTOM
   private HBox createLegend() {
      HBox legend = new HBox(30);
      legend.setPadding(new Insets(15));
      legend.setAlignment(Pos.CENTER);
      legend.getStyleClass().add("legend-box");
    
      Label legendTitle = new Label("Legend:");
      legendTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #c4d0e6;");
    
      HBox regularItem = createLegendItem("#2980b9", "Enlisted Course");
      HBox newItem = createLegendItem("#2ecc71", "Newly Added");
    
      legend.getChildren().addAll(legendTitle, regularItem, newItem);
      return legend;
   }
   // CREATE INDIVIDUAL LEGEND ITEM
   private HBox createLegendItem(String color, String text) {
      HBox item = new HBox(8);
      item.setAlignment(Pos.CENTER_LEFT);
    
      Label colorBox = new Label();
      colorBox.setPrefSize(20, 20);
      colorBox.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 4px;");
    
      Label label = new Label(text);
      label.setStyle("-fx-font-size: 12px; -fx-text-fill: #c4d0e6;");
    
      item.getChildren().addAll(colorBox, label);
      return item;
   }
   
   //  PARSING HELPERS 
   private List<String> parseDays(String days) {
      List<String> result = new ArrayList<>();
      if (days == null) return result;
      days = days.trim();
    
      if (days.contains("Mon")) result.add("Mon");
      if (days.contains("Tues")) result.add("Tues");
      if (days.contains("Wed")) result.add("Wed");
      if (days.contains("Thurs")) result.add("Thurs");
      if (days.contains("Fri")) result.add("Fri");
      if (days.contains("Sat")) result.add("Sat");
    
      if (days.contains("TTh") || days.equals("TTh")) {
          if (!result.contains("Tues")) result.add("Tues");
          if (!result.contains("Thurs")) result.add("Thurs");
      }
      if (days.contains("WF") || days.equals("WF")) {
          if (!result.contains("Wed")) result.add("Wed");
          if (!result.contains("Fri")) result.add("Fri");
      }
      return result;
   }
   
   private String parseStartTime(String time) {
      if (time.contains("-")) {
          return time.split("-")[0].trim();
      }
      return time;
   }
   
   private int calculateDuration(String time) {
      if (!time.contains("-")) return 2;
      try {
          String[] parts = time.split("-");
          if (parts.length != 2) return 2;
        
          String startStr = parts[0].trim();
          String endStr = parts[1].trim();
        
          int startMinutes = parseTimeToMinutes(startStr, false);
          int endMinutes = parseTimeToMinutes(endStr, true);
        
          int durationMinutes = endMinutes - startMinutes;
          if (durationMinutes <= 0) return 2;
        
          int slots = durationMinutes / 30;
          if (durationMinutes % 30 != 0) slots++;
          return slots;
        
      } catch (Exception e) {
          return 2;
      }
   }
   
   private int parseTimeToMinutes(String timeStr, boolean isEndTime) {
      String[] parts = timeStr.split(":");
      int hour = Integer.parseInt(parts[0]);
      int minute = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
    
      if (hour == 12) hour = 12;
      else if (hour >= 1 && hour <= 6) hour += 12;
      else if (hour == 7 && isEndTime) hour += 12;
    
      return hour * 60 + minute;
   }
   
   //  CONFLICT DETECTION (Used by EnlistmentUI) 
   public boolean hasTimeConflict(Course newCourse) {
      String newDays = newCourse.getDays();
      String newTime = newCourse.getTime();  
    
      if (newDays == null || newTime == null || newDays.equals("TBA") || newTime.equals("TBA")) return false;
       // Parse new course details
      List<String> newDaysList = parseDays(newDays);
      String newStartTime = parseStartTime(newTime);
      int newDuration = calculateDuration(newTime);
    // Find new course start index
      int newStartIndex = -1;
      for (int i = 0; i < TIME_SLOTS.length; i++) {
          if (TIME_SLOTS[i].equals(newStartTime)) {
              newStartIndex = i;
              break;
          }
      }
      if (newStartIndex < 0) return false;
      int newEndIndex = newStartIndex + newDuration;
     // Check against existing courses
      for (Course existing : student.getCoursesTaken()) {
          String existingDays = existing.getDays();
          String existingTime = existing.getTime();
          if (existingDays == null || existingTime == null || existingDays.equals("TBA") || existingTime.equals("TBA")) continue;
         // Parse existing course details
          List<String> existingDaysList = parseDays(existingDays);
          String existingStartTime = parseStartTime(existingTime);
          int existingDuration = calculateDuration(existingTime);
         // Find existing course start index
          int existingStartIndex = -1;
          for (int i = 0; i < TIME_SLOTS.length; i++) {
              if (TIME_SLOTS[i].equals(existingStartTime)) {
                  existingStartIndex = i;
                  break;
              }
          }
          if (existingStartIndex < 0) continue; // Invalid index
          int existingEndIndex = existingStartIndex + existingDuration; // Calculate end index
         
          for (String newDay : newDaysList) { // Check each new course day
              if (existingDaysList.contains(newDay)) { // Day overlap
                  if (newStartIndex < existingEndIndex && existingStartIndex < newEndIndex) { // Time overlap
                      return true; // Conflict detected
                  }
              }
          }
      }
      return false; // No conflicts
   }
}
