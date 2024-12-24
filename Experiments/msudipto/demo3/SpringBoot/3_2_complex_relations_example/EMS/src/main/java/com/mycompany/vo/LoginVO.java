package com.mycompany.vo;

public class LoginVO {
    private String email;
    private String password;

    // Default constructor
    public LoginVO() {
    }

    // Parameterized constructor
    public LoginVO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && !email.isEmpty()) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null && !password.isEmpty()) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }

    // toString method
    @Override
    public String toString() {
        return "LoginVO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}