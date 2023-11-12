module edu.gwu.seas.csci.architecture6461 {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires lombok;

    opens edu.gwu.seas.csci.architecture6461 to javafx.fxml;
    exports edu.gwu.seas.csci.architecture6461;
    
    opens edu.gwu.seas.csci.architecture6461.controllers to javafx.fxml;
    exports edu.gwu.seas.csci.architecture6461.controllers;
    
    opens edu.gwu.seas.csci.architecture6461.views to javafx.fxml;
    exports edu.gwu.seas.csci.architecture6461.views;

    exports edu.gwu.seas.csci.architecture6461.util;
    exports edu.gwu.seas.csci.architecture6461.models;
}
