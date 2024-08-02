package com.marrok.schoolmanagermvn.controllers.inscription;

import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AddInscriptionController implements Initializable {

    @FXML private ChoiceBox<String> studentChoiceBox;
    @FXML private ChoiceBox<String> sessionChoiceBox;
    @FXML private DatePicker registrationDateField;
    @FXML private TextField priceField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private InscriptionController parentController;
    private DatabaseHelper dbHelper;
    private ObservableList<String> students = FXCollections.observableArrayList();
    private Map<String, Integer> sessionsMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            dbHelper = new DatabaseHelper();
            students.setAll(dbHelper.getStudentNames()); // Replace with actual method to get student names

            studentChoiceBox.setItems(students);
            sessionsMap = dbHelper.getSessionNames();
            sessionChoiceBox.setItems(FXCollections.observableArrayList(sessionsMap.keySet()));

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
            // Get selected values from UI components
            String selectedStudent = studentChoiceBox.getValue();
            String selectedSessionDisplay = sessionChoiceBox.getValue();
            LocalDate registrationDate = registrationDateField.getValue();
            String price = priceField.getText();

            // Validate that selections are made
            if (selectedStudent == null || selectedSessionDisplay == null || registrationDate == null || price == null || price.trim().isEmpty()) {
                GeneralUtil.showAlert(Alert.AlertType.WARNING, "Validation Error", "Please complete all fields.");
                return;
            }

            // Retrieve IDs based on selections
            int studentId = dbHelper.getStudentIdByName(selectedStudent);
            int sessionId = sessionsMap.getOrDefault(selectedSessionDisplay, -1);

            if (sessionId == -1) {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Session not Found", "The selected session could not be found.");
                return;
            }

            // Add inscription to database
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
        } catch (Exception e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void cancel(MouseEvent event) {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
}
