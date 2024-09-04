module com.example.shop_laptop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;

    opens com.example.shop_laptop.productAdmin to javafx.fxml;
    exports com.example.shop_laptop.productAdmin;
    opens com.example.shop_laptop.productUser to javafx.fxml;
    exports com.example.shop_laptop.productUser;
    opens com.example.shop_laptop to javafx.fxml;
    exports com.example.shop_laptop;
}