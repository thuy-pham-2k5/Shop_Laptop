package com.example.shop_laptop.signin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private ImageView logoImageView;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button showPasswordButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button resetButton; // Nút làm mới

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private VBox mainContainer;

    private boolean isPasswordVisible = false;
    private UserDataBase userDataBase;

    @FXML
    public void initialize() {
        passwordTextField.setVisible(false);
        passwordTextField.setPromptText("Password");

        showPasswordButton.setOnAction(e -> handleShowPasswordAction());

        if (forgotPasswordLink != null) {
            forgotPasswordLink.setOnAction(e -> handleForgotPasswordAction());
        }

        if (registerButton != null) {
            registerButton.setOnAction(e -> handleRegisterAction());
        }

        if (loginButton != null) {
            loginButton.setOnAction(e -> handleLoginAction());
        }

        if (resetButton != null) {
            resetButton.setOnAction(e -> handleResetAction()); // Thiết lập sự kiện cho nút làm mới
        }
    }

    public void setUserDataBase(UserDataBase userDataBase) {
        this.userDataBase = userDataBase;
    }

    @FXML
    protected void handleLoginAction() {
        String username = emailField.getText();
        String password = isPasswordVisible ? passwordTextField.getText() : passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Input Error", "Please enter both username and password");
            return;
        }

        if (validateLogin(username, password)) {
            showAlert("Login Successful", "You have logged in successfully.");
            loadMainScene();
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    private void handleShowPasswordAction() {
        if (isPasswordVisible) {
            passwordTextField.setVisible(false);
            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            showPasswordButton.setText("Show Password");
        } else {
            passwordField.setVisible(false);
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);
            showPasswordButton.setText("Hide Password");
        }
        isPasswordVisible = !isPasswordVisible;
    }

    @FXML
    protected void handleRegisterAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shop_laptop/RegisterScene.fxml"));
            AnchorPane registerRoot = loader.load();
            RegisterController registerController = loader.getController();
            registerController.setUserDataBase(userDataBase);
            Scene scene = new Scene(registerRoot);
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Cannot load the registration screen.");
        }
    }

    private boolean validateLogin(String username, String password) {
        return userDataBase != null && userDataBase.validateCredentials(username, password);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void loadMainScene() {
        try {
            // Tải cảnh từ FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shop_laptop/ProductAdmin.fxml"));
            VBox mainRoot = loader.load();

            // Tạo cảnh và thiết lập cho PrimaryStage
            Scene scene = new Scene(mainRoot);
            Stage stage = (Stage) loginButton.getScene().getWindow(); // Sử dụng loginButton thay vì someButton
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Cannot load the main screen. Check the console for details.");
        }
    }

    private void handleForgotPasswordAction() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Forgot Password");
        dialog.setHeaderText("Reset Your Password");
        VBox content = new VBox(10);
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter new password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm new password");
        content.getChildren().addAll(usernameField, newPasswordField, confirmPasswordField);
        dialog.getDialogPane().setContent(content);
        ButtonType resetButtonType = new ButtonType("Reset", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(resetButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == resetButtonType) {
                String username = usernameField.getText();
                String newPassword = newPasswordField.getText();
                String confirmPassword = confirmPasswordField.getText();

                if (username.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    showAlert("Input Error", "Please fill in all fields.");
                    return null;
                }

                if (!newPassword.equals(confirmPassword)) {
                    showAlert("Input Error", "Passwords do not match.");
                    return null;
                }

                if (userDataBase != null && userDataBase.userExists(username)) {
                    userDataBase.updatePassword(username, newPassword);
                    try {
                        userDataBase.saveUserData(); // Cập nhật để gọi phương thức không tham số
                    } catch (IOException e) {
                        e.printStackTrace();
                        showAlert("Error", "Failed to save new password.");
                    }
                    showAlert("Success", "Password reset successfully.");
                } else {
                    showAlert("Error", "User does not exist.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void handleResetAction() {
        // Làm mới các trường nhập liệu
        emailField.clear();
        passwordField.clear();
        passwordTextField.clear();

        // Đặt lại trạng thái hiển thị mật khẩu
        passwordTextField.setVisible(false);
        passwordField.setVisible(true);
        showPasswordButton.setText("Show Password");
    }
}
