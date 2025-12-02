package GUIDisplays;
import components.User;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import application.Main;
import java.util.ArrayList;
import java.util.List;
public class About {
   // Store references to all elements we add
   private List<javafx.scene.Node> aboutElements;
  
   public About() {
       aboutElements = new ArrayList<>();
   }
  
   public void displayAbout(Pane root, Stage primaryStage) {
       // Clear any previous about elements
       removeAbout(root);
      
       ImageView messageContainer = new ImageView();
       try {
           Image block = new Image(getClass().getResourceAsStream("/images/MessageContainer.png"));
           messageContainer.setImage(block);
           messageContainer.setPreserveRatio(true);
           messageContainer.setFitWidth(730);
       } catch (Exception e) {
           System.err.println("Logo image not found: " + e.getMessage());
       }
       messageContainer.setLayoutX(125);
       messageContainer.setLayoutY(570);
       messageContainer.getStyleClass().add("image-with-shadow");
      
       ImageView messageContainer1 = new ImageView();
       try {
           Image block1 = new Image(getClass().getResourceAsStream("/images/MessageContainer.png"));
           messageContainer1.setImage(block1);
           messageContainer1.setPreserveRatio(true);
           messageContainer1.setFitWidth(730);
       } catch (Exception e) {
           System.err.println("Logo image not found: " + e.getMessage());
       }
       messageContainer1.setLayoutX(1075);
       messageContainer1.setLayoutY(570);
       messageContainer1.getStyleClass().add("image-with-shadow");
      
       Label whatIsR = new Label("What is REGICS?");
       whatIsR.getStyleClass().add("title-text");
       whatIsR.setLayoutX(820);
       whatIsR.setLayoutY(370);
      
       // Store references to all elements
       aboutElements.clear();
       aboutElements.add(messageContainer);
       aboutElements.add(messageContainer1);
       aboutElements.add(whatIsR);
      
       // Add directly to root
       root.getChildren().addAll(messageContainer, messageContainer1, whatIsR);
   }
  
   // New method to remove everything added by displayAbout
   public void removeAbout(Pane root) {
       if (aboutElements != null && !aboutElements.isEmpty()) {
           root.getChildren().removeAll(aboutElements);
           aboutElements.clear();
       }
   }
}

