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

public class UpdateInscriptionController implements Initializable {

    private int inscriptionId; // ID of the inscription to be updated

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
            students.setAll(dbHelper.getStudentNames());
            studentChoiceBox.setItems(students);

            sessionsMap = dbHelper.getSessionNames();
            sessionChoiceBox.setItems(FXCollections.observableArrayList(sessionsMap.keySet()));


        } catch (SQLException e) {
            e.printStackTrace();
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to initialize data: " + e.getMessage());
        }
    }

    public void setController(InscriptionController parentController, int inscriptionId){
        this.parentController = parentController;
        this.inscriptionId = inscriptionId;
        try {
            loadInscriptionData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Inscription ID set in controller: " + inscriptionId); // Debug statement
    }

    private void loadInscriptionData() throws SQLException {
        System.out.println("UpdateInscriptionController.loadInscriptionData");
        System.out.println("inscriptionId: " + inscriptionId);
        Map<String, Object> inscriptionData = dbHelper.getInscriptionById(inscriptionId);
        if (inscriptionData != null) {
            String studentName = (String) inscriptionData.get("student_name");
            String sessionDisplayName = (String) inscriptionData.get("session_display_name");
            LocalDate registrationDate = (LocalDate) inscriptionData.get("registration_date");
            String price = (String) inscriptionData.get("price");

            studentChoiceBox.setValue(studentName);
            sessionChoiceBox.setValue(sessionDisplayName);
            registrationDateField.setValue(registrationDate);
            priceField.setText(price);
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Inscription not found.");
            ((Stage) saveButton.getScene().getWindow()).close();
        }
    }

    @FXML
    private void saveUpdatedInscription(MouseEvent event) {
        try {
            String selectedStudent = studentChoiceBox.getValue();
            String selectedSessionDisplay = sessionChoiceBox.getValue();
            LocalDate registrationDate = registrationDateField.getValue();
            String price = priceField.getText();

            if (selectedStudent == null || selectedSessionDisplay == null || registrationDate == null || price == null || price.trim().isEmpty()) {
                GeneralUtil.showAlert(Alert.AlertType.WARNING, "Validation Error", "Please complete all fields.");
                return;
            }

            int studentId = dbHelper.getStudentIdByName(selectedStudent);
            int sessionId = sessionsMap.getOrDefault(selectedSessionDisplay, -1);

            if (sessionId == -1) {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Session not Found", "The selected session could not be found.");
                return;
            }

            boolean success = dbHelper.updateInscription(inscriptionId, studentId, sessionId, registrationDate, price);

            if (success) {
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Inscription updated successfully.");
                if (parentController != null) {
                    parentController.loadInscriptionsFromDatabase();
                }
                ((Stage) saveButton.getScene().getWindow()).close();
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to update inscription.");
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
