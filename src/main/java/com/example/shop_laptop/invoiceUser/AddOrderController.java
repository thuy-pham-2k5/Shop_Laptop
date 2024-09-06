package com.example.shop_laptop.invoiceUser;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AddOrderController {
    private static final String FILE_ORDER = "D:\\Shop_Laptop\\src\\main\\java\\com\\example\\shop_laptop\\text\\order.txt";
    @FXML
    private Label label;

    @FXML
    public void initialize() {
        readFileAndSetLabel();
    }

    private void readFileAndSetLabel() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_ORDER))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        label.setText(content.toString());
    }
}
