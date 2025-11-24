package GUIDisplays;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class Login {
	public Login() {
		
	}
	
	public void displayLogInPopUp (Pane root, Stage primaryStage) {
		
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
	   
       Label loginText = new Label("Login to continue.");
       loginText.getStyleClass().add("title-text");
       loginText.setLayoutX(630);
       loginText.setLayoutY(340);
      
       Label email = new Label("email");
       email.getStyleClass().add("normal-login-text");
       email.setLayoutX(632);
       email.setLayoutY(400);
      
       Label password = new Label("password");
       password.getStyleClass().add("normal-login-text");
       password.setLayoutX(632);
       password.setLayoutY(485);
	   
	    TextField txtEmail = new TextField();
	    PasswordField txtPassword = new PasswordField();
	    txtEmail.setLayoutX(632);
	    txtEmail.setLayoutY(415);
	    txtPassword.setLayoutX(632);
	    txtPassword.setLayoutY(500);
	   
	    txtEmail.getStyleClass().add("transparent-field");
	    txtPassword.getStyleClass().add("transparent-field");
	   
	    Button btnLogin = new Button("LOGIN");
	    btnLogin.setLayoutX(715);
	    btnLogin.setLayoutY(616);
	    btnLogin.getStyleClass().add("diagonal-gradient-btn");
       Label dhaaText = new Label("Dont have an acount?");
       dhaaText.getStyleClass().add("normal-login-text");
       dhaaText.setLayoutX(665);
       dhaaText.setLayoutY(694);
      
	    Button register = new Button("Register now.");
	    register.setLayoutX(785);
	    register.setLayoutY(691);
	    register.getStyleClass().add("hyperlink-btn");
	    positionedContainer.getChildren().addAll(topImage, loginText, email, password, txtEmail, txtPassword, btnLogin, dhaaText, register);
	   
	    root.getChildren().add(positionedContainer);
	   
	    // Set initial state (invisible)
	    positionedContainer.setOpacity(0);
	    // Create fade in animation
	    FadeTransition fadeIn = new FadeTransition(Duration.millis(500), positionedContainer);
	    fadeIn.setFromValue(0);
	    fadeIn.setToValue(1);
	    fadeIn.play();
	    //
	   
	    Label incorrectText = new Label("Incorrect email or password.");
	    incorrectText.getStyleClass().add("error-text");
	    incorrectText.setLayoutX(631);
	    incorrectText.setLayoutY(560);
	    incorrectText.setVisible(false);
	   
	    //
	   
       btnLogin.disableProperty().bind(
       		txtEmail.textProperty().isEmpty()
                   .or(txtPassword.textProperty().isEmpty())
       );
      
      
       btnLogin.setOnAction(e -> {
       	
           if (!txtEmail.getText().equals("hanna") || !txtPassword.getText().equals("123")) {
       	    positionedContainer.getChildren().add(incorrectText);
       	    incorrectText.setVisible(true);
           } else {
               positionedContainer.getChildren().removeAll(loginText, email, password, txtEmail, txtPassword, btnLogin, dhaaText, register);
               if(incorrectText.isVisible()) {
                   positionedContainer.getChildren().remove(incorrectText);
               }
           }
       });
      
       topImage.setLayoutX((root.getWidth() - topImage.getFitWidth()) / 2);
	    topImage.setLayoutY(280);
//	    Scene scene = new Scene(root);
//	    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//	    primaryStage.setScene(scene);
//	    primaryStage.setMaximized(true);
//
//	    primaryStage.show();
	   
	}
}

