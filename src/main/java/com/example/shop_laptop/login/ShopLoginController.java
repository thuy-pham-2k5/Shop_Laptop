package com.example.shop_laptop.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ShopLoginController {
    @FXML
    private TextField userTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;
    private ArrayList<ShopAdmin> adminList = new ArrayList<>();

    public ShopLoginController() {
        // Dữ liệu cho admin
        adminList.add(new ShopAdmin("admin1", "password1"));
        adminList.add(new ShopAdmin("admin2", "password2"));
        adminList.add(new ShopAdmin("admin3", "password3"));
        adminList.add(new ShopAdmin("admin4", "password4"));
        adminList.add(new ShopAdmin("admin5", "password5"));
    }

    @FXML
    private void handleLogin() {
        try {
            String username = userTextField.getText();
            String password = passwordField.getText();

            // Kiểm tra thông tin đăng nhập
            boolean loginSuccessful = adminList.stream()
                    .anyMatch(admin -> admin.getUsername().equals(username) && admin.getPassword().equals(password));

            if (loginSuccessful) {
                messageLabel.setText("Login successful!");
                messageLabel.setStyle("-fx-text-fill: green;");

                // Chuyển sang trang menuShop sau khi đăng nhập thành công
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shop_laptop/menu-shop.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage(); // Tạo một cửa sổ mới
                stage.setScene(new Scene(root));
                stage.show();

                // Đóng cửa sổ đăng nhập sau khi chuyển qua trang menuShop
                Stage currentStage = (Stage) userTextField.getScene().getWindow();
                currentStage.close();
            } else {
                messageLabel.setText("Invalid username or password.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (IOException e) {
            // Xử lý ngoại lệ khi không thể chuyển giao diện
            messageLabel.setText("Error: Unable to load menuShop.");
            messageLabel.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }
}
