package com.example.inventoryapp;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.inventoryapp.provider.ItemContentProvider;
import com.example.inventoryapp.provider.MyContentProvider;
import com.example.inventoryapp.provider.UserContentProvider;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {

    private ContentResolver myCR;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_ITEMS = "items";
    public static final String TABLE_USER = "users";

    public static final String COLUMN_ID ="_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_DESCRIPTION = "description";

    public static final String COLUMN_CODE ="code";
    public static final String COLUMN_ITEMNAME = "itemname";
    public static final String COLUMN_PRICE = "price";

    public static final String COLUMN_USERNAME ="username";
    public static final String COLUMN_PASSWORD = "password";


    public MyDBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        myCR = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_PRODUCTS +"(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_PRODUCTNAME
                + " TEXT," + COLUMN_DESCRIPTION + " TEXT" + ")";

        String CREATE_ITEMS_TABLE = "CREATE TABLE " +
                TABLE_ITEMS +"(" +
                COLUMN_CODE + " INTEGER PRIMARY KEY," +
                COLUMN_ITEMNAME
                + " TEXT," + COLUMN_PRICE + " INTEGER" + ")";

        String CREATE_USER_TABLE = "CREATE TABLE " +
                TABLE_USER +"(" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY," +
                COLUMN_PASSWORD
                + " TEXT" + ")";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ITEMS);
        onCreate(db);
    }




    /************Register and sign in user*****************/
    public User findUser(String username,String password){
        String[] projection = {COLUMN_USERNAME, COLUMN_PASSWORD};

        String selection = "username =\"" +username +"\" AND password=\""+password +"\"";

        Cursor cursor = myCR.query(UserContentProvider.CONTENT_URI, projection,selection, null,null);

        User user = new User();
        System.out.println(cursor.moveToFirst());
        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            user.setUsername(cursor.getString(0));
            user.setPassword((cursor.getString(1)));
            cursor.close();
        }else{
            user =null;
        }
        return user;
    }
    public boolean addUser(User user){
        ContentValues values=new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());

        myCR.insert(UserContentProvider.CONTENT_URI,values);
        return true;
    }




    /*************************************/
    public void addProduct(Product product){
        if(findProduct(product.getProductName()) !=null){
            updateProduct(product);
        }else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PRODUCTNAME, product.getProductName());
            values.put(COLUMN_DESCRIPTION, product.getDescription());

            myCR.insert(MyContentProvider.CONTENT_URI, values);
        }
    }
    public void addItem(Item item){
        if(findItem(item.getCode()) !=null){
            updateItem(item);
        }else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CODE, item.getCode());
            values.put(COLUMN_ITEMNAME, item.getItemName());
            values.put(COLUMN_PRICE, item.getPrice());

            myCR.insert(ItemContentProvider.CONTENT_URI, values);
        }
    }
    public void updateProduct(Product product){
        String selection = "productname =\"" +product.getProductName() +"\"";
        ContentValues values=new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_DESCRIPTION, product.getDescription());

        myCR.update(MyContentProvider.CONTENT_URI,values,selection,null);
    }

    public void updateItem(Item item){
        String selection = "code =" +item.getCode();
        ContentValues values=new ContentValues();
        values.put(COLUMN_ITEMNAME, item.getItemName());
        values.put(COLUMN_PRICE, item.getPrice());

        myCR.update(ItemContentProvider.CONTENT_URI,values,selection,null);
    }

    public Product findProduct(String productname){
        String[] projection = {COLUMN_ID, COLUMN_PRODUCTNAME,COLUMN_DESCRIPTION};

        String selection = "productname =\"" +productname +"\"";
        Cursor cursor = myCR.query(MyContentProvider.CONTENT_URI, projection,selection, null,null);

        Product product = new Product();

        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setProductName((cursor.getString(1)));
            product.setDescription(cursor.getString(2));
            cursor.close();
        }else{
            product =null;
        }
        return product;
    }
    public Item findItem(int itemcode){
        String[] projection = {COLUMN_CODE, COLUMN_ITEMNAME,COLUMN_PRICE};

        String selection = "code =" +itemcode;
        Cursor cursor = myCR.query(ItemContentProvider.CONTENT_URI, projection,selection, null,null);

        Item item = new Item();

        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            item.setCode(Integer.parseInt(cursor.getString(0)));
            item.setItemName((cursor.getString(1)));
            item.setPrice(Integer.parseInt(cursor.getString(2)));
            cursor.close();
        }else{
            item =null;
        }
        return item;
    }

    public  boolean deleteProduct(String productname){
        boolean result = false;

        String selection = "productname = \"" +productname + "\"";
        int rowsDeleted = myCR.delete(MyContentProvider.CONTENT_URI,selection,null);

        if(rowsDeleted > 0)
            result = true;

        return result;
    }

    public  boolean deleteItem(int itemCode){
        boolean result = false;

        String selection = "code = \"" +itemCode + "\"";
        int rowsDeleted = myCR.delete(ItemContentProvider.CONTENT_URI,selection,null);

        if(rowsDeleted > 0)
            result = true;

        return result;
    }

    public ArrayList<Item> selectAllItems(){
        ArrayList<Item> itemlist=new ArrayList<>();
        String[] projection = {COLUMN_CODE, COLUMN_ITEMNAME,COLUMN_PRICE};

        String selection = "";
        Cursor cursor = myCR.query(ItemContentProvider.CONTENT_URI, projection,null, null,null);

        while (cursor.moveToNext()) {
            itemlist.add(new Item(Integer.parseInt(cursor.getString(0)),cursor.getString(1),Integer.parseInt(cursor.getString(2))));
        }
        cursor.close();

        return itemlist;
    }
    public ArrayList<Item> selectItemsByItemName(String itemName){
        ArrayList<Item> itemlist=new ArrayList<>();
        String[] projection = {COLUMN_CODE, COLUMN_ITEMNAME,COLUMN_PRICE};

        String selection = "itemname = \"" +itemName + "\"";
        Cursor cursor = myCR.query(ItemContentProvider.CONTENT_URI, projection,null, null,null);

        while (cursor.moveToNext()) {
            itemlist.add(new Item(Integer.parseInt(cursor.getString(0)),cursor.getString(1),Integer.parseInt(cursor.getString(2))));
        }
        cursor.close();

        return itemlist;
    }
    public ArrayList<Product> selectAllProducts(){
        ArrayList<Product> productList=new ArrayList<>();
        String[] projection = {COLUMN_ID, COLUMN_PRODUCTNAME,COLUMN_DESCRIPTION};

        String selection = "";
        Cursor cursor = myCR.query(MyContentProvider.CONTENT_URI, projection,null, null,null);

        while (cursor.moveToNext()) {
            productList.add(new Product(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(1)));
        }
        cursor.close();

        return productList;
    }


}

