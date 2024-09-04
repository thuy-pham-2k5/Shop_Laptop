package com.example.shop_laptop.Account;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;

public class AccountController {

    @FXML
    private Label hoTen;
    @FXML
    private Label soDienThoai;
    @FXML
    private Label email;
    @FXML
    private Label diaChi;

    private static final String ACCOUNT_FILE_PATH = "Account.txt";

    @FXML
    private void GoToAccount(ActionEvent event) {
        System.out.println("Navigating to Account...");
        changeScene(event, "/com/example/shop_laptop/account.fxml");
    }

    @FXML
    private void Customer(ActionEvent event) {
        System.out.println("Navigating to Customer...");
        changeScene(event, "/com/example/shop_laptop/Customer.fxml");
    }

    @FXML
    private void logout(ActionEvent event) {
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");

        // Wait for the user response
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            // Proceed with logout
            System.out.println("Logging out...");
            changeScene(event, "/com/example/shop_laptop/LoginScene.fxml");
        }
    }

    private void changeScene(ActionEvent event, String fxmlPath) {
        try {
            URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                System.err.println("Error: FXML file not found at " + fxmlPath);
                return;
            }
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            Stage stage = getStageFromEvent(event);

            if (stage == null) {
                System.err.println("Error: Stage is null. Unable to change scene.");
                return;
            }

            Scene scene = new Scene(root);
            URL cssResource = getClass().getResource("/com/example/shop_laptop/style.css");
            if (cssResource != null) {
                scene.getStylesheets().add(cssResource.toExternalForm());
            } else {
                System.err.println("Error: CSS file not found.");
            }
            stage.setScene(scene);
            stage.show();
            System.out.println("Successfully changed to scene: " + fxmlPath);
        } catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An error occurred while changing the scene: " + e.getMessage());
        }
    }

    private Stage getStageFromEvent(ActionEvent event) {
        if (event.getSource() instanceof MenuItem) {
            MenuItem source = (MenuItem) event.getSource();
            return (Stage) source.getParentPopup().getOwnerWindow();
        } else if (event.getSource() instanceof Button) {
            Button source = (Button) event.getSource();
            return (Stage) source.getScene().getWindow();
        } else {
            throw new IllegalArgumentException("Event source is not a MenuItem or Button.");
        }
    }

    public void initialize() {
        loadAccountData();
    }

    @FXML
    public void edit(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shop_laptop/EditAccount.fxml"));
            Parent editView = loader.load();

            EditAccountController editController = loader.getController();
            editController.setAccountData(hoTen.getText(), soDienThoai.getText(), email.getText(), diaChi.getText());

            Stage stage = new Stage();
            stage.setScene(new Scene(editView));
            stage.showAndWait();

            String[] updatedData = editController.getUpdatedData();
            hoTen.setText(updatedData[0]);
            soDienThoai.setText(updatedData[1]);
            email.setText(updatedData[2]);
            diaChi.setText(updatedData[3]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAccountData() {
        File file = new File(ACCOUNT_FILE_PATH);
        if (!file.exists()) {
            System.out.println("Account file not found. A new file will be created when saving.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ", 2);
                if (parts.length == 2) {
                    switch (parts[0]) {
                        case "Họ tên":
                            hoTen.setText(parts[1]);
                            break;
                        case "Số điện thoại":
                            soDienThoai.setText(parts[1]);
                            break;
                        case "Email":
                            email.setText(parts[1]);
                            break;
                        case "Địa chỉ":
                            diaChi.setText(parts[1]);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from account file: " + e.getMessage());
        }
    }

    public void saveAccountData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE_PATH))) {
            writer.write("Họ tên: " + hoTen.getText());
            writer.newLine();
            writer.write("Số điện thoại: " + soDienThoai.getText());
            writer.newLine();
            writer.write("Email: " + email.getText());
            writer.newLine();
            writer.write("Địa chỉ: " + diaChi.getText());
        } catch (IOException e) {
            System.err.println("Error writing to account file: " + e.getMessage());
        }
    }

    public void onClose() {
        saveAccountData();
        Platform.exit();
    }
}
