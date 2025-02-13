package com.example.glamfinal;

public class Item {

    private String name;
    private String info;
    private String price;
    private int type;

    public Item() {
    }

    public Item(String name, String info, String price, int type) {
        this.name = name;
        this.info = info;

        this.price = price;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}