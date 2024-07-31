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
import javafx.stage.StageStyle;

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
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (root != null) {
            Scene scene = new Scene(root);
            scene.setCursor(Cursor.HAND);

            // Create a new stage if a new style is needed
            Stage newStage = new Stage();
            newStage.initStyle(StageStyle.DECORATED); // Set style before showing
            newStage.setScene(scene);
            newStage.setResizable(true);
            newStage.centerOnScreen();

            newStage.show();

            // Optionally, close the current stage
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }



    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }
}