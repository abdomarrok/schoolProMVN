package com.marrok.schoolmanagermvn.controllers.student;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.marrok.schoolmanagermvn.controllers.dashboard.DashboardController;
import com.marrok.schoolmanagermvn.model.Student;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dlsc.gemsfx.FilterView;
import com.dlsc.gemsfx.FilterView.Filter;
import com.dlsc.gemsfx.FilterView.FilterGroup;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StudentsController implements Initializable {

    public TableView<Student> tbData;
    public TableColumn<Student, String> gender;
    public TableColumn<Student, Integer> contact;
    public TableColumn<Student, Integer> year;
    public TableColumn<Student, Integer> id;
    public TableColumn<Student, String> firstName;
    public TableColumn<Student, String> lastName;
    public JFXDrawer drawer;
    public JFXHamburger hamburger;
    public FilterView<Student> filterView;

    private ObservableList<Student> studentsModels = FXCollections.observableArrayList();
    private DatabaseHelper dbHelper;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            dbHelper = new DatabaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        loadFilter(); // Setup filters and data before initializing the table
        initTable();  // Initialize the table after filters are set up
        loadDrawer();
        CSSFX.start();
    }

    @FXML
    private void addStudent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/schoolmanagermvn/views/student/add.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Student");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/schoolmanagermvn/img/lg.png")));
            AddController controller = loader.getController();
            controller.setController(this);

            stage.showAndWait();
            // Refresh the table data after adding a student
            loadStudentsFromDatabase();
        } catch (IOException e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not open the add student form.");
            e.printStackTrace();
        }
    }


    @FXML
    private void updateStudent(ActionEvent event) {
        Student selectedStudent = tbData.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/schoolmanagermvn/views/student/update.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Update Student");
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/schoolmanagermvn/img/lg.png")));
                UpdateController controller = loader.getController();
                controller.setStudent(selectedStudent);
                controller.setController(this);

                stage.showAndWait();
                // Refresh the table data after updating the student
                loadStudentsFromDatabase();
            } catch (IOException e) {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not open the update student form.");
                e.printStackTrace();
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "No Student SelectedPlease select a student to update.");
        }
    }


    @FXML
    private void deleteStudent(ActionEvent event) {
        // Get the selected student from the TableView
        Student selectedStudent = tbData.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            // No student is selected
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student to delete.");
            return;
        }

        // Confirm deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected student?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // Proceed with deletion
            boolean success = dbHelper.deleteStudent(selectedStudent.getId());

            if (success) {
                // Refresh the table
                loadStudentsFromDatabase();
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", "The student was deleted successfully.");
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Failed to delete the student.");
            }
        }
    }


    private void loadFilter() {
        filterView.getFilterGroups().clear();
        // Enhanced text filter based on multiple fields
        filterView.setTextFilterProvider(text -> student -> {
            if (text == null || text.isEmpty()) {
                return true; // Show all students if no text filter is applied
            }
            String lowerCaseText = text.toLowerCase();
            return student.getFname().toLowerCase().contains(lowerCaseText) ||
                    student.getLname().toLowerCase().contains(lowerCaseText) ||
                    String.valueOf(student.getContact()).contains(lowerCaseText) ||
                    (student.getGender() ? "male".contains(lowerCaseText) : "female".contains(lowerCaseText));
        });

        // Filter groups as previously defined
        FilterGroup<Student> firstNameGroup = new FilterGroup<>("First Name");
        FilterGroup<Student> lastNameGroup = new FilterGroup<>("Last Name");
        FilterGroup<Student> classroomGroup = new FilterGroup<>("Classroom");

        FilterGroup<Student> genderGroup = new FilterGroup<>("Gender");

        List<String> recommendedFirstNames = dbHelper.getRecommendedFirstNames();
        for (String firstName : recommendedFirstNames) {
            firstNameGroup.getFilters().add(new Filter<>(firstName) {
                @Override
                public boolean test(Student student) {
                    return firstName.equalsIgnoreCase(student.getFname());
                }
            });
        }

        List<String> recommendedLastNames = dbHelper.getRecommendedLastNames();
        for (String lastName : recommendedLastNames) {
            lastNameGroup.getFilters().add(new Filter<>(lastName) {
                @Override
                public boolean test(Student student) {
                    return lastName.equalsIgnoreCase(student.getLname());
                }
            });
        }




        genderGroup.getFilters().add(new Filter<>("Male") {
            @Override
            public boolean test(Student student) {
                return student.getGender();
            }
        });
        genderGroup.getFilters().add(new Filter<>("Female") {
            @Override
            public boolean test(Student student) {
                return !student.getGender();
            }
        });

        filterView.getFilterGroups().setAll(firstNameGroup, lastNameGroup, classroomGroup, genderGroup);

        loadStudentsFromDatabase();

        SortedList<Student> sortedList = new SortedList<>(filterView.getFilteredItems());
        tbData.setItems(sortedList);
        sortedList.comparatorProperty().bind(tbData.comparatorProperty());
    }

    private void initTable() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("fname"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lname"));
        gender.setCellValueFactory(celldata -> {
            if (celldata.getValue().getGender()) {
                return new SimpleStringProperty("male");
            } else {
                return new SimpleStringProperty("female");
            }
        });
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        year.setCellValueFactory(new PropertyValueFactory<>("year"));
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

    public void loadStudentsFromDatabase() {
        try {
            studentsModels = dbHelper.getStudents(); // Load students from the database
            filterView.getItems().setAll(studentsModels); // Set the loaded students to the filter view
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }
}
