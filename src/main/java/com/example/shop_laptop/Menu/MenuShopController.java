package com.example.shop_laptop.Menu;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MenuShopController {
    @FXML
    private Label welcomeLabel;

    public void setUsername(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }
}
