package GUIDisplays;
import components.User;
import javafx.stage.Screen;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import application.Main;
public class LandingPage {
   private static final double SIDEBAR_WIDTH = 300;
   private Pane announcementPane;
   private Pane landingContent;
   private Pane dashboardPane;
   private Pane overlay;
   private Main mainApp;
   private User currentUser;
   private boolean isAnnouncementOpen = false;
   private boolean isSidebarOpen = false;
  
   // UI Elements
   private Button logoutBtn;
   private Button announcementsBtn;
   private VBox centerContent;
   private Pane viewContainer;
  
   // Navigation elements
   private ImageView backgroundImage;
   private ImageView logoImage;
   private Button homeBtn;
   private Button aboutBtn;
   private Button creditsBtn;
   private Rectangle separator;
   private Button menuBtn;
  
   // Page state flags
   private boolean isAboutPageDisplayed = false;
   private boolean isCreditsPageDisplayed = false;
  
   // About and Credits pages
   private About aboutPage;
   private Credits creditsPage;
  
   public LandingPage() {
       // Default constructor
   }
  
   public LandingPage(Main mainApp, User user) {
       this.mainApp = mainApp;
       this.currentUser = user;
   }
  
   public void displayLandingPage(Pane root, Stage primaryStage) {
       root.getChildren().clear();
      
       // Reset page states
       isAboutPageDisplayed = false;
       isCreditsPageDisplayed = false;
      
       // 1. Main Container
       viewContainer = new Pane();
       viewContainer.prefWidthProperty().bind(root.widthProperty());
       viewContainer.prefHeightProperty().bind(root.heightProperty());
      
       // 2. Background Image
       backgroundImage = new ImageView();
       try {
           Image bgImage = new Image(getClass().getResourceAsStream("/images/LoginPage.png"));
           backgroundImage.setImage(bgImage);
           backgroundImage.setFitWidth(Screen.getPrimary().getBounds().getWidth());
           backgroundImage.setFitHeight(Screen.getPrimary().getBounds().getHeight());
           backgroundImage.setPreserveRatio(false);
       } catch (Exception e) {
           System.err.println("Background image not found: " + e.getMessage());
       }
      
       // 3. Content Layer
       landingContent = new Pane();
       landingContent.prefWidthProperty().bind(root.widthProperty());
       landingContent.prefHeightProperty().bind(root.heightProperty());
       landingContent.setStyle("-fx-background-color: transparent;");
       landingContent.setPickOnBounds(false);
      
       // --- NAVIGATION ELEMENTS ---
       // Logo
       logoImage = new ImageView();
       try {
           Image logoImg = new Image(getClass().getResourceAsStream("/images/REGICSLogo.png"));
           logoImage.setImage(logoImg);
           logoImage.setPreserveRatio(true);
           logoImage.setFitWidth(80);
       } catch (Exception e) {
           System.err.println("Logo image not found: " + e.getMessage());
       }
       logoImage.setLayoutX(100);
       logoImage.setLayoutY(40);
      
       // Home Button
       homeBtn = new Button("Home");
       homeBtn.getStyleClass().add("main-btns");
       homeBtn.setStyle("-fx-text-fill: linear-gradient(to right, #c4d0e6, #a8eaff);");
      
       // About Button
       aboutBtn = new Button("About");
       aboutBtn.getStyleClass().add("main-btns");
       aboutBtn.setStyle("-fx-text-fill: linear-gradient(to bottom, #1b4284, #a8eaff, #c4d0e6);");
      
       // Credits Button
       creditsBtn = new Button("Credits");
       creditsBtn.getStyleClass().add("main-btns");
       creditsBtn.setStyle("-fx-text-fill: linear-gradient(to right, #a8eaff, #e6d9bb);");
      
       // Separator
       separator = new Rectangle();
       separator.getStyleClass().add("thin-rectangle");
       separator.setHeight(1);
       separator.setWidth(1850);
      
       // Hamburger Menu
       menuBtn = new Button("☰");
       menuBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #c4d0e6; -fx-font-size: 24px; -fx-cursor: hand;");
       menuBtn.setLayoutX(40);
       menuBtn.setLayoutY(40);
      
       // Logout Button
       logoutBtn = new Button("Sign Out");
       logoutBtn.getStyleClass().add("main-login-btn");
       logoutBtn.setStyle("-fx-pref-width: 87px;");
       // --- CENTER CONTENT ---
       centerContent = new VBox(15);
       centerContent.setAlignment(Pos.CENTER);
       centerContent.setPickOnBounds(false);
      
       String displayFirstName = (currentUser != null) ? currentUser.getFirstName() : "Student";
       Label titleLine1 = new Label("Hello, " + displayFirstName);
       titleLine1.getStyleClass().add("title-textt");
       titleLine1.setAlignment(Pos.CENTER);
      
       Text textPart1 = new Text("Let's ");
       textPart1.getStyleClass().add("title-text-flow");
      
       Text textHighlight = new Text("plan");
       textHighlight.getStyleClass().add("highlightt-textt");
      
       Text textPart2 = new Text(" together");
       textPart2.getStyleClass().add("title-text-flow");
      
       TextFlow titleLine2 = new TextFlow(textPart1, textHighlight, textPart2);
       titleLine2.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
       titleLine2.setMaxWidth(1000);
       titleLine2.prefWidthProperty().bind(viewContainer.widthProperty());
      
       Label subtitle = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do\neiusmod tempor incididunt ut labore et dolore magna aliqua.");
       subtitle.getStyleClass().add("subtitle-text");
       subtitle.setWrapText(true);
       subtitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
       subtitle.setMaxWidth(600);
       subtitle.setAlignment(Pos.CENTER);
      
       centerContent.getChildren().addAll(titleLine1, titleLine2, subtitle);
      
       // Announcements Button
       announcementsBtn = new Button();
       announcementsBtn.getStyleClass().add("image-button");
       try {
           Image announceIcon = new Image(getClass().getResourceAsStream("/images/announcement.png"));
           ImageView announceView = new ImageView(announceIcon);
           announceView.setFitHeight(40);
           announceView.setPreserveRatio(true);
           announcementsBtn.setGraphic(announceView);
           announcementsBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
       } catch (Exception e) {
           announcementsBtn.setText("Announcements");
           announcementsBtn.getStyleClass().add("announcements-button");
           announcementsBtn.setPrefWidth(220);
       }
      
       // --- ADD ELEMENTS TO LANDING CONTENT ---
       landingContent.getChildren().addAll(
           logoImage, homeBtn, aboutBtn, creditsBtn, separator,
           centerContent, logoutBtn, announcementsBtn, menuBtn
       );
      
       // --- SETUP OVERLAYS & SIDEBAR ---
       overlay = new Pane();
       overlay.prefWidthProperty().bind(root.widthProperty());
       overlay.prefHeightProperty().bind(root.heightProperty());
       overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
       overlay.setVisible(false);
       overlay.setOnMouseClicked(e -> closeSidebar());
      
       Dashboard dashboard = new Dashboard(SIDEBAR_WIDTH, this.mainApp, primaryStage, this.currentUser);
       dashboardPane = dashboard.getView();
       dashboardPane.prefHeightProperty().bind(root.heightProperty());
       dashboardPane.setTranslateX(-SIDEBAR_WIDTH);
      
       Announcement announcementPage = new Announcement(root.getWidth(), root.getHeight());
       announcementPane = announcementPage.getView();
       announcementPane.prefWidthProperty().bind(root.widthProperty());
       announcementPane.prefHeightProperty().bind(root.heightProperty());
       announcementPane.setLayoutY(root.getHeight());
      
       // --- ADD LAYERS ---
       viewContainer.getChildren().addAll(
           backgroundImage, landingContent, overlay, dashboardPane, announcementPane
       );
      
       // --- EVENTS ---
       menuBtn.setOnAction(e -> {
           if (isSidebarOpen) closeSidebar(); else openSidebar();
       });
      
       // NAVIGATION
       aboutBtn.setOnAction(e -> {
           if (!isAboutPageDisplayed) {
               // Remove Credits page if it's showing
               if (isCreditsPageDisplayed && creditsPage != null) {
                   creditsPage.removeCredits(landingContent);
                   isCreditsPageDisplayed = false;
               }
              
               // Remove landing page content
               landingContent.getChildren().remove(centerContent);
               landingContent.getChildren().remove(announcementsBtn);
              
               // Change to About background
               try {
                   Image aboutBgImage = new Image(getClass().getResourceAsStream("/images/AboutPage.png"));
                   backgroundImage.setImage(aboutBgImage);
               } catch (Exception ex) {
                   System.err.println("About background image not found: " + ex.getMessage());
               }
              
               // Create and display About page content
               aboutPage = new About();
               aboutPage.displayAbout(landingContent, primaryStage);
              
               isAboutPageDisplayed = true;
           }
       });
      
       creditsBtn.setOnAction(e -> {
           if (!isCreditsPageDisplayed) {
               // Remove About page if it's showing
               if (isAboutPageDisplayed && aboutPage != null) {
                   aboutPage.removeAbout(landingContent);
                   isAboutPageDisplayed = false;
               }
              
               // Remove landing page content
               landingContent.getChildren().remove(centerContent);
               landingContent.getChildren().remove(announcementsBtn);
              
               // Change background for Credits
               try {
                   Image creditsBgImage = new Image(getClass().getResourceAsStream("/images/LoginPage.png"));
                   backgroundImage.setImage(creditsBgImage);
               } catch (Exception ex) {
                   System.err.println("Credits background image not found");
               }
              
               // Show Credits page
               creditsPage = new Credits();
               creditsPage.displayCredits(landingContent, primaryStage);
              
               isCreditsPageDisplayed = true;
           }
       });
      
       homeBtn.setOnAction(e -> {
           if (isAboutPageDisplayed || isCreditsPageDisplayed) {
               // Revert to LoginPage.png
               try {
                   Image loginBgImage = new Image(getClass().getResourceAsStream("/images/LoginPage.png"));
                   backgroundImage.setImage(loginBgImage);
               } catch (Exception ex) {
                   System.err.println("Login background image not found");
               }
              
               // Remove About page if showing
               if (isAboutPageDisplayed && aboutPage != null) {
                   aboutPage.removeAbout(landingContent);
                   isAboutPageDisplayed = false;
               }
              
               // Remove Credits page if showing
               if (isCreditsPageDisplayed && creditsPage != null) {
                   creditsPage.removeCredits(landingContent);
                   isCreditsPageDisplayed = false;
               }
              
               // Add back landing page content
               if (!landingContent.getChildren().contains(centerContent)) {
                   landingContent.getChildren().add(centerContent);
               }
               if (!landingContent.getChildren().contains(announcementsBtn)) {
                   landingContent.getChildren().add(announcementsBtn);
               }
              
               // Update positions
               updatePositions(root, root.getWidth(), root.getHeight());
           }
       });
      
       logoutBtn.setOnAction(e -> {
           if (mainApp != null) {
               mainApp.setLogin(false, null);
               mainApp.restoreMainDisplay(root);
           }
       });
      
       announcementsBtn.setOnAction(e -> {
           if (!isAnnouncementOpen) {
               double h = root.getHeight();
               TranslateTransition moveLandingUp = new TranslateTransition(Duration.seconds(0.6), landingContent);
               moveLandingUp.setFromY(0);
               moveLandingUp.setToY(-h);
               moveLandingUp.setInterpolator(Interpolator.EASE_BOTH);
              
               TranslateTransition moveAnnouncementUp = new TranslateTransition(Duration.seconds(0.6), announcementPane);
               moveAnnouncementUp.setFromY(0);
               moveAnnouncementUp.setToY(-h);
               moveAnnouncementUp.setInterpolator(Interpolator.EASE_BOTH);
              
               announcementPane.setLayoutY(h);
              
               ParallelTransition scrollUpAnimation = new ParallelTransition(moveLandingUp, moveAnnouncementUp);
               scrollUpAnimation.play();
               isAnnouncementOpen = true;
           }
       });
      
       announcementPage.setOnClose(() -> {
           double h = root.getHeight();
           TranslateTransition moveLandingDown = new TranslateTransition(Duration.seconds(0.6), landingContent);
           moveLandingDown.setFromY(-h);
           moveLandingDown.setToY(0);
           moveLandingDown.setInterpolator(Interpolator.EASE_BOTH);
          
           TranslateTransition moveAnnouncementDown = new TranslateTransition(Duration.seconds(0.6), announcementPane);
           moveAnnouncementDown.setFromY(-h);
           moveAnnouncementDown.setToY(0);
           moveAnnouncementDown.setInterpolator(Interpolator.EASE_BOTH);
          
           ParallelTransition scrollDownAnimation = new ParallelTransition(moveLandingDown, moveAnnouncementDown);
           scrollDownAnimation.play();
           isAnnouncementOpen = false;
       });
      
       // Add to root
       root.getChildren().add(viewContainer);
      
       // Initial Layout - Wait for scene to be rendered
       javafx.application.Platform.runLater(() -> {
           updatePositions(root, root.getWidth(), root.getHeight());
       });
      
       // Responsive Listeners
       root.widthProperty().addListener((obs, oldVal, newVal) -> {
           if (newVal.doubleValue() > 0) {
               updatePositions(root, newVal.doubleValue(), root.getHeight());
           }
       });
       root.heightProperty().addListener((obs, oldVal, newVal) -> {
           if (newVal.doubleValue() > 0) {
               updatePositions(root, root.getWidth(), newVal.doubleValue());
           }
       });
   }
  
