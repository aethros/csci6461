package edu.gwu.seas.csci.architecture6461.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.gwu.seas.csci.architecture6461.managers.SessionManager;
import edu.gwu.seas.csci.architecture6461.views.CPUView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;

public class AssemblerController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(AssemblerController.class.getName());

    @FXML
    Button selectButton;
    @FXML
    Button assembleButton;
    @FXML
    TextField filePath;

    public AssemblerController() {
        // No initialization needed.
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // No initialization needed.
    }

    public void load(ActionEvent event) {
        LOGGER.info("Loading the main window.");
        this.launchSimulator(true);
    }

    public void assembleAndLoad(ActionEvent event) {
        String path = filePath.getText();
        if (path == null || path.isEmpty()) {
            LOGGER.warning("No file selected for assembly.");
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            LOGGER.log(Level.WARNING, "File does not exist: {0}", path);
            return;
        }
        LOGGER.log(Level.INFO, "Assembling file: {0}", path);

        this.handleAssemble(path, file);
        this.launchSimulator(false);
    }

    public void selectFile(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage primaryStage = (Stage) source.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        ExtensionFilter asmFilter = new ExtensionFilter("Assembly files (*.S, *.s, *.asm, *.ASM)", "*.S", "*.s", "*.asm", "*.ASM");
        ExtensionFilter extFilter = new ExtensionFilter("All files (*.*)", "*.*");
        fileChooser.getExtensionFilters().addAll(asmFilter, extFilter);
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            filePath.setText(file.getAbsolutePath());
        }
    }

    private void handleAssemble(String path, File file) {
        this.assembleButton.setDisable(true);
        this.selectButton.setDisable(true);
        this.filePath.setDisable(true);

        var assembled = SessionManager.getInstance().getAssembler().assemble(file.getAbsolutePath(), file.getParent());
        if (assembled != null && !assembled.isEmpty()) {
            LOGGER.log(Level.INFO, "File assembled successfully: {0}", path);
            SessionManager.getInstance().loadProgram(assembled);
        } else {
            LOGGER.log(Level.WARNING, "File could not be assembled: {0}", path);
        }
    }

    private void launchSimulator(boolean skipAssemble) {
        if (skipAssemble || SessionManager.getInstance().isProgramLoaded()) {
            // Close the current window.
            Scene scene = this.assembleButton.getScene();
            Window window = scene.getWindow();
            window.hide();

            try {
                // Open the main window.
                Stage primaryStage = new Stage();
                primaryStage.setScene(new Scene(new CPUView(SessionManager.getInstance().getControlUnit().getCpu())));
                primaryStage.show();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to open the main window.", e);
            }
        }
        else {
            this.assembleButton.setDisable(false);
            this.selectButton.setDisable(false);
            this.filePath.setDisable(false);
        }
    }
}
