package edu.gwu.seas.csci.architecture6461.views;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.gwu.seas.csci.architecture6461.App;
import edu.gwu.seas.csci.architecture6461.controllers.RegisterController;
import edu.gwu.seas.csci.architecture6461.models.Register;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import lombok.val;

public class RegisterView extends Pane {
    private static final Logger LOGGER = Logger.getLogger(RegisterView.class.getName());
    private RegisterController controller;
    private String name;

    public RegisterView() throws IOException {
        this.loadFXML("register.fxml");
    }

    public void setRegister(String name, Register register) {
        this.name = name;
        this.controller.setRegister(this.name, register);
        this.setView();
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

    private void setView()
    {
        LOGGER.log(Level.INFO, "RegisterView: {0} loaded.", name);
        // Set the view by controlling the visibility of register chiclets.
        // loop
            // get the element id which corresponds to i
            // set visibility to invisible
        //
    }
}
