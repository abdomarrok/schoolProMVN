package com.marrok.schoolmanagermvn.controllers.module;

import com.jfoenix.controls.JFXButton;
import com.marrok.schoolmanagermvn.model.Module;
import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ModuleController {

    @FXML
    private TableView<Module> moduleTable;

    @FXML
    private TableColumn<Module, Integer> idColumn;

    @FXML
    private TableColumn<Module, String> nameColumn;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton deleteButton;

    private ObservableList<Module> moduleList = FXCollections.observableArrayList();
    private DatabaseHelper dbHelper;

    @FXML
    public void initialize() {
        try {
            dbHelper = new DatabaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        initTable();
        loadModulesFromDatabase();
    }

    private void initTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    public void loadModulesFromDatabase() {
        try {
            moduleList = dbHelper.getModules(); // Load modules from the database
            moduleTable.setItems(moduleList); // Set the loaded modules to the table view
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }


    @FXML
    private void updateModule() {
        Module selectedModule = moduleTable.getSelectionModel().getSelectedItem();
        if (selectedModule != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/schoolmanagermvn/views/module/update.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Update Module");
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/schoolmanagermvn/img/lg.png")));

                // Set the controller for the update form
                UpdateController controller = loader.getController();
                controller.setModule(selectedModule);
                controller.setController(this);

                stage.showAndWait();
                // Refresh the table data after updating the module
                loadModulesFromDatabase();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Could not open the module form.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No module selected. Please select a module to update.");
        }
    }


    @FXML
    private void deleteModule() {
        Module selectedModule = moduleTable.getSelectionModel().getSelectedItem();
        if (selectedModule != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setContentText("Are you sure you want to delete the selected module?");
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                try {
                    boolean success = dbHelper.deleteModule(selectedModule.getId());
                    if (success) {
                        loadModulesFromDatabase();
                        showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", "Module deleted successfully.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Failed to delete the module.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the module.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No module selected. Please select a module to delete.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void addModule(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/schoolmanagermvn/views/module/add.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Module");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/schoolmanagermvn/img/lg.png")));
            AddController controller = loader.getController();
            controller.setController(this);

            stage.showAndWait();
            // Refresh the table data after adding a student
            loadModulesFromDatabase();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open the module form.");
        }
    }

    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }
}
