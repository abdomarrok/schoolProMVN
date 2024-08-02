package com.marrok.schoolmanagermvn.controllers.inscription;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.marrok.schoolmanagermvn.model.StudentInscription;
import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dlsc.gemsfx.FilterView;
import com.dlsc.gemsfx.FilterView.Filter;
import com.dlsc.gemsfx.FilterView.FilterGroup;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InscriptionController implements Initializable {

    @FXML private TableView<StudentInscription> tbData;
    @FXML private TableColumn<StudentInscription, Integer> inscriptionId;
    @FXML private TableColumn<StudentInscription, String> studentFullName;
    @FXML private TableColumn<StudentInscription, String> sessionDetails;
    @FXML private TableColumn<StudentInscription, String> registrationDate;
    @FXML private TableColumn<StudentInscription, String> price;

    @FXML private JFXDrawer drawer;
    @FXML private JFXHamburger hamburger;
    @FXML private FilterView<StudentInscription> filterView;

    private ObservableList<StudentInscription> inscriptionsModels = FXCollections.observableArrayList();
    private DatabaseHelper dbHelper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            dbHelper = new DatabaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        loadFilter();
        initTable();
        loadDrawer();
        CSSFX.start();
    }

    @FXML
    private void addInscription(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/schoolmanagermvn/views/inscription/add.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Inscription");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/schoolmanagermvn/img/lg.png")));
            AddInscriptionController controller = loader.getController();
            controller.setController(this);

            stage.showAndWait();
            // Refresh the table data after adding an inscription
            loadInscriptionsFromDatabase();
        } catch (IOException e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not open the add inscription form.");
            e.printStackTrace();
        }
    }

    @FXML
    private void updateInscription(ActionEvent event) {
        System.out.println("InscriptionController.updateInscription");
        StudentInscription selectedInscription = tbData.getSelectionModel().getSelectedItem();
        System.out.println("selectedInscription getid"+selectedInscription.getId());
        if (selectedInscription != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/schoolmanagermvn/views/inscription/update.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Update Inscription");
                stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/com/marrok/schoolmanagermvn/img/lg.png"))));
                UpdateInscriptionController controller = loader.getController();
                int inscriptionId = selectedInscription.getId();
                System.out.println("Inscriptionid before set controller: " + inscriptionId);
                controller.setController(this ,inscriptionId);

                stage.showAndWait();
                // Refresh the table data after updating the inscription
                loadInscriptionsFromDatabase();
            } catch (IOException e) {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not open the update inscription form.");
                e.printStackTrace();
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "No Inscription Selected. Please select an inscription to update.");
        }
    }

    @FXML
    private void deleteInscription(ActionEvent event) {
        StudentInscription selectedInscription = tbData.getSelectionModel().getSelectedItem();

        if (selectedInscription == null) {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an inscription to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected inscription?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            boolean success = dbHelper.deleteInscription(selectedInscription.getId());

            if (success) {
                loadInscriptionsFromDatabase();
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", "The inscription was deleted successfully.");
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Failed to delete the inscription.");
            }
        }
    }

    private void loadFilter() {
        filterView.setTextFilterProvider(text -> inscription -> {
            if (text == null || text.isEmpty()) {
                return true;
            }
            String lowerCaseText = text.toLowerCase();
            return String.valueOf(inscription.getStudentId()).contains(lowerCaseText) ||
                    String.valueOf(inscription.getSessionId()).contains(lowerCaseText) ||
                    inscription.getRegistrationDate().toString().toLowerCase().contains(lowerCaseText) ||
                    inscription.getPrice().toLowerCase().contains(lowerCaseText);
        });

        // Add any specific filters or filter groups if needed
        // Example: FilterGroup<Student> group = new FilterGroup<>("Group Name");
        // filterView.getFilterGroups().add(group);

        loadInscriptionsFromDatabase();

        SortedList<StudentInscription> sortedList = new SortedList<>(filterView.getFilteredItems());
        tbData.setItems(sortedList);
        sortedList.comparatorProperty().bind(tbData.comparatorProperty());
    }

    private void initTable() {
        inscriptionId.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentFullName.setCellValueFactory(new PropertyValueFactory<>("studentFullName"));
        sessionDetails.setCellValueFactory(new PropertyValueFactory<>("sessionDetails"));
        registrationDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void loadInscriptionsFromDatabase() {
        try {
            // Fetch the inscriptions along with the full name and session details
            List<StudentInscription> inscriptions = dbHelper.getAllInscriptionsWithDetails();
            inscriptionsModels.setAll(inscriptions);
            filterView.getItems().setAll(inscriptionsModels);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDrawer() {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/com/marrok/schoolmanagermvn/views/NavDrawer.fxml"));
            drawer.setSidePane(box);
            drawer.setMinWidth(0);

        } catch (IOException ex) {
            Logger.getLogger(InscriptionController.class.getName()).log(Level.SEVERE, null, ex);
        }

        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
        task.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> drawer.toggle());

        drawer.setOnDrawerOpening(event -> {
            task.setRate(task.getRate() * -1);
            task.play();
            drawer.setMinWidth(220);
        });

        drawer.setOnDrawerClosed(event -> {
            task.setRate(task.getRate() * -1);
            task.play();
            drawer.setMinWidth(0);
        });
    }



    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }
}
