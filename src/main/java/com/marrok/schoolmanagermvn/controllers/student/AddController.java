package com.marrok.schoolmanagermvn.controllers.student;

import com.marrok.schoolmanagermvn.model.Student;
import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField contactField;

    @FXML
    private TextField yearField;

    @FXML
    private ComboBox<String> genderComboBox;
    private StudentsController studentsController;
    private DatabaseHelper dbHelper;

    @FXML
    public void initialize() {
        // Initialize comboBox with gender options
        try {
            dbHelper = new DatabaseHelper();
        } catch (SQLException e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Database connection error.");
            e.printStackTrace();
        }
        genderComboBox.getItems().addAll("Male", "Female");
    }

    @FXML
    public void addStudent(ActionEvent event) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String contactStr = contactField.getText().trim();
        String yearStr = yearField.getText().trim();
        String gender = genderComboBox.getValue();

        // Perform validation
        if (firstName.isEmpty() || lastName.isEmpty() || contactStr.isEmpty() || yearStr.isEmpty() || gender == null) {
            // Display an error message or handle invalid input
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please fill in all fields.");
            return;
        }

        try {
            // Parse integers
            int contact = Integer.parseInt(contactStr);
            int year = Integer.parseInt(yearStr);

            // Create a new Student object
            Student student = new Student(
                    // Assuming you have a constructor that includes ID generation or you handle it in DB
                    -1, // Or use appropriate logic to handle ID generation
                    firstName,
                    lastName,
                    year,
                    contact,
                    "Male".equalsIgnoreCase(gender),
                    0 // Or set to appropriate classroom value or remove if not used
            );

            // Save the student data to the database
            if (dbHelper.addStudent(student)) {
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Student Added", "The student was added successfully.");
                // Optionally, close the window or clear fields
                Stage stage = (Stage) firstNameField.getScene().getWindow();
                stage.close();
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Addition Failed", "Failed to add the student.");
            }
        } catch (NumberFormatException e) {
            // Handle invalid number format
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please ensure contact and year are valid numbers.");
        } catch (Exception e) {
            // Handle other unexpected exceptions
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred while adding the student.");
            e.printStackTrace(); // Log the exception
        }
    }


    @FXML
    public void cancel(ActionEvent event) {
        // Close the window or reset fields
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }

    public void setController(StudentsController studentsController) {
        this.studentsController=studentsController;
    }
}
