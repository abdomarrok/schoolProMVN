package com.marrok.schoolmanagermvn.controllers.settings;

import com.marrok.schoolmanagermvn.util.DatabaseConnection;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;

public class SettingsController {

    @FXML
    private void handleBackupData(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Backup Location");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQL Files", "*.sql"));

        // Open the file chooser dialog
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            try {
                // Get the absolute path of the selected file
                String backupPath = selectedFile.getAbsolutePath();

                // Perform the backup
                DatabaseConnection.backupDatabase(backupPath);

                // Show success alert
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Backup Data", "Backup completed successfully.");
            } catch (Exception e) {
                // Show error alert
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Backup Data", "Backup failed: " + e.getMessage());

                // Optionally log the exception
                e.printStackTrace();
            }
        } else {
            // Show warning alert if no file was selected
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "Backup Data", "No file selected for backup.");
        }
    }

    @FXML
    void handleLogout(ActionEvent event) {
        // Handle Logout Action
        GeneralUtil.goBackLogin(event);
    }
    @FXML
    public void go_Dashboard(ActionEvent event) {
        GeneralUtil.goBackDashboard(event);
    }
}
