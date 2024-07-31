package com.marrok.schoolmanagermvn.controllers.session;

import com.marrok.schoolmanagermvn.model.Session_model;
import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class FormController {

    @FXML
    private TextField moduleIdField;

    @FXML
    private TextField teacherIdField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private DatabaseHelper dbHelper;
    private SessionController parentController;
    private Session_model selectedSession;

    @FXML
    public void initialize() {
        try {
            dbHelper = new DatabaseHelper();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleSave() {
        // Validate inputs and perform save or update operation
        int moduleId = Integer.parseInt(moduleIdField.getText());
        int teacherId = Integer.parseInt(teacherIdField.getText());

        boolean success;
        if (selectedSession == null) {
            success = dbHelper.addSession(moduleId, teacherId);
        } else {
            success = dbHelper.updateSession(selectedSession.getId(), moduleId, teacherId);
        }

        if (success) {
            parentController.loadSessionsFromDatabase(); // Refresh the session list
            ((Stage) moduleIdField.getScene().getWindow()).close(); // Close the form
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not save the session.");
        }
    }

    @FXML
    private void handleCancel() {
        closeForm();
    }

    public void setParentController(SessionController parentController) {
        this.parentController = parentController;
    }

    private void closeForm() {
        // Close the form (if it's a new Stage)
        ((Stage) saveButton.getScene().getWindow()).close();
    }


    public void setSession(Session_model session) {
        this.selectedSession = session;
        // Set fields with session data
        moduleIdField.setText(String.valueOf(session.getModule_ID()));
        teacherIdField.setText(String.valueOf(session.getTeacher_ID()));
    }
}
