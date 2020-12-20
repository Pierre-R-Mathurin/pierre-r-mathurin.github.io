package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    private TableLayout tl;
    private EditText product_name,product_description;
    private Button addItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        product_name =(EditText)findViewById(R.id.product_name_input);
        product_description =(EditText)findViewById(R.id.product_description_input);
        tl=(TableLayout)findViewById(R.id.tableLayout);
        addItem =(Button)findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, databaseActivity.class);
                startActivity(intent);

            }
        });
        populateTable();
    }

    public void newProduct (View view){
        String e_productname=product_name.getText().toString(),e_productdesc =product_description.getText().toString();
        if( e_productname.isEmpty()||e_productdesc.isEmpty()) {
            Toast.makeText(this, "Please Fill in All the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        /**Add to databatabase**/

        MyDBHandler dbHandler = new MyDBHandler(this, null, null,1);

        Product product = new Product(e_productname,e_productdesc);

        dbHandler.addProduct(product);
        /******/

        product_name.setText("");
        product_description.setText("");
        populateTable();
    }
    public void lookUpProduct (View view){
        MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);
        TableRow row =(TableRow)view.getParent();
        TextView textView = (TextView)row.getChildAt(1);

        Product product = dbHandler.findProduct(textView.getText().toString());
        if(product != null){
            product_name.setText(product.getProductName());

            product_description.setText(product.getDescription());
        }else{
            product_name.setText("No Match Found");
        }
    }
    public boolean removeProduct (View view){
        MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);
        TableRow row =(TableRow)view.getParent();
        TextView textView = (TextView)row.getChildAt(1);

        boolean result = dbHandler.deleteProduct(textView.getText().toString());

        if(result){
            product_name.setText("Record Deleted");
            product_description.setText("");
        }else
            product_name.setText("No Match Found");
        return result;
    }
    public void deleteRow(View v){
        View row =(View)v.getParent();
        ViewGroup container =((ViewGroup)row.getParent());
        container.removeView(row);
        container.invalidate();
    }
    public void populateTable(){
        MyDBHandler dbHandler = new MyDBHandler(this, null, null,1);
        ArrayList<Product> productList = dbHandler.selectAllProducts();
        int count =tl.getChildCount();
        tl.removeViews(1,count-1);

        for(Product product:productList) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tr.setBackgroundColor(Color.parseColor("#DAE8FC"));
            tr.setPadding(5, 5, 5, 5);

            TextView tx1 = new TextView(this);
            TextView tx2 = new TextView(this);
            TextView tx3 = new TextView(this);


            tx1.setText(String.valueOf(product.getID()));

            tx2.setText(product.getProductName());

            tx3.setText(countItems(product.getProductName()));
            ImageView img = new ImageView(this);
            img.setImageResource(R.drawable.ic_action_delete);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (removeProduct(v)) deleteRow(v);
                }
            });
            ImageView img1 = new ImageView(this);
            img1.setImageResource(R.drawable.ic_action_edit);
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lookUpProduct(v);
                }
            });

            tr.addView(tx1);
            tr.addView(tx2);
            tr.addView(tx3);
            tr.addView(img);
            tr.addView(img1);
            tl.addView(tr);
        }
    }
    public String countItems(String productName){
        int count=0;
        ArrayList<Item> items =new ArrayList<>();
        for(Item item: items){
            count++;
        }
        sendSMS(productName);
        return String.valueOf(count);
    }
    public void sendSMS(String item_name){
        String phoneNo = "+254727597119";     //txtphoneNo.getText().toString();
        String message = item_name+" Is Running out you need to restock";

        try{
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(phoneNo,null,message,null,null);
            Toast.makeText(ProductActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(ProductActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }
}