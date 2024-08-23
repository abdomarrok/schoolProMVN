package com.marrok.schoolmanagermvn.controllers;

import com.jfoenix.controls.JFXButton;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NavDrawerController implements Initializable {



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
    public void goDashboard(ActionEvent event) {
        System.out.println("NavDrawerController.goDashboard");
        loadScene("/com/marrok/schoolmanagermvn/views/dashboard/dashboard.fxml", event);
    }

    public void goTeachers(ActionEvent event) {
        System.out.println("NavDrawerController.goTeachers");
        loadScene("/com/marrok/schoolmanagermvn/views/teacher/teacher_view.fxml", event);
    }

    public void  goModules(ActionEvent event){
        System.out.println("NavDrawerController.goModules");
        loadScene("/com/marrok/schoolmanagermvn/views/module/module.fxml", event);
    }
    public void goSession(ActionEvent event){
        System.out.println("NavDrawerController.goSession");
        loadScene("/com/marrok/schoolmanagermvn/views/session/session.fxml", event);
    }

    public void goSettings(ActionEvent event) {
        System.out.println("NavDrawerController.goSettings");
        loadScene("/com/marrok/schoolmanagermvn/views/settings/settings.fxml", event);
    }

    public void goInscriptions(ActionEvent event) {
        System.out.println("NavDrawerController.goInscriptions");
        loadScene("/com/marrok/schoolmanagermvn/views/inscription/inscription.fxml", event);
    }
    private void loadScene(String resourcePath, ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root,screenSize.getWidth(), screenSize.getHeight());
            scene.setCursor(Cursor.HAND);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();//
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/schoolmanagermvn/img/lg.png")));
            stage.setMaximized(true);//
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(NavDrawerController.class.getName()).log(Level.SEVERE, "Error loading scene: " + resourcePath, ex);
            GeneralUtil.showAlert(Alert.AlertType.ERROR,"Error", "Could not load the requested scene. Please try again later.");

        }
    }



}