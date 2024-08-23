package com.marrok.schoolmanagermvn.controllers.dashboard;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.marrok.schoolmanagermvn.model.Student;
import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController implements Initializable {

    public TableView<Student> tbData;
    public TableColumn<Student, String> gender;
    public TableColumn<Student, Integer> contact;
    public TableColumn<Student, Integer> year;
    public TableColumn<Student, Integer> id;
    public TableColumn<Student, String> firstName;
    public TableColumn<Student, String> lastName;

    public JFXDrawer drawer;
    public JFXHamburger hamburger;

    public PieChart pieChart;

    private ObservableList<Student> studentsModels = FXCollections.observableArrayList();
    private DatabaseHelper dbHelper;
    @FXML
    private Label totalStudentsLabel;
    @FXML
    private Label totalInscription;
    @FXML
    private Label totalClasses;
    @FXML
    private Label totalTeacher;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            dbHelper = new DatabaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        loadDrawer();
        loadChart();
        loadStudents(); // Load students from the database
        updateTotalsCount();
    }

    private void loadDrawer() {
        try {
            // Load the expanded and collapsed views
            VBox expandedBox = FXMLLoader.load(getClass().getResource("/com/marrok/schoolmanagermvn/views/NavDrawer.fxml"));
            VBox collapsedBox = FXMLLoader.load(getClass().getResource("/com/marrok/schoolmanagermvn/views/NavDrawersmall.fxml"));

            // Set the initial content and size
            drawer.setSidePane(collapsedBox); // Initially set to collapsed view
            drawer.setPrefWidth(60); // Small width for collapsed state
            drawer.setMinWidth(60); // Ensure minimum width to accommodate the collapsed view

            // Hamburger transition
            HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
            task.setRate(-1);
            hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> drawer.toggle());

            // Handle drawer opening and closing
            drawer.setOnDrawerOpening(event -> {
                task.setRate(task.getRate() * -1);
                task.play();
                drawer.setSidePane(expandedBox); // Set the expanded view
                drawer.setPrefWidth(220); // Full width when opening
            });

            drawer.setOnDrawerClosed(event -> {
                task.setRate(task.getRate() * -1);
                task.play();
                drawer.setSidePane(collapsedBox); // Set the collapsed view
                drawer.setPrefWidth(60); // Small width for collapsed state
            });

        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    private void loadChart() {
        try {
            int totalClasses = dbHelper.getTotalClasses();
            int totalTeachers = dbHelper.getTotalTeachers();

            // Clear existing data
            pieChart.getData().clear();

            // Add new data
            pieChart.getData().add(new PieChart.Data("Classes", totalClasses));
            pieChart.getData().add(new PieChart.Data("Teachers", totalTeachers));

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }
    private void loadStudents() {
        try {
            // Fetch recommended first names, last names, and classrooms
            List<String> recommendedFirstNames = dbHelper.getRecommendedFirstNames();
            List<String> recommendedLastNames = dbHelper.getRecommendedLastNames();
            List<Integer> classrooms = dbHelper.getClassrooms();

            // Example of how to use the recommended data (you can adjust as needed)
            for (String firstName : recommendedFirstNames) {
                // Do something with the recommended first names
            }

            for (String lastName : recommendedLastNames) {
                // Do something with the recommended last names
            }

            for (Integer classroom : classrooms) {
                // Do something with the classrooms
            }

            // Fetch students from the database
            studentsModels = dbHelper.getStudents();
            tbData.setItems(studentsModels);


        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }

        // Initialize table columns
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

    private void updateTotalsCount() {
        try {
            int totalclasses=dbHelper.getTotalClasses();
            int totalInscriptions=dbHelper.getTotalInscriptionCount();
            int totalStudents = dbHelper.getTotalStudentCount();
            int totalTeachers = dbHelper.getTotalTeachers();
            totalClasses.setText(String.valueOf(totalclasses));
            totalStudentsLabel.setText(String.valueOf(totalStudents));
            totalInscription.setText(String.valueOf(totalInscriptions));
            totalTeacher.setText(String.valueOf(totalTeachers));
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }
}
