package com.example.shop_laptop.Cart;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Tải file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shop_laptop/cart-view.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("4P Tech Store - Giỏ Hàng");

            // Tạo Scene và thiết lập cho Stage
            Scene scene = new Scene(root);

            // Hiển thị cửa sổ ứng dụng
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); // Gọi phương thức launch() để khởi chạy ứng dụng
    }
}
