package GUIDisplays;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
public class Announcement {
  private final double width;
  private final double height;
  private Runnable onCloseAction;
  public Announcement(double width, double height) {
      this.width = width;
      this.height = height;
  }
  public void setOnClose(Runnable action) {
      this.onCloseAction = action;
  }
  public Pane getView() {
      Pane root = new Pane();
      Rectangle2D screenBounds = Screen.getPrimary().getBounds();
      double screenWidth = screenBounds.getWidth();
      double screenHeight = screenBounds.getHeight();
     
      // Set the root pane to match the screen size initially
      root.setPrefSize(screenWidth, screenHeight);
      root.setStyle("-fx-background-color: #050816;");
     
      Rectangle solidBackground = new Rectangle(screenWidth, screenHeight);
      solidBackground.setFill(Color.web("#050816"));
     
      solidBackground.widthProperty().bind(root.widthProperty());
      solidBackground.heightProperty().bind(root.heightProperty());
      // Header
      Label mainTitle = new Label("Announcements");
      mainTitle.getStyleClass().add("announcement-title");
      mainTitle.setLayoutX(60);
      mainTitle.setLayoutY(40);
      Button closeBtn = new Button("Close");
      closeBtn.getStyleClass().add("close-button");
      closeBtn.layoutXProperty().bind(root.widthProperty().subtract(120));
      closeBtn.setLayoutY(45);
      closeBtn.setOnAction(e -> {
          if (onCloseAction != null) onCloseAction.run();
      });
      ScrollPane scrollPane = new ScrollPane();
      scrollPane.getStyleClass().add("custom-scroll-pane");
     
      scrollPane.setLayoutY(100);
      // Bind size to fill the remaining space
      scrollPane.setPrefWidth(screenWidth);
      scrollPane.setPrefHeight(screenHeight - 100);
     
      scrollPane.prefWidthProperty().bind(root.widthProperty());
      scrollPane.prefHeightProperty().bind(root.heightProperty().subtract(100));
     
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
      scrollPane.setFitToWidth(true);
      scrollPane.setPannable(true);
      FlowPane contentContainer = new FlowPane();
      contentContainer.setAlignment(Pos.TOP_CENTER);
      contentContainer.setHgap(40);
      contentContainer.setVgap(40);
      contentContainer.setPadding(new Insets(20, 0, 100, 0));
      contentContainer.getStyleClass().add("scroll-content");
      contentContainer.prefWidthProperty().bind(scrollPane.widthProperty());
    
     
      contentContainer.getChildren().add(createCard(
          "Help us Improve",
          "If you encounter any problems, errors or bugs please report it through the REGICS Support Form.",
          "https://ui-avatars.com/api/?name=Survey&background=random&size=200&color=fff"
      ));
      contentContainer.getChildren().add(createCard(
          "System Maintenance",
          "We will be performing scheduled updates on the server this weekend. Service may be intermittent.",
          "https://ui-avatars.com/api/?name=System&background=random&size=200&color=fff"
      ));
    
      contentContainer.getChildren().add(createCard(
          "New Features Added",
          "Check out the new dark mode and improved dashboard analytics available now in settings.",
          "https://ui-avatars.com/api/?name=Features&background=random&size=200&color=fff"
      ));
    
      contentContainer.getChildren().add(createCard(
          "Holiday Schedule",
          "Support team availability will be limited during the upcoming public holidays.",
          "https://ui-avatars.com/api/?name=Holiday&background=random&size=200&color=fff"
      ));
      scrollPane.setContent(contentContainer);
      root.getChildren().addAll(solidBackground, mainTitle, closeBtn, scrollPane);
      return root;
  }
  private Pane createCard(String title, String description, String imageUrl) {
      Pane card = new Pane();
      card.getStyleClass().add("card-body");
      card.setPrefSize(400, 350);
      card.setStyle("-fx-background-color: #12182B; -fx-background-radius: 20;");
      Label titleLbl = new Label(title);
      titleLbl.getStyleClass().add("card-title");
      titleLbl.setLayoutX(25);
      titleLbl.setLayoutY(25);
      Label descLbl = new Label(description);
      descLbl.getStyleClass().add("card-desc");
      descLbl.setWrapText(true);
      descLbl.setPrefWidth(350);
      descLbl.setLayoutX(25);
      descLbl.setLayoutY(60);
      Pane imageContainer = new Pane();
      imageContainer.getStyleClass().add("card-image-container");
      imageContainer.setPrefSize(350, 180);
      imageContainer.setLayoutX(25);
      imageContainer.setLayoutY(145);
    
      try {
          ImageView imgView = new ImageView(new Image(imageUrl));
          imgView.setFitWidth(350);
          imgView.setFitHeight(180);
          imgView.setPreserveRatio(false);
        
          Rectangle clip = new Rectangle(350, 180);
          clip.setArcWidth(12);
          clip.setArcHeight(12);
          imgView.setClip(clip);
        
          imageContainer.getChildren().add(imgView);
      } catch (Exception e) {
          Label placeholder = new Label("Image");
          placeholder.setStyle("-fx-text-fill: #64748b;");
          placeholder.setLayoutX(150);
          placeholder.setLayoutY(80);
          imageContainer.getChildren().add(placeholder);
      }
      card.getChildren().addAll(titleLbl, descLbl, imageContainer);
      return card;
  }
}



