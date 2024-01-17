package com.company;

public abstract class Product {
    protected String id;
    protected String name;
    protected double price;
    private int availableItems;
    protected  String info;

    public Product(String id, String name, double price, int availableItems,String info) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availableItems = availableItems;
        this.info = info;// Make sure to pass availableItems in the constructor
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getProductType();

    public int getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    }
    public Product(/* parameters including info */) {
        // initialization
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}


