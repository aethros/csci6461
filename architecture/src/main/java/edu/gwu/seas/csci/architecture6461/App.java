package edu.gwu.seas.csci.architecture6461;

import java.io.IOException;

import edu.gwu.seas.csci.architecture6461.views.PrimaryView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene appScene;

    @Override
    public void start(Stage stage) throws IOException {
        initScene();
        stage.setScene(appScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setView(Pane view) {
        appScene.setRoot(view);
    }

    private static void initScene() throws IOException {
        appScene = new Scene(new PrimaryView(), 640, 480);
    }
}