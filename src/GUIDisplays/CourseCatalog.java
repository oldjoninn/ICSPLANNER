package GUIDisplays;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.List;
import components.*;
import application.Main;
public class CourseCatalog {
   private Student student;
   private ObservableList<Course> catalogCourses;
   private ObservableList<Course> filteredCourses;
  
   private Pane dashboardPane;
   private Pane overlay;
   private boolean isSidebarOpen = false;
   private Main mainApp;
   private static final double SIDEBAR_WIDTH = 300;
 
   public CourseCatalog(Main mainApp, Student student) {
       this.mainApp = mainApp;
       this.student = student;
       this.catalogCourses = FXCollections.observableArrayList();
       this.filteredCourses = FXCollections.observableArrayList();
       loadCatalogCourses();
       filteredCourses.addAll(catalogCourses);
   }
   public Scene createCatalogScene(Stage primaryStage) {
       Pane mainRoot = new Pane();
       mainRoot.setStyle("-fx-background-color: #f5f5f5;"); // Keep original bg color
      
      
       Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
       double screenWidth = screenBounds.getWidth();
       double screenHeight = screenBounds.getHeight();
      
       Pane contentRoot = new Pane();
       contentRoot.setPrefSize(screenWidth, screenHeight);
    
      
       // Menu Button
       Button menuBtn = new Button("☰");
       menuBtn.setLayoutX(20);
       menuBtn.setLayoutY(10);
       menuBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #2c3e50; -fx-font-size: 24px; -fx-cursor: hand;");
       menuBtn.setOnAction(e -> {
           if (isSidebarOpen) closeSidebar(); else openSidebar();
       });
       // Title Label
       Label titleLabel = new Label("Course Catalog - ICS Department");
       titleLabel.setLayoutX(60);
       titleLabel.setLayoutY(20);
       titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #2c3e50;");
       // Search Field
       TextField searchField = new TextField();
       searchField.setPromptText("Enter Course Code (e.g., CMSC)");
       searchField.setLayoutX(20);
       searchField.setLayoutY(70);
       searchField.setPrefWidth(250);
       searchField.getStyleClass().add("search-field"); // Assuming shared CSS
       // Search Button
       Button searchButton = new Button("Search");
       searchButton.setLayoutX(280);
       searchButton.setLayoutY(70);
       searchButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold;");
       // Catalog Table
       TableView<Course> catalogTable = new TableView<>();
       catalogTable.setItems(filteredCourses);
       catalogTable.setLayoutX(20);
       catalogTable.setLayoutY(110);
      
      
       catalogTable.setPrefWidth(screenWidth - 40);
       catalogTable.setPrefHeight(screenHeight * 0.80);
      
       catalogTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
       // Columns
       TableColumn<Course, String> codeColumn = new TableColumn<>("Code");
       TableColumn<Course, String> titleColumn = new TableColumn<>("Title");
       TableColumn<Course, String> descriptionColumn = new TableColumn<>("Description");
       TableColumn<Course, Integer> unitsColumn = new TableColumn<>("Units");
       codeColumn.setCellValueFactory(new PropertyValueFactory<>("courseID"));
       titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
       descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
       unitsColumn.setCellValueFactory(new PropertyValueFactory<>("units"));
      
   
       descriptionColumn.setCellFactory(tc -> {
           TableCell<Course, String> cell = new TableCell<>();
           Text text = new Text();
           cell.setGraphic(text);
           cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
           text.wrappingWidthProperty().bind(descriptionColumn.widthProperty().subtract(10));
           text.textProperty().bind(cell.itemProperty());
           return cell;
       });
     
       codeColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);  
       titleColumn.setMaxWidth(1f * Integer.MAX_VALUE * 25);
       descriptionColumn.setMaxWidth(1f * Integer.MAX_VALUE * 55);
       unitsColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10); 
       catalogTable.getColumns().addAll(codeColumn, titleColumn, descriptionColumn, unitsColumn);
      
      
       searchButton.setOnAction(e -> {
           String courseCode = searchField.getText().trim().toUpperCase();
           filteredCourses.clear();
           int foundCount = 0;
           for (Course course : catalogCourses) {
               if (course.getCourseID() != null && course.getCourseID().toUpperCase().contains(courseCode)) {
                   filteredCourses.add(course);
                   foundCount++;
               }
           }
           if (foundCount == 0) {
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setTitle("Search Result");
               alert.setHeaderText(null);
               alert.setContentText("No courses found matching: " + courseCode);
               alert.showAndWait();
              
           }
       });
       contentRoot.getChildren().addAll(menuBtn, titleLabel, searchField, searchButton, catalogTable);
      
       overlay = new Pane();
       overlay.setPrefSize(screenWidth, screenHeight);
       overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
       overlay.setVisible(false);
       overlay.setOnMouseClicked(e -> closeSidebar());
     
       Dashboard dashboard = new Dashboard(SIDEBAR_WIDTH, this.mainApp, primaryStage, this.student);
       dashboardPane = dashboard.getView();
       dashboardPane.setPrefHeight(screenHeight);
       dashboardPane.setTranslateX(-SIDEBAR_WIDTH); // Initially hidden
     
       mainRoot.getChildren().addAll(contentRoot, overlay, dashboardPane);
      
       mainRoot.widthProperty().addListener((obs, oldVal, newVal) -> {
          double newWidth = newVal.doubleValue();
          overlay.setPrefWidth(newWidth);
          contentRoot.setPrefWidth(newWidth);
        
          catalogTable.setPrefWidth(newWidth - 40);
       });
      
       mainRoot.heightProperty().addListener((obs, oldVal, newVal) -> {
          double newHeight = newVal.doubleValue();
          overlay.setPrefHeight(newHeight);
          contentRoot.setPrefHeight(newHeight);
          dashboardPane.setPrefHeight(newHeight);
        
          catalogTable.setPrefHeight(newHeight * 0.80);
       });
       Scene scene = new Scene(mainRoot, screenWidth, screenHeight);
      
       try {
           scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
       } catch (Exception e) {}
       return scene;
   }
 
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
   private void loadCatalogCourses() {
       String csvPath = "src/application/course_offerings.csv";
       List<Course> loadedCourses = application.CSVReader.readProgramCourses(csvPath, null);
       if (loadedCourses != null) {
           catalogCourses.addAll(loadedCourses);
       }
   }
}

