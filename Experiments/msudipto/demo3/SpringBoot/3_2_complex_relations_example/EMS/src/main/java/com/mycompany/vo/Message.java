package com.mycompany.vo;

public class Message {

    private String message;
    private int errorCode;

    // Default constructor
    public Message() {
    }

    // Parameterized constructor
    public Message(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (message != null && !message.isEmpty()) {
            this.message = message;
        } else {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        if (errorCode >= 0) {
            this.errorCode = errorCode;
        } else {
            throw new IllegalArgumentException("Error code cannot be negative");
        }
    }

    // toString method
    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}