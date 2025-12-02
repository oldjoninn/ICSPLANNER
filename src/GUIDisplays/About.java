package GUIDisplays;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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

        // Image will be 60% of root width
        double widthFactor = 0.6;

        ImageView messageContainer = new ImageView();
        try {
            Image block = new Image(getClass().getResourceAsStream("/images/MessageContainer.png"));
            messageContainer.setImage(block);
            messageContainer.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Logo image not found: " + e.getMessage());
        }

        // Bind width to 60% of root width and center horizontally
        messageContainer.fitWidthProperty().bind(root.widthProperty().multiply(widthFactor));
        messageContainer.layoutXProperty().bind(
                root.widthProperty().subtract(messageContainer.fitWidthProperty()).divide(2)
        );
        messageContainer.setLayoutY(525);
        messageContainer.getStyleClass().add("image-with-shadow");

        ImageView messageContainer1 = new ImageView();
        try {
            Image block1 = new Image(getClass().getResourceAsStream("/images/MessageContainer.png"));
            messageContainer1.setImage(block1);
            messageContainer1.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Logo image not found: " + e.getMessage());
        }

        // Same 60% width and centering
        messageContainer1.fitWidthProperty().bind(root.widthProperty().multiply(widthFactor));
        messageContainer1.layoutXProperty().bind(
                root.widthProperty().subtract(messageContainer1.fitWidthProperty()).divide(2)
        );
        messageContainer1.setLayoutY(525);
        messageContainer1.getStyleClass().add("image-with-shadow");

        Label whatIsR = new Label("What is REGICS?");
        whatIsR.getStyleClass().add("title-text");
        whatIsR.setStyle("   -fx-font-size: 50px;");
        whatIsR.setLayoutX(750);
        whatIsR.setLayoutY(350);

        Label subtitle = new Label(
                "REGICS (Registration and Enrollment Gateway of ICS) is a comprehensive academic management system designed specifically for the Institute of Computer Science (ICS). It serves as a centralized digital platform that streamlines and automates the student registration and enrollment processes, transforming traditional paper-based systems into an efficient, user-friendly digital experience."
        );
        subtitle.getStyleClass().add("subtitle-text");
        subtitle.setStyle("   -fx-font-size: 20px; -fx-text-fill: #c4d0e6;");
        subtitle.setWrapText(true);
        subtitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        subtitle.setMaxWidth(1000);
        subtitle.setAlignment(Pos.CENTER);
        subtitle.setLayoutX(460);
        subtitle.setLayoutY(407);

        Label Mission = new Label("Mission");
        Mission.getStyleClass().add("title-text");
        Mission.setStyle("   -fx-font-size: 22px;");
        Mission.setLayoutX(450);
        Mission.setLayoutY(685);

        Label MissionText = new Label(
                "To create a functional course planner that helps ICS students efficiently plan their semester schedules while avoiding time conflicts and meeting degree requirements."
        );
        MissionText.getStyleClass().add("subtitle-text");
        MissionText.setStyle("   -fx-font-size: 20px; -fx-text-fill: #c4d0e6;");
        MissionText.setWrapText(true);
        MissionText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        MissionText.setMaxWidth(600);
        MissionText.setAlignment(Pos.CENTER);
        MissionText.setLayoutX(185);
        MissionText.setLayoutY(700);

        Label Vision = new Label("Vision");
        Vision.getStyleClass().add("title-text");
        Vision.setStyle("   -fx-font-size: 22px;");
        Vision.setLayoutX(1400);
        Vision.setLayoutY(685);

        Label VisionText = new Label(
                "To create an academic management system for computer science education that evolves to meet the changing needs of students, faculty, and administrators in the digital age."
        );
        VisionText.getStyleClass().add("subtitle-text");
        VisionText.setStyle("   -fx-font-size: 20px; -fx-text-fill: #c4d0e6;");
        VisionText.setWrapText(true);
        VisionText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        VisionText.setMaxWidth(580);
        VisionText.setAlignment(Pos.CENTER);
        VisionText.setLayoutX(1143);
        VisionText.setLayoutY(700);

        // Store references to all elements
        aboutElements.clear();
        aboutElements.add(messageContainer);
        aboutElements.add(messageContainer1);
        aboutElements.add(whatIsR);
        aboutElements.add(subtitle);
        aboutElements.add(Mission);
        aboutElements.add(Vision);
        aboutElements.add(MissionText);
        aboutElements.add(VisionText);

        // Add directly to root
        root.getChildren().addAll(
                messageContainer, messageContainer1, whatIsR,
                subtitle, Mission, Vision, MissionText, VisionText
        );
    }

    // New method to remove everything added by displayAbout
    public void removeAbout(Pane root) {
        if (aboutElements != null && !aboutElements.isEmpty()) {
            root.getChildren().removeAll(aboutElements);
            aboutElements.clear();
        }
    }
}
