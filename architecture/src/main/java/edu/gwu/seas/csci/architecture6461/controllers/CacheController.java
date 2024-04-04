package edu.gwu.seas.csci.architecture6461.controllers;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import edu.gwu.seas.csci.architecture6461.models.Cache;
import edu.gwu.seas.csci.architecture6461.views.CacheLineView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.Pair;

public class CacheController implements Initializable {
    @FXML
    private CacheLineView c0;
    @FXML
    private CacheLineView c1;
    @FXML
    private CacheLineView c2;
    @FXML
    private CacheLineView c3;
    @FXML
    private CacheLineView c4;
    @FXML
    private CacheLineView c5;
    @FXML
    private CacheLineView c6;
    @FXML
    private CacheLineView c7;
    @FXML
    private CacheLineView c8;
    @FXML
    private CacheLineView c9;
    @FXML
    private CacheLineView c10;
    @FXML
    private CacheLineView c11;
    @FXML
    private CacheLineView c12;
    @FXML
    private CacheLineView c13;
    @FXML
    private CacheLineView c14;
    @FXML
    private CacheLineView c15;

    private CacheLineView[] cacheLines; // Set of all cacheLines.

    private Cache cache;

    public CacheController(Cache cache) {
        this.cache = cache;
        this.cache.addEventListener(this::cacheEventListener);
    }

    private void cacheEventListener(LinkedHashMap<Integer,Pair<Integer, int[]>> cacheState) {
        cacheState.entrySet().forEach(entry -> {
            var index = entry.getKey();
            this.cacheLines[index].updateCacheLine(entry.getValue());
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.cacheLines = new CacheLineView[]{
            c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15
        };

        for (int i = 0; i < cacheLines.length; i++) {
            this.cacheLines[i].setCacheLine("c" + i, this.cache.getCacheLine(i));
        }
    }
}
