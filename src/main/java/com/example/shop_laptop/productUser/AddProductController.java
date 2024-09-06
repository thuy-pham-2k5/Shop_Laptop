package com.example.shop_laptop.productUser;

import com.example.shop_laptop.productAdmin.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.*;

public class AddProductController {
    private static final String FILE_CART = "D:/Shop_Laptop/src/main/java/com/example/shop_laptop/text/cart.txt";

    @FXML
    private ImageView productA_image;

    @FXML
    private Label productA_name;

    @FXML
    private Label productA_price;

    @FXML
    private Button productA_add;

    private Product product;

    public void setProduct(Product product) {
        this.product = product;
        productA_image.setImage(product.getImage());
        productA_name.setText(product.getName());
        productA_price.setText(String.valueOf(product.getPrice()));
    }

    @FXML
    public void initialize() {
        productA_add.setOnAction(event -> addToCart());
    }

    private void addToCart() {
        if (product != null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_CART, true))) {
                bw.write(product.toString());
                bw.newLine();

                // Hiển thị thông báo thành công
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Thêm vào giỏ hàng thành công");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Product information is incomplete.");
        }
    }
}