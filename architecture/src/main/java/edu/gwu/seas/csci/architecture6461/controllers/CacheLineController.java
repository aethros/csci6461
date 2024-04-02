package edu.gwu.seas.csci.architecture6461.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Pair;
public class CacheLineController implements Initializable {
    @FXML
    private Text tag;
    @FXML
    private Text addr0;
    @FXML
    private Text addr1;
    @FXML
    private Text addr2;
    @FXML
    private Text addr3;
    @FXML
    private Text addr4;
    @FXML
    private Text addr5;
    @FXML
    private Text addr6;
    @FXML
    private Text addr7;

    private String name; // The index of the cache line.
    private Pair<Text, Text[]> cacheLineText; // Tag plus list of all addresses (Text Fields) in the cache line.

    public CacheLineController() {
        // No initialization needed.
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // No initialization needed.
    }

    public void setCacheLine(String name, Pair<Integer, int[]> cacheLine) {
        this.name = name;
        this.cacheLineText = new Pair<>(tag, new Text[]{addr0, addr1, addr2, addr3, addr4, addr5, addr6, addr7});
        this.tag.setFont(Font.font("Monospaced", 12));
        for (int i = 0; i < cacheLine.getValue().length; i++) {
            this.cacheLineText.getValue()[i].setFont(Font.font("Monospaced", 12));
        }
        this.updateCacheLine(cacheLine);
    }

    public void updateCacheLine(Pair<Integer, int[]> cacheLine) {
        this.tag.setText(String.format("%03d", cacheLine.getKey()));
        for (int i = 0; i < cacheLine.getValue().length; i++) {
            this.cacheLineText.getValue()[i].setText(String.format("%05d", cacheLine.getValue()[i]));
        }
    }
    
}
