module com.example.shop_laptop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;

    opens com.example.shop_laptop to javafx.fxml;
    exports com.example.shop_laptop.signin;
    opens com.example.shop_laptop.signin to javafx.fxml;
    exports com.example.shop_laptop.Account;
    opens com.example.shop_laptop.Account to javafx.fxml;
    opens com.example.shop_laptop.Customer to javafx.fxml;
    exports com.example.shop_laptop.Customer;
    opens com.example.shop_laptop.productAdmin to javafx.fxml;
    exports com.example.shop_laptop.productAdmin;
    opens com.example.shop_laptop.Order to javafx.fxml;
    exports com.example.shop_laptop.Order;
    opens com.example.shop_laptop.Cart to javafx.fxml;
    exports com.example.shop_laptop.Cart;
    opens com.example.shop_laptop.productUser to javafx.fxml;
    exports com.example.shop_laptop.productUser;
}

