package GUIDisplays;

import components.User;
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
    
    public LandingPage() {
        // Default constructor
    }
    
    public LandingPage(Main mainApp, User user) {
        this.mainApp = mainApp;
        this.currentUser = user;
    }

    public void displayLandingPage(Pane root, Stage primaryStage) {
        // 1. Main Container (Transparent Overlay)
        viewContainer = new Pane();
        viewContainer.prefWidthProperty().bind(root.widthProperty());
        viewContainer.prefHeightProperty().bind(root.heightProperty());
        viewContainer.setStyle("-fx-background-color: transparent;"); 
        viewContainer.setPickOnBounds(false); // Let clicks pass through empty areas

        // 2. Content Layer
        landingContent = new Pane();
        landingContent.prefWidthProperty().bind(root.widthProperty());
        landingContent.prefHeightProperty().bind(root.heightProperty());
        landingContent.setStyle("-fx-background-color: transparent;"); 
        landingContent.setPickOnBounds(false); 

        // --- INITIALIZE UI ELEMENTS ---

        // A. Hamburger Menu
        Button menuBtn = new Button("☰");
        menuBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px; -fx-cursor: hand;");
        menuBtn.setLayoutX(30); 
        menuBtn.setLayoutY(35);

        // B. Logo 
        ImageView logoView = new ImageView();
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/images/REGICSLogo.png"));
            logoView.setImage(logoImage);
            logoView.setFitHeight(40);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {}
        logoView.setLayoutX(80); 
        logoView.setLayoutY(40); 

        // D. Logout Image Button (Top Right)
        logoutBtn = new Button();
        logoutBtn.getStyleClass().add("image-button"); // You can define hover effects in CSS
        try {
            // Ensure you have logout.png in your images folder
            Image icon = new Image(getClass().getResourceAsStream("/images/logout.png"));
            ImageView iconView = new ImageView(icon);
            iconView.setFitHeight(30); // Adjust size to match design
            iconView.setPreserveRatio(true);
            logoutBtn.setGraphic(iconView);
            logoutBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        } catch (Exception e) {
            // Fallback to text if image missing
            logoutBtn.setText("Sign Out");
            logoutBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #4a6fa5; -fx-border-radius: 20px; -fx-text-fill: white;");
        }
        
        logoutBtn.setLayoutY(40); // Y Position fixed
        
        if (mainApp != null) {
            logoutBtn.setOnAction(e -> {
               mainApp.setLogin(false, null);
               mainApp.restoreMainDisplay(root); 
            });
        }

        centerContent = new VBox(15); // Changed spacing to 15 for better look
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPickOnBounds(false); 

        String displayFirstName = (currentUser != null) ? currentUser.getFirstName() : "Student";
        Label titleLine1 = new Label("Hello, " + displayFirstName);
        titleLine1.getStyleClass().add("title-textt");
        // Ensure text is centered within the label
        titleLine1.setAlignment(Pos.CENTER); 

        Text textPart1 = new Text("Let's ");
        textPart1.getStyleClass().add("title-text-flow");
        
        Text textHighlight = new Text("plan");
        textHighlight.getStyleClass().add("highlightt-textt");
        
        Text textPart2 = new Text(" together");
        textPart2.getStyleClass().add("title-text-flow");

        TextFlow titleLine2 = new TextFlow(textPart1, textHighlight, textPart2);
        titleLine2.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        // titleLine2.setMaxWidth(800); // OLD
        // Use a tighter width to force wrapping if screen is small, but 800 is generally fine.
        titleLine2.setMaxWidth(1000); 
        // Force centering of TextFlow itself inside VBox
        titleLine2.prefWidthProperty().bind(viewContainer.widthProperty()); 

        Label subtitle = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do\neiusmod tempor incididunt ut labore et dolore magna aliqua.");
        subtitle.getStyleClass().add("subtitle-text");
        subtitle.setWrapText(true);
        subtitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        subtitle.setMaxWidth(600);
        subtitle.setAlignment(Pos.CENTER);

        centerContent.getChildren().addAll(titleLine1, titleLine2, subtitle);
        
        // Add listener to re-center when dimensions change (crucial for accurate centering)
        centerContent.widthProperty().addListener((obs, old, newVal) -> updatePositions(root.getWidth(), root.getHeight()));
        centerContent.heightProperty().addListener((obs, old, newVal) -> updatePositions(root.getWidth(), root.getHeight()));

        announcementsBtn = new Button();
        announcementsBtn.getStyleClass().add("image-button"); 
        try {
            // Ensure you have announcement.png (or appropriate image) in your images folder
            Image announceIcon = new Image(getClass().getResourceAsStream("/images/announcement.png"));
            ImageView announceView = new ImageView(announceIcon);
            announceView.setFitHeight(40); // Adjust size as needed, e.g., slightly larger than logout
            announceView.setPreserveRatio(true);
            announcementsBtn.setGraphic(announceView);
            announcementsBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        } catch (Exception e) {
             // Fallback to text if image missing
            announcementsBtn.setText("Announcements");
            announcementsBtn.getStyleClass().add("announcements-button");
            announcementsBtn.setPrefWidth(220); 
        }

        // --- ADD ELEMENTS TO LANDING CONTENT ---
        landingContent.getChildren().addAll(
            logoView,                  
            centerContent,           
            logoutBtn, announcementsBtn, menuBtn
        );

        // --- SETUP OVERLAYS & SIDEBAR ---

        // Overlay
        overlay = new Pane();
        overlay.prefWidthProperty().bind(root.widthProperty());
        overlay.prefHeightProperty().bind(root.heightProperty());
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
        overlay.setVisible(false);
        overlay.setOnMouseClicked(e -> closeSidebar()); 

        // Sidebar
        // Dashboard constructor is Dashboard(double width, Main mainApp, Stage primaryStage, User user)
        Dashboard dashboard = new Dashboard(SIDEBAR_WIDTH, this.mainApp, primaryStage, this.currentUser);
        dashboardPane = dashboard.getView();
        dashboardPane.prefHeightProperty().bind(root.heightProperty()); 
        dashboardPane.setTranslateX(-SIDEBAR_WIDTH); 

        // Announcement
        Announcement announcementPage = new Announcement(root.getWidth(), root.getHeight());
        announcementPane = announcementPage.getView();
        announcementPane.prefWidthProperty().bind(root.widthProperty());
        announcementPane.prefHeightProperty().bind(root.heightProperty());
        announcementPane.setLayoutY(root.getHeight()); 

        // --- ADD LAYERS ---
        viewContainer.getChildren().addAll(
            landingContent, 
            overlay, 
            dashboardPane, 
            announcementPane
        );

        // --- EVENTS ---
        menuBtn.setOnAction(e -> {
            if (isSidebarOpen) closeSidebar(); else openSidebar();
        });

        announcementsBtn.setOnAction(e -> {
            if (!isAnnouncementOpen) {
                double h = root.getHeight();
                // Slide Landing UP
                TranslateTransition moveLandingUp = new TranslateTransition(Duration.seconds(0.6), landingContent);
                moveLandingUp.setFromY(0); moveLandingUp.setToY(-h); moveLandingUp.setInterpolator(Interpolator.EASE_BOTH);
                
                // Slide Announcement UP
                TranslateTransition moveAnnouncementUp = new TranslateTransition(Duration.seconds(0.6), announcementPane);
                moveAnnouncementUp.setFromY(0); moveAnnouncementUp.setToY(-h); moveAnnouncementUp.setInterpolator(Interpolator.EASE_BOTH);
                
                announcementPane.setLayoutY(h); 
                
                ParallelTransition scrollUpAnimation = new ParallelTransition(moveLandingUp, moveAnnouncementUp);
                scrollUpAnimation.play();
                isAnnouncementOpen = true;
            }
        });
        
        announcementPage.setOnClose(() -> {
            double h = root.getHeight();
            // Slide Landing DOWN
            TranslateTransition moveLandingDown = new TranslateTransition(Duration.seconds(0.6), landingContent);
            moveLandingDown.setFromY(-h); moveLandingDown.setToY(0); moveLandingDown.setInterpolator(Interpolator.EASE_BOTH);
            
            // Slide Announcement DOWN
            TranslateTransition moveAnnouncementDown = new TranslateTransition(Duration.seconds(0.6), announcementPane);
            moveAnnouncementDown.setFromY(-h); moveAnnouncementDown.setToY(0); moveAnnouncementDown.setInterpolator(Interpolator.EASE_BOTH);
            
            ParallelTransition scrollDownAnimation = new ParallelTransition(moveLandingDown, moveAnnouncementDown);
            scrollDownAnimation.play();
            isAnnouncementOpen = false;
        });

        // Add to root
        root.getChildren().add(viewContainer);
        
        // Initial Layout
        updatePositions(root.getWidth(), root.getHeight());
        
        // Responsive Listeners
        root.widthProperty().addListener((obs, oldVal, newVal) -> updatePositions(newVal.doubleValue(), root.getHeight()));
        root.heightProperty().addListener((obs, oldVal, newVal) -> updatePositions(root.getWidth(), newVal.doubleValue()));
    }

    private void updatePositions(double width, double height) {
        double centerX = width / 2;
        
        if (logoutBtn != null) logoutBtn.setLayoutX(width - 150); // Keep top right
        
        if (centerContent != null) {
        
            double contentWidth = centerContent.getWidth();
            if (contentWidth == 0) contentWidth = 600; // Fallback estimate
            centerContent.setLayoutX(centerX - (contentWidth / 2));
   
            centerContent.setLayoutY((height / 2) - (centerContent.getHeight() / 2) - 50); // NEW: True Vertical Center (slightly raised)
            
    
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