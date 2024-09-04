package com.example.shop_laptop.Cart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class CartController {

    @FXML
    private TableView<Product> productTableView; // Bảng hiển thị sản phẩm

    @FXML
    private TableColumn<Product, String> productName; // Cột tên sản phẩm

    @FXML
    private TableColumn<Product, Double> productPrice; // Cột giá sản phẩm

    @FXML
    private TableColumn<Product, Integer> productQuantity; // Cột số lượng sản phẩm

    @FXML
    private TableColumn<Product, Void> productRemove; // Cột nút xóa sản phẩm

    @FXML
    private Text totalAmount; // TextField hiển thị tổng giá trị giỏ hàng

    @FXML
    private TextField voucherField; // TextField nhập mã voucher

    @FXML
    private CheckBox selectAllCheckbox; // Checkbox chọn tất cả sản phẩm

    @FXML
    private Button checkoutButton; // Nút thanh toán

    @FXML
    private Button cancelButton; // Nút hủy bỏ

    @FXML
    private Button removeButton; // Nút xóa sản phẩm đã chọn

    private ObservableList<Product> cartItems = FXCollections.observableArrayList(); // Danh sách sản phẩm trong giỏ hàng
    @FXML
    private TableColumn<Product, Boolean> selectColumn; // Cột checkbox chọn sản phẩm


    @FXML
    public void initialize() {
        // Đặt chế độ chỉnh sửa cho TableView
        productTableView.setEditable(true);
        productTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Thiết lập cột cho TableView và kết nối với thuộc tính của Product
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Cấu hình cột số lượng để cho phép chỉnh sửa trực tiếp
        productQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        productQuantity.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setQuantity(event.getNewValue());
            updateTotalAmount(); // Cập nhật tổng tiền sau khi thay đổi số lượng
            saveProductsToFile("cart.txt"); // Lưu thay đổi vào tệp
        });

        // Cấu hình cho cột checkbox
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        // Thiết lập cột nút "Remove" để xóa sản phẩm
        productRemove.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Remove");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    deleteButton.setOnAction(event -> {
                        Product product = getTableView().getItems().get(getIndex());
                        cartItems.remove(product);
                        updateTotalAmount();
                        saveProductsToFile("cart.txt");
                    });
                    setGraphic(deleteButton);
                }
            }
        });

        // Gán danh sách sản phẩm vào TableView
        productTableView.setItems(cartItems);

        // Nạp dữ liệu từ tệp tin vào giỏ hàng
        loadProductsFromFile();

        // Thêm sự kiện cho checkbox "Chọn tất cả"
        selectAllCheckbox.setOnAction(event -> handleSelectAll());

        // Thêm listener cho thuộc tính selected của từng sản phẩm
        for (Product product : cartItems) {
            product.selectedProperty().addListener((observable, oldValue, newValue) -> {
                updateTotalAmount();
                // Cập nhật trạng thái của checkbox "Chọn tất cả"
                boolean allSelected = cartItems.stream().allMatch(Product::isSelected);
                selectAllCheckbox.setSelected(allSelected);
            });
        }
    }


    // Xử lý sự kiện khi nhấn checkbox "Select All"
    @FXML
    public void handleSelectAll() {
        boolean isSelected = selectAllCheckbox.isSelected();
        for (Product product : cartItems) {
            product.setSelected(isSelected);
        }
        productTableView.refresh(); // Cập nhật lại TableView
    }


    // Phương thức thêm sản phẩm vào giỏ hàng
    public void addProductToCart(String name, double price, int quantity) {
        Product product = new Product(name, price, quantity);
        cartItems.add(product);
        updateTotalAmount();
        saveProductsToFile("cart.txt");
    }

    @FXML
    public void removeSelectedProducts() {
        // Lấy tất cả các sản phẩm đã chọn trong TableView
        ObservableList<Product> selectedProducts = productTableView.getItems().stream()
                .filter(Product::isSelected) // Lọc sản phẩm đã chọn
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        // Xóa các sản phẩm đã chọn khỏi giỏ hàng
        cartItems.removeAll(selectedProducts);

        // Cập nhật số tiền tổng
        updateTotalAmount();

        // Lưu trạng thái của giỏ hàng vào tập tin
        saveProductsToFile("cart.txt");
    }


    // Phương thức tính và cập nhật tổng giá trị của các sản phẩm được chọn
    public void updateTotalAmount() {
        double total = 0;
        for (Product product : cartItems) {
            if (product.isSelected()) {
                total += product.getTotalPrice();
            }
        }
        totalAmount.setText(String.format("%.2f", total));
    }

    // xử lý nút thanh toán
    @FXML
    public void handleCheckout() {
        // Lấy tất cả các sản phẩm đã chọn trong TableView
        ObservableList<Product> selectedProducts = productTableView.getItems().stream()
                .filter(Product::isSelected) // Lọc sản phẩm đã chọn
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        if (selectedProducts.isEmpty()) {
            showAlert("No Selection", "Please select at least one product before checking out.");
        } else {
            // Hiển thị thông báo thanh toán thành công
            showAlert("Checkout Successful", "You have successfully checked out with a total amount of: " + totalAmount.getText() + " VND");

            // Ghi thông tin thanh toán vào file order.txt
            saveOrderToFile(selectedProducts);

            // Xóa các sản phẩm đã chọn khỏi giỏ hàng
            cartItems.removeAll(selectedProducts);

            // Cập nhật lại tổng số tiền
            updateTotalAmount();

            // Lưu trạng thái giỏ hàng vào cart.txt
            saveProductsToFile("cart.txt");
        }
    }

    private void saveOrderToFile(ObservableList<Product> selectedProducts) {
        // Định dạng ngày giờ hiện tại
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        // Ghi vào file order.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("order.txt", true))) {
            writer.write("Time: " + formattedDateTime);
            writer.newLine();
            writer.write("Product Name, Quantity, Unit Price");
            writer.newLine();

            // Ghi thông tin từng sản phẩm đã chọn
            for (Product product : selectedProducts) {
                writer.write(product.getName() + "," + product.getQuantity() + "," + product.getPrice());
                writer.newLine();
            }
            writer.write("Total Amount: " + totalAmount.getText() + " VND");
            writer.newLine();
            writer.write("--------------------------------------------------");
            writer.newLine();

        } catch (IOException e) {
            showAlert("Error Saving Order", "An error occurred while saving the order to the file.");
        }
    }

    // Xử lý sự kiện khi nhấn nút "Cancel"
    @FXML
    public void handleCancel() {
        cartItems.clear();
        updateTotalAmount();
        saveProductsToFile("cart.txt");
    }


    private void loadProductsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("cart.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] productData = line.split(",");
                if (productData.length == 3) {
                    try {
                        String name = productData[0].trim();
                        double price = Double.parseDouble(productData[1].trim());
                        int quantity = Integer.parseInt(productData[2].trim());
                        Product product = new Product(name, price, quantity);
                        cartItems.add(product);
                    } catch (NumberFormatException e) {
                        showAlert("Error Parsing Data", "An error occurred while parsing product data: " + line);
                    }
                } else {
                    showAlert("Invalid Data Format", "Invalid product data format: " + line);
                }
            }
        } catch (IOException e) {
            showAlert("Error Loading Products", "An error occurred while loading products from the file: " + e.getMessage());
        }
    }


    // Lưu danh sách sản phẩm vào tệp tin
    private void saveProductsToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Product product : cartItems) {
                writer.write(product.getName() + "," + product.getPrice() + "," + product.getQuantity());
                writer.newLine();
            }
        } catch (IOException e) {
            showAlert("Error Saving Products", "An error occurred while saving products to the file.");
        }
    }

    // Hiển thị thông báo cho người dùng
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
