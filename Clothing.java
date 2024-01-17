package com.company;

public class Clothing extends Product {
    public Clothing(String id, String name, double price, int availableItems,String info) {
        super(id, name, price, availableItems,info);
    }

    @Override
    public String getProductType() {
        return "Clothing";
    }
}

