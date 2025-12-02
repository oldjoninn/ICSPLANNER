package GUIDisplays;
import components.DegreeProgram;
import application.Main;
import components.Save_Load;
import components.Student;
import components.PasswordUtils;
import javafx.animation.FadeTransition;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.stage.Screen;
public class Login {
 private Main mainApp;
  public Login() {}
  public Login(Main mainApp) {
     this.mainApp = mainApp;
 }
  public void displayLogInPopUp(Pane root, Stage primaryStage) {
     Pane positionedContainer = new Pane();
 
     ImageView topImage = new ImageView();
     try {
         Image topImg = new Image(getClass().getResourceAsStream("/images/LoginPopUp.png"));
         topImage.setImage(topImg);
         topImage.setPreserveRatio(true);
         topImage.setFitWidth(875);
         topImage.getStyleClass().add("image-with-shadow");
      
      
     } catch (Exception e) {
         System.err.println("Top image not found");
     }
 
     // Close Button
     Button closeButton = new Button("X");
     closeButton.getStyleClass().add("close-button");
     closeButton.setLayoutX(1265);
     closeButton.setLayoutY(345);
     closeButton.setOnAction(e -> {
         root.getChildren().remove(positionedContainer);
         if (mainApp != null) mainApp.restoreMainDisplay(root);
     });
   
     Label loginText = new Label("Login to continue.");
     loginText.getStyleClass().add("title-text");
     loginText.setLayoutX(635);
     loginText.setLayoutY(370);
      Label email = new Label("email");
     email.getStyleClass().add("normal-login-text");
     email.setLayoutX(637);
     email.setLayoutY(430);
      Label password = new Label("password");
     password.getStyleClass().add("normal-login-text");
     password.setLayoutX(637);
     password.setLayoutY(515);
 
     TextField txtEmail = new TextField();
     PasswordField txtPassword = new PasswordField();
     txtEmail.setLayoutX(637);
     txtEmail.setLayoutY(445);
     txtPassword.setLayoutX(637);
     txtPassword.setLayoutY(530);
 
     txtEmail.getStyleClass().add("transparent-field");
     txtPassword.getStyleClass().add("transparent-field");
 
     Button btnLogin = new Button("LOGIN");
     btnLogin.setLayoutX(720);
     btnLogin.setLayoutY(646);
     btnLogin.getStyleClass().add("diagonal-gradient-btn");
  
     Label dhaaText = new Label("Dont have an acount?");
     dhaaText.getStyleClass().add("normal-login-text");
     dhaaText.setLayoutX(670);
     dhaaText.setLayoutY(724);
      Button register = new Button("Register now.");
     register.setLayoutX(790);
     register.setLayoutY(721);
     register.getStyleClass().add("hyperlink-btn");
  
     positionedContainer.getChildren().addAll(
         topImage, loginText, email, password, txtEmail, txtPassword,
         btnLogin, dhaaText, register, closeButton
     );
 
     root.getChildren().add(positionedContainer);
     positionedContainer.setOpacity(0);
     FadeTransition fadeIn = new FadeTransition(Duration.millis(500), positionedContainer);
     fadeIn.setFromValue(0); fadeIn.setToValue(1); fadeIn.play();
 
     Label incorrectText = new Label("Incorrect email or password.");
     incorrectText.getStyleClass().add("error-text");
     incorrectText.setLayoutX(637);
     incorrectText.setLayoutY(585);
     incorrectText.setVisible(false);
 
     btnLogin.disableProperty().bind(txtEmail.textProperty().isEmpty().or(txtPassword.textProperty().isEmpty()));
      register.setOnAction(e -> {
         positionedContainer.getChildren().removeAll(loginText, email, password, txtEmail, txtPassword, btnLogin, dhaaText, register, incorrectText);
         Register registerDisplay = new Register(mainApp);
         registerDisplay.displayRegisterPopUp(positionedContainer, root, primaryStage);
     });
      btnLogin.setOnAction(e -> {
         String username = txtEmail.getText();
         String passwordLogin = txtPassword.getText();
         //Admin
         if (username.equals("admin") && passwordLogin.equals("admin123")) {
             // Pass the Main application instance so AdminDashboard has app context
             AdminDashboard adminDash = new AdminDashboard(primaryStage, Main.courseOfferings, mainApp);
             adminDash.show();
           
             return;
         }
         //Student
         Student student = Save_Load.loadStudent(username);
         if (student != null && PasswordUtils.verifyPassword(passwordLogin, student.getPassword(), student.getEmail())) {
          
             // Load Degree
             DegreeProgram degree = student.getDegree();
             if (degree != null && degree.getCoursesOffered().isEmpty()) {
                 degree.loadCourses();
             }
            
             if (mainApp != null) {
                 mainApp.setLogin(true, student);
             }
          
          
             Pane mainRoot = new Pane();
             LandingPage landingPage = new LandingPage(mainApp, student);
             landingPage.displayLandingPage(mainRoot, primaryStage);
            
            
             Scene scene = new Scene(mainRoot, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
            
           
             try {
                 scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
             } catch (Exception ex) {
                 System.out.println("CSS not found");
             }
            
             primaryStage.setScene(scene);
             primaryStage.setMaximized(true);
          
         } else {
             if (!positionedContainer.getChildren().contains(incorrectText)) {
                 positionedContainer.getChildren().add(incorrectText);
             }
             incorrectText.setVisible(true);
         }
     });
      topImage.setLayoutX(((root.getWidth() - topImage.getFitWidth()) / 2)+5);
     topImage.setLayoutY(310);
 }
}