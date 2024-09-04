package com.example.shop_laptop.productAdmin;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product, Integer> productID;
    @FXML
    private TableColumn<Product, Image> productImage;
    @FXML
    private TableColumn<Product, String> productName;
    @FXML
    private TableColumn<Product, String> productType;
    @FXML
    private TableColumn<Product, Double> productPrice;
    @FXML
    private TableColumn<Product, Integer> productQuantity;
    @FXML
    private TableColumn<Product, Double> productTotal;

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;
    @FXML
    private ImageView product_imageView;
    @FXML
    private Button product_importBtn;
    @FXML
    private Button product_addBtn;
    @FXML
    private Button product_updateBtn;
    @FXML
    private Button product_clearBtn;
    @FXML
    private Button product_deleteBtn;
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final FileChooser fileChooser = new FileChooser();
    private Image image;
    
    
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

    @FXML
    private void initialize() {
        // Initialize table columns
        productID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        productImage.setCellValueFactory(cellData -> cellData.getValue().imageProperty());
        productName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        productType.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        productPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        productQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        productTotal.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());

        productTableView.setItems(products);

        // Configure file chooser for image
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        // Set button actions
        product_importBtn.setOnAction(e -> handleImportImage());
        product_addBtn.setOnAction(e -> handleAddProduct());
        product_updateBtn.setOnAction(e -> handleUpdateProduct());
        product_clearBtn.setOnAction(e -> handleClearFields());
        product_deleteBtn.setOnAction(e -> handleDeleteProduct());

        // Initialize image view and text fields (if needed)
        product_imageView.setImage(null); // Initialize with no image

        // Add TextFormatter to fields
        idField.setTextFormatter(createIntegerFormatter());
        priceField.setTextFormatter(createDoubleFormatter());
        quantityField.setTextFormatter(createIntegerFormatter());

        // Add listeners to update total when price or quantity changes
        addFieldListeners();

        // Initialize the typeComboBox with string values
        typeComboBox.getItems().addAll("MacBook", "Gaming Laptop", "2-in-1 Laptop", "Ultrabook", "Chromebook", "Netbook", "Workstation Laptop", "Business Laptop");

        // MacBook, Gaming Laptop, 2-in-1 Laptop, Ultrabook, Chromebook, Netbook, Workstation Laptop, Business Laptop

        // Cấu hình cột hình ảnh
        setupImageColumn();
        List<Product> loadedProducts = loadProductsFromFile();
        products.addAll(loadedProducts);

        // Add listener to select product and populate fields
        productTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> populateFields(newValue)
        );
    }
    

    private void populateFields(Product product) {
        if (product != null) {
            idField.setText(String.valueOf(product.getId()));
            nameField.setText(product.getName());
            typeComboBox.setValue(product.getType());
            priceField.setText(String.valueOf(product.getPrice()));
            quantityField.setText(String.valueOf(product.getQuantity()));
            product_imageView.setImage(product.getImage());
            image = product.getImage(); // Update the image reference
        }
    }

    private void setupImageColumn() {
        productImage.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Product, Image> call(TableColumn<Product, Image> column) {
                return new TableCell<>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(Image image, boolean empty) {
                        super.updateItem(image, empty);
                        if (empty || image == null) {
                            setGraphic(null);
                        } else {
                            imageView.setImage(image);
                            imageView.setFitWidth(100); // Đặt kích thước ảnh phù hợp
                            imageView.setPreserveRatio(true); // Giữ tỷ lệ ảnh
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });
    }

    private TextFormatter<Integer> createIntegerFormatter() {
        return new TextFormatter<>(new IntegerStringConverter(), 0, change -> {
            if (change.getControlNewText().isEmpty()) {
                return change; // Allow empty input
            }
            try {
                Integer.parseInt(change.getControlNewText());
                return change; // Valid integer input
            } catch (NumberFormatException e) {
                return null; // Invalid input
            }
        });
    }

    private TextFormatter<Double> createDoubleFormatter() {
        return new TextFormatter<>(new DoubleStringConverter(), 0.0, change -> {
            if (change.getControlNewText().isEmpty()) {
                return change; // Allow empty input
            }
            try {
                Double.parseDouble(change.getControlNewText());
                return change; // Valid double input
            } catch (NumberFormatException e) {
                return null; // Invalid input
            }
        });
    }

    private void addFieldListeners() {
        // Listener cho priceField và quantityField
        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
            updateTotalColumn();
        };

        priceField.textProperty().addListener(listener);
        quantityField.textProperty().addListener(listener);
    }

    private void updateTotalColumn() {
        // Lấy tất cả các sản phẩm trong bảng và cập nhật giá trị total
        for (Product product : products) {
            product.setTotal(product.getPrice() * product.getQuantity());
        }
        productTableView.refresh(); // Refresh the table view to reflect updates
    }
    @FXML
    private void handleImportImage() {
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                image = new Image(file.toURI().toString());
                product_imageView.setImage(image);

                // Lưu đường dẫn ảnh vào đối tượng Product
                // Lưu ý: Chỉ áp dụng khi thêm sản phẩm mới hoặc cập nhật sản phẩm hiện tại
                Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {
                    selectedProduct.setImage(image);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleAddProduct() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String type = typeComboBox.getValue();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            double total = price * quantity; // Tính toán total

            Product newProduct = new Product(id, image, name, type, price, quantity, total);
            products.add(newProduct);
            saveToFile();
            clearFields();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for product fields.");
        }
    }

    private void clearFields() {
    }

    @FXML
    private void handleUpdateProduct() {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String type = typeComboBox.getValue();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                double total = price * quantity; // Tính toán total

                selectedProduct.setId(id);
                selectedProduct.setImage(image);
                selectedProduct.setName(name);
                selectedProduct.setType(type);
                selectedProduct.setPrice(price);
                selectedProduct.setQuantity(quantity);
                selectedProduct.setTotal(total); // Cập nhật total

                productTableView.refresh(); // Refresh the table view to reflect updates
                saveToFile(); // Optionally update the file
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for product fields.");
            }
        }
    }

    @FXML
    private void handleClearFields() {
        idField.clear();
        nameField.clear();
        typeComboBox.getSelectionModel().clearSelection(); // Clear ComboBox selection
        priceField.clear();
        quantityField.clear();
        product_imageView.setImage(null);
        image = null; // Reset image
    }

    @FXML
    private void handleDeleteProduct() {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            products.remove(selectedProduct);
            // Optionally remove from file
        }
    }

    private static final String FILE_PATH = "D:/CODEGYM/ProjectCodegym/OOP2/Downloads/Shop_Laptop/src/main/java/com/example/shop_laptop/text/product.txt";

    private void saveToFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (Product product : products) {
                writer.write(product.toString() + System.lineSeparator());
            }
            writer.flush(); // Đảm bảo dữ liệu được ghi ra tệp
        } catch (IOException e) {
            showError("Không thể lưu sản phẩm: " + e.getMessage());
        }
    }

    private void showError(String s) {
        // Hiển thị lỗi (có thể sử dụng Alert trong JavaFX)
        System.err.println(s);
    }

    private List<Product> loadProductsFromFile() {
        List<Product> productList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Đảm bảo đúng số lượng phần tử trong mỗi dòng dữ liệu
                if (parts.length == 7) {
                    int id = Integer.parseInt(parts[0].trim());
                    Image image = new Image(parts[1]);
                    String name = parts[2].trim();
                    String type = parts[3].trim();
                    double price = Double.parseDouble(parts[4].trim());
                    int quantity = Integer.parseInt(parts[5].trim());
                    double total = Double.parseDouble(parts[6].trim());

                    // Tạo đối tượng Product với total được tính tự động từ price và quantity
                    productList.add(new Product(id, image, name, type, price, quantity, total));
                }
            }
        } catch (FileNotFoundException e) {
            showError("Tệp không tìm thấy: " + e.getMessage());
        } catch (IOException e) {
            showError("Lỗi đọc tệp: " + e.getMessage());
        }
        return productList;
    }

    public void productList(ActionEvent event) {
        changeScene(event , "/com/example/shop_laptop/ProductAdmin.fxml");
    }

    public void customerList(ActionEvent event) {
        changeScene(event , "/com/example/shop_laptop/Customer.fxml");
    }
    public void Logout(ActionEvent event) {
        changeScene(event , "/com/example/shop_laptop/LoginScene.fxml");
    }

    public void account(ActionEvent event) {
        changeScene(event , "/com/example/shop_laptop/account.fxml");
    }

    public void register(ActionEvent event) {
        changeScene(event , "/com/example/shop_laptop/RegisterScene.fxml");
    }
}