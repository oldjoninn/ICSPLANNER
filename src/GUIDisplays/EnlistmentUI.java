package GUIDisplays;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Rectangle2D;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import components.Course;
import components.Student;
import components.Save_Load;
import application.Main;
public class EnlistmentUI {
   private Main mainApp;
   private Stage primaryStage;
   private Student student;
   private ObservableList<Course> availableCourses;
   private ObservableList<Course> plannedCourses;
   private List<Course> courseOfferings;
   private Set<String> newlyAddedCourses;
   private static final double SIDEBAR_WIDTH = 300;
   private Pane dashboardPane;
   private Pane overlay;
   private boolean isSidebarOpen = false;
   public EnlistmentUI(Main mainApp, Student student, List<Course> courseOfferings, Stage primaryStage) {
       this.mainApp = mainApp;
       this.primaryStage = primaryStage;
       this.student = student;
       this.availableCourses = FXCollections.observableArrayList();
       this.courseOfferings = courseOfferings;
       this.plannedCourses = FXCollections.observableArrayList(student.getCoursesTaken());
       this.newlyAddedCourses = new HashSet<>();
   }
   public Scene EnlistScreen(Stage primaryStage) {
   	// 1. Main Container (Supports Layers)
       Pane mainRoot = new Pane();
       mainRoot.getStyleClass().add("enlistment-view");
       // 2. Screen Dimensions
       Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
       double screenWidth = screenBounds.getWidth();
       double screenHeight = screenBounds.getHeight();
       // 3. Content Layer (Holds all your UI elements)
       Pane contentRoot = new Pane();
       contentRoot.setPrefSize(screenWidth, screenHeight);
       // --- UI ELEMENTS ---
       // Menu Button (Replaces Back Button)
       Button menuBtn = new Button("☰");
       menuBtn.setLayoutX(20);
       menuBtn.setLayoutY(10);
       menuBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px; -fx-cursor: hand;");
       menuBtn.setOnAction(e -> {
           if (isSidebarOpen) closeSidebar(); else openSidebar();
       });
       // View Calendar Button - Pinned to Top Right
       Button viewCalendarButton = new Button("📅 View Calendar");
       // Calculate position based on screen width
       viewCalendarButton.setLayoutX(screenWidth - 180);
       viewCalendarButton.setLayoutY(10);
       viewCalendarButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
                                  "-fx-font-weight: bold; -fx-padding: 8px 15px; -fx-background-radius: 20px;");
       viewCalendarButton.setOnAction(e -> {
           CalendarView calendarView = new CalendarView(mainApp, student, primaryStage);
           for (String courseKey : newlyAddedCourses) {
               for (Course c : student.getCoursesTaken()) {
                   if ((c.getCourseID() + "-" + c.getSection()).equals(courseKey)) {
                       calendarView.markAsNewlyAdded(c);
                   }
               }
           }
           Scene calendarScene = calendarView.createCalendarScene();
           primaryStage.setScene(calendarScene);
           primaryStage.setMaximized(true); // Ensure it stays maximized
       });
       // Search Field
       TextField searchField = new TextField();
       searchField.setPromptText("Enter Course Code (e.g., CMSC)");
       searchField.setLayoutX(20);
       searchField.setLayoutY(50);
       searchField.setPrefWidth(250);
       searchField.getStyleClass().add("search-field");
       // Search Button
       Button searchButton = new Button("Search");
       searchButton.setLayoutX(280); // Adjusted for wider search field
       searchButton.setLayoutY(50);
       searchButton.getStyleClass().add("action-btn");
       // --- AVAILABLE COURSES SECTION ---
       Label availableLabel = new Label("Available Courses");
       availableLabel.setLayoutX(20);
       availableLabel.setLayoutY(90);
       availableLabel.getStyleClass().add("section-label");
       TableView<Course> availableTable = new TableView<>();
       availableTable.setItems(availableCourses);
       availableTable.setLayoutX(20);
       availableTable.setLayoutY(115);
     
       // Dynamic Size: Width is screen width minus padding, Height is ~45% of screen
       availableTable.setPrefWidth(screenWidth - 40);
       availableTable.setPrefHeight(screenHeight * 0.45);
     
