package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingGUI extends JFrame {
    private JTable productsTable;
    private DefaultTableModel productsTableModel;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JTextArea productDetailsArea;
    private JComboBox<String> categoryFilterComboBox;
    private ProductManager productManager;
    private JLabel totalPriceLabel;
    private JLabel firstPurchaseDiscountLabel;
    private JLabel categoryDiscountLabel;
    private JLabel finalTotalLabel;

    public ShoppingGUI(ProductManager manager) {
        this.productManager = manager;
        initializeComponents();
        setupLayout();
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        productsTableModel = new DefaultTableModel(
                new String[]{"Product ID", "Name", "Category", "Price(£)","Info","Available Items",}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productsTable = new JTable(productsTableModel);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(productsTableModel);
        productsTable.setRowSorter(sorter);
        sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));

        productsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && productsTable.getSelectedRow() != -1) {
                displayProductDetails();
            }
        });
        setCellRenderer();

        productDetailsArea = new JTextArea(6, 30);
        productDetailsArea.setEditable(false);
        categoryFilterComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothing"});
        categoryFilterComboBox.addActionListener(e -> filterProducts());
        cartTableModel = new DefaultTableModel(new String[]{"Product", "Quantity", "Price(£)"}, 0);
        cartTable = new JTable(cartTableModel);
        totalPriceLabel = new JLabel("Total: £0.00");

        // Styling components
        styleComponents();

        firstPurchaseDiscountLabel = new JLabel("First Purchase Discount: £0.00");
        categoryDiscountLabel = new JLabel("Category Discount: £0.00");
        finalTotalLabel = new JLabel("Final Total: £0.00");

        loadProductsToTable(productManager.getProducts());
    }

    private void styleComponents() {
        // Define new color scheme
        Color headerColor = new Color(70, 130, 180); // Steel Blue
        Color backgroundColor = new Color(240, 248, 255); // Alice Blue
        Color textColor = new Color(0, 0, 0); // Black
        Font headerFont = new Font("SansSerif", Font.BOLD, 16);
        Font normalFont = new Font("SansSerif", Font.PLAIN, 14);

        // Styling tables
        productsTable.setFont(normalFont);
        productsTable.setRowHeight(25);
        productsTable.getTableHeader().setFont(headerFont);
        productsTable.getTableHeader().setBackground(headerColor);
        productsTable.getTableHeader().setForeground(Color.WHITE);
        cartTable.setFont(normalFont);
        cartTable.setRowHeight(25);
        cartTable.getTableHeader().setFont(headerFont);
        cartTable.getTableHeader().setBackground(headerColor);
        cartTable.getTableHeader().setForeground(Color.WHITE);

        // Styling JTextArea
        productDetailsArea.setFont(normalFont);
        productDetailsArea.setBackground(backgroundColor);
        productDetailsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        productDetailsArea.setForeground(textColor);

        // Styling JComboBox
        categoryFilterComboBox.setFont(normalFont);
        categoryFilterComboBox.setBackground(Color.WHITE);
        categoryFilterComboBox.setForeground(textColor);
    }

    private void setupLayout() {
        setTitle("Westminster Shopping Centre");
        getContentPane().setBackground(new Color(245, 245, 245));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(173, 216, 230));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(new JLabel("Select Product Category:"));
        topPanel.add(categoryFilterComboBox);

        JScrollPane productsScrollPane = new JScrollPane(productsTable);
        productsScrollPane.setPreferredSize(new Dimension(600, 150));
        JScrollPane detailsScrollPane = new JScrollPane(productDetailsArea);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setPreferredSize(new Dimension(200, 150));
        JPanel productDetailsPanel = new JPanel();
        productDetailsPanel.setBorder(BorderFactory.createTitledBorder("Selected Product - Details"));
        productDetailsPanel.add(detailsScrollPane);

        JButton addToCartButton = new JButton("Add to Shopping Cart");
        addToCartButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        addToCartButton.setBackground(new Color(100, 149, 237));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setFocusPainted(false);
        addToCartButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addToCartButton.addActionListener(e -> addToCart());

        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainContentPanel.add(productsScrollPane, BorderLayout.NORTH);
        mainContentPanel.add(productDetailsPanel, BorderLayout.CENTER);
        mainContentPanel.add(addToCartButton, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
        add(cartScrollPane, BorderLayout.EAST);

        JPanel totalPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPricePanel.setBackground(new Color(173, 216, 230));
        totalPricePanel.add(totalPriceLabel);

        JPanel discountPanel = new JPanel(new GridLayout(0, 1));
        discountPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        discountPanel.setBackground(new Color(245, 245, 245));
        discountPanel.add(firstPurchaseDiscountLabel);
        discountPanel.add(categoryDiscountLabel);
        discountPanel.add(finalTotalLabel);

        add(totalPricePanel, BorderLayout.SOUTH);
        add(discountPanel, BorderLayout.SOUTH);

        getContentPane().setBackground(new Color(240, 248, 255)); // Alice Blue background
        topPanel.setBackground(new Color(173, 216, 230));
    }

    private void loadProductsToTable(List<Product> products) {
        productsTableModel.setRowCount(0);
        for (Product p : products) {
            productsTableModel.addRow(new Object[]{
                    p.getId(), p.getName(), p.getProductType(), p.getPrice(), p.getInfo(), p.getAvailableItems() // Assuming getAvailableItems() exists
            });
        }
    }

    private void filterProducts() {
        String selectedCategory = (String) categoryFilterComboBox.getSelectedItem();
        List<Product> filteredProducts;
        if ("All".equals(selectedCategory)) {
            filteredProducts = productManager.getProducts();
        } else {
            filteredProducts = productManager.getProductsByCategory(selectedCategory);
        }
        loadProductsToTable(filteredProducts);
    }



    private void addToCart() {
        int selectedRowIndex = productsTable.getSelectedRow();
        if (selectedRowIndex >= 0) {
            String productId = productsTableModel.getValueAt(selectedRowIndex, 0).toString();
            Product product = productManager.getProductById(productId);
            if (product != null) {
                boolean isPresent = false;
                for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                    String cartProductId = cartTableModel.getValueAt(i, 0).toString();
                    if (cartProductId.equals(product.getId())) {
                        int quantity = (int) cartTableModel.getValueAt(i, 1) + 1;
                        cartTableModel.setValueAt(quantity, i, 1);
                        isPresent = true;
                        break;
                    }
                }
                if (!isPresent) {
                    cartTableModel.addRow(new Object[]{product.getId(), 1, product.getPrice()});
                }
                updateTotalPrice();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to add to cart.", "Selection Required", JOptionPane.INFORMATION_MESSAGE);
        }
    }



    private void updateTotalPrice() {
        double total = 0.0;
        double electronicsTotal = 0.0;
        double clothingTotal = 0.0;
        int electronicsCount = 0;
        int clothingCount = 0;

        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            String productId = (String) cartTableModel.getValueAt(i, 0);
            Product p = productManager.getProductById(productId);
            int quantity = (int) cartTableModel.getValueAt(i, 1);
            double price = (double) cartTableModel.getValueAt(i, 2);

            if ("Electronics".equals(p.getProductType())) {
                electronicsTotal += price * quantity;
                electronicsCount += quantity;
            } else if ("Clothing".equals(p.getProductType())) {
                clothingTotal += price * quantity;
                clothingCount += quantity;
            }

            total += price * quantity;
        }

        double discount = 0.0;
        if (productManager.isFirstPurchase()) {
            discount += total * 0.10;
            productManager.registerFirstPurchase();
        }
        if (electronicsCount >= 3) {
            discount += electronicsTotal * 0.20;
        }
        if (clothingCount >= 3) {
            discount += clothingTotal * 0.20;
        }

        double finalTotal = total - discount;

        firstPurchaseDiscountLabel.setText(String.format("First Purchase Discount (10%%): -£%.2f", productManager.isFirstPurchase() ? total * 0.10 : 0.0));
        categoryDiscountLabel.setText(String.format("Category Discount (20%%): -£%.2f", (electronicsCount >= 3 ? electronicsTotal * 0.20 : 0.0) + (clothingCount >= 3 ? clothingTotal * 0.20 : 0.0)));
        finalTotalLabel.setText(String.format("Final Total: £%.2f", finalTotal));

        totalPriceLabel.setText(String.format("Total: £%.2f", finalTotal));
    }


    private void displayProductDetails() {
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow >= 0) {
            String details = "ID: " + productsTableModel.getValueAt(selectedRow, 0) +
                    "\nName: " + productsTableModel.getValueAt(selectedRow, 1) +
                    "\nCategory: " + productsTableModel.getValueAt(selectedRow, 2) +
                    "\nPrice: £" + productsTableModel.getValueAt(selectedRow, 3) +
                    "\nInfo: " + productsTableModel.getValueAt(selectedRow, 4) +
                    "\nAvailable Items: " + productsTableModel.getValueAt(selectedRow, 5); // Display Available Items
            productDetailsArea.setText(details);
        }
    }

    private void setCellRenderer() {
        productsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                Object availabilityValue = table.getValueAt(row, 3);
                if (availabilityValue instanceof Integer) {
                    int available = (Integer) availabilityValue;
                    if (available < 3) {
                        c.setForeground(Color.RED);
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                }
                return c;
            }
        });
    }

    public static void main(String[] args) {
        ProductManager manager = new ProductManager();
        SwingUtilities.invokeLater(() -> {
            ShoppingGUI gui = new ShoppingGUI(manager);
            gui.setVisible(true);
        });
    }
}