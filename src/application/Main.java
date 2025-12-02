package application;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import GUIDisplays.About;
import GUIDisplays.Credits;
import GUIDisplays.LandingPage;
import GUIDisplays.Login;
import components.*;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
public class Main extends Application {
    public static Map<String, DegreeProgram> degreePrograms = new HashMap<>();
   public static List<Course> courseOfferings = new ArrayList<>();
   private boolean isLogin = false;
    private User currentUser = null;
   private Stage primaryStage;
   private Label welcomeText;
   private Label abbrievText;
   private Button About;
   private Rectangle separator;
   private Button Login;
   private Pane root;
   private LandingPage landingPage;
   private About aboutPage;
   private Credits creditsPage;
   private boolean isCreditsPageDisplayed = false;
   private ImageView mainBackgroundImage;
   private boolean isAboutPageDisplayed = false;
    public void start(Stage primaryStage) {
       this.primaryStage = primaryStage;
      
       System.out.println("=== APPLICATION STARTING ===");
      
       loadDegreePrograms();
       loadCourseOfferings();
      
       System.out.println("Course offerings loaded: " + courseOfferings.size());
    
       root = new Pane();
       displayMain(root, primaryStage);
    
       Scene scene = new Scene(root);
      
       // Load CSS with better error handling
       try {
           java.net.URL cssUrl = getClass().getResource("/application/application.css");
           if (cssUrl != null) {
               System.out.println("✓ Main CSS loaded from: " + cssUrl);
               scene.getStylesheets().add(cssUrl.toExternalForm());
           } else {
               System.err.println("✗ CSS not found at /application/application.css");
           }
       } catch(Exception e) {
           System.err.println("✗ Error loading CSS: " + e.getMessage());
           e.printStackTrace();
       }
    
       primaryStage.setScene(scene);
       primaryStage.setMaximized(true);
       primaryStage.show();
    
       updatePositions(root);
   }
    private void displayMain(Pane root, Stage primaryStage) {
       mainBackgroundImage = new ImageView();
       ImageView logoImage = new ImageView();
      
       try {
           Image bgImage = new Image(getClass().getResourceAsStream("/images/LoginPage.png"));
           mainBackgroundImage.setImage(bgImage);
           mainBackgroundImage.setFitWidth(Screen.getPrimary().getBounds().getWidth());
           mainBackgroundImage.setFitHeight(Screen.getPrimary().getBounds().getHeight());
           mainBackgroundImage.setPreserveRatio(false);
           System.out.println("✓ Background image loaded");
        
           Image Logo = new Image(getClass().getResourceAsStream("/images/REGICSLogo.png"));
           logoImage.setImage(Logo);
           System.out.println("✓ Logo image loaded");
        
       } catch (Exception e) {
           System.err.println("✗ Background image not found: " + e.getMessage());
           root.setStyle("-fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);");
       }
    
       welcomeText = new Label("Welcome to REGICS.");
       welcomeText.getStyleClass().add("welcome-text");
       abbrievText = new Label("Registration and Enrollment Gateway of ICS");
       abbrievText.getStyleClass().add("abbriev-text");
    
       FadeTransition fadeIn = new FadeTransition(Duration.millis(4500), welcomeText);
       fadeIn.setFromValue(0.0); fadeIn.setToValue(1.0); fadeIn.play();
    
       FadeTransition fadeIn2 = new FadeTransition(Duration.millis(4500), abbrievText);
       fadeIn2.setFromValue(0.0); fadeIn2.setToValue(1.0); fadeIn2.play();
    
       Login = new Button("Login");
       // Position login responsively via updatePositions (avoids persistent bindings that may move it off-screen)
       Login.setLayoutY(50);
       // set a safe default X; final X is set in updatePositions
       Login.setLayoutX(0);
        Login.getStyleClass().add("main-login-btn");
    
       Button Home = new Button("Home");
       Home.setLayoutX(665);
       Home.setLayoutY(50);
       Home.getStyleClass().add("main-btns");
       Home.setStyle("-fx-text-fill: linear-gradient(to right, #c4d0e6, #a8eaff);");
    
       Button Credits = new Button("Credits");
       Credits.setLayoutX(1200);
       Credits.setLayoutY(50);
       Credits.getStyleClass().add("main-btns");
       Credits.setStyle("-fx-text-fill: linear-gradient(to right, #a8eaff, #e6d9bb);");
      
       About = new Button("About");
       About.setLayoutX(930);
       About.setLayoutY(50);
       About.getStyleClass().add("main-btns");
       About.setStyle("-fx-text-fill: linear-gradient(to bottom, #1b4284, #a8eaff, #c4d0e6);");
      
       separator = new Rectangle();
       separator.getStyleClass().add("thin-rectangle");
       separator.setHeight(1);
       separator.setWidth(1850);
       separator.setLayoutY(125);
    
       logoImage.setLayoutX(100);
       logoImage.setLayoutY(40);
       logoImage.setPreserveRatio(true);
       logoImage.setFitWidth(80);
    
       root.getChildren().addAll(mainBackgroundImage, Home, About, Credits, Login, logoImage, separator, welcomeText, abbrievText);
    
       Home.setOnAction(e -> {
           if (isAboutPageDisplayed || isCreditsPageDisplayed) { // CHECK BOTH FLAGS
               // First, revert the background image
               try {
                   Image loginBgImage = new Image(getClass().getResourceAsStream("/images/LoginPage.png"));
                   mainBackgroundImage.setImage(loginBgImage);
                   mainBackgroundImage.setFitWidth(Screen.getPrimary().getBounds().getWidth());
                   mainBackgroundImage.setFitHeight(Screen.getPrimary().getBounds().getHeight());
                   mainBackgroundImage.setPreserveRatio(false);
               } catch (Exception ex) {
                   System.err.println("Login background image not found: " + ex.getMessage());
               }
              
               // Remove About page if showing
               if (isAboutPageDisplayed && aboutPage != null) {
                   aboutPage.removeAbout(root);
                   isAboutPageDisplayed = false;
               }
              
               // Remove Credits page if showing
               if (isCreditsPageDisplayed && creditsPage != null) {
                   creditsPage.removeCredits(root);
                   isCreditsPageDisplayed = false;
               }
              
               // Restore the elements that were removed
               if (!root.getChildren().contains(Login)) root.getChildren().add(Login);
               if (!root.getChildren().contains(welcomeText)) root.getChildren().add(welcomeText);
               if (!root.getChildren().contains(abbrievText)) root.getChildren().add(abbrievText);
              
               // Update positions AFTER elements are added back
               updatePositions(root);
           } else {
               // If not coming from About or Credits page, just reload everything
               root.getChildren().clear();
               displayMain(root, primaryStage);
              
               // Call updatePositions after a short delay to ensure stage is rendered
               javafx.application.Platform.runLater(() -> {
                   updatePositions(root);
               });
           }
       });
      
       About.setOnAction(e -> {
           if (!isAboutPageDisplayed) {
               // Remove Credits page if it's showing
               if (isCreditsPageDisplayed && creditsPage != null) {
                   creditsPage.removeCredits(root);
                   isCreditsPageDisplayed = false;
               }
              
               if (isLogin) {
                   root.getChildren().removeAll(Login, welcomeText, abbrievText);
               } else {
                   root.getChildren().removeAll(welcomeText, abbrievText);
               }
              
               // Change to About background
               try {
                   Image aboutBgImage = new Image(getClass().getResourceAsStream("/images/AboutPage.png"));
                   mainBackgroundImage.setImage(aboutBgImage);
                   mainBackgroundImage.setFitWidth(Screen.getPrimary().getBounds().getWidth());
                   mainBackgroundImage.setFitHeight(Screen.getPrimary().getBounds().getHeight());
                   mainBackgroundImage.setPreserveRatio(false);
                  
                   // Create about page
                   aboutPage = new About();
                   aboutPage.displayAbout(root, primaryStage);
                  
               } catch (Exception ex) {
                   System.err.println("About background image not found: " + ex.getMessage());
               }
              
               isAboutPageDisplayed = true;
           }
       });
      
       Credits.setOnAction(e -> {
           if (!isCreditsPageDisplayed) {
               // Remove About page if it's showing
               if (isAboutPageDisplayed && aboutPage != null) {
                   aboutPage.removeAbout(root);
                   isAboutPageDisplayed = false;
               }
              
               // Change background for Credits (or keep same)
               try {
                   Image creditsBgImage = new Image(getClass().getResourceAsStream("/images/LoginPage.png"));
                   mainBackgroundImage.setImage(creditsBgImage);
                   mainBackgroundImage.setFitWidth(Screen.getPrimary().getBounds().getWidth());
                   mainBackgroundImage.setFitHeight(Screen.getPrimary().getBounds().getHeight());
                   mainBackgroundImage.setPreserveRatio(false);
               } catch (Exception ex) {
                   System.err.println("Credits background image not found");
               }
              
               // Remove UI elements
               if (isLogin) {
                   root.getChildren().removeAll(Login, welcomeText, abbrievText);
               } else {
                   root.getChildren().removeAll(welcomeText, abbrievText);
               }
              
               // Show Credits page
               creditsPage = new Credits();
               creditsPage.displayCredits(root, primaryStage);
              
               isCreditsPageDisplayed = true;
           }
       });
      
       Login.setOnAction(e -> {
           root.getChildren().removeAll(Login, welcomeText, abbrievText);
           if (isAboutPageDisplayed) {
               isAboutPageDisplayed = false;
           }
           Login login = new Login(this);
           login.displayLogInPopUp(root, primaryStage);
       });
    
       root.sceneProperty().addListener((obs, oldScene, newScene) -> {
           if (newScene != null) updatePositions(root);
       });
   }
  