       availableTable.getStyleClass().add("styled-table");
       // Make columns auto-resize to fill the wider table
       availableTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
       // Columns
       TableColumn<Course, String> codeColumn = new TableColumn<>("Course Code");
       TableColumn<Course, String> titleColumn = new TableColumn<>("Course Title");
       TableColumn<Course, String> sectionColumn = new TableColumn<>("Section");
       TableColumn<Course, String> timeColumn = new TableColumn<>("Time");
       TableColumn<Course, String> dayColumn = new TableColumn<>("Day");
       TableColumn<Course, String> roomColumn = new TableColumn<>("Room");
       TableColumn<Course, Button> addButtonColumn = new TableColumn<>("Action");
       codeColumn.setCellValueFactory(new PropertyValueFactory<>("courseID"));
       titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
       sectionColumn.setCellValueFactory(new PropertyValueFactory<>("section"));
       timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
       dayColumn.setCellValueFactory(new PropertyValueFactory<>("days"));
       roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
       addButtonColumn.setCellFactory(param -> new TableCell<>() {
           public Button addButton = new Button("Add");
           public void updateItem(Button item, boolean empty) {
               super.updateItem(item, empty);
               if (empty) { setGraphic(null); } else {
                   addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
                   addButton.setOnAction(e -> {
                       Course selectedCourse = getTableRow().getItem();
                       if (selectedCourse != null) {
                           
                            // 1. CHECK DEGREE OFFERING FIRST
                            boolean isOfferedInDegree = false;
                            for (Course degreeCourse : student.getDegree().getCoursesOffered()) {
                                if (degreeCourse.getCourseID().equals(selectedCourse.getCourseID())) {
                                    isOfferedInDegree = true;
                                    break;
                                }
                            }
                            if (!isOfferedInDegree) {
                                showErrorPopup("Invalid Selection", "This course is not offered in your degree program.");
                                return;
                            }
                            // 2. CHECK DUPLICATE (Moved UP - This solves your issue!)
                            // Check if already enrolled in ANY section of the same course code
                            boolean alreadyEnrolled = false;
                            for (Course enrolledCourse : student.getCoursesTaken()) {
                                if (enrolledCourse.getCourseID().equals(selectedCourse.getCourseID())) {
                                    alreadyEnrolled = true;
                                    break;
                                }
                            }
                            if (alreadyEnrolled) {
                                showErrorPopup("Duplicate", "You are already enrolled in " + selectedCourse.getCourseID());
                                return;
                            }
                           
                            String degreeName = student.getDegree().getName();
                           
                            if (degreeName.equals("BSCS")) {
                                // FIX: Only enforce Lab requirement IF the course actually HAS labs
                                if (courseHasLabs(selectedCourse.getCourseID())) {
                                    if (!isLabSection(selectedCourse.getSection())) {
                                        showErrorPopup("Invalid Selection", "This course has a laboratory component.\nPlease select a Lab section (e.g., G-1L).");
                                        return;
                                    }
                                }
                                // If courseHasLabs is false (like CMSC 190), we allow adding the section directly.
                               
                            } else if (degreeName.equals("MSCS") || degreeName.equals("PHD")) {
                                if (isLabSection(selectedCourse.getSection())) {
                                    showErrorPopup("Invalid Selection", "Please add a lecture section.");
                                    return;
                                }
                            } else if (degreeName.equals("MIT")) {
                                // Keeping MIT logic strict as requested previously
                                boolean hasLabs = selectedCourse.getCourseID().equals("IT 210") || selectedCourse.getCourseID().equals("IT 238");
                                if (hasLabs) {
                                    if (!isLabSection(selectedCourse.getSection())) {
                                        showErrorPopup("Invalid Selection", "Please add a lab section instead");
                                        return;
                                    }
                                } else {
                                    if (isLabSection(selectedCourse.getSection())) {
                                        showErrorPopup("Invalid Selection", "Please add a lecture section.");
                                        return;
                                    }
                                }
                            }
                           
                            // 4. CHECK TIME CONFLICT
                            CalendarView tempCalendar = new CalendarView(mainApp, student, primaryStage);
                            if (tempCalendar.hasTimeConflict(selectedCourse)) {
                                showErrorPopup("Schedule Conflict", "This course conflicts with your existing schedule!");
                                return;
                            }
                           
                            // --- PROCEED TO ADD ---
                            student.addCourse(selectedCourse);
                            plannedCourses.add(selectedCourse);
                            newlyAddedCourses.add(selectedCourse.getCourseID() + "-" + selectedCourse.getSection());
                           
                            // Auto-add Lecture Logic
                            if (degreeName.equals("BSCS")|| (degreeName.equals("MIT") && isLabSection(selectedCourse.getSection()))){
                                String lectureSection = getLectureSection(selectedCourse.getSection());
                                Course lectureCourse = findLectureCourse(selectedCourse.getCourseID(), lectureSection);
                                if (lectureCourse != null) {
                                    boolean lectureAlreadyEnrolled = false;
                                    for (Course enrolled : student.getCoursesTaken()) {
                                        if (enrolled.getCourseID().equals(lectureCourse.getCourseID()) &&
                                            enrolled.getSection().equals(lectureCourse.getSection())) {
                                            lectureAlreadyEnrolled = true;
                                            break;
                                        }
                                    }
                                    if (!lectureAlreadyEnrolled) {
                                        student.addCourse(lectureCourse);
                                        plannedCourses.add(lectureCourse);
                                        newlyAddedCourses.add(lectureCourse.getCourseID() + "-" + lectureCourse.getSection());
                                    }
                                }
                            }
                            Save_Load.saveStudent(student);
                            showInfoPopup("Course Added", "Successfully added: " + selectedCourse.getCourseID());
                       }
                   });
                   setGraphic(addButton);
               }
           }
       });
       availableTable.getColumns().addAll(codeColumn, titleColumn, sectionColumn, timeColumn, dayColumn, roomColumn, addButtonColumn);
       // --- ENLISTED COURSES SECTION ---
       Label enlistedLabel = new Label("Enlisted Courses (" + plannedCourses.size() + " courses)");
       enlistedLabel.setLayoutX(20);
       // Position below the first table with some padding
       double enlistedY = 125 + (screenHeight * 0.40) + 20;
       enlistedLabel.setLayoutY(enlistedY);
       enlistedLabel.getStyleClass().add("section-label");
       TableView<Course> plannedTable = new TableView<>();
       plannedTable.setItems(plannedCourses);
       plannedTable.setLayoutX(20);
       plannedTable.setLayoutY(enlistedY + 25);
     
