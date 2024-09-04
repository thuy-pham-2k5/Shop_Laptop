package com.example.shop_laptop.Customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AddCustomerController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;

    private CustomerController customerController;

    public void setCustomerController(CustomerController controller) {
        this.customerController = controller;
    }

    @FXML
    public void addCustomer(ActionEvent event) {
        if (validateFields()) {
            String customerData = String.join(":",
                    usernameField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    emailField.getText(),
                    phoneField.getText());

            writeToFile("users.txt", customerData); // Write data to file

            // Update TableView in CustomerController
            if (customerController != null) {
                customerController.loadData(); // Refresh data in TableView
            }

            closeStage(); // Close the add customer window
        }
    }

    private void writeToFile(String fileName, String data) {
        try (FileWriter fw = new FileWriter(fileName, true); // true to append data
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(data); // Write data to file
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception
            showAlert("Error", "An error occurred while saving data: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                nameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                phoneField.getText().isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return false;
        }

        if (!isValidEmail(emailField.getText())) {
            showAlert("Error", "Email must be in a valid format.");
            return false;
        }

        if (!isValidPhone(phoneField.getText())) {
            showAlert("Error", "Phone number must start with 0, be exactly 10 digits long, and contain no letters.");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        // Improved email regex to cover more valid email formats
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("0\\d{9}"); // Starts with 0 and exactly 10 digits long
    }

    private void closeStage() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
