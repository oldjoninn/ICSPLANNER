package GUIDisplays;
import components.DegreeProgram;
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
import application.Main;

public class Register {
   private Main mainApp;
   private String registerEmail;
   private String registerPassword;
   private String registerFirstName;
   private String registerLastName;
  
   public Register(Main mainApp) {
       this.mainApp = mainApp;
   }
  
   public void displayRegisterPopUp(Pane positionedContainer, Pane root, Stage primaryStage) {
       positionedContainer.getChildren().clear();
       
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
      
       // Close Button - matching Login position
       Button closeButton = new Button("X");
       closeButton.getStyleClass().add("close-button");
       closeButton.setLayoutX(1165);
       closeButton.setLayoutY(345);
       closeButton.setOnAction(e -> {
           root.getChildren().remove(positionedContainer);
           if (mainApp != null) mainApp.restoreMainDisplay(root);
       });
      
       Label registerText = new Label("Register today.");
       registerText.getStyleClass().add("title-text");
       registerText.setLayoutX(450); 
       registerText.setLayoutY(370);
    
       // Page 1 Elements
       Label email = new Label("email");
       email.getStyleClass().add("normal-login-text");
       email.setLayoutX(450); 
       email.setLayoutY(430);
    
       Label password = new Label("password");
       password.getStyleClass().add("normal-login-text");
       password.setLayoutX(450); 
       password.setLayoutY(515);
    
       TextField txtEmail = new TextField();
       PasswordField txtPassword = new PasswordField();
       txtEmail.setLayoutX(450); 
       txtEmail.setLayoutY(445);
       txtPassword.setLayoutX(450); 
       txtPassword.setLayoutY(530);
     
       txtEmail.getStyleClass().add("transparent-field");
       txtPassword.getStyleClass().add("transparent-field");
    
       Button btnNext = new Button("NEXT");
       btnNext.setLayoutX(550); 
       btnNext.setLayoutY(646);
       btnNext.getStyleClass().add("diagonal-gradient-btn");
    
       Label ahaaText = new Label("Already have an account?");
       ahaaText.getStyleClass().add("normal-login-text");
       ahaaText.setLayoutX(500); 
       ahaaText.setLayoutY(724);
    
       Button btnLogin = new Button("Login");
       btnLogin.setLayoutX(642); 
       btnLogin.setLayoutY(721);
       btnLogin.getStyleClass().add("hyperlink-btn");
     
       // Page 2 Elements
       Label firstName = new Label("first name");
       firstName.getStyleClass().add("normal-login-text");
       firstName.setLayoutX(450); 
       firstName.setLayoutY(430);
    
       Label lastName = new Label("last name");
       lastName.getStyleClass().add("normal-login-text");
       lastName.setLayoutX(568); 
       lastName.setLayoutY(430);
       
       Label studentNumber = new Label("student no.");
       studentNumber.getStyleClass().add("normal-login-text");
       studentNumber.setLayoutX(684); 
       studentNumber.setLayoutY(430);
    
       TextField txtFirstName = new TextField();
       TextField txtLastName = new TextField();
       TextField txtStudentNumber = new TextField();
       
       txtFirstName.setLayoutX(450); 
       txtFirstName.setLayoutY(445);
       txtFirstName.getStyleClass().add("transparent-field");
       txtFirstName.setStyle("-fx-pref-width: 110px;");
       
       txtLastName.setLayoutX(568); 
       txtLastName.setLayoutY(445);
       txtLastName.getStyleClass().add("transparent-field");
       txtLastName.setStyle("-fx-pref-width: 108px;");
       
       txtStudentNumber.setLayoutX(684); 
       txtStudentNumber.setLayoutY(445);
       txtStudentNumber.getStyleClass().add("transparent-field");
       txtStudentNumber.setStyle("-fx-pref-width: 80px;");
      
       Label degreeText = new Label("what is your degree?");
       degreeText.getStyleClass().add("normal-login-text");
       degreeText.setLayoutX(450); 
       degreeText.setLayoutY(515);
     
       ComboBox<String> degreeDropdown = new ComboBox<>();
       degreeDropdown.getItems().addAll(
           "Bachelor of Science in Computer Science", 
           "Master of Science in Computer Science", 
           "Master of Science in Information Technology", 
           "Doctor of Philosophy in Computer Science"
       );
       degreeDropdown.setLayoutX(450); 
       degreeDropdown.setLayoutY(530);
       degreeDropdown.getStyleClass().add("combo-box");
       degreeDropdown.setStyle("-fx-pref-width: 314px;");
    
       Button btnFinish = new Button("FINISH");
       btnFinish.setLayoutX(550); 
       btnFinish.setLayoutY(646);
       btnFinish.getStyleClass().add("diagonal-gradient-btn");
      
       // Add initial page elements
       positionedContainer.getChildren().addAll(
           topImage, closeButton, registerText, email, password, txtEmail, txtPassword, 
           ahaaText, btnLogin, btnNext
       );
       
       // Fade in animation
       positionedContainer.setOpacity(0);
       FadeTransition fadeIn = new FadeTransition(Duration.millis(500), positionedContainer);
       fadeIn.setFromValue(0); 
       fadeIn.setToValue(1); 
       fadeIn.play();
     
       // Login button action
       btnLogin.setOnAction(e -> {
           positionedContainer.getChildren().clear();
           Login login = new Login(mainApp);
           login.displayLogInPopUp(root, primaryStage);
       });
     
       // Next button action
       btnNext.setOnAction(e -> {
           registerEmail = txtEmail.getText();
           registerPassword = txtPassword.getText();
          
           // Duplicate Check
           if (Save_Load.loadStudent(registerEmail) != null) {
               Alert alert = new Alert(Alert.AlertType.ERROR, "User already exists!");
               alert.showAndWait();
               return;
           }
         
           positionedContainer.getChildren().removeAll(
               email, password, txtEmail, txtPassword, btnNext, ahaaText, btnLogin
           );
           positionedContainer.getChildren().addAll(
               firstName, lastName, studentNumber, txtFirstName, txtLastName, 
               txtStudentNumber, degreeText, degreeDropdown, btnFinish
           );
       });
    
       btnNext.disableProperty().bind(
           txtEmail.textProperty().isEmpty()
               .or(txtPassword.textProperty().isEmpty())
       );
     
       btnFinish.disableProperty().bind(
           txtFirstName.textProperty().isEmpty()
               .or(txtLastName.textProperty().isEmpty())
               .or(degreeDropdown.valueProperty().isNull())
       );
     
       btnFinish.setOnAction(e -> {
           registerFirstName = txtFirstName.getText();
           registerLastName = txtLastName.getText();
           String stdNum = txtStudentNumber.getText();
           String degreeName = degreeDropdown.getValue();
          
           String degreeKey = "BSCS";
           if (degreeName.equals("Master of Science in Computer Science")) degreeKey = "MSCS";
           else if (degreeName.equals("Master of Science in Information Technology")) degreeKey = "MIT";
           else if (degreeName.equals("Doctor of Philosophy in Computer Science")) degreeKey = "PHD";
           DegreeProgram degreeObj = Main.degreePrograms.get(degreeKey);
          
           // Hash password before saving
           String hashed = PasswordUtils.hashPassword(registerPassword, registerEmail);
           Student newUser = new Student(
               registerEmail, hashed, registerFirstName, registerLastName, 
               registerEmail, stdNum, degreeObj
           );
           Save_Load.saveStudent(newUser);
          
           Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account Created! Please Login.");
           alert.showAndWait();
          
           positionedContainer.getChildren().clear();
           Login login = new Login(mainApp);
           login.displayLogInPopUp(root, primaryStage);
       });
      
       // Position image - matching Login
       topImage.setLayoutX(((root.getWidth() - topImage.getFitWidth()) / 2) + 5);
       topImage.setLayoutY(310);
   }
}
