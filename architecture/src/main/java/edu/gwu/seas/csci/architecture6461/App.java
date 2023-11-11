package edu.gwu.seas.csci.architecture6461;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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

    public static void setRoot(String fxml) throws IOException {
        appScene.setRoot(loadFXML(fxml));
    }

    private static void initScene() throws IOException {
        appScene = new Scene(loadFXML("primary"), 640, 480);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}