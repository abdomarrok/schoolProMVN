package com.marrok.schoolmanagermvn.controllers;

import com.jfoenix.controls.JFXButton;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NavDrawerController implements Initializable {


    @FXML
    private JFXButton setting_btn;
    @FXML
    private JFXButton students_btn;
    @FXML
    private JFXButton dashboard_btn;
    @FXML
    private JFXButton classes_btn;
    @FXML
    private JFXButton timetable_btn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }


    @FXML
    public void goStudents(ActionEvent event) {
        System.out.println("NavDrawerController.goStudents");
        loadScene("/com/marrok/schoolmanagermvn/views/student/Students.fxml", event);
    }

    private void loadScene(String resourcePath, ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.setCursor(Cursor.HAND);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(NavDrawerController.class.getName()).log(Level.SEVERE, "Error loading scene: " + resourcePath, ex);
            GeneralUtil.showAlert(Alert.AlertType.ERROR,"Error", "Could not load the requested scene. Please try again later.");

        }
    }
}