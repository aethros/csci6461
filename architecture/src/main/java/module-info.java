module edu.gwu.seas.csci.architecture6461 {
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens edu.gwu.seas.csci.architecture6461 to javafx.fxml;
    exports edu.gwu.seas.csci.architecture6461;
}
