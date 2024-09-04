package com.example.shop_laptop.Customer;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EditCustomerController {
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
    private String originalUsername;
    private CustomerController customerController;

    public void setCustomer(Customer customer) {
        usernameField.setText(customer.getUsername());
        passwordField.setText(customer.getPassword());
        nameField.setText(customer.getName());
        emailField.setText(customer.getEmail());
        phoneField.setText(customer.getPhone());
        this.originalUsername = customer.getUsername();
    }

    public void setCustomerController(CustomerController controller) {
        this.customerController = controller;
    }

    @FXML
    private void editCustomer() {
        if (validateFields()) {
            List<String> lines = readFromFile("users.txt");
            boolean updated = updateCustomerData(lines);
            if (updated) {
                writeToFile("users.txt", lines); // Ghi lại các dòng đã cập nhật vào file

                if (customerController != null) {
                    customerController.loadData(); // Làm mới dữ liệu trong TableView
                }

                closeStage();
            } else {
                showAlert("Lỗi", "Khách hàng không được tìm thấy.");
            }
        }
    }

    private boolean updateCustomerData(List<String> lines) {
        boolean updated = false;
        String updatedData = String.join(":",
                usernameField.getText(),
                passwordField.getText(),
                nameField.getText(),
                emailField.getText(),
                phoneField.getText()
        );

        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(":", 5);
            if (parts[0].equals(originalUsername)) {
                lines.set(i, updatedData);
                updated = true;
                break; // Thoát khỏi vòng lặp sau khi cập nhật
            }
        }
        return updated;
    }

    private List<String> readFromFile(String fileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Có lỗi xảy ra khi đọc thông tin khách hàng.");
        }
        return lines;
    }

    private void writeToFile(String fileName, List<String> lines) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Có lỗi xảy ra khi lưu thông tin khách hàng: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                nameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                phoneField.getText().isEmpty()) {
            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.");
            return false;
        }

        if (!isValidEmail(emailField.getText())) {
            showAlert("Lỗi", "Email phải có định dạng @gmail.com.");
            return false;
        }

        if (!isValidPhone(phoneField.getText())) {
            showAlert("Lỗi", "Số điện thoại phải bắt đầu bằng số 0, tối đa 10 số và không có chữ.");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return email.endsWith("@gmail.com");
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("0\\d{9}"); // Bắt đầu bằng 0 và có đúng 10 chữ số
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
