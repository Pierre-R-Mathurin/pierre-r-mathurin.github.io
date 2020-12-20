package com.example.inventoryapp;


public class Item {
    private int _code;
    private String _itemname;
    private int _price;

    public Item(){

    }

    public Item(int code,String itemname, int price){
        this._code =code;
        this._itemname = itemname;
        this._price =price;
    }

    public  Item(String itemname, int price){
        this._itemname = itemname;
        this._price = price;
    }

    public int getCode() {
        return _code;
    }

    public void setCode(int code) {
        this._code = code;
    }

    public String getItemName() {
        return _itemname;
    }

    public void setItemName(String productname) {
        this._itemname = productname;
    }

    public int getPrice() {
        return _price;
    }

    public void setPrice(int quantity) {
        this._price = quantity;
    }
}
