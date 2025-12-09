package org.credit.demo.entity; // 【注意】这里改成了你的实际包名

public class User {
    private String email;
    private String password;

    // ============================
    // 1. 构造方法
    // ============================
    
    // 无参构造
    public User() {
    }

    // 全参构造
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // ============================
    // 2. Getter 和 Setter 方法
    // ============================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ============================
    // 3. toString 方法
    // ============================
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}