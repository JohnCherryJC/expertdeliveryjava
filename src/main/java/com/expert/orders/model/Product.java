package com.expert.orders.model;

public class Product {
    private String name;
    private String category;
    private double price;

    public Product() {}

    public Product(String name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public String getCategory() { return category; }
    public void setCategory(String c) { this.category = c; }
    public double getPrice() { return price; }
    public void setPrice(double p) { this.price = p; }

    @Override
    public String toString() { return name; }
}
