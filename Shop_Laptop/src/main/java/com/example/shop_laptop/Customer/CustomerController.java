package com.example.shop_laptop.Customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;

public class CustomerController {

    @FXML
    private TableView<Customer> tableView;
    @FXML
    private TableColumn<Customer, String> usernameColumn;
    @FXML
    private TableColumn<Customer, String> passwordColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> emailColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        loadData();
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

    @FXML
    public void addCustomer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shop_laptop/add_customer.fxml"));
            Parent root = loader.load();

            AddCustomerController controller = loader.getController(); // Đảm bảo đúng controller
            controller.setCustomerController(this); // Truyền controller hiện tại vào

            Stage stage = new Stage();
            stage.setTitle("Thêm Khách Hàng");
            stage.setScene(new Scene(root, 400, 300));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open customer form: " + e.getMessage());
        }
    }


    @FXML
    public void editCustomer(ActionEvent event) {
        Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            showAlert("Error", "Please select a customer to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shop_laptop/edit_customer.fxml"));
            Parent root = loader.load();

            // Lấy controller của EditCustomer
            EditCustomerController controller = loader.getController();
            controller.setCustomer(selectedCustomer); // Truyền khách hàng đã chọn vào controller
            controller.setCustomerController(this); // Truyền controller hiện tại vào

            Stage stage = new Stage();
            stage.setTitle("Chỉnh Sửa Khách Hàng");
            stage.setScene(new Scene(root, 400, 300));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open customer form: " + e.getMessage());
        }
    }


    @FXML
    public void removeCustomer(ActionEvent event) {
        Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            showAlert("Error", "Please select a customer to remove.");
            return;
        }

        tableView.getItems().remove(selectedCustomer);
        saveData();
        showAlert("Success", "Customer removed successfully.");
    }

    void loadData() {
        String fileName = "users.txt";
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(":");
                if (fields.length == 5) {
                    customers.add(new Customer(fields[0], fields[1], fields[2], fields[3], fields[4]));
                }
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to load data: " + e.getMessage());
        }
        tableView.setItems(customers);
    }

    private void saveData() {
        String fileName = "users.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Customer customer : tableView.getItems()) {
                bw.write(String.format("%s:%s:%s:%s:%s",
                        customer.getUsername(),
                        customer.getPassword(),
                        customer.getName(),
                        customer.getEmail(),
                        customer.getPhone()));
                bw.newLine();
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to save data: " + e.getMessage());
        }
    }

    private void changeScene(ActionEvent event, String fxmlPath) {
        try {
            URL resource = getClass().getResource(fxmlPath);
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to change scene: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
