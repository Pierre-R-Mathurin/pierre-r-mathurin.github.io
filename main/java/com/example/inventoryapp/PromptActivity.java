package com.example.inventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PromptActivity extends AppCompatActivity {

    private int RECIEVE_SMS_PERMISSION_CODE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);
        Button grant_permission =(Button) findViewById(R.id.grant_permission_btn);

        grant_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekPermission(v);
                //Toast.makeText(PromptActivity.this,"I was clicked",Toast.LENGTH_SHORT).show();

            }
        });
        if (ContextCompat.checkSelfPermission(
                PromptActivity.this, Manifest.permission.SEND_SMS) ==
                PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(PromptActivity.this, databaseActivity.class);
            startActivity(intent);
        }
    }

    public void seekPermission(View view) {
        if (ContextCompat.checkSelfPermission(
                PromptActivity.this, Manifest.permission.SEND_SMS) ==
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(PromptActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PromptActivity.this, databaseActivity.class);
            startActivity(intent);
        } else{

            if (ActivityCompat.shouldShowRequestPermissionRationale(PromptActivity.this, Manifest.permission.SEND_SMS)) {

                new AlertDialog.Builder(this)
                        .setTitle("Permmision required")
                        .setMessage("This permission is required so that you may recieve automated system notifications")
                        .setPositiveButton("GRANT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(PromptActivity.this, new String[]{Manifest.permission.SEND_SMS}, RECIEVE_SMS_PERMISSION_CODE);
                            }
                        })
                        .setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, RECIEVE_SMS_PERMISSION_CODE);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      if(requestCode == RECIEVE_SMS_PERMISSION_CODE){
          if(grantResults.length >0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
              Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
          }else{
              Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
          }
      }
        Intent intent = new Intent(PromptActivity.this, databaseActivity.class);
        startActivity(intent);
    }
}
