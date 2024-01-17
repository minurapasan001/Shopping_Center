package com.company;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Product> products;


    public ShoppingCart() {
        products = new ArrayList<>();
    }


    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(String productId) {
        products.removeIf(p -> p.getId().equals(productId));
    }

    public double calculateTotalCost() {
        double total = 0.0;
        for (Product product : products) {
            total += product.getPrice() * product.getAvailableItems();
        }
        return total;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}


