package com.company;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ProductManager {
    private List<Product> products;
    private boolean firstPurchase = true;


    public ProductManager() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        if (products.size() < 50) {
            products.add(product);
            System.out.println(product.getName() + " added.");
        } else {
            System.out.println("Product limit reached.");
        }
    }

    public void deleteProduct(String id) {
        products.removeIf(p -> p.getId().equals(id));
        System.out.println("Product with ID " + id + " removed.");
    }

    public void printProducts() {
        products.stream()
                .sorted(Comparator.comparing(Product::getId))
                .forEach(p -> System.out.println(p.getId() + ": " + p.getName() + " [" + p.getProductType() + "]"));
    }
    public void registerFirstPurchase() {
        this.firstPurchase = false;
    }

    // Getters
    public List<Product> getProducts() {
        return products;
    }

    public Product getProductById(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Product> getProductsByCategory(String category) {
        return products.stream()
                .filter(p -> category.equalsIgnoreCase("All") || p.getProductType().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public void saveProductDetailsToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Product p : products) {
                writer.write(p.getId() + ", " + p.getName() + ", " + p.getProductType() + ", " + p.getPrice() + ", " + p.getAvailableItems() + "," + p.getInfo());
                writer.newLine();
            }
        }
    }

    public boolean isFirstPurchase() {
        // This method should check if the customer has made a purchase before
        // For now, we'll assume it's always the first purchase for simplicity
        return true;
    }
}

