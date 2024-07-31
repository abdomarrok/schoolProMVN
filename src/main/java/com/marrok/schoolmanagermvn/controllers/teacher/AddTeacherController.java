package com.marrok.schoolmanagermvn.controllers.teacher;


import com.marrok.schoolmanagermvn.model.Teacher;
import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddTeacherController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private ChoiceBox<String> genderChoiceBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private TeachersController parentController;
    private DatabaseHelper dbHelper;

    @FXML
    public void initialize() throws SQLException {
        genderChoiceBox.getItems().addAll("Male", "Female");
        dbHelper = new DatabaseHelper();
    }

    public void setController(TeachersController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void saveTeacher(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String selectedGender = genderChoiceBox.getValue();
        boolean gender;

        // Ensure the selected value is not null
        if (selectedGender != null) {
            // Set the gender based on the selected value
            gender= "Male".equalsIgnoreCase(selectedGender);
        } else {
            // Handle case where no gender is selected (optional)
           gender=false;// or true, depending on your default choice
        }

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return;
        }

        Teacher newTeacher = new Teacher(firstName, lastName, Integer.parseInt(phone), address, gender);

        boolean success = dbHelper.addTeacher(newTeacher);
        if (success) {
            GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Teacher added successfully.");
            if (parentController != null) {
                parentController.loadTeachersFromDatabase();
            }
            closeWindow();
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to add teacher.");
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
