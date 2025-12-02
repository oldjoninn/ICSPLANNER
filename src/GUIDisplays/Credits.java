package GUIDisplays;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Credits {

    private List<Node> creditsElements;

    public Credits() {
        creditsElements = new ArrayList<>(); // Initialize the list
    }

    public void displayCredits(Pane root, Stage primaryStage) {
        // Clear any previous credits elements first
        removeCredits(root);

        double creditsMargin = 80; // left/right margin for credits image

        ImageView creditsContainer1 = new ImageView();
        try {
            Image block = new Image(getClass().getResourceAsStream("/images/TeamProfile.png"));
            creditsContainer1.setImage(block);
            creditsContainer1.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Credits image 1 not found");
        }

        // Bind width and center horizontally
        creditsContainer1.fitWidthProperty().bind(root.widthProperty().subtract(creditsMargin * 2));
        creditsContainer1.layoutXProperty().bind(
                root.widthProperty().subtract(creditsContainer1.fitWidthProperty()).divide(2)
        );
        creditsContainer1.setLayoutY(150);
        creditsContainer1.getStyleClass().add("image-with-shadow");
        creditsContainer1.setId("credits-container1");

        // Add container to creditsElements BEFORE adding to root
        creditsElements.add(creditsContainer1);

        Label broughtBy = new Label("This program is brought to you by");
        broughtBy.getStyleClass().add("subtitle-text");
        broughtBy.setStyle("   -fx-font-size: 20px; -fx-text-fill: #c4d0e6;");
        broughtBy.setLayoutX(806);
        broughtBy.setLayoutY(280);

        Label creditsTitle = new Label("Candy Jar ni Tita Mayeth");
        creditsTitle.getStyleClass().add("title-text");
        creditsTitle.setLayoutX(760);
        creditsTitle.setLayoutY(350);
        creditsTitle.setId("credits-title");

        creditsElements.add(creditsTitle);
        creditsElements.add(broughtBy);

        // Add all elements to root
        root.getChildren().addAll(creditsContainer1, creditsTitle, broughtBy);
    }

    public void removeCredits(Pane root) {
        if (creditsElements != null && !creditsElements.isEmpty()) {
            root.getChildren().removeAll(creditsElements);
            creditsElements.clear();
        }
    }
}
