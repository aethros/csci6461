package edu.gwu.seas.csci.architecture6461.views;

import java.io.IOException;

import edu.gwu.seas.csci.architecture6461.App;
import edu.gwu.seas.csci.architecture6461.controllers.CacheLineController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.Pair;
import lombok.val;

public class CacheLineView extends Pane {

    private CacheLineController controller;

    public CacheLineView() throws IOException {
        this.loadFXML("cacheline.fxml");
    }

    public void setCacheLine(String name, Pair<Integer, int[]> cacheLine) {
        this.controller.setCacheLine(name, cacheLine);
    }

    public void updateCacheLine(Pair<Integer, int[]> cacheLine) {
        this.controller.updateCacheLine(cacheLine);
    }

    private void loadFXML(String fxml) throws IOException {
        val fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        fxmlLoader.setRoot(this);
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                controller = new CacheLineController();
                return controller;
            }
        });
        fxmlLoader.load();
    }
}
