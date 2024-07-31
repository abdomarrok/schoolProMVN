package com.marrok.schoolmanagermvn.controllers.inscription;

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

public class AddInscriptionController implements Initializable {

    @FXML private TextField studentIdField;
    @FXML private TextField sessionIdField;
    @FXML private DatePicker registrationDateField;
    @FXML private TextField priceField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

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

    public void setController(InscriptionController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void saveInscription(MouseEvent event) {
        try {
            int studentId = Integer.parseInt(studentIdField.getText());
            int sessionId = Integer.parseInt(sessionIdField.getText());
            LocalDate registrationDate = registrationDateField.getValue();
            String price = priceField.getText();

            // Check if registrationDate is null
            if (registrationDate == null) {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select a valid registration date.");
                return;
            }

            // Add the inscription to the database
            boolean success = dbHelper.addInscription(studentId, sessionId, registrationDate, price);

            if (success) {
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Inscription added successfully.");
                if (parentController != null) {
                    parentController.loadInscriptionsFromDatabase();
                }
                ((Stage) saveButton.getScene().getWindow()).close();
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to add inscription.");
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
