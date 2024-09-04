package com.example.shop_laptop.signin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private UserDataBase userDataBase = new UserDataBase();

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load dữ liệu người dùng từ tệp khi khởi động ứng dụng
        userDataBase.loadUserData(); // Không truyền tham số

        // Tải giao diện đăng nhập
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shop_laptop/LoginScene.fxml"));
        Scene scene = new Scene(loader.load());

        // Truyền userDataBase vào LoginController
        LoginController loginController = loader.getController();
        loginController.setUserDataBase(userDataBase);

        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
