package com.marrok.schoolmanagermvn.controllers.teacher;

import com.marrok.schoolmanagermvn.model.Teacher;
import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

public class UpdateTeacherController {

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

    private Teacher teacher;
    private TeachersController parentController;
    private DatabaseHelper dbHelper;

    @FXML
    public void initialize() throws SQLException {
        genderChoiceBox.getItems().addAll("Male", "Female");
        dbHelper = new DatabaseHelper();
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        populateFields();
    }

    public void setController(TeachersController parentController) {
        this.parentController = parentController;
    }

    private void populateFields() {
        firstNameField.setText(teacher.getFname());
        lastNameField.setText(teacher.getLname());
        phoneField.setText(String.valueOf(teacher.getPhone()));
        addressField.setText(teacher.getAddress());
        String selectedGender = genderChoiceBox.getValue();
        genderChoiceBox.setValue(teacher.getGender() ? "Male" : "Female");

    }

    @FXML
    private void saveTeacher(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String gender = genderChoiceBox.getValue();

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || address.isEmpty() || gender == null) {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return;
        }

        teacher.setFname(firstName);
        teacher.setLname(lastName);
        teacher.setPhone(Integer.parseInt(phone));
        teacher.setAddress(address);
        // Get the selected value from the ComboBox
        String selectedGender = genderChoiceBox.getValue();

        // Ensure the selected value is not null
        if (selectedGender != null) {
            // Set the gender based on the selected value
            teacher.setGender("Male".equalsIgnoreCase(selectedGender));
        } else {
            // Handle case where no gender is selected (optional)
            teacher.setGender(false); // or true, depending on your default choice
        }

        boolean success = dbHelper.updateTeacher(teacher);
        if (success) {
            GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Teacher updated successfully.");
            if (parentController != null) {
                parentController.loadTeachersFromDatabase();
            }
            closeWindow();
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to update teacher.");
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
