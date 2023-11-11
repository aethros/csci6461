module edu.gwu.seas.csci.architecture6461 {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;

    opens edu.gwu.seas.csci.architecture6461 to javafx.fxml;
    exports edu.gwu.seas.csci.architecture6461;
    
    opens edu.gwu.seas.csci.architecture6461.controllers to javafx.fxml;
    exports edu.gwu.seas.csci.architecture6461.controllers;
}
