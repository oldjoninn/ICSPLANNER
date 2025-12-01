package GUIDisplays;
import components.DegreeProgram;
import components.Save_Load;
import components.Student;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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
       ImageView topImage = new ImageView();
       try {
           Image topImg = new Image(getClass().getResourceAsStream("/images/LoginPopUp.png"));
           topImage.setImage(topImg);
           topImage.setPreserveRatio(true);
           topImage.setFitWidth(875);
           topImage.getStyleClass().add("image-with-shadow");
         
           Button closeButton = new Button("X");
           closeButton.getStyleClass().add("close-button");
           closeButton.setLayoutX(topImage.getFitWidth() - 30);
           closeButton.setLayoutY(10);
           closeButton.setOnAction(e -> {
               root.getChildren().remove(positionedContainer);
               if (mainApp != null) mainApp.restoreMainDisplay(root);
           });
           positionedContainer.getChildren().add(closeButton);
         
       } catch (Exception e) { System.err.println("Top image not found"); }
      
       Label registerText = new Label("Register today.");
       registerText.getStyleClass().add("title-text");
       registerText.setLayoutX(635); registerText.setLayoutY(370);
    
       Label email = new Label("email");
       email.getStyleClass().add("normal-login-text");
       email.setLayoutX(637); email.setLayoutY(430);
    
       Label password = new Label("password");
       password.getStyleClass().add("normal-login-text");
       password.setLayoutX(637); password.setLayoutY(515);
    
       TextField txtEmail = new TextField();
       PasswordField txtPassword = new PasswordField();
       txtEmail.setLayoutX(637); txtEmail.setLayoutY(445);
       txtPassword.setLayoutX(637); txtPassword.setLayoutY(530);
     
       txtEmail.getStyleClass().add("transparent-field");
       txtPassword.getStyleClass().add("transparent-field");
    
       Button btnNext = new Button("NEXT");
       btnNext.setLayoutX(720); btnNext.setLayoutY(646);
       btnNext.getStyleClass().add("diagonal-gradient-btn");
     
       Button btnFinish = new Button("FINISH");
       btnFinish.setLayoutX(730); btnFinish.setLayoutY(646);
       btnFinish.getStyleClass().add("diagonal-gradient-btn");
    
       Label ahaaText = new Label("Already have an acount?");
       ahaaText.getStyleClass().add("normal-login-text");
       ahaaText.setLayoutX(685); ahaaText.setLayoutY(724);
    
       Button btnLogin = new Button("Login");
       btnLogin.setLayoutX(819); btnLogin.setLayoutY(721);
       btnLogin.getStyleClass().add("hyperlink-btn");
     
       Label firstName = new Label("First Name");
       firstName.getStyleClass().add("normal-login-text");
       firstName.setLayoutX(637); firstName.setLayoutY(430);
    
       Label lastName = new Label("Last Name");
       lastName.getStyleClass().add("normal-login-text");
       lastName.setLayoutX(755); lastName.setLayoutY(430);
    
       TextField txtFirstName = new TextField();
       TextField txtlastName = new TextField();
       txtFirstName.setLayoutX(637); txtFirstName.setLayoutY(445);
       txtlastName.setLayoutX(755); txtlastName.setLayoutY(445);
     
       txtFirstName.getStyleClass().add("transparent-field");
       txtFirstName.setStyle("-fx-pref-width: 110px;");
       txtlastName.getStyleClass().add("transparent-field");
       txtlastName.setStyle("-fx-pref-width: 88px;");
      
       Label degreeText = new Label("What is your Degree?");
       degreeText.getStyleClass().add("normal-login-text");
       degreeText.setLayoutX(637); degreeText.setLayoutY(530);
     
       ComboBox<String> degreeDropdown = new ComboBox<>();
       degreeDropdown.getItems().addAll("Bachelor of Science in Computer Science", "Master of Science in Computer Science", "Master of Science in Information Technology", "Doctor of Philosophy in Computer Science");
       degreeDropdown.setLayoutX(637); degreeDropdown.setLayoutY(560);
       degreeDropdown.getStyleClass().add("combo-box");
    
       Label studentNumber = new Label("Student No.");
       studentNumber.getStyleClass().add("normal-login-text");
       studentNumber.setLayoutX(852); studentNumber.setLayoutY(430);
    
       TextField txtStudentNumber = new TextField();
       txtStudentNumber.setLayoutX(852); txtStudentNumber.setLayoutY(445);
       txtStudentNumber.getStyleClass().add("transparent-field");
       txtStudentNumber.setStyle("-fx-pref-width: 80px;");
      
       positionedContainer.getChildren().addAll(
           topImage, btnLogin, registerText, email, password, txtEmail, txtPassword, ahaaText, btnNext
       );
     
      
       btnLogin.setOnAction(e -> {
           positionedContainer.getChildren().clear();
           Login login = new Login(mainApp);
           login.displayLogInPopUp(root, primaryStage);
       });
     
       btnNext.setOnAction(e -> {
           registerEmail = txtEmail.getText();
           registerPassword = txtPassword.getText();
          
           // Duplicate Check
           if (Save_Load.loadStudent(registerEmail) != null) {
               Alert alert = new Alert(Alert.AlertType.ERROR, "User already exists!");
               alert.showAndWait();
               return;
           }
         
           positionedContainer.getChildren().removeAll(email, password, txtEmail, txtPassword, btnNext);
           positionedContainer.getChildren().addAll(firstName, lastName, txtFirstName, txtlastName, degreeText, studentNumber, txtStudentNumber, degreeDropdown, btnFinish);
       });
    
       btnNext.disableProperty().bind(txtEmail.textProperty().isEmpty().or(txtPassword.textProperty().isEmpty()));
     
       btnFinish.disableProperty().bind(
           txtFirstName.textProperty().isEmpty()
               .or(txtlastName.textProperty().isEmpty())
               .or(degreeDropdown.valueProperty().isNull())
       );
     
       btnFinish.setOnAction(e -> {
           registerFirstName = txtFirstName.getText();
           registerLastName = txtlastName.getText();
           String stdNum = txtStudentNumber.getText();
           String degreeName = degreeDropdown.getValue();
          
          
           String degreeKey = "BSCS";
           if (degreeName.equals("Master of Science in Computer Science")) degreeKey = "MSCS";
           else if (degreeName.equals("Master of Science in Information Technology")) degreeKey = "MIT";
           else if (degreeName.equals("Doctor of Philosophy in Computer Science")) degreeKey = "PHD";
           
           DegreeProgram degreeObj = Main.degreePrograms.get(degreeKey);
           // Error check
           if (degreeObj == null) {
               Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading degree program. Please try again.");
               alert.showAndWait();
               return;
           }
           
           // Load courses if not already loaded
           if (degreeObj.getCoursesOffered().isEmpty()) {
               degreeObj.loadCourses();
           }
          
           // SAVE STUDENT
           Student newUser = new Student(registerEmail, registerPassword, registerFirstName, registerLastName, registerEmail, stdNum, degreeObj);
           Save_Load.saveStudent(newUser);
          
           Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account Created! Please Login.");
           alert.showAndWait();
          
           positionedContainer.getChildren().clear();
           Login login = new Login(mainApp);
           login.displayLogInPopUp(root, primaryStage);
       });
      
       topImage.setLayoutX(((root.getWidth() - topImage.getFitWidth()) / 2)+5);
       topImage.setLayoutY(310);
   }
}



