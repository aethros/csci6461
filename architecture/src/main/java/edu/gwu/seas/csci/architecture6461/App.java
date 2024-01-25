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

    private static Scene appScene;

    @Override
    public void start(Stage stage) throws IOException {
        initApp();
        stage.setScene(appScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private static void initApp() throws IOException {
        // val manager = ProgramManager.getInstance();
        // appScene = new Scene(new ApplicationView(manager.getCpu()), 1280, 960);
        appScene = new Scene(new AssemblerView(), 1280, 960);
    }
}