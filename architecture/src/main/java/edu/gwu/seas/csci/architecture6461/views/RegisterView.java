package edu.gwu.seas.csci.architecture6461.views;

import java.io.IOException;

import edu.gwu.seas.csci.architecture6461.App;
import edu.gwu.seas.csci.architecture6461.controllers.RegisterController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class RegisterView extends Pane {

    private RegisterController controller;

    public RegisterView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("register.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                controller = new RegisterController();
                return controller;
            }
        });
        fxmlLoader.load();
    }
}
