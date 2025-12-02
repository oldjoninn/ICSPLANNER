package GUIDisplays;
import components.Admin;
import components.Student;
import components.User;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
public class Dashboard {
   private final double width;
   private User currentUser;
   private Main mainApp;
   private Stage primaryStage;
   public Dashboard(double width, Main mainApp, Stage primaryStage, User user) {
       this.width = width;
       this.currentUser = user;
       this.mainApp = mainApp;
       this.primaryStage = primaryStage;
   }
   public Pane getView() {
       Pane root = new Pane();
       root.setPrefWidth(width);
       root.setStyle("-fx-background-color: #050816;");
     
       Rectangle solidBg = new Rectangle();
       solidBg.setWidth(width);
       solidBg.heightProperty().bind(root.heightProperty());
       solidBg.setFill(Color.web("#050816"));
     
       // Logo
       ImageView logoView = new ImageView();
       try {
           Image logoImage = new Image(getClass().getResourceAsStream("/images/REGICSLogo.png"));
           logoView.setImage(logoImage);
           logoView.setFitHeight(40);
           logoView.setPreserveRatio(true);
       } catch (Exception e) {}
       logoView.setLayoutX(125);
       logoView.setLayoutY(25);
     
       VBox profileBox = new VBox(5);
       profileBox.setLayoutX(30);
       profileBox.setLayoutY(100);
       String firstName = (currentUser != null) ? currentUser.getFirstName() : "User";
       String lastName = (currentUser != null) ? currentUser.getLastName() : "Name";
       String email = (currentUser != null) ? currentUser.getEmail() : "email@gmail.com";
       Label nameLabel = new Label( firstName  + " "+ lastName );
       nameLabel.getStyleClass().add("user-name");
       Label emailLabel = new Label(email);
       emailLabel.getStyleClass().add("user-email");
       profileBox.getChildren().addAll(nameLabel, emailLabel);
 
       Line separator = new Line(0, 0, width - 60, 0);
       separator.getStyleClass().add("separator-line");
       separator.setLayoutX(30);
       separator.setLayoutY(170);
      
       VBox menuContainer = new VBox(15);
       menuContainer.setLayoutX(20);
       menuContainer.setLayoutY(200);
   
       Button btnDashboard = createMenuItem("🏠", "Dashboard");
       btnDashboard.setOnAction(e -> {
           if (mainApp != null) {
             
               Pane mainRoot = new Pane(); 
               LandingPage landing = new LandingPage(mainApp, currentUser);
               landing.displayLandingPage(mainRoot, primaryStage);
              
               // Size to screen visual bounds so it fills the display and maximize
               javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
               Scene scene = new Scene(mainRoot, screenBounds.getWidth(), screenBounds.getHeight());
               try {
                   scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
               } catch (Exception ex) {}
               primaryStage.setScene(scene);
               primaryStage.setMaximized(true);
           }
       });
       menuContainer.getChildren().add(btnDashboard);
      
       if (currentUser instanceof Student || (currentUser != null && "Student".equalsIgnoreCase(currentUser.getUserType()))) {
          
        
           Button btnPlanner = createMenuItem("✓", "Planner");  
           btnPlanner.setOnAction(e -> {
               if (currentUser instanceof Student && primaryStage != null) {
                   EnlistmentUI enlist = new EnlistmentUI(mainApp, (Student) currentUser, Main.courseOfferings, primaryStage);
                   Scene s = enlist.EnlistScreen(primaryStage);
                   primaryStage.setScene(s);
                   primaryStage.setMaximized(true);
               }
           });
    
           Button btnCalendar = createMenuItem("📅", "Calendar");
           btnCalendar.setOnAction(e -> {
               if (currentUser instanceof Student && primaryStage != null) {
                   CalendarView cal = new CalendarView(mainApp, (Student) currentUser, primaryStage);
                   Scene s = cal.createCalendarScene();
                   primaryStage.setScene(s);
                   primaryStage.setMaximized(true);
               }
           });
          
         
           Button btnCatalog = createMenuItem("🎓", "Course List");
           btnCatalog.setOnAction(e -> {
               if (currentUser instanceof Student && primaryStage != null) {
                   // Uses the updated constructor (Main, Student)
                   CourseCatalog catalog = new CourseCatalog(mainApp, (Student) currentUser);
                   Scene s = catalog.createCatalogScene(primaryStage);
                   primaryStage.setScene(s);
                   primaryStage.setMaximized(true);
               }
           });
           menuContainer.getChildren().addAll(btnPlanner, btnCalendar, btnCatalog);
         
       } else if (currentUser instanceof Admin || (currentUser != null && "Admin".equalsIgnoreCase(currentUser.getUserType()))) {
           Button btnCatalogue = createMenuItem("📂", "Academic View Catalogue");
           btnCatalogue.setOnAction(e -> {
              System.out.println("[Dashboard] Academic View Catalogue clicked by user: " + (currentUser != null ? currentUser.getEmail() : "null"));
               // Ensure Main knows admin is logged in so landing page and navigation work correctly
               if (mainApp != null) {
                   mainApp.setLogin(true, currentUser);
               }
               if (primaryStage != null) {
                  System.out.println("[Dashboard] Opening AdminDashboard...");
                   AdminDashboard adminDash = new AdminDashboard(primaryStage, Main.courseOfferings, mainApp);
                   adminDash.show();
               }
           });
           menuContainer.getChildren().add(btnCatalogue);
       }
       root.getChildren().addAll(solidBg, logoView, profileBox, separator, menuContainer);
       return root;
   }
   private Button createMenuItem(String icon, String text) {
       Button btn = new Button();
       btn.getStyleClass().add("menu-item");
       btn.setPrefWidth(width - 40);
     
       Label iconLbl = new Label(icon);
       iconLbl.getStyleClass().add("menu-icon");
     
       Label textLbl = new Label(text);
       textLbl.getStyleClass().add("menu-text");
       HBox content = new HBox(15);
       content.setAlignment(Pos.CENTER_LEFT);
       content.getChildren().addAll(iconLbl, textLbl);
       btn.setGraphic(content);
       return btn;
   }
}