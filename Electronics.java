package com.company;

public class Electronics extends Product {
    public Electronics(String id, String name, double price, int availableItems , String info) {
        super(id, name, price, availableItems,info);
    }

    @Override
    public String getProductType() {
        return "Electronics";
    }
}


