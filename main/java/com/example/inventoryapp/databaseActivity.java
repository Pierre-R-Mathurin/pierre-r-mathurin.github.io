package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class databaseActivity extends AppCompatActivity {
    private TableLayout tl;
    private EditText item_code_input,item_name_input,item_price_input;
    private Button  addCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

         tl = (TableLayout) findViewById(R.id.tableLayout);
         item_code_input = (EditText)findViewById(R.id.product_name_input);
         item_name_input = (EditText)findViewById(R.id.product_description_input);
         item_price_input = (EditText)findViewById(R.id.item_price_input);

         addCategory =(Button)findViewById(R.id.addCategory);
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(databaseActivity.this, ProductActivity.class);
                    startActivity(intent);

            }
        });

        populateTable();
    }

    public void AddToTable(View view){
        String e_itemcode=item_code_input.getText().toString(),
                e_itemname=item_name_input.getText().toString(),
                e_itemprice=item_price_input.getText().toString();
        if(e_itemcode.isEmpty() || e_itemname.isEmpty()||e_itemprice.isEmpty()) {
            Toast.makeText(this, "Please Fill in All the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        ImageView img =new ImageView(this);
        img.setImageResource(R.drawable.ic_action_delete);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(removeItem(v)) deleteRow(v);
            }
        });
        ImageView img1 =new ImageView(this);
        img1.setImageResource(R.drawable.ic_action_edit);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookUpItem(v);
            }
        });


        /**Add to databatabase**/

        MyDBHandler dbHandler = new MyDBHandler(this, null, null,1);

        int code= Integer.parseInt(e_itemcode);
        int price = Integer.parseInt(e_itemprice);

        Item item = new Item(code,e_itemname.toString(),price);

        dbHandler.addItem(item);
        /******/
        item_code_input.setText("");
        item_price_input.setText("");
        item_name_input.setText("");
        populateTable();
    }
    public void populateTable(){
        MyDBHandler dbHandler = new MyDBHandler(this, null, null,1);
        ArrayList<Item> itemList = dbHandler.selectAllItems();
        int count =tl.getChildCount();
        tl.removeViews(1,count-1);
        for(Item item:itemList) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tr.setBackgroundColor(Color.parseColor("#DAE8FC"));
            tr.setPadding(5, 5, 5, 5);

            TextView tx1 = new TextView(this);
            TextView tx2 = new TextView(this);
            TextView tx3 = new TextView(this);


            tx1.setText(String.valueOf(item.getCode()));

            tx2.setText(item.getItemName());

            tx3.setText(String.valueOf(item.getPrice()));
            ImageView img = new ImageView(this);
            img.setImageResource(R.drawable.ic_action_delete);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (removeItem(v)) deleteRow(v);
                }
            });
            ImageView img1 = new ImageView(this);
            img1.setImageResource(R.drawable.ic_action_edit);
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lookUpItem(v);
                }
            });

            tr.addView(tx1);
            tr.addView(tx2);
            tr.addView(tx3);
            tr.addView(img);
            tr.addView(img1);
            tl.addView(tr);
            countItems(item.getItemName());
        }
    }
    public void deleteRow(View v){
        View row =(View)v.getParent();
        ViewGroup container =((ViewGroup)row.getParent());
        container.removeView(row);
        container.invalidate();
    }


    public void lookUpItem (View view){
        MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);

        TableRow row =(TableRow)view.getParent();

        TextView textView = (TextView)row.getChildAt(0);
        Item item = dbHandler.findItem(Integer.parseInt(textView.getText().toString()));

        if(item != null){
            item_code_input.setText(String.valueOf(item.getCode()));
            item_name_input.setText(item.getItemName());
            item_price_input.setText(String.valueOf(item.getPrice()));
        }else{
            item_code_input.setText("No Match Found");
        }
    }


    public boolean removeItem (View view){
        MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);
        TableRow row =(TableRow)view.getParent();

        TextView textView = (TextView)row.getChildAt(0);
        boolean result = dbHandler.deleteItem(Integer.parseInt(textView.getText().toString()));

        if(result){
            item_code_input.setText("Record Deleted");
            item_name_input.setText("");
            item_price_input.setText("");
        }else
            item_code_input.setText("Delete Unsuccessful");

        return result;
    }
    public String countItems(String productName){
        int count=0;
        ArrayList<Item> items =new ArrayList<>();
        String items_running_low ="";
        for(Item item: items){
            count++;
        }
        if(count<10){
            items_running_low =productName;
            sendSMS(productName);
        }
        return String.valueOf(count);
    }
    public void sendSMS(String item_name){
       String phoneNo = "+254727597119";     //txtphoneNo.getText().toString();
        String message = item_name+" Is Running out you need to restock";

        try{
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(phoneNo,null,message,null,null);
            Toast.makeText(databaseActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(databaseActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }
}
