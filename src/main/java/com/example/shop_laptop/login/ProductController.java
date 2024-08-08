package com.example.shop_laptop.login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class ProductController {
    @FXML
    private TableView<LaptopProduct> productTable;
    @FXML
    private TableColumn<LaptopProduct, String> nameColumn;
    @FXML
    private TableColumn<LaptopProduct, Double> priceColumn;
    @FXML
    private TableColumn<LaptopProduct, Integer> quantityColumn;
    @FXML
    private TableColumn<LaptopProduct, Void> actionColumn;

    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;

    private ObservableList<LaptopProduct> productList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Khởi tạo cột bảng và các xử lý khác
        // ...
    }

    @FXML
    private void handleAddProduct() {
        // Xử lý thêm sản phẩm
        // ...
    }

    // Các phương thức xử lý sự kiện khác
}
