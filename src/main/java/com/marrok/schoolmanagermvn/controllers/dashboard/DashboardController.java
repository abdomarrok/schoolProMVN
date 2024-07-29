package com.marrok.schoolmanagermvn.controllers.dashboard;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.marrok.schoolmanagermvn.model.Student;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController implements Initializable {

    public TableView<Student> tbData;
    public TableColumn<Student,String> gender;
    public TableColumn<Student, Integer> contact;
    public TableColumn<Student,Integer> year;
    public TableColumn<Student, Integer> id;
    public TableColumn<Student, String> firstName;
    public TableColumn<Student, String> lastName;

    public JFXDrawer drawer;
    public JFXHamburger hamburger;

    public PieChart pieChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDrawer();
        loadChart();
        loadStudents();
    }

    private void loadDrawer() {
        try {
            // TODO
            VBox box = FXMLLoader.load(getClass().getResource("/com/marrok/schoolmanagermvn/views/NavDrawer.fxml"));
            drawer.setSidePane(box);
            drawer.setMinWidth(0); // this is the new code added

        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
        task.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            drawer.toggle();
        });
        drawer.setOnDrawerOpening((event) -> {
            task.setRate(task.getRate() * -1);
            task.play();
            drawer.setMinWidth(220);
        });
        drawer.setOnDrawerClosed((event) -> {
            task.setRate(task.getRate() * -1);
            task.play();
            drawer.setMinWidth(0);
        });
    }

    private void loadChart()
    {

        PieChart.Data slice1 = new PieChart.Data("Classes", 213);
        PieChart.Data slice2 = new PieChart.Data("Attendance"  , 67);
        PieChart.Data slice3 = new PieChart.Data("Teachers" , 36);

        pieChart.getData().add(slice1);
        pieChart.getData().add(slice2);
        pieChart.getData().add(slice3);

    }



    private ObservableList<Student> studentsModels = FXCollections.observableArrayList(

            new Student(1,"Amos", "Chepchieng",2004,555555,true,5),
            new Student(2,"Amos2", "Chepchieng2",2005,555555,false,5),
            new Student(3,"Amos3", "Chepchieng3",2006,555555,true,5)
    );


    private void loadStudents()
    {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("fname"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lname"));
        gender.setCellValueFactory(celldata->{
           if( celldata.getValue().getGender()){
               return new SimpleStringProperty("male");
           }else {
               return  new SimpleStringProperty("female");
           }
        });
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        year.setCellValueFactory(new PropertyValueFactory<>("year"));
        tbData.setItems(studentsModels);
    }

    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }
}