       // Dynamic Size: Width is screen width minus padding, Height fills rest of screen minus bottom margin
       plannedTable.setPrefWidth(screenWidth - 40);
       plannedTable.setPrefHeight(screenHeight - (enlistedY + 100));
     
       plannedTable.getStyleClass().add("styled-table");
       plannedTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
       // Enlisted Columns
       TableColumn<Course, String> enlistedCodeCol = new TableColumn<>("Code");
       TableColumn<Course, String> enlistedTitleCol = new TableColumn<>("Course Title");
       TableColumn<Course, String> enlistedSectionCol = new TableColumn<>("Section");
       TableColumn<Course, String> enlistedTimeCol = new TableColumn<>("Time");
       TableColumn<Course, String> enlistedDayCol = new TableColumn<>("Day");
       TableColumn<Course, String> enlistedRoomCol = new TableColumn<>("Room");
       TableColumn<Course, Button> enlistedRemoveCol = new TableColumn<>("Remove");
       enlistedCodeCol.setCellValueFactory(new PropertyValueFactory<>("courseID"));
       enlistedTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
       enlistedSectionCol.setCellValueFactory(new PropertyValueFactory<>("section"));
       enlistedTimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
       enlistedRoomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
       enlistedDayCol.setCellValueFactory(new PropertyValueFactory<>("days"));
       enlistedRemoveCol.setCellFactory(param -> new TableCell<>() {
           Button removeButton = new Button("Remove");
           public void updateItem(Button item, boolean empty) {
               super.updateItem(item, empty);
               if (empty) { setGraphic(null); } else {
                   removeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
                   removeButton.setOnAction(e -> {
                       Course selectedCourse = getTableRow().getItem();
                       if (selectedCourse != null) {
                          
                           // 1. Remove Selected
                           removeCourseFromList(selectedCourse);
                          
                           // 2. Remove Paired Course
                           String currentID = selectedCourse.getCourseID();
                           String currentSection = selectedCourse.getSection();
                          
                           if (isLabSection(currentSection)) {
                               String lectureSection = getLectureSection(currentSection);
                               Course partneredLecture = findEnrolledCourse(currentID, lectureSection);
                               if (partneredLecture != null) removeCourseFromList(partneredLecture);
                           } else {
                               List<Course> labsToRemove = new ArrayList<>();
                               for (Course c : plannedCourses) {
                                   if (c.getCourseID().equals(currentID) &&
                                       isLabSection(c.getSection()) &&
                                       getLectureSection(c.getSection()).equals(currentSection)) {
                                       labsToRemove.add(c);
                                   }
                               }
                               for (Course lab : labsToRemove) removeCourseFromList(lab);
                           }
                           enlistedLabel.setText("Enlisted Courses (" + plannedCourses.size() + " courses)");
                           Save_Load.saveStudent(student);
                       }
                   });
                   setGraphic(removeButton);
               }
           }
       });
       plannedTable.getColumns().addAll(enlistedCodeCol, enlistedTitleCol, enlistedSectionCol,
           enlistedTimeCol, enlistedDayCol, enlistedRoomCol, enlistedRemoveCol);
       // Search Action
       searchButton.setOnAction(e -> {
           String courseCode = searchField.getText().trim().toUpperCase();
           availableCourses.clear();
           int foundCount = 0;
           for (Course course : courseOfferings) {
               if (course.getCourseID() != null && course.getCourseID().toUpperCase().startsWith(courseCode)) {
                   availableCourses.add(course);
                   foundCount++;
               }
           }
           if (foundCount == 0) {
               showErrorPopup("", "No courses found for course code: " + courseCode);
           }
       });
       contentRoot.getChildren().addAll(menuBtn, viewCalendarButton, searchField, searchButton,
               availableLabel, availableTable, enlistedLabel, plannedTable);
           // 4. Sidebar & Overlay Setup
           overlay = new Pane();
           overlay.setPrefSize(screenWidth, screenHeight);
           overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
           overlay.setVisible(false);
           overlay.setOnMouseClicked(e -> closeSidebar());
           Dashboard dashboard = new Dashboard(SIDEBAR_WIDTH, mainApp, primaryStage, student);
           dashboardPane = dashboard.getView();
           dashboardPane.setPrefHeight(screenHeight);
           dashboardPane.setTranslateX(-SIDEBAR_WIDTH); // Initially hidden
           // 5. Add layers to Main Root
           mainRoot.getChildren().addAll(contentRoot, overlay, dashboardPane);
         
           // Listeners for resizing
           mainRoot.widthProperty().addListener((obs, oldVal, newVal) -> {
              overlay.setPrefWidth(newVal.doubleValue());
              contentRoot.setPrefWidth(newVal.doubleValue());
           });
           mainRoot.heightProperty().addListener((obs, oldVal, newVal) -> {
              overlay.setPrefHeight(newVal.doubleValue());
              contentRoot.setPrefHeight(newVal.doubleValue());
              dashboardPane.setPrefHeight(newVal.doubleValue());
           });
           Scene scene = new Scene(mainRoot, screenWidth, screenHeight);
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
 
   // Extracted logic to keep UI code clean
  
   private void openSidebar() {
       if (!isSidebarOpen) {
           overlay.setVisible(true);
           TranslateTransition openAnim = new TranslateTransition(Duration.seconds(0.4), dashboardPane);
           openAnim.setToX(0);
           openAnim.setInterpolator(Interpolator.EASE_OUT);
           openAnim.play();
           isSidebarOpen = true;
       }
   }
   private void closeSidebar() {
       if (isSidebarOpen) {
           TranslateTransition closeAnim = new TranslateTransition(Duration.seconds(0.4), dashboardPane);
           closeAnim.setToX(-SIDEBAR_WIDTH);
           closeAnim.setInterpolator(Interpolator.EASE_IN);
           closeAnim.setOnFinished(e -> overlay.setVisible(false));
           closeAnim.play();
           isSidebarOpen = false;
       }
   }
   private void handleAddCourse(Course selectedCourse) {
       boolean isOfferedInDegree = false;
       for (Course degreeCourse : student.getDegree().getCoursesOffered()) {
           if (degreeCourse.getCourseID().equals(selectedCourse.getCourseID())) {
               isOfferedInDegree = true;
               break;
           }
       }
       if (!isOfferedInDegree) {
           showErrorPopup("Invalid Selection", "This course is not offered in your degree program.");
           return;
       }
       String degreeName = student.getDegree().getName();
       if (degreeName.equals("BSCS")) {
           if (!isLabSection(selectedCourse.getSection())) {
               showErrorPopup("Invalid Selection", "Please add a lab section instead.");
               return;
           }
       } else if (degreeName.equals("MSCS") || degreeName.equals("PHD")) {
           if (isLabSection(selectedCourse.getSection())) {
               showErrorPopup("Invalid Selection", "Please add a lecture section.");
               return;
           }
       } else if (degreeName.equals("MIT")) {
           boolean hasLabs = selectedCourse.getCourseID().equals("IT 210") || selectedCourse.getCourseID().equals("IT 238");
           if (hasLabs) {
               if (!isLabSection(selectedCourse.getSection())) {
                   showErrorPopup("Invalid Selection", "Please add a lab section instead");
                   return;
               }
           } else {
               if (isLabSection(selectedCourse.getSection())) {
                   showErrorPopup("Invalid Selection", "Please add a lecture section.");
                   return;
               }
           }
       }
       boolean alreadyEnrolled = false;
       for (Course enrolledCourse : student.getCoursesTaken()) {
           if (enrolledCourse.getCourseID().equals(selectedCourse.getCourseID())) {
               alreadyEnrolled = true;
               break;
           }
       }
       if (alreadyEnrolled) {
           showErrorPopup("", "You are already enrolled in " + selectedCourse.getCourseID());
           return;
       }
       CalendarView tempCalendar = new CalendarView(mainApp, student, primaryStage);
       if (tempCalendar.hasTimeConflict(selectedCourse)) {
           showErrorPopup("Schedule Conflict",
               "Cannot add " + selectedCourse.getCourseID() + " " + selectedCourse.getSection() +
               "\n\nThis course conflicts with your existing schedule!" +
               "\nTime: " + selectedCourse.getTime() + " (" + selectedCourse.getDays() + ")" +
               "\n\nPlease choose a different section or remove the conflicting course first.");
           return;
       }
       student.addCourse(selectedCourse);
       plannedCourses.add(selectedCourse);
       newlyAddedCourses.add(selectedCourse.getCourseID() + "-" + selectedCourse.getSection());
       if (degreeName.equals("BSCS") || (degreeName.equals("MIT") && isLabSection(selectedCourse.getSection()))) {
           String lectureSection = getLectureSection(selectedCourse.getSection());
           Course lectureCourse = findLectureCourse(selectedCourse.getCourseID(), lectureSection);
           if (lectureCourse != null) {
               boolean lectureAlreadyEnrolled = false;
               for (Course enrolled : student.getCoursesTaken()) {
                   if (enrolled.getCourseID().equals(lectureCourse.getCourseID()) &&
                       enrolled.getSection().equals(lectureCourse.getSection())) {
                       lectureAlreadyEnrolled = true;
                       break;
                   }
               }
               if (!lectureAlreadyEnrolled) {
                   student.addCourse(lectureCourse);
                   plannedCourses.add(lectureCourse);
                   newlyAddedCourses.add(lectureCourse.getCourseID() + "-" + lectureCourse.getSection());
               }
           }
       }
       Save_Load.saveStudent(student);
       showInfoPopup("Course Added",
           "Successfully added: " + selectedCourse.getCourseID() + " " + selectedCourse.getSection() +
           "\n\nClick 'View Calendar' to see your updated schedule!");
   }
   private boolean courseHasLabs(String courseID) {
       for (Course c : courseOfferings) {
           if (c.getCourseID().equals(courseID) && isLabSection(c.getSection())) {
               return true;
           }
       }
       return false;
   }
  
   private void removeCourseFromList(Course c) {
       student.removeCourse(c);
       plannedCourses.remove(c);
       newlyAddedCourses.remove(c.getCourseID() + "-" + c.getSection());
   }
  
   private Course findEnrolledCourse(String courseID, String section) {
       for (Course c : plannedCourses) {
           if (c.getCourseID().equals(courseID) && c.getSection().equals(section)) {
               return c;
           }
       }
       return null;
   }
   private boolean isLabSection(String section) {
       return section != null && section.contains("-") && section.endsWith("L");
   }
   private String getLectureSection(String labSection) {
       if (labSection != null && labSection.contains("-")) {
           return labSection.substring(0, labSection.indexOf("-"));
       }
       return labSection;
   }
   private Course findLectureCourse(String courseID, String lectureSection) {
       for (Course course : courseOfferings) {
           if (course.getCourseID().equals(courseID) && course.getSection().equals(lectureSection)) {
               return course;
           }
       }
       return null;
   }
   private void showErrorPopup(String title, String message) {
       Alert alert = new Alert(Alert.AlertType.ERROR);
       alert.setTitle(title);
       alert.setHeaderText(null);
       alert.setContentText(message);
       alert.showAndWait();
   }
   private void showInfoPopup(String title, String message) {
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setTitle(title);
       alert.setHeaderText(null);
       alert.setContentText(message);
       alert.showAndWait();
   }
}



