package GUIDisplays;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class About {
    private ImageView aboutBackgroundImage;
    
    public About() {
        
    }
    
    public void displayAbout(Pane root, Stage primaryStage) {
        try {
            // Load and set up the about background image
            Image aboutImage = new Image(getClass().getResourceAsStream("/images/AboutPage.png"));
            aboutBackgroundImage = new ImageView(aboutImage);
            
            // Make background image fill the entire screen
            aboutBackgroundImage.setFitWidth(primaryStage.getWidth());
            aboutBackgroundImage.setFitHeight(primaryStage.getHeight());
            aboutBackgroundImage.setPreserveRatio(false); // Stretch to fill
            
            // Set initial opacity to 0 for fade in
            aboutBackgroundImage.setOpacity(0);
            
            // Add the about background image on top of main background but behind buttons
            addAtCorrectZIndex(root, aboutBackgroundImage);
            
            // Create fade in animation
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), aboutBackgroundImage);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
            
        } catch (Exception e) {
            System.err.println("About background image not found");
        }
    }
    
    public void revertToMainBackground(Pane root, Stage primaryStage, Runnable onFinished) {
        if (aboutBackgroundImage != null && root.getChildren().contains(aboutBackgroundImage)) {
            // Create fade out animation
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), aboutBackgroundImage);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                // Remove the about background after fade out completes
                root.getChildren().remove(aboutBackgroundImage);
                if (onFinished != null) {
                    onFinished.run();
                }
            });
            fadeOut.play();
        } else {
            if (onFinished != null) {
                onFinished.run();
            }
        }
    }
    
    // Helper method to add elements at the correct z-index
    private void addAtCorrectZIndex(Pane root, javafx.scene.Node node) {
        // Find the main background image to add our elements right after it
        for (int i = 0; i < root.getChildren().size(); i++) {
            javafx.scene.Node child = root.getChildren().get(i);
            if (child instanceof ImageView && child != aboutBackgroundImage) {
                // Add right after the main background
                root.getChildren().add(i + 1, node);
                return;
            }
        }
        // Fallback: add at position 1 (after background if it's at 0)
        if (root.getChildren().size() > 0) {
            root.getChildren().add(1, node);
        } else {
            root.getChildren().add(node);
        }
    }
    
    public void revertToMainBackground(Pane root, Stage primaryStage) {
        revertToMainBackground(root, primaryStage, null);
    }
}
