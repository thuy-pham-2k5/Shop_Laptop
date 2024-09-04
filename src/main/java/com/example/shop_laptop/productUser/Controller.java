package com.example.shop_laptop.productUser;

import com.example.shop_laptop.productAdmin.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static final String FILE_PATH = "D:/CODEGYM/ProjectCodegym/OOP2/Downloads/Shop_Laptop/src/main/java/com/example/shop_laptop/text/product.txt";
    @FXML
    private FlowPane productA_flowpane;

    @FXML
    public void initialize() {
        loadProducts();
    }

    private void loadProducts() {
        List<Product> products = readProductsFromFile(FILE_PATH);
        for (Product product : products) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shop_laptop/AddProductUser.fxml"));
                Pane addProductUserPane = loader.load();

                // Lấy controller của AddProductUser.fxml
                AddProductController addProductController = loader.getController();
                addProductController.setProduct(product);

                productA_flowpane.getChildren().add(addProductUserPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Product> readProductsFromFile(String filePath) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                Product product = Product.fromString(line);
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }
}
