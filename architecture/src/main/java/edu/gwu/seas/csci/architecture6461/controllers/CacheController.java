package edu.gwu.seas.csci.architecture6461.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gwu.seas.csci.architecture6461.models.Cache;
import javafx.fxml.Initializable;

public class CacheController implements Initializable {
    private Cache cache;

    public CacheController(Cache cache) {
        this.cache = cache;

        this.cache.addEventListener(cacheState ->
            cacheState.forEach(entry -> {
                // update UI elements per entry
                // ...
            })
        );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // No initialization needed.
    }
    
}
