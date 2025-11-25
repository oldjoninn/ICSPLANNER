module ICSPlanner {
    requires javafx.controls;
    
    opens application to javafx.graphics, javafx.fxml;
    opens components to javafx.base;
    opens GUIDisplays to javafx.graphics, javafx.fxml, javafx.base;
}