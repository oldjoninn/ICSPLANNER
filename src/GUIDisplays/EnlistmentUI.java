package GUIDisplays;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import components.Course;
import components.Student;

public class EnlistmentUI {
    private Student student;
    private ObservableList<Course> availableCourses;
    private ObservableList<Course> plannedCourses;
    private List<Course> courseOfferings;
    private Set<String> newlyAddedCourses; // Track newly added courses in this session
    
    public EnlistmentUI(Student student, List<Course> courseOfferings) {
        this.student = student;
        this.availableCourses = FXCollections.observableArrayList();
        this.courseOfferings = courseOfferings;
        this.plannedCourses = FXCollections.observableArrayList(student.getCoursesTaken());
        this.newlyAddedCourses = new HashSet<>();
    }
    
    public Scene EnlistScreen(Stage primaryStage) {
        Pane root = new Pane();
        
        // Back Button
        Button backButton = new Button("← Back to Dashboard");
        backButton.setLayoutX(10);
        backButton.setLayoutY(10);
        backButton.setOnAction(e -> {
            root.getChildren().clear();
            LandingPage landingPage = new LandingPage();
            landingPage.displayaDashboard(root, primaryStage, student);
        });
        
        // View Calendar Button
        Button viewCalendarButton = new Button("📅 View Calendar");
        viewCalendarButton.setLayoutX(650);
        viewCalendarButton.setLayoutY(10);
        viewCalendarButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
                                   "-fx-font-weight: bold; -fx-padding: 8px 15px;");
        viewCalendarButton.setOnAction(e -> {
            CalendarView calendarView = new CalendarView(student, primaryStage);
            
            // Mark newly added courses
            for (String courseKey : newlyAddedCourses) {
                for (Course c : student.getCoursesTaken()) {
                    if ((c.getCourseID() + "-" + c.getSection()).equals(courseKey)) {
                        calendarView.markAsNewlyAdded(c);
                    }
                }
            }
            
            Scene calendarScene = calendarView.createCalendarScene();
            primaryStage.setScene(calendarScene);
        });
        
        // Field for search
        TextField searchField = new TextField();
        searchField.setPromptText("Enter Course Code (e.g., CMSC)");
        searchField.setLayoutX(10);
        searchField.setLayoutY(50);
        searchField.setPrefWidth(200);
        
        // Button for Search
        Button searchButton = new Button("Search");
        searchButton.setLayoutX(220);
        searchButton.setLayoutY(50);
        
        // Label for Search
        Label availableLabel = new Label("Available Courses");
        availableLabel.setLayoutX(10);
        availableLabel.setLayoutY(90);
        availableLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        // Available Courses Table
        TableView<Course> availableTable = new TableView<>();
        availableTable.setItems(availableCourses);
        availableTable.setLayoutX(10);
        availableTable.setLayoutY(110);
        availableTable.setPrefWidth(900);
        availableTable.setPrefHeight(280);
        
        // Observable list for the available courses
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
        
        codeColumn.setPrefWidth(100);
        titleColumn.setPrefWidth(280);
        sectionColumn.setPrefWidth(80);
        timeColumn.setPrefWidth(120);
        dayColumn.setPrefWidth(100);
        roomColumn.setPrefWidth(150);
        
        addButtonColumn.setCellFactory(param -> new TableCell<>() {
            public Button addButton = new Button("Add");
            public void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
                    addButton.setOnAction(e -> {
                        Course selectedCourse = getTableRow().getItem();
                        if (selectedCourse != null) {
                            // Check if already enrolled in any section of the same course code
                            boolean alreadyEnrolled = false;
                            for (Course enrolledCourse : student.getCoursesTaken()) {
                                if (enrolledCourse.getCourseID().equals(selectedCourse.getCourseID())) {
                                    alreadyEnrolled = true;
                                    break;
                                }
                            }
                            if (alreadyEnrolled) {
                                showErrorPopup("", "You are already enrolled in " + selectedCourse.getCourseID());
                            } else{
                            	CalendarView tempCalendar = new CalendarView(student, primaryStage);
                                if (tempCalendar.hasTimeConflict(selectedCourse)) {
                                    showErrorPopup("Schedule Conflict", 
                                        "Cannot add " + selectedCourse.getCourseID() + " " + selectedCourse.getSection() + 
                                        "\n\nThis course conflicts with your existing schedule!" +
                                        "\nTime: " + selectedCourse.getTime() + " (" + selectedCourse.getDays() + ")" +
                                        "\n\nPlease choose a different section or remove the conflicting course first.");
                            } else {
                                student.addCourse(selectedCourse);
                                plannedCourses.add(selectedCourse);
                                // Track as newly added
                                newlyAddedCourses.add(selectedCourse.getCourseID() + "-" + selectedCourse.getSection());
                                showInfoPopup("Course Added", 
                                    "Successfully added: " + selectedCourse.getCourseID() + " " + selectedCourse.getSection() +
                                    "\n\nClick 'View Calendar' to see your updated schedule!");
                            }
                        }
                      }
                    });
                    setGraphic(addButton);
                }
            }
        });
        
        availableTable.getColumns().addAll(codeColumn, titleColumn, sectionColumn, timeColumn, dayColumn, roomColumn, addButtonColumn);
        
        Label enlistedLabel = new Label("Enlisted Courses (" + plannedCourses.size() + " courses)");
        enlistedLabel.setLayoutX(10);
        enlistedLabel.setLayoutY(400);
        enlistedLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        // Enlisted Courses Table
        TableView<Course> plannedTable = new TableView<>();
        plannedTable.setItems(plannedCourses);
        plannedTable.setLayoutX(10);
        plannedTable.setLayoutY(420);
        plannedTable.setPrefWidth(900);
        plannedTable.setPrefHeight(250);
        
        // Observable List for Enlisted Course
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
        enlistedDayCol.setCellValueFactory(new PropertyValueFactory<>("days"));
        enlistedRoomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
        
        enlistedCodeCol.setPrefWidth(80);
        enlistedTitleCol.setPrefWidth(260);
        enlistedSectionCol.setPrefWidth(70);
        enlistedTimeCol.setPrefWidth(110);
        enlistedDayCol.setPrefWidth(90);
        enlistedRoomCol.setPrefWidth(140);
        
        enlistedRemoveCol.setCellFactory(param -> new TableCell<>() {
            Button removeButton = new Button("Remove");
            public void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    removeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
                    removeButton.setOnAction(e -> {
                        Course selectedCourse = getTableRow().getItem();
                        if (selectedCourse != null) {
                            student.removeCourse(selectedCourse);
                            plannedCourses.remove(selectedCourse);
                            // Remove from newly added if it was there
                            newlyAddedCourses.remove(selectedCourse.getCourseID() + "-" + selectedCourse.getSection());
                            enlistedLabel.setText("Enlisted Courses (" + plannedCourses.size() + " courses)");
                        }
                    });
                    setGraphic(removeButton);
                }
            }
        });
        
        plannedTable.getColumns().addAll(enlistedCodeCol, enlistedTitleCol, enlistedSectionCol, 
                                         enlistedTimeCol, enlistedDayCol, enlistedRoomCol, enlistedRemoveCol);
        
        // Search Button Action
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
        
        root.getChildren().addAll(backButton, viewCalendarButton, searchField, searchButton, 
                                  availableLabel, availableTable, enlistedLabel, plannedTable);
        
        Scene scene = new Scene(root, 1000, 700);
        return scene;
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