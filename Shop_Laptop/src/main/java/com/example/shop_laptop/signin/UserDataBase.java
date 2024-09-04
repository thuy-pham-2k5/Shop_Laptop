package com.example.shop_laptop.signin;

import java.io.*;
import java.util.HashMap;

public class UserDataBase {

    private HashMap<String, User> users = new HashMap<>();
    private String filePath = "users.txt"; // Đường dẫn tệp

    // Load dữ liệu người dùng từ tệp
    public void loadUserData() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            addDefaultAdmins();
            saveUserData();  // Lưu dữ liệu mặc định nếu tệp không tồn tại
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");  // Sử dụng dấu ":" làm phân cách
                    if (parts.length == 5) {
                        users.put(parts[0], new User(parts[0], parts[1], parts[2], parts[3], parts[4]));
                    }
                }
            }
        }
    }

    // Lưu tất cả dữ liệu người dùng vào tệp
    public void saveUserData() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {  // Ghi đè tệp hiện tại
            for (User user : users.values()) {
                writer.write(String.join(":", user.getUsername(), user.getPassword(), user.getName(), user.getEmail(), user.getPhone()));
                writer.newLine();
            }
        }
    }

    // Kiểm tra thông tin đăng nhập
    public boolean validateCredentials(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    // Cập nhật mật khẩu người dùng
    public boolean updatePassword(String username, String newPassword) {
        User user = users.get(username);
        if (user != null) {
            user.setPassword(newPassword);
            try {
                saveUserData();  // Lưu lại dữ liệu sau khi cập nhật mật khẩu
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    // Thêm người dùng mới vào cơ sở dữ liệu
    public void addUser(User user) {
        users.put(user.getUsername(), user);
        try {
            saveUserData();  // Lưu lại dữ liệu sau khi thêm người dùng mới
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra xem người dùng đã tồn tại hay chưa
    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    // Thêm tài khoản quản trị mặc định
    public void addDefaultAdmins() {
        addUser(new User("phamthuy", "pass2", "Admin Two", "admin2@example.com", "0123456789"));
        addUser(new User("admin1", "pass1", "Admin One", "admin1@example.com", "0987654321"));
        addUser(new User("phuonganh", "pass4", "Admin Four", "admin4@example.com", "0123456789"));
        addUser(new User("tandung", "pass5", "Admin Five", "admin5@example.com", "0987654321"));
        addUser(new User("hoangvu", "pass3", "Admin Three", "admin3@example.com", "0123456789"));
    }

    // Getter cho users HashMap
    public HashMap<String, User> getUsers() {
        return users;
    }
}
