package GUIDisplays;
import components.Student;
import application.Main;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LandingPage {
    
    public void displayaDashboard(Pane root, Stage primaryStage, Student student) {
        root.getChildren().clear();
        root.getStyleClass().add("main-background");
        
        // Top Header Bar
        Pane header = new Pane();
        header.setPrefHeight(80);
        header.getStyleClass().add("header-bar");
        header.prefWidthProperty().bind(root.widthProperty());
        
        // Left Vertical Dots
        Label leftMenuIcon = new Label("⋮");
        leftMenuIcon.getStyleClass().add("header-icon");
        leftMenuIcon.setLayoutX(30);
        leftMenuIcon.setLayoutY(15);
        
        // Welcome Text
        String nameDisplay = (student != null) ? student.getFirstName() + " " + student.getLastName() : "User";
        Label welcomeLabel = new Label("Welcome, " + nameDisplay);
        welcomeLabel.getStyleClass().add("welcome-text");
        welcomeLabel.setLayoutX(80);
        welcomeLabel.setLayoutY(20);
        
        // Right Icon
        Label rightIcon = new Label("☰");
        rightIcon.getStyleClass().add("header-icon");
        rightIcon.setLayoutY(15);
        rightIcon.layoutXProperty().bind(root.widthProperty().subtract(70));
        
        header.getChildren().addAll(leftMenuIcon, welcomeLabel, rightIcon);
        
        // Dashboard of the program
        HBox container = new HBox(50);
        container.setAlignment(Pos.CENTER);
        
        // Create Items
        VBox plannerItem = createDashboardItem("Planner", "/images/planner_icon.png");
        VBox calendarItem = createDashboardItem("Calendar", "/images/calendar_icon.png");
        VBox announcementsItem = createDashboardItem("Announcements", "/images/megaphone_icon.png");
        VBox exitItem = createDashboardItem("Exit", "/images/door_icon.png");
        
        container.getChildren().addAll(plannerItem, calendarItem, announcementsItem, exitItem);
        
        // Logic Part
        // Link Planner Button -> EnlistmentUI
        Button plannerBtn = (Button) plannerItem.getChildren().get(1);
        plannerBtn.setOnAction(e -> {
            EnlistmentUI enlistment = new EnlistmentUI(student, Main.courseOfferings);
            Scene enlistmentScene = enlistment.EnlistScreen(primaryStage);
            try {
                String css = getClass().getResource("/application/application.css").toExternalForm();
                enlistmentScene.getStylesheets().add(css);
            } catch (Exception ex) {
                System.out.println("CSS file not found, using default styles");
            }
            primaryStage.setScene(enlistmentScene);
            primaryStage.setMaximized(true);
        });
        
        // Link Calendar Button -> CalendarView
        Button calendarBtn = (Button) calendarItem.getChildren().get(1);
        calendarBtn.setOnAction(e -> {
            CalendarView calendarView = new CalendarView(student, primaryStage);
            Scene calendarScene = calendarView.createCalendarScene();
            primaryStage.setScene(calendarScene);
            primaryStage.setMaximized(true);
        });
        
        // Announcements Button (placeholder)
        Button announcementsBtn = (Button) announcementsItem.getChildren().get(1);
        announcementsBtn.setOnAction(e -> {
            System.out.println("Announcements feature coming soon!");
        });
        
        // Link Exit Button
        Button exitBtn = (Button) exitItem.getChildren().get(1);
        exitBtn.setOnAction(e -> {
            System.exit(0);
        });
        
        root.getChildren().addAll(header, container);
        
        container.layoutXProperty().bind(root.widthProperty().subtract(container.widthProperty()).divide(2));
        container.layoutYProperty().bind(root.heightProperty().subtract(container.heightProperty()).divide(2));
    }
    
    // Helper to create the Icon Box + Button structure
    private VBox createDashboardItem(String buttonText, String iconPath) {
        VBox container = new VBox(20); // 20px spacing between Icon Box and Button
        container.setAlignment(Pos.CENTER);
        
        Pane iconBox = new Pane();
        iconBox.setPrefSize(200, 200);
        iconBox.getStyleClass().add("icon-box");
        
        ImageView iconView = new ImageView();
        try {
            Image img = new Image(getClass().getResourceAsStream(iconPath));
            iconView.setImage(img);
        } catch (Exception e) {
            System.err.println("Icon not found: " + iconPath);
        }
        
        iconView.setFitWidth(100);
        iconView.setFitHeight(100);
        iconView.setPreserveRatio(true);
        iconView.setLayoutX(50);
        iconView.setLayoutY(50);
        iconBox.getChildren().add(iconView);
        
        Button actionBtn = new Button(buttonText);
        actionBtn.setPrefSize(200, 50);
        actionBtn.getStyleClass().add("dashboard-btn");
        
        container.getChildren().addAll(iconBox, actionBtn);
        return container;
    }
}