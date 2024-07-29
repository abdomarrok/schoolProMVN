package com.marrok.schoolmanagermvn;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println(Main.class.getResource("/com/marrok/schoolmanagermvn/views/signin/signin.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/marrok/schoolmanagermvn/views/signin/signin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        System.out.println(Main.class.getResourceAsStream("/com/marrok/schoolmanagermvn/img/lg.png"));
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/com/marrok/schoolmanagermvn/img/lg.png"))));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("SchoolPro");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
