package com.marrok.schoolmanagermvn.controllers.teacher;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.marrok.schoolmanagermvn.controllers.dashboard.DashboardController;
import com.marrok.schoolmanagermvn.model.Teacher;
import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.dlsc.gemsfx.FilterView;
import com.dlsc.gemsfx.FilterView.Filter;
import com.dlsc.gemsfx.FilterView.FilterGroup;
import fr.brouillard.oss.cssfx.CSSFX;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TeachersController implements Initializable {

    public TableView<Teacher> tbData;
    public TableColumn<Teacher, Integer> id;
    public TableColumn<Teacher, String> firstName;
    public TableColumn<Teacher, String> lastName;
    public TableColumn<Teacher, Integer> phone;
    public TableColumn<Teacher, String> gender;
    public TableColumn<Teacher, String> address;
    public JFXDrawer drawer;
    public JFXHamburger hamburger;
    public FilterView<Teacher> filterView;

    private ObservableList<Teacher> teachersModels = FXCollections.observableArrayList();
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
    private void addTeacher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/schoolmanagermvn/views/teacher/add.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Teacher");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/schoolmanagermvn/img/lg.png")));
            AddTeacherController controller = loader.getController();
            controller.setController(this);

            stage.showAndWait();
            loadTeachersFromDatabase();
        } catch (IOException e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not open the add teacher form.");
            e.printStackTrace();
        }
    }

    @FXML
    private void updateTeacher(ActionEvent event) {
        Teacher selectedTeacher = tbData.getSelectionModel().getSelectedItem();
        if (selectedTeacher != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/schoolmanagermvn/views/teacher/update.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Update Teacher");
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/schoolmanagermvn/img/lg.png")));
                UpdateTeacherController controller = loader.getController();
                controller.setTeacher(selectedTeacher);
                controller.setController(this);

                stage.showAndWait();
                loadTeachersFromDatabase();
            } catch (IOException e) {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not open the update teacher form.");
                e.printStackTrace();
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "No Teacher Selected. Please select a teacher to update.");
        }
    }

    @FXML
    private void deleteTeacher(ActionEvent event) {
        Teacher selectedTeacher = tbData.getSelectionModel().getSelectedItem();
        if (selectedTeacher == null) {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a teacher to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected teacher?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            boolean success = dbHelper.deleteTeacher(selectedTeacher.getId());
            if (success) {
                loadTeachersFromDatabase();
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", "The teacher was deleted successfully.");
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Failed to delete the teacher.");
            }
        }
    }

    private void loadFilter()  {
        filterView.setTextFilterProvider(text -> teacher -> {
            if (text == null || text.isEmpty()) {
                return true;
            }
            String lowerCaseText = text.toLowerCase();
            return teacher.getFname().toLowerCase().contains(lowerCaseText) ||
                    teacher.getLname().toLowerCase().contains(lowerCaseText) ||
                    String.valueOf(teacher.getPhone()).contains(lowerCaseText) ||
                    teacher.getAddress().toLowerCase().contains(lowerCaseText);
        });

        FilterGroup<Teacher> firstNameGroup = new FilterGroup<>("First Name");
        FilterGroup<Teacher> lastNameGroup = new FilterGroup<>("Last Name");
        FilterGroup<Teacher> genderGroup = new FilterGroup<>("Gender");

        List<String> recommendedFirstNames = null;
        try {
            recommendedFirstNames = dbHelper.getRecommendedTeacherFirstNames();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (String firstName : recommendedFirstNames) {
            firstNameGroup.getFilters().add(new Filter<>(firstName) {
                @Override
                public boolean test(Teacher teacher) {
                    return firstName.equalsIgnoreCase(teacher.getFname());
                }
            });
        }

        List<String> recommendedLastNames = null;
        try {
            recommendedLastNames = dbHelper.getRecommendedTeacherLastNames();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (String lastName : recommendedLastNames) {
            lastNameGroup.getFilters().add(new Filter<>(lastName) {
                @Override
                public boolean test(Teacher teacher) {
                    return lastName.equalsIgnoreCase(teacher.getLname());
                }
            });
        }

        genderGroup.getFilters().add(new Filter<>("Male") {
            @Override
            public boolean test(Teacher teacher) {
                return "male".equalsIgnoreCase(String.valueOf(teacher.getGender()));
            }
        });
        genderGroup.getFilters().add(new Filter<>("Female") {
            @Override
            public boolean test(Teacher teacher) {
                return "female".equalsIgnoreCase(String.valueOf(teacher.getGender()));
            }
        });

        filterView.getFilterGroups().setAll(firstNameGroup, lastNameGroup, genderGroup);

        loadTeachersFromDatabase();

        SortedList<Teacher> sortedList = new SortedList<>(filterView.getFilteredItems());
        tbData.setItems(sortedList);
        sortedList.comparatorProperty().bind(tbData.comparatorProperty());
    }

    private void initTable() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("fname"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lname"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        gender.setCellValueFactory(celldata -> {
            if (celldata.getValue().getGender()) {
                return new SimpleStringProperty("male");
            } else {
                return new SimpleStringProperty("female");
            }
        });
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    private void loadDrawer() {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/com/marrok/schoolmanagermvn/views/NavDrawer.fxml"));
            drawer.setSidePane(box);
            drawer.setMinWidth(0);

        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
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

    public void loadTeachersFromDatabase() {
        try {
            teachersModels = dbHelper.getTeachers();
         for (Teacher teacher : teachersModels) {
             System.out.println(teacher);
         }
            filterView.getItems().setAll(teachersModels);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }
}
