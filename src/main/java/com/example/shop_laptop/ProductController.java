package com.example.shop_laptop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
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
    private TableColumn<LaptopProduct, Void> actionColumn; // tùy chỉnh

    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;

    private ObservableList<LaptopProduct> productList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Khởi tạo cột bảng
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // tạo ô tùy chỉnh
        actionColumn.setCellFactory(new Callback<TableColumn<LaptopProduct, Void>, TableCell<LaptopProduct, Void>>() {
            @Override
            public TableCell<LaptopProduct, Void> call(TableColumn<LaptopProduct, Void> param) {
                return new TableCell<LaptopProduct, Void>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");
                    private final HBox hbox = new HBox(editButton, deleteButton);

                    {
                        editButton.setOnAction(e -> handleEdit(getTableRow().getItem()));
                        deleteButton.setOnAction(e -> handleDelete(getTableRow().getItem()));
                        hbox.setSpacing(10);
                        setGraphic(hbox);
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                            setGraphic(null);
                        } else {
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });

        // Đặt các mục vào bảng
        productTable.setItems(productList);
    }

    @FXML
    private void handleAddProduct() {
        try {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            LaptopProduct newProduct = new LaptopProduct(name, price, quantity);
            productList.add(newProduct);

           //Xóa nội dung trong các trường nhập sau khi thêm vào.
            nameField.clear();
            priceField.clear();
            quantityField.clear();
        } catch (NumberFormatException e) {
            // Xử lý đầu vào không hợp lệ
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private void handleEdit(LaptopProduct product) {
        // xử lý chỉnh sửa
        System.out.println("Editing: " + product.getName());
    }

    private void handleDelete(LaptopProduct product) {
        productList.remove(product);
    }
}
