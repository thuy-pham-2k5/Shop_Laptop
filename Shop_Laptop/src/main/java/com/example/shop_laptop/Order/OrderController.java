package com.example.shop_laptop.Order;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class OrderController {
    @FXML
    private void GoToAccount(ActionEvent event) {
        System.out.println("Navigating to Account...");
        changeScene(event, "/com/example/shop_laptop/account.fxml");
    }
    @FXML
    private void Customer(ActionEvent event) {
        System.out.println("Navigating to Account...");
        changeScene(event, "/com/example/shop_laptop/Customer.fxml");
    }

    @FXML
    private void logout(ActionEvent event) {
        System.out.println("Logging out...");
        changeScene(event, "/com/example/shop_laptop/LoginScene.fxml");
    }
    @FXML
    public void listProduct(ActionEvent event) {
        changeScene(event, "/com/example/shop_laptop/ProductAdmin.fxml");
    }
    @FXML
    public void hoadon(ActionEvent event) {
        changeScene(event, "/com/example/shop_laptop/Staff-view.fxml");
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
}
