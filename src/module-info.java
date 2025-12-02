module ICSPlanner {
    requires javafx.controls;
    requires javafx.media;
    
    opens application to javafx.graphics, javafx.fxml;
    opens components to javafx.base;
    opens GUIDisplays to javafx.graphics, javafx.fxml, javafx.base, javafx.media;
}