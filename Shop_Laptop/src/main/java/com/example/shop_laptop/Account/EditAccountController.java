package com.example.shop_laptop.Account;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.function.UnaryOperator;

public class EditAccountController {

    @FXML
    private TextField hoTenInput;
    @FXML
    private TextField soDienThoaiInput;
    @FXML
    private TextField emailInput;
    @FXML
    private TextField diaChiInput;

    @FXML
    public void initialize() {
        // Apply numeric filter to soDienThoaiInput with max length 10
        UnaryOperator<TextFormatter.Change> numericFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && newText.length() <= 10) { // Allow only digits and limit length to 10
                return change;
            }
            return null; // Reject the change
        };
        soDienThoaiInput.setTextFormatter(new TextFormatter<>(numericFilter));
    }

    public void setAccountData(String hoTen, String soDienThoai, String email, String diaChi) {
        hoTenInput.setText(hoTen);
        soDienThoaiInput.setText(soDienThoai);
        emailInput.setText(email);
        diaChiInput.setText(diaChi);
    }

    public String[] getUpdatedData() {
        return new String[]{
                hoTenInput.getText(),
                soDienThoaiInput.getText(),
                emailInput.getText(),
                diaChiInput.getText()
        };
    }

    @FXML
    private void saveChanges(ActionEvent event) {
        if (isValidData()) {
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }

    @FXML
    private void cancelChanges(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    private boolean isValidData() {
        String phoneNumber = soDienThoaiInput.getText();
        String email = emailInput.getText();
        String hoTen = hoTenInput.getText();
        String diaChi = diaChiInput.getText();

        // Validate phone number
        if (phoneNumber.length() != 10) {
            showAlert(AlertType.ERROR, "Validation Error", "Phone number must be exactly 10 digits.");
            return false;
        }

        // Validate email
        if (!email.contains("@")) {
            showAlert(AlertType.ERROR, "Validation Error", "Email must contain '@'.");
            return false;
        }

        // Validate name (hoTen) - must not contain numbers
        if (hoTen.matches(".*\\d.*")) {
            showAlert(AlertType.ERROR, "Validation Error", "Name must not contain numbers.");
            return false;
        }

        // Validate address (diaChi) - must not contain numbers
        if (diaChi.matches(".*\\d.*")) {
            showAlert(AlertType.ERROR, "Validation Error", "Address must not contain numbers.");
            return false;
        }

        return true;
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