   public void setLogin(boolean loginStatus, User user) {
       this.isLogin = loginStatus;
       this.currentUser = user;
    
       if (isLogin && user != null) {
           // User logging in
           root.getChildren().removeIf(node ->
               node == Login || node == welcomeText || node == abbrievText
           );
           displayLandingPage();
       } else {
           // User logged out - restore main display properly
           isLogin = false;
           currentUser = null;
          
           // Clear and rebuild
           root.getChildren().clear();
           displayMain(root, primaryStage);
          
           // Update positions after rendering
           javafx.application.Platform.runLater(() -> {
               updatePositions(root);
           });
       }
   }
  
   private void displayLandingPage() {
       // Pass the Main instance and the current user so the landing page and dashboard
       // receive the correct application context and can open Planner/Calendar.
       landingPage = new LandingPage(this, currentUser);
       landingPage.displayLandingPage(root, primaryStage);
       primaryStage.setMaximized(true);
   }
    public User getCurrentUser() {
       return currentUser;
   }
    private void updatePositions(Pane root) {
       About.setLayoutX((root.getWidth() - About.getWidth()) / 2);
       separator.setLayoutX((root.getWidth() - separator.getWidth()) / 2);
       welcomeText.setLayoutX((root.getWidth() - welcomeText.getWidth()) / 2);
       welcomeText.setLayoutY((root.getHeight() - welcomeText.getHeight()) / 2);
       abbrievText.setLayoutX((root.getWidth() - abbrievText.getWidth()) / 2);
       abbrievText.setLayoutY(550);
      // Position the Login button reliably near the right edge with a minimum margin
      if (Login != null) {
          double x = root.getWidth() - 180;
          if (x < 20) x = 20; // ensure button isn't off-screen on narrow windows
          Login.setLayoutX(x);
      }
    }
    private void revertToMainBackground() {
       // No longer needed - kept for compatibility
       if (!root.getChildren().contains(mainBackgroundImage)) {
           root.getChildren().add(0, mainBackgroundImage);
       }
    }
    public void restoreMainDisplay(Pane root) {
        // Always restore the application's primary main root so the Login button and main UI are consistent.
        // Ignore the passed-in root if it's different from the app root.
        Pane targetRoot = this.root;

        // If the stage currently shows a different root, replace the scene so the app's main root is used.
        if (primaryStage != null) {
            Scene currentScene = primaryStage.getScene();
            if (currentScene == null || currentScene.getRoot() != targetRoot) {
                javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                Scene newScene = new Scene(targetRoot, screenBounds.getWidth(), screenBounds.getHeight());
                try {
                    newScene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
                } catch (Exception ex) { /* ignore */ }
                primaryStage.setScene(newScene);
            }
        }

        // Clear and rebuild the main root contents
        targetRoot.getChildren().clear();
        displayMain(targetRoot, primaryStage);

        // Reset login state
        isLogin = false;
        currentUser = null;
        isAboutPageDisplayed = false;
        landingPage = null;

        // Force a layout pass and update positions after rendering
        javafx.application.Platform.runLater(() -> {
            // Apply CSS and layout to ensure proper width/height calculations
            targetRoot.applyCss();
            targetRoot.layout();

            // Force layout on text elements
            if (welcomeText != null) {
                welcomeText.applyCss();
                welcomeText.layout();
            }
            if (abbrievText != null) {
                abbrievText.applyCss();
                abbrievText.layout();
            }

            // Update positions with proper measurements
            updatePositions(targetRoot);
        });
    }
    private static void loadDegreePrograms() {
       System.out.println("Loading degree programs...");
      
       DegreeProgram bscs = new DegreeProgram("BSCS", "src/application/ics_cmsc_courses.csv");
       DegreeProgram mscs = new DegreeProgram("MSCS", "src/application/ics_mscs_courses.csv");
       DegreeProgram mit = new DegreeProgram("MIT", "src/application/ics_mit_courses.csv");
       DegreeProgram phd = new DegreeProgram("PHD", "src/application/ics_phd_courses.csv");
    
       bscs.loadCourses();
       mscs.loadCourses();
       mit.loadCourses();
       phd.loadCourses();
      
       System.out.println("  BSCS: " + bscs.getCoursesOffered().size() + " courses");
       System.out.println("  MSCS: " + mscs.getCoursesOffered().size() + " courses");
       System.out.println("  MIT: " + mit.getCoursesOffered().size() + " courses");
       System.out.println("  PHD: " + phd.getCoursesOffered().size() + " courses");
      
       degreePrograms.put("BSCS", bscs);
       degreePrograms.put("MSCS", mscs);
       degreePrograms.put("MIT", mit);
       degreePrograms.put("PHD", phd);
   }
  
   private static void loadCourseOfferings() {
       System.out.println("Loading course offerings...");
       CSVReader.readCourseOfferings("src/application/course_offerings.csv", courseOfferings);
       System.out.println("  Loaded " + courseOfferings.size() + " course offerings");
   }
  
   public static void main(String[] args) {
       launch();
   }
}