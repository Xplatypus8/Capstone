package com.example.ftmanager;

public class Product {
    private String name, quantity;
    private boolean isNeeded;

    public Product(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
        this.isNeeded = false;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setNeeded(boolean needed) {
        isNeeded = needed;
    }

    public boolean isNeeded() {
        return isNeeded;
    }
}
