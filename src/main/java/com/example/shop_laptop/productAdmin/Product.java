package com.example.shop_laptop.productAdmin;

import javafx.beans.property.*;
import javafx.scene.image.Image;

public class Product {
    private final IntegerProperty id;
    private final ObjectProperty<Image> image;
    private final StringProperty name;
    private final StringProperty type;
    private final DoubleProperty price;
    private final IntegerProperty quantity;
    private final DoubleProperty total;

    public Product(int id, Image image, String name, String type, double price, int quantity, double total) {
        this.id = new SimpleIntegerProperty(id);
        this.image = new SimpleObjectProperty<>(image);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.total = new SimpleDoubleProperty();

        // Bind total to price * quantity (tự động cập nhật)
        this.total.bind(this.price.multiply(this.quantity));
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty typeProperty() {
        return type;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public Image getImage() {
        return image.get();
    }

    public void setImage(Image image) {
        this.image.set(image);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setTotal(double value) {
        if (total.isBound()) {
            total.unbind();
        }
        total.set(value);
    }

    public double getTotal() {
        return total.get();
    }

    public DoubleProperty totalProperty() {
        return total;
    }

    @Override
    public String toString() {
        return id.get() + "," + image.get().getUrl() + "," + name.get() + "," + type.get() + "," + price.get() + "," + quantity.get() + "," + total.get();
    }

    public static Product fromString(String line) {
//         Phân tách chuỗi để tạo đối tượng Product
//         Chú ý: xử lý ảnh và các giá trị khác phù hợp với định dạng của bạn
        String[] parts = line.split(",", 7);
        if (parts.length < 7) {
            throw new IllegalArgumentException("Invalid product format.");
        }
        int id = Integer.parseInt(parts[0]);
        Image image = new Image(parts[1]);
        String name = parts[2];
        String type = parts[3];
        double price = Double.parseDouble(parts[4]);
        int quantity = Integer.parseInt(parts[5]);
        double total = Double.parseDouble(parts[6]);
        return new Product(id, image, name, type, price, quantity, total);
    }
}