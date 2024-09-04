package com.example.shop_laptop.Cart;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

// Lớp này định nghĩa sản phẩm với các thuộc tính: tên, giá và số lượng
public class Product {
    private String name; // Tên sản phẩm
    private double price; // Giá sản phẩm
    private int quantity; // Số lượng sản phẩm
    private BooleanProperty selected = new SimpleBooleanProperty(false);


    // Constructor để khởi tạo sản phẩm
    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getter và Setter cho tên sản phẩm
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter và Setter cho giá sản phẩm
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Getter và Setter cho số lượng sản phẩm
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Tính tổng giá trị sản phẩm (giá x số lượng)
    public double getTotalPrice() {
        return price * quantity;
    }

    // Getter và Setter cho thuộc tính selected
    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
