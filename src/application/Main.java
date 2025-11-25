package application;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import GUIDisplays.Login;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import components.*;

public class Main extends Application {
    
    public static Map<String, DegreeProgram> degreePrograms = new HashMap<>();
    public static List<Course> courseOfferings = new ArrayList<>();
      
    @Override
    public void start(Stage primaryStage) {
        loadDegreePrograms();
        loadCourseOfferings();
        
        Pane root = new Pane();
        
        // 1. FULL SIZE BACKGROUND IMAGE
        ImageView backgroundImage = new ImageView();
        ImageView logoImage = new ImageView();
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/images/LoginPage.png"));
            backgroundImage.setImage(bgImage);
            
            // Make background image fill the entire screen
            backgroundImage.setFitWidth(Screen.getPrimary().getBounds().getWidth());
            backgroundImage.setFitHeight(Screen.getPrimary().getBounds().getHeight());
            backgroundImage.setPreserveRatio(false); // Stretch to fill
            
            Image Logo = new Image(getClass().getResourceAsStream("/images/REGICSLogo.png"));
            logoImage.setImage(Logo);
            
        } catch (Exception e) {
            System.err.println("Background image not found, using fallback color");
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);");
        }
        
        Label welcomeText = new Label("Welcome to REGICS.");
        welcomeText.getStyleClass().add("welcome-text");
        Label abbrievText = new Label("Registration and Enrollment Gateway of ICS");
        abbrievText.getStyleClass().add("abbriev-text");
        
        FadeTransition fadeIn = new FadeTransition();
        fadeIn.setDuration(Duration.millis(4500));
        fadeIn.setNode(welcomeText);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        
        FadeTransition fadeIn2 = new FadeTransition();
        fadeIn2.setDuration(Duration.millis(4500));
        fadeIn2.setNode(abbrievText);
        fadeIn2.setFromValue(0.0);
        fadeIn2.setToValue(1.0);
        fadeIn2.play();
        
        Button Login = new Button("Login");
        Login.setLayoutX(1500);
        Login.setLayoutY(50);
        logoImage.setLayoutX(30);
        logoImage.setLayoutY(40);
        logoImage.setPreserveRatio(true);
        logoImage.setFitWidth(80);
        
        root.getChildren().addAll(backgroundImage, Login, logoImage, welcomeText, abbrievText);
        
        Login.setOnAction(e -> {
            root.getChildren().removeAll(Login, welcomeText, abbrievText, logoImage);
            Login login = new Login();
            login.displayLogInPopUp(root, primaryStage);
        });
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
        
        welcomeText.setLayoutX((root.getWidth() - welcomeText.getWidth()) / 2);
        welcomeText.setLayoutY((root.getHeight() - welcomeText.getHeight()) / 2);
        
        abbrievText.setLayoutX((root.getWidth() - abbrievText.getWidth()) / 2);
        abbrievText.setLayoutY(550);
    }
    
    private static void loadDegreePrograms() {
        // Instantiate all DegProgs
        DegreeProgram bscs = new DegreeProgram("BSCS", "src/application/ics_cmsc_courses.csv");
        DegreeProgram mscs = new DegreeProgram("MSCS", "src/application/ics_mscs_courses.csv");
        DegreeProgram mit = new DegreeProgram("MIT", "src/application/ics_mit_courses.csv");
        DegreeProgram phd = new DegreeProgram("PHD", "src/application/ics_phd_courses.csv");
        
        bscs.loadCourses();
        mscs.loadCourses();
        mit.loadCourses();
        phd.loadCourses();
        
        degreePrograms.put("BSCS", bscs);
        degreePrograms.put("MSCS", mscs);
        degreePrograms.put("MIT", mit);
        degreePrograms.put("PHD", phd);
    }
    
    private static void loadCourseOfferings() {
        CSVReader.readCourseOfferings("src/application/course_offerings.csv", courseOfferings);
    }
    
    public static void main(String[] args) {
        launch();
    }
}