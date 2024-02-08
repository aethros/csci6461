package edu.gwu.seas.csci.architecture6461.views;

import java.io.IOException;

import edu.gwu.seas.csci.architecture6461.App;
import edu.gwu.seas.csci.architecture6461.controllers.AssemblerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import lombok.val;

public class AssemblerView extends Pane {

    private AssemblerController controller;

    public AssemblerView() throws IOException {
        this.loadFXML("assembler.fxml");
    }

    private void loadFXML(String fxml) throws IOException {
        val fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        fxmlLoader.setRoot(this);
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                controller = new AssemblerController();
                return controller;
            }
        });
        fxmlLoader.load();
    }
}
