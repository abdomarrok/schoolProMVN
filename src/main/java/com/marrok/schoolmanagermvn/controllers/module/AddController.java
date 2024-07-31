package com.marrok.schoolmanagermvn.controllers.module;

import com.marrok.schoolmanagermvn.model.Module;
import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddController {

    @FXML
    private TextField nameField;

    @FXML
    private Button saveButton;

    private ModuleController moduleController;
    private DatabaseHelper dbHelper;

    @FXML
    public void initialize() {
        try {
            dbHelper = new DatabaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void setController(ModuleController moduleController) {
        this.moduleController = moduleController;
    }

    @FXML
    private void saveModule() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "Validation Error", "Module name cannot be empty.");
            return;
        }

        Module newModule = new Module(null, name);

        try {
            boolean success = dbHelper.addModule(newModule);
            if (success) {
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Module added successfully.");
                moduleController.loadModulesFromDatabase(); // Refresh the list
                Stage stage = (Stage) nameField.getScene().getWindow();
                stage.close();
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to add the module.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to add the module.");
        }
    }

    public void cancel(ActionEvent event) {
        // Close the window or reset fields
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

}
