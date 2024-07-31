package com.marrok.schoolmanagermvn.controllers.session;


import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.marrok.schoolmanagermvn.controllers.dashboard.DashboardController;
import com.marrok.schoolmanagermvn.model.Session_model;
import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionController {

    @FXML
    private TableView<Session_model> sessionTable;

    @FXML
    private TableColumn<Session_model, Integer> idColumn;

    @FXML
    private TableColumn<Session_model, String> moduleColumn;

    @FXML
    private TableColumn<Session_model, String> teacherColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;
    public JFXDrawer drawer;
    public JFXHamburger hamburger;

    private ObservableList<Session_model> sessionList = FXCollections.observableArrayList();
    private DatabaseHelper dbHelper;

    @FXML
    public void initialize() {
        try {
            dbHelper = new DatabaseHelper();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initTable();
        loadDrawer();
        loadSessionsFromDatabase();
    }

    private void initTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        moduleColumn.setCellValueFactory(new PropertyValueFactory<>("module_ID"));
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacher_ID"));
        moduleColumn.setCellValueFactory(cellData -> {
            int moduleId = cellData.getValue().getModule_ID();
            String moduleName = dbHelper.getModuleById(moduleId);
            return new SimpleStringProperty(moduleName != null ? moduleName : "Unknown Module");
        });

        // Custom cell value factory to get the teacher name by its ID
        teacherColumn.setCellValueFactory(cellData -> {
            int teacherId = cellData.getValue().getTeacher_ID();
            String teacherName = dbHelper.getTeacherFullNameById(teacherId);
            return new SimpleStringProperty(teacherName != null ? teacherName : "Unknown Teacher");
        });
    }

    public void loadSessionsFromDatabase() {
        try {
            sessionList = dbHelper.getSessions();
            sessionTable.setItems(sessionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDrawer() {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/com/marrok/schoolmanagermvn/views/NavDrawer.fxml"));
            drawer.setSidePane(box);
            drawer.setMinWidth(0); // Set initial min width to 0

        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
        task.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> drawer.toggle());

        drawer.setOnDrawerOpening(event -> {
            task.setRate(task.getRate() * -1);
            task.play();
            drawer.setMinWidth(220); // Set min width to 220 when opening
        });

        drawer.setOnDrawerClosed(event -> {
            task.setRate(task.getRate() * -1);
            task.play();
            drawer.setMinWidth(0); // Reset min width to 0 when closed
        });
    }


    @FXML
    private void addSession() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/schoolmanagermvn/views/session/form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Session");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/schoolmanagermvn/img/lg.png")));

            FormController controller = loader.getController();
            controller.setParentController(this); // Pass the parent controller

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not open the session form.");
        }
    }

    @FXML
    private void updateSession() {
        Session_model selectedSession = sessionTable.getSelectionModel().getSelectedItem();
        if (selectedSession != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/schoolmanagermvn/views/session/form.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Update Session");
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/schoolmanagermvn/img/lg.png")));

                FormController controller = loader.getController();
                controller.setParentController(this); // Pass the parent controller
                controller.setSession(selectedSession); // Pass the selected session for update

                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not open the session form.");
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "No session selected. Please select a session to update.");
        }
    }


    @FXML
    private void deleteSession() {
        Session_model selectedSession = sessionTable.getSelectionModel().getSelectedItem();
        if (selectedSession != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setContentText("Are you sure you want to delete the selected session?");
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                try {
                    boolean success = dbHelper.deleteSession(selectedSession.getId());
                    if (success) {
                        loadSessionsFromDatabase(); // Refresh the table data
                        GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", "Session deleted successfully.");
                    } else {
                        GeneralUtil.showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Failed to delete the session.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the session.");
                }
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "No session selected. Please select a session to delete.");
        }
    }




}
