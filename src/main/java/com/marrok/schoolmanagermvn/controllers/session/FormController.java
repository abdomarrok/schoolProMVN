package com.marrok.schoolmanagermvn.controllers.session;

import com.marrok.schoolmanagermvn.model.Session_model;
import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class FormController {

    @FXML
    private ChoiceBox<String> moduleChoiceBox;

    @FXML
    private ChoiceBox<String> teacherChoiceBox;

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
            populateChoiceBoxes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void populateChoiceBoxes() {
        try {
            ObservableList<String> modules = FXCollections.observableArrayList(dbHelper.getAllModuleNames());
            moduleChoiceBox.setItems(modules);

            ObservableList<String> teachers = FXCollections.observableArrayList(dbHelper.getAllTeacherNames());
            teacherChoiceBox.setItems(teachers);
        } catch (SQLException e) {
            e.printStackTrace();
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not load modules or teachers.");
        }
    }

    @FXML
    private void handleSave() {
        String selectedModule = moduleChoiceBox.getValue();
        String selectedTeacher = teacherChoiceBox.getValue();

        if (selectedModule != null && selectedTeacher != null) {
            try {
                int moduleId = dbHelper.getModuleIdByName(selectedModule);
                int teacherId = dbHelper.getTeacherIdByName(selectedTeacher);

                boolean success;
                if (selectedSession == null) {
                    success = dbHelper.addSession(moduleId, teacherId);
                } else {
                    success = dbHelper.updateSession(selectedSession.getId(), moduleId, teacherId);
                }

                if (success) {
                    parentController.loadSessionsFromDatabase(); // Refresh the session list
                    closeForm(); // Close the form
                } else {
                    GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not save the session.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not save the session.");
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "Warning", "Please select both a module and a teacher.");
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
        // Set ChoiceBox fields with session data
        String moduleName = dbHelper.getModuleById(session.getModule_ID());
        moduleChoiceBox.setValue(moduleName);

        String teacherName = dbHelper.getTeacherFullNameById(session.getTeacher_ID());
        teacherChoiceBox.setValue(teacherName);
    }
}
