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
    
    public EnlistmentUI(Student student, List<Course> courseOfferings) {
        this.student = student;
        this.availableCourses = FXCollections.observableArrayList();
        this.courseOfferings = courseOfferings;
        this.plannedCourses = FXCollections.observableArrayList(student.getCoursesTaken());
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
        
        // Available Courses Table
        TableView<Course> availableTable = new TableView<>();
        availableTable.setItems(availableCourses);
        availableTable.setLayoutX(10);
        availableTable.setLayoutY(110);
        availableTable.setPrefWidth(600);
        availableTable.setPrefHeight(300);
        
        // Observable list for the available courses
        TableColumn<Course, String> codeColumn = new TableColumn<>("Course Code");
        TableColumn<Course, String> sectionColumn = new TableColumn<>("Section");
        TableColumn<Course, String> timeColumn = new TableColumn<>("Time");
        TableColumn<Course, String> dayColumn = new TableColumn<>("Day");
        TableColumn<Course, String> roomColumn = new TableColumn<>("Room");
        TableColumn<Course, Button> addButtonColumn = new TableColumn<>("Action");
        
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        sectionColumn.setCellValueFactory(new PropertyValueFactory<>("section"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("days"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
        addButtonColumn.setCellFactory(param -> new TableCell<>() {
            public Button addButton = new Button("Add");
            public void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
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
                            } else {
                                student.addCourse(selectedCourse);
                                plannedCourses.add(selectedCourse);
                            }
                        }
                    });
                    setGraphic(addButton);
                }
            }
        });
        
        availableTable.getColumns().addAll(codeColumn, sectionColumn, timeColumn, dayColumn, roomColumn, addButtonColumn);
        availableTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        Label enlistedLabel = new Label("Enlisted Courses");
        enlistedLabel.setLayoutX(10);
        enlistedLabel.setLayoutY(420);
        
        // Enlisted Courses Table
        TableView<Course> plannedTable = new TableView<>();
        plannedTable.setItems(plannedCourses);
        plannedTable.setLayoutX(10);
        plannedTable.setLayoutY(440);
        plannedTable.setPrefWidth(600);
        plannedTable.setPrefHeight(250);
        
        // Observable List for Enlisted Course
        TableColumn<Course, String> enlistedCodeCol = new TableColumn<>("Code");
        TableColumn<Course, String> enlistedSectionCol = new TableColumn<>("Section");
        TableColumn<Course, String> enlistedTimeCol = new TableColumn<>("Time");
        TableColumn<Course, String> enlistedDayCol = new TableColumn<>("Day");
        TableColumn<Course, String> enlistedRoomCol = new TableColumn<>("Room");
        TableColumn<Course, Button> enlistedRemoveCol = new TableColumn<>("Remove");
        
        enlistedCodeCol.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        enlistedSectionCol.setCellValueFactory(new PropertyValueFactory<>("section"));
        enlistedTimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        enlistedDayCol.setCellValueFactory(new PropertyValueFactory<>("days"));
        enlistedRoomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
        enlistedRemoveCol.setCellFactory(param -> new TableCell<>() {
            Button removeButton = new Button("Remove");
            public void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    removeButton.setOnAction(e -> {
                        Course selectedCourse = getTableRow().getItem();
                        if (selectedCourse != null) {
                            student.removeCourse(selectedCourse);
                            plannedCourses.remove(selectedCourse);
                        }
                    });
                    setGraphic(removeButton);
                }
            }
        });
        enlistedRemoveCol.setPrefWidth(70);
        
        plannedTable.getColumns().addAll(enlistedCodeCol, enlistedSectionCol, enlistedTimeCol, enlistedDayCol, enlistedRoomCol, enlistedRemoveCol);
        plannedTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
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
        
        root.getChildren().addAll(backButton, searchField, searchButton, availableLabel, availableTable, enlistedLabel, plannedTable);
        
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
}