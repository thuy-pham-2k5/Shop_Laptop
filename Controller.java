package com.example.shop_laptop.cartUser;

import com.example.shop_laptop.productAdmin.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Controller {
    private static final String FILE_CART = "D:\\Shop_Laptop\\src\\main\\java\\com\\example\\shop_laptop\\text\\cart.txt";
    private static final String FILE_ORDER = "D:\\Shop_Laptop\\src\\main\\java\\com\\example\\shop_laptop\\text\\order.txt";

    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product, Image> productImage;
    @FXML
    private TableColumn<Product, String> productName;
    @FXML
    private TableColumn<Product, Double> productPrice;
    @FXML
    private TableColumn<Product, Integer> productQuantity;
    @FXML
    private TableColumn<Product, Boolean> productSelected;
    @FXML
    private TableColumn<Product, Void> productRemove;
    @FXML
    private TextField thanhtoan;
    @FXML
    private Button checkoutButton;
    @FXML
    private Button cancelButton;
    @FXML
    private CheckBox selectAllCheckbox;

    private ObservableList<Product> productList = FXCollections.observableArrayList();

    public void initialize() {
        productImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        checkoutButton.setOnAction(event -> handleCheckout());
        cancelButton.setOnAction(event -> handleCancel());
        selectAllCheckbox.setOnAction(event -> handleSelectAll());

        // Tạo TableCell tùy chỉnh để hiển thị ImageView
        productImage.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Product, Image> call(TableColumn<Product, Image> param) {
                return new TableCell<>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(Image item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            imageView.setImage(item);
                            imageView.setFitHeight(100); // Chiều cao của hình ảnh
                            imageView.setFitWidth(100);  // Chiều rộng của hình ảnh
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });

        // Tạo TableCell tùy chỉnh để hiển thị CheckBox
        productSelected.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Product, Boolean> call(TableColumn<Product, Boolean> param) {
                return new TableCell<>() {
                    private final CheckBox checkBox = new CheckBox();

                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            checkBox.setSelected(item != null && item);
                            checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                                Product product = getTableView().getItems().get(getIndex());
                                product.setSelected(isNowSelected);
                                updateTotal();
                            });
                            setGraphic(checkBox);
                        }
                    }
                };
            }
        });

        productQuantity.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Product, Integer> call(TableColumn<Product, Integer> param) {
                return new TableCell<>() {
                    private final Spinner<Integer> spinner = new Spinner<>();

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Product product = getTableView().getItems().get(getIndex());
                            int maxQuantity = getMaxQuantityFromFile(product.getName());

                            setupSpinner(spinner, product, maxQuantity);
                            setGraphic(spinner);
                        }
                    }
                };
            }
        });

        productRemove.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final Button removeButton = new Button("Remove");

                    {
                        removeButton.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation Dialog");
                            alert.setHeaderText(null);
                            alert.setContentText("Bạn có muốn xóa khỏi giỏ hàng không?");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                getTableView().getItems().remove(product);
                                removeProductFromFile(product);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(removeButton);
                        }
                    }
                };
            }
        });

        loadDataFromFileCart(FILE_CART);
        productTableView.setItems(productList);
    }

    private int getMaxQuantityFromFile(String productName) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_CART))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7 && data[2].equals(productName)) {
                    return Integer.parseInt(data[5]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1; // Giá trị mặc định nếu không tìm thấy
    }

    private void setupSpinner(Spinner<Integer> spinner, Product product, int maxQuantity) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxQuantity, 1);
        spinner.setValueFactory(valueFactory);

        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue > maxQuantity) {
                spinner.getValueFactory().setValue(maxQuantity);
            }
            product.setQuantity(newValue);
            updateTotal();
        });

    }

    private void loadDataFromFileCart(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7) {
                    Image image = new Image(data[1]);
                    String name = data[2];
                    double price = Double.parseDouble(data[4]);
                    boolean selected = Boolean.parseBoolean(data[6]);
                    productList.add(new Product(image, name, price, selected));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTotal() {
        double total = 0;
        for (Product product : productList) {
            if (product.isSelected()) {
                total += product.getPrice() * product.getQuantity();
            }
        }
        thanhtoan.setText(String.valueOf(total));
    }

    private void removeProductFromFile(Product product) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_CART))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7 && !data[2].equals(product.getName())) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_CART))) {
            for (String line : lines) {
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Cập nhật ObservableList sau khi xóa sản phẩm khỏi file
        productList.remove(product);
    }

    @FXML
    private void handleCheckout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thanh toán");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn thanh toán không?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_ORDER, true))) {
                for (Product product : productList) {
                    if (product.isSelected()) {
                        String orderStatus = "chờ giao hàng";
                        String dateNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        String orderInfo = String.format("%s,%s,%s,%s,%d,%f",
                                dateNow,
                                orderStatus,
                                product.getName(),
                                product.getImage().getUrl(),
                                product.getQuantity(),
                                product.getPrice() * product.getQuantity());
                        writer.write(orderInfo + System.lineSeparator());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void handleCancel() {
        for (Product product : productList) {
            product.setSelected(false);
            product.setQuantity(1);
        }
        productTableView.refresh();
    }
    @FXML
    private void handleSelectAll() {
        boolean isSelected = selectAllCheckbox.isSelected();
        for (Product product : productList) {
            product.setSelected(isSelected);
        }
        productTableView.refresh();
    }
}
