package GUIDisplays;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
public class Credits {
   public void displayCredits(Pane root, Stage primaryStage) {
       root.getChildren().clear();
      
       root.getStyleClass().add("credits-background");
       if (root.getScene() != null) {
            String css = getClass().getResource("credits.css").toExternalForm();
            if (!root.getScene().getStylesheets().contains(css)) {
                root.getScene().getStylesheets().add(css);
            }
       }
       Label title = new Label("Credits:");
       title.getStyleClass().add("credits-title");
       title.setLayoutX(50);
       title.setLayoutY(40);
       double startX = 80;
       double gap = 220;
       double yPos = 180;
       Pane card1 = createProfileCard("LANCE", "/images/profile_placeholder.png");
       card1.setLayoutX(startX);
       card1.setLayoutY(yPos);
       Pane card2 = createProfileCard("MEMBER 2", "/images/profile_placeholder.png");
       card2.setLayoutX(startX + gap);
       card2.setLayoutY(yPos);
       Pane card3 = createProfileCard("MEMBER 3", "/images/profile_placeholder.png");
       card3.setLayoutX(startX + (gap * 2));
       card3.setLayoutY(yPos);
      
       Pane card4 = createProfileCard("MEMBER 4", "/images/profile_placeholder.png");
       card4.setLayoutX(startX + (gap * 3));
       card4.setLayoutY(yPos);
       Button backBtn = new Button("← Back");
       backBtn.getStyleClass().add("back-button");
       backBtn.setLayoutX(50);
       backBtn.setLayoutY(530);
      
       backBtn.setOnAction(e -> {
           LandingPage landing = new LandingPage();
           landing.displayLandingPage(root, primaryStage);
       });
       root.getChildren().addAll(title, card1, card2, card3, card4, backBtn);
   }
   private Pane createProfileCard(String name, String imagePath) {
       Pane card = new Pane();
       card.setPrefSize(180, 250);
       Rectangle border = new Rectangle(180, 180);
       border.getStyleClass().add("profile-border");
       border.setArcWidth(20);
       border.setArcHeight(20);
       border.setLayoutX(0);
       border.setLayoutY(0);
       StackPane imageContainer = new StackPane();
       imageContainer.setPrefSize(180, 180);
      
       ImageView profileImg = new ImageView();
       try {
           Image img = new Image(getClass().getResourceAsStream(imagePath));
           profileImg.setImage(img);
       } catch (Exception e) {
       }
       profileImg.setFitWidth(120);
       profileImg.setFitHeight(120);
       profileImg.setPreserveRatio(true);
      
       Circle clip = new Circle(60, 60, 60);
       profileImg.setClip(clip);
       Circle placeholder = new Circle(60);
       placeholder.getStyleClass().add("profile-placeholder-circle");
       imageContainer.getChildren().addAll(placeholder, profileImg);
       imageContainer.setLayoutX(0);
       imageContainer.setLayoutY(0);
      
       Label nameLabel = new Label(name);
       nameLabel.getStyleClass().add("name-pill");
       nameLabel.setPrefWidth(160);
       nameLabel.setLayoutX(10);   
       nameLabel.setLayoutY(195);  
       nameLabel.setAlignment(javafx.geometry.Pos.CENTER);
       card.getChildren().addAll(border, imageContainer, nameLabel);
       return card;
   }
}

