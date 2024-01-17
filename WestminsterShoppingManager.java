package com.company;

import javax.swing.*;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.SwingUtilities;

public class WestminsterShoppingManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductManager supervisor = new ProductManager();
        User currentUser = null;
        ShoppingCart cart = new ShoppingCart();
        ShoppingGUI gui = null;

        while (true) {
            System.out.println("0. Register / Login");
            System.out.println("1. Add a new product");
            System.out.println("2. Delete a product");
            System.out.println("3. Print merchandise");
            System.out.println("4. Show GUI");
            System.out.println("5. Save file");
            System.out.println("6. Add product to cart");
            System.out.println("7. View cart and Checkout");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");
            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            System.out.println("---------------------------------------------------------------------");

            switch (choice) {
                case 0:
                    currentUser = handleUserRegistrationLogin(scanner);
                    break;
                case 1:
                    addProduct(scanner, supervisor);
                    break;
                case 2:
                    System.out.print("Enter product ID to delete: ");
                    String id = scanner.nextLine();
                    supervisor.deleteProduct(id);
                    break;
                case 3:
                    supervisor.printProducts();
                    break;
                case 4:
                    if (gui == null || !gui.isVisible()) {
                        gui = new ShoppingGUI(supervisor);
                        ShoppingGUI finalGui = gui;
                        SwingUtilities.invokeLater(() -> finalGui.setVisible(true));
                    } else {
                        System.out.println("GUI is already walking.");
                    }
                    break;
                case 5:
                    System.out.print("Enter the filename to save product details: ");
                    String filename = scanner.nextLine();
                    try {
                        supervisor.saveProductDetailsToFile(filename);
                        System.out.println("Product details saved to " + filename);
                    } catch (IOException e) {
                        System.err.println("Error saving product details: " + e.getMessage());
                    }
                    break;
                case 6:
                    if (currentUser != null) {
                        handleAddToCart(scanner, supervisor, cart);
                    } else {
                        System.out.println("Please login first.");
                    }
                    break;
                case 7:
                    if (currentUser != null) {
                        handleViewCartAndCheckout(cart);
                    } else {
                        System.out.println("Please login first.");
                    }
                    break;
                case 8:
                    System.out.println("Exiting...");
                    if (gui != null) {
                        gui.dispose();
                    }
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static User handleUserRegistrationLogin(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        return new User(username, password);
    }

    private static void handleAddToCart(Scanner scanner, ProductManager supervisor, ShoppingCart cart) {
        System.out.print("Enter product ID to add to cart: ");
        String id = scanner.nextLine();
        Product product = supervisor.getProductById(id);
        if (product != null) {
            cart.addProduct(product);
            System.out.println("Added " + product.getName() + " to cart.");
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void handleViewCartAndCheckout(ShoppingCart cart) {
        System.out.println("Products in your cart:");
        for (Product product : cart.getProducts()) {
            System.out.println(product.getName() + " - $" + product.getPrice());
        }
        System.out.println("Total Cost: $" + cart.calculateTotalCost());
        // Checkout logic can be added here
    }

    private static void addProduct(Scanner scanner, ProductManager supervisor) {
        System.out.print("Enter product type (Electronics/Clothing): ");
        String kind = scanner.nextLine();
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter product call: ");
        String call = scanner.nextLine();
        System.out.print("Enter product fee: ");
        double price = scanner.nextDouble();
        System.out.print("Enter wide variety of to be had objects: ");
        int availableItems = scanner.nextInt();
        System.out.println("Enter information about product");
        scanner.nextLine(); // Consume the leftover newline
        String info = scanner.nextLine();
        scanner.nextLine();

        Product product;
        if ("Electronics".equalsIgnoreCase(kind)) {
            product = new Electronics(id, call, price, availableItems,info);
        }else if ("Clothing".equalsIgnoreCase(kind)) {
            product = new Clothing(id, call, price, availableItems,info);
        }else {
            System.out.println("Invalid product kind.");
            return;
        }


        supervisor.addProduct(product);
    }
}

