package edu.gwu.seas.csci.architecture6461;

import java.io.IOException;

import edu.gwu.seas.csci.architecture6461.views.AssemblerView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public final class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Scene appScene = new Scene(new AssemblerView(), 1280, 960);
        stage.setScene(appScene);
        stage.show();
    }

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format","[%1$tc] %4$s: %5$s %6$s%n");
        launch();
    }
}