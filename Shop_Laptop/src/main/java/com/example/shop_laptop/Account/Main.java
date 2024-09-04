package com.example.shop_laptop.Account;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shop_laptop/Account.fxml"));
            Parent root = loader.load();

            primaryStage.setTitle("Quản lý Tài khoản");
            primaryStage.setScene(new Scene(root, 400, 300));
            primaryStage.setOnCloseRequest(event -> {
                AccountController controller = loader.getController();
                controller.onClose();
            });
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
