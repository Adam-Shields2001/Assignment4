package com.fyp1.assignment4.POJOS;

import java.io.Serializable;

public class StockItem implements Serializable {
    private String id;
    private String title;
    private String manufacturer;
    private String price;
    private String category;
    private String stock;
    private String image;
    private int quantity;

    public StockItem() {}

    public StockItem(String title, String manufacturer, String price, String category, String stock, String image) {
        this.title = title;
        this.manufacturer = manufacturer;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

