package com.example.shop_laptop.signin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button registerButton;
    @FXML
    private Button backButton;
    @FXML
    private VBox mainContainer;

    private UserDataBase userDataBase;

    @FXML
    public void initialize() {
        // Giới hạn "Full Name" chỉ cho phép chữ cái
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) {
                nameField.setText(oldValue);
            }
        });

        // Giới hạn "Phone" chỉ cho phép nhập số và bắt đầu bằng "0", tối đa 10 chữ số
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Xóa ký tự không phải số
            String filteredValue = newValue.replaceAll("[^0-9]", "");

            // Đảm bảo số điện thoại bắt đầu bằng 0 và có tối đa 10 chữ số
            if (filteredValue.matches("0\\d{0,9}")) {
                phoneField.setText(filteredValue);
            } else {
                phoneField.setText(oldValue);
            }
        });

        registerButton.setOnAction(e -> handleRegisterAction());
        backButton.setOnAction(e -> handleBackAction());
    }

    public void setUserDataBase(UserDataBase userDataBase) {
        this.userDataBase = userDataBase;
    }

    @FXML
    private void handleRegisterAction() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Input Error", "Vui lòng điền vào tất cả các trường.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Password Mismatch", "Mật khẩu và xác nhận không khớp.");
            return;
        }

        if (!email.matches("^[\\w-]+@([\\w-]+\\.)+[a-zA-Z]{2,7}$")) {
            showAlert("Invalid Email", "Vui lòng nhập địa chỉ email hợp lệ.");
            return;
        }

        if (!phone.matches("0\\d{9}")) {  // Số điện thoại phải có đúng 10 chữ số và bắt đầu bằng 0
            showAlert("Invalid Phone Number", "Số điện thoại phải có chính xác 10 chữ số và bắt đầu bằng số 0.");
            return;
        }

        // Check if username already exists
        if (userDataBase != null && userDataBase.userExists(username)) {
            showAlert("Username Exists", "Tên người dùng đã tồn tại. Vui lòng chọn một cái khác.");
            return;
        }

        // Save the new user data
        if (userDataBase != null) {
            User newUser = new User(username, password, name, email, phone);
            userDataBase.addUser(newUser);
            try {
                userDataBase.saveUserData();  // Lưu tất cả người dùng
                showAlert("Đăng ký thành công", "Tạo tài khoản thành công.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Không thể lưu dữ liệu người dùng. Kiểm tra bảng điều khiển để biết chi tiết.");
            }
        } else {
            showAlert("Error", "Cơ sở dữ liệu người dùng chưa được khởi tạo.");
        }
    }

    @FXML
    private void handleBackAction() {
        try {
            // Tải cảnh đăng nhập từ FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shop_laptop/LoginScene.fxml"));
            AnchorPane loginRoot = loader.load();

            // Lấy controller và thiết lập UserDataBase
            LoginController loginController = loader.getController();
            loginController.setUserDataBase(userDataBase);

            // Tạo cảnh và thiết lập cho PrimaryStage
            Scene scene = new Scene(loginRoot);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Không thể tải màn hình đăng nhập. Kiểm tra bảng điều khiển để biết chi tiết.");
        }
    }

    private void showAlert(String title, String message) {
        Alert.AlertType alertType = title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
