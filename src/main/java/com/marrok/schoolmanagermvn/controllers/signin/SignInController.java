package com.marrok.schoolmanagermvn.controllers.signin;

import com.marrok.schoolmanagermvn.util.DatabaseHelper;
import com.marrok.schoolmanagermvn.util.GeneralUtil;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignInController implements Initializable {

    @FXML
    private TextField userId;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;

    private Scene scene;
    private Parent root;
    public ToggleButton switch_them_btn0;
    private final Properties themeProperties = new Properties();
    @FXML
    private FontAwesomeIcon exit_btn;
    @FXML
    private AnchorPane background;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void processLogin(ActionEvent event) {
        String user = userId.getText();
        String pass = password.getText();

        try {
            DatabaseHelper dbHelper = new DatabaseHelper();
            boolean loginTest = dbHelper.validateLogin(user, pass);

            if (loginTest) {
                System.out.println("Login success");
                showDashboard(event);
            } else {
                System.out.println("Login failed");
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception, possibly by showing an alert to the user
        }
    }


    private void showDashboard(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/schoolmanagermvn/views/dashboard/dashboard.fxml"));
        try {
            root = loader.load();

        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }

        scene = new Scene(root);
        scene.setCursor(Cursor.HAND);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        stage.setResizable(false);
        stage.centerOnScreen();
       // stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }


    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }
}