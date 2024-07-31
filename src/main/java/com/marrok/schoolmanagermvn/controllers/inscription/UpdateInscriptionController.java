package com.marrok.schoolmanagermvn.controllers.inscription;

import com.marrok.schoolmanagermvn.model.StudentInscription;
import com.marrok.schoolmanagermvn.model.StudentInscription;
import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateInscriptionController implements Initializable {

    @FXML private TextField studentIdField;
    @FXML private TextField sessionIdField;
    @FXML private DatePicker registrationDateField;
    @FXML private TextField priceField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private StudentInscription inscription;
    private InscriptionController parentController;
    private DatabaseHelper dbHelper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            dbHelper = new DatabaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void setInscription(StudentInscription inscription) {
        this.inscription = inscription;
        if (inscription != null) {
            studentIdField.setText(String.valueOf(inscription.getStudentId()));
            sessionIdField.setText(String.valueOf(inscription.getSessionId()));
            registrationDateField.setValue(inscription.getRegistrationDate()); // Directly use LocalDate
            priceField.setText(inscription.getPrice());
        }
    }


    public void setController(InscriptionController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void saveInscription(MouseEvent event) {
        try {
            // Get input values
            int studentId = Integer.parseInt(studentIdField.getText());
            int sessionId = Integer.parseInt(sessionIdField.getText());
            LocalDate registrationDate = registrationDateField.getValue();
            String price = priceField.getText();

            // Ensure registrationDate is not null
            if (registrationDate == null) {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select a valid registration date.");
                return;
            }


            // Update inscription in the database
            boolean success = dbHelper.updateInscription( inscription.getId(), // Ensure 'inscription' is defined and available
                    studentId,
                    sessionId,
                    registrationDate,
                    price);

            if (success) {
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Inscription updated successfully.");
                if (parentController != null) {
                    parentController.loadInscriptionsFromDatabase();
                }
                ((Stage) saveButton.getScene().getWindow()).close();
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to update inscription.");
            }
        } catch (NumberFormatException e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numeric values.");
        }
    }


    @FXML
    private void cancel(MouseEvent event) {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
}
