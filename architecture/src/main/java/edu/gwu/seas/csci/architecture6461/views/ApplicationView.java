package edu.gwu.seas.csci.architecture6461.views;

import java.io.IOException;

import edu.gwu.seas.csci.architecture6461.App;
import edu.gwu.seas.csci.architecture6461.controllers.ApplicationController;
import edu.gwu.seas.csci.architecture6461.models.CPU;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import lombok.val;

public class ApplicationView extends Pane {

    private ApplicationController controller;

    public ApplicationView(CPU cpu) throws IOException {
        this.loadFXML(cpu, "app.fxml");
    }

    private void loadFXML(CPU cpu, String fxml) throws IOException {
        val fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        fxmlLoader.setRoot(this);
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                controller = new ApplicationController(cpu);
                return controller;
            }
        });
        fxmlLoader.load();
    }
}
