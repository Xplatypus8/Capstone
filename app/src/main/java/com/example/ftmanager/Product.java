package com.example.ftmanager;

public class Product {
    private String name, quantity, date;
    private int locationID;
    private boolean isNeeded;

    public Product(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
        this.locationID = 0;
        this.date = "unspecified";
        this.isNeeded = false;
    }

    public Product(String [] data) {
        this.name = data[0];
        this.quantity = data[1];
        this.locationID = Integer.parseInt(data[2]);
        this.date = data[3];
        this.isNeeded = true;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDate(){return date;}

    public int getLocationID(){return locationID;}

    public void setNeeded(boolean needed) {
        isNeeded = needed;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public boolean isNeeded() {
        return isNeeded;
    }
}
