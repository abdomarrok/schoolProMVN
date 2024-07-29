package com.marrok.schoolmanagermvn.controllers.student;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.marrok.schoolmanagermvn.controllers.dashboard.DashboardController;
import com.marrok.schoolmanagermvn.model.Student;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dlsc.gemsfx.FilterView;
import com.dlsc.gemsfx.FilterView.Filter;
import com.dlsc.gemsfx.FilterView.FilterGroup;
import fr.brouillard.oss.cssfx.CSSFX;

public class StudentsController implements Initializable {

    public TableView<Student> tbData;
    public TableColumn<Student, Boolean> gender;
    public TableColumn<Student, Integer> contact;
    public TableColumn<Student, Integer> year;
    public TableColumn<Student, Integer> id;
    public TableColumn<Student, String> firstName;
    public TableColumn<Student, String> lastName;
    public JFXDrawer drawer;
    public JFXHamburger hamburger;
    public FilterView<Student> filterView;

    private ObservableList<Student> studentsModels = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFilter(); // Setup filters and data before initializing the table
        initTable();// Initialize the table after filters are set up
        loadDrawer();
        CSSFX.start();
    }

    private void loadFilter() {
        filterView.setTitle("Filter Students");
        filterView.setSubtitle("Use the filters below to narrow down the list of students");
        filterView.setTextFilterProvider(text -> student ->
                student.getFname().toLowerCase().contains(text.toLowerCase()) ||
                        student.getLname().toLowerCase().contains(text.toLowerCase())
        );

        FilterGroup<Student> firstNameGroup = new FilterGroup<>("First Name");
        FilterGroup<Student> lastNameGroup = new FilterGroup<>("Last Name");
        FilterGroup<Student> classroomGroup = new FilterGroup<>("Classroom");

        firstNameGroup.getFilters().addAll(
                new Filter<>("Steve or Jennifer") {
                    @Override
                    public boolean test(Student student) {
                        return "Steve".equals(student.getFname()) || "Jennifer".equals(student.getFname());
                    }
                },
                new Filter<>("Paul or Eric") {
                    @Override
                    public boolean test(Student student) {
                        return "Paul".equals(student.getFname()) || "Eric".equals(student.getFname());
                    }
                },
                new Filter<>("Elizabeth") {
                    @Override
                    public boolean test(Student student) {
                        return "Elizabeth".equals(student.getFname());
                    }
                }
        );

        lastNameGroup.getFilters().addAll(
                new Filter<>("Miller") {
                    @Override
                    public boolean test(Student student) {
                        return "Miller".equals(student.getLname());
                    }
                },
                new Filter<>("Smith") {
                    @Override
                    public boolean test(Student student) {
                        return "Smith".equals(student.getLname());
                    }
                }
        );

        classroomGroup.getFilters().addAll(
                new Filter<>("Classroom 1") {
                    @Override
                    public boolean test(Student student) {
                        return student.getClassRooms() == 1;
                    }
                },
                new Filter<>("Classroom 2") {
                    @Override
                    public boolean test(Student student) {
                        return student.getClassRooms() == 2;
                    }
                }
        );

        filterView.getFilterGroups().setAll(firstNameGroup, lastNameGroup, classroomGroup);

        // Adding example data
        studentsModels.addAll(
                new Student(1, "Amos", "Chepchieng", 2004, 555555, true, 1),
                new Student(2, "Steve", "Miller", 2003, 555556, true, 2),
                new Student(3, "Jennifer", "Smith", 2002, 555557, false, 1),
                new Student(4, "Paul", "Jones", 2001, 555558, true, 2),
                new Student(5, "Elizabeth", "Brown", 2000, 555559, false, 1)
        );

        filterView.getItems().setAll(studentsModels);

        SortedList<Student> sortedList = new SortedList<>(filterView.getFilteredItems());
        tbData.setItems(sortedList);
        sortedList.comparatorProperty().bind(tbData.comparatorProperty());
    }

    private void initTable() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("fname"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lname"));
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        year.setCellValueFactory(new PropertyValueFactory<>("year"));

        // Binding the table data should already be done in loadFilter()
        // So no need to set tbData.setItems here
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

    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }
}
