package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button login_btn,signup_btn;
    EditText username,password;
    TextView forgot_password,user_state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=(EditText)findViewById(R.id.username_input);
        password=(EditText)findViewById(R.id.password_input);
        login_btn =(Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput(v)) {
                   signIn(v);
                }
            }
        });
        signup_btn =(Button) findViewById(R.id.signup_btn);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput(v)) {
                    register(v);
                }
            }
        });
        forgot_password =(TextView)findViewById(R.id.forgot_password_link);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "This feature will be implemented soon", Toast.LENGTH_SHORT).show();
            }
        });
        user_state =(TextView)findViewById(R.id.user_state);
    }
    public void signIn(View view){
        String username_in = username.getText().toString();
        String password_in =password.getText().toString();
        MyDBHandler db =new MyDBHandler(this,null,null,1);

        User user = db.findUser(username_in,password_in);
        if(user !=null) {
            Intent intent = new Intent(MainActivity.this, PromptActivity.class);
            startActivity(intent);
        }else{
            user_state.setText("User Not found, Register to login");
        }
        username.setText("");
        password.setText("");

    }

    public void register(View view){
        String username_in = username.getText().toString();
        String password_in =password.getText().toString();
        MyDBHandler db =new MyDBHandler(this,null,null,1);
        User user = new User(username_in,password_in);
        db.addUser(user);
        if(db.addUser(user)) {
            user_state.setText("You are now registered to the database, Login to continue");
        }else{
            user_state.setText("Registration error, please try again.");
        }
        username.setText("");
        password.setText("");
    }

    public boolean validateInput(View v){

        if(username.getText().toString().isEmpty() ||password.getText().toString().isEmpty()){
            Toast.makeText(this, "Please Fill in All the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }
}