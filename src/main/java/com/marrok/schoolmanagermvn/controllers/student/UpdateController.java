package com.marrok.schoolmanagermvn.controllers.student;

import com.marrok.schoolmanagermvn.model.Student;
import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class UpdateController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField contactField;
    @FXML
    private TextField birthDateField; // Updated field for birth date
    @FXML
    private TextField levelField; // Updated field for level

    private Student student;
    private StudentsController studentsController;
    private DatabaseHelper dbHelper;

    public void setStudent(Student student) {
        this.student = student;
        populateFields();
    }

    public void setController(StudentsController studentsController) {
        this.studentsController = studentsController;
    }

    @FXML
    private void initialize() {
        try {
            dbHelper = new DatabaseHelper();
        } catch (SQLException e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Database connection error.");
            e.printStackTrace();
        }
        genderComboBox.getItems().addAll("Male", "Female"); // Add gender options
    }

    private void populateFields() {
        if (student != null) {
            firstNameField.setText(student.getFname());
            lastNameField.setText(student.getLname());

            // Set the selected item for the ComboBox based on the gender
            genderComboBox.setValue(student.getGender() ? "Male" : "Female");

            contactField.setText(String.valueOf(student.getContact()));
            birthDateField.setText(student.getBirthDate()); // Update this to get the birth date
            levelField.setText(student.getLevel()); // Update this to get the level
        }
    }

    public void updateStudent() {
        if (student != null) {
            try {
                // Get the selected value from the ComboBox
                String selectedGender = genderComboBox.getValue();

                // Ensure the selected value is not null
                if (selectedGender != null) {
                    // Set the gender based on the selected value
                    student.setGender("Male".equalsIgnoreCase(selectedGender));
                } else {
                    // Handle case where no gender is selected (optional)
                    student.setGender(false); // or true, depending on your default choice
                }

                // Update other fields as necessary
                student.setFname(firstNameField.getText());
                student.setLname(lastNameField.getText());
                student.setContact(contactField.getText());
                student.setBirthDate(birthDateField.getText()); // Assuming a method exists to set birth date
                student.setLevel(levelField.getText()); // Assuming a method exists to set level

                // Save the updated student to the database
                if (dbHelper.updateStudent(student)) {
                    GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Student Update", "The student was updated successfully.");
                    // Close the stage
                    Stage stage = (Stage) firstNameField.getScene().getWindow();
                    stage.close();
                } else {
                    GeneralUtil.showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update the student.");
                }

            } catch (NumberFormatException e) {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please ensure all fields are correctly filled.");
            } catch (Exception e) {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Update Failed", "An unexpected error occurred while updating the student.");
                e.printStackTrace(); // Optionally log the exception
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }
}
