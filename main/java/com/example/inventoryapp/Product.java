package com.example.inventoryapp;


public class Product {
    private int _id;
    private String _productname;
    private String _description;

    public Product(){

    }

    public Product(int id,String productname, String description){
        this._id =id;
        this._productname = productname;
        this._description =description;
    }

    public  Product(String productname, String description){
        this._productname = productname;
        this._description = description;
    }

    public int getID() {
        return _id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public String getProductName() {
        return _productname;
    }

    public void setProductName(String productname) {
        this._productname = productname;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }
}
