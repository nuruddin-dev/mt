package com.graphicless.modeltest.model;

public class SingleItemModel {

    private final int itemImage;
    private final String itemName;

    public SingleItemModel(int itemImage, String itemName) {
        this.itemImage = itemImage;
        this.itemName = itemName;
    }

    public int getItemImage() {
        return itemImage;
    }

    public String getItemName() {
        return itemName;
    }
}
