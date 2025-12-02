package GUIDisplays;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class Credits {
  
   public Credits() {
   }
  
   public void displayCredits(Pane root, Stage primaryStage) {
       // Clear any previous credits elements first
       removeCredits(root);
      
       ImageView creditsContainer1 = new ImageView();
       try {
           Image block = new Image(getClass().getResourceAsStream("/images/MessageContainer.png"));
           creditsContainer1.setImage(block);
           creditsContainer1.setPreserveRatio(true);
           creditsContainer1.setFitWidth(730);
       } catch (Exception e) {
           System.err.println("Credits image 1 not found");
       }
       creditsContainer1.setLayoutX(125);
       creditsContainer1.setLayoutY(570);
       creditsContainer1.getStyleClass().add("image-with-shadow");
       creditsContainer1.setId("credits-container1"); // SET ID HERE TOO!
      
       Label creditsTitle = new Label("Credits");
       creditsTitle.getStyleClass().add("title-text");
       creditsTitle.setLayoutX(850);
       creditsTitle.setLayoutY(370);
       creditsTitle.setId("credits-title"); // Set ID
      
       root.getChildren().addAll(creditsContainer1, creditsTitle);
   }
  
   public void removeCredits(Pane root) {
       // Remove by IDs
       root.getChildren().removeIf(node ->
           node.getId() != null && node.getId().startsWith("credits-")
       );
   }
}



