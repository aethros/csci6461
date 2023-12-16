package edu.gwu.seas.csci.architecture6461.views;

import java.io.IOException;

import edu.gwu.seas.csci.architecture6461.App;
import edu.gwu.seas.csci.architecture6461.controllers.RegisterController;
import edu.gwu.seas.csci.architecture6461.models.Register;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import lombok.val;

public class RegisterView extends Pane {
    private RegisterController controller;

    public RegisterView() throws IOException {
        this.loadFXML("register.fxml");
    }

    public void setRegister(String name, Register register) {
        this.controller.setRegister(name, register);
    }

    private void loadFXML(String fxml) throws IOException {
        val fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
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