   private void updatePositions(Pane root, double width, double height) {
       if (width <= 0 || height <= 0) return;
      
       double centerX = width / 2;
      
       // Position buttons responsively - FIXED POSITIONING
       if (homeBtn != null) {
           homeBtn.setLayoutX(665); // Adjusted from 300
           homeBtn.setLayoutY(50);
       }
      
       if (aboutBtn != null) {
           aboutBtn.setLayoutX(centerX - (aboutBtn.getWidth() / 2));
           aboutBtn.setLayoutY(50);
       }
      
       if (creditsBtn != null) {
           creditsBtn.setLayoutX(1200); // Adjusted from 200
           creditsBtn.setLayoutY(50);
       }
      
       if (logoutBtn != null) {
           logoutBtn.setLayoutX(1700);
           logoutBtn.setLayoutY(50);
       }
      
       if (separator != null) {
           separator.setLayoutX((width - separator.getWidth()) / 2);
           separator.setLayoutY(125);
       }
      
       // FIXED: Center content positioning
       if (centerContent != null) {
           // Calculate position based on estimated width
           double estimatedWidth = 700; // Estimate based on your content
          
           centerContent.setLayoutX((root.getWidth() - centerContent.getWidth()) / 2);
           centerContent.setLayoutY(400);
          
       }
      
       if (announcementsBtn != null) {
           announcementsBtn.setLayoutX(centerX - (announcementsBtn.getPrefWidth() / 2));
           announcementsBtn.setLayoutY(height - 100);
       }
      
       if (announcementPane != null && !isAnnouncementOpen) {
           announcementPane.setLayoutY(height);
       }
   }
  
   private void openSidebar() {
       if (!isSidebarOpen) {
           overlay.setVisible(true);
           TranslateTransition openAnim = new TranslateTransition(Duration.seconds(0.4), dashboardPane);
           openAnim.setToX(0);
           openAnim.setInterpolator(Interpolator.EASE_OUT);
           openAnim.play();
           isSidebarOpen = true;
       }
   }
  
   private void closeSidebar() {
       if (isSidebarOpen) {
           TranslateTransition closeAnim = new TranslateTransition(Duration.seconds(0.4), dashboardPane);
           closeAnim.setToX(-SIDEBAR_WIDTH);
           closeAnim.setInterpolator(Interpolator.EASE_IN);
           closeAnim.setOnFinished(e -> overlay.setVisible(false));
           closeAnim.play();
           isSidebarOpen = false;
       }
   }
}


