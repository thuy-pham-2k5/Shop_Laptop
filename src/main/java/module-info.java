module com.example.ngay1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.shop_laptop to javafx.fxml;
    exports com.example.shop_laptop;
}