package edu.gwu.seas.csci.architecture6461.views;

import java.io.IOException;

import edu.gwu.seas.csci.architecture6461.App;
import edu.gwu.seas.csci.architecture6461.controllers.CacheController;
import edu.gwu.seas.csci.architecture6461.managers.SessionManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import lombok.val;

public class CacheView extends Pane {

    private CacheController controller;

    public CacheView() throws IOException {
        this.loadFXML("cache.fxml");
    }

    private void loadFXML(String fxml) throws IOException {
        val fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        fxmlLoader.setRoot(this);
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                controller = new CacheController(SessionManager.getInstance().getControlUnit().getDataInterface().getCache());
                return controller;
            }
        });
        fxmlLoader.load();
    }
}
