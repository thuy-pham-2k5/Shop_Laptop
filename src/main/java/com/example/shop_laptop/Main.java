package com.example.shop_laptop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            Main.primaryStage = primaryStage;
            String fxmlPath = "D:\\CODEGYM\\ProjectCodegym\\OOP2\\Downloads\\Shop_Laptop\\src\\main\\resources\\com\\example\\shop_laptop\\ProductAdmin.fxml";
            Parent root = FXMLLoader.load(new File(fxmlPath).toURI().toURL());
            root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
            primaryStage.setTitle("4P Tech Store");
            primaryStage.setScene(new Scene(root, 1280, 800));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeScene(String fxml) throws Exception {
        Parent pane = FXMLLoader.load(Main.class.getResource(fxml));
        primaryStage.setScene(new Scene(pane, 760, 600));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
