package com.marrok.schoolmanagermvn.controllers.student;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.marrok.schoolmanagermvn.controllers.dashboard.DashboardController;
import com.marrok.schoolmanagermvn.model.Student;
import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

import com.dlsc.gemsfx.FilterView;
import com.dlsc.gemsfx.FilterView.Filter;
import com.dlsc.gemsfx.FilterView.FilterGroup;
import fr.brouillard.oss.cssfx.CSSFX;

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

    private void loadFilter() {
        filterView.setTextFilterProvider(text -> student ->
                student.getFname().toLowerCase().contains(text.toLowerCase()) ||
                        student.getLname().toLowerCase().contains(text.toLowerCase())
        );

        FilterGroup<Student> firstNameGroup = new FilterGroup<>("First Name");
        FilterGroup<Student> lastNameGroup = new FilterGroup<>("Last Name");
        FilterGroup<Student> classroomGroup = new FilterGroup<>("Classroom");

        try {
            DatabaseHelper dbHelper = new DatabaseHelper();

            // Fetch recommended first names
            List<String> recommendedFirstNames = dbHelper.getRecommendedFirstNames();
            for (String firstName : recommendedFirstNames) {
                firstNameGroup.getFilters().add(new Filter<>(firstName) {
                    @Override
                    public boolean test(Student student) {
                        return firstName.equals(student.getFname());
                    }
                });
            }

            // Fetch recommended last names
            List<String> recommendedLastNames = dbHelper.getRecommendedLastNames();
            for (String lastName : recommendedLastNames) {
                lastNameGroup.getFilters().add(new Filter<>(lastName) {
                    @Override
                    public boolean test(Student student) {
                        return lastName.equals(student.getLname());
                    }
                });
            }

            // Fetch classrooms
            List<Integer> classrooms = dbHelper.getClassrooms();
            for (Integer classroom : classrooms) {
                classroomGroup.getFilters().add(new Filter<>("Classroom " + classroom) {
                    @Override
                    public boolean test(Student student) {
                        return student.getClassRooms() == classroom;
                    }
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        filterView.getFilterGroups().setAll(firstNameGroup, lastNameGroup, classroomGroup);

        loadStudentsFromDatabase(); // Load students from the database

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

    private void loadStudentsFromDatabase() {
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
