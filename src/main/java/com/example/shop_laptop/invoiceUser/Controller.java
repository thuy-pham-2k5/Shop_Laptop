package com.example.shop_laptop.invoiceUser;

import com.example.shop_laptop.productAdmin.Product;
import com.example.shop_laptop.productUser.AddProductController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static final String FILE_ORDER = "D:\\Shop_Laptop\\src\\main\\java\\com\\example\\shop_laptop\\text\\order.txt";
    private static final String FXML_AddInvoiceUser = "/com/example/shop_laptop/AddInvoiceUser.fxml";

    @FXML
    private VBox vBox;

    @FXML
    public void initialize() {
        loadProducts();
    }

    private void loadProducts() {
        List<Product> products = readProductsFromFileOrder(FILE_ORDER);
        for (Product product : products) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_AddInvoiceUser));
                Pane addProductUserPane = loader.load();

                // Lấy controller của AddProductUser.fxml
                AddProductController addProductController = loader.getController();
                addProductController.setProduct(product);

                vBox.getChildren().add(addProductUserPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Product> readProductsFromFileOrder(String filePath) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_ORDER))) {
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
