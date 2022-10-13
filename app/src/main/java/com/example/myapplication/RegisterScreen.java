package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterScreen extends AppCompatActivity {

    EditText fullName, userName, password;
    myDbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        db = new myDbAdapter(this);
        fullName = findViewById(R.id.fullName);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
    }

    public void registerBtn(View view) {
        String userFullName = fullName.getText().toString();
        String userUserName = userName.getText().toString();
        String userPassword = password.getText().toString();
        Intent Main = new Intent(this, MainActivity.class);


        if(userFullName.equals("") || userUserName.equals("") || userPassword.equals("")){
            Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show();
        }else {
            if (db.insertNewAccount(userUserName, userPassword, userFullName)) {
                Toast successMessage = Toast.makeText(this, "Successfully registered!", Toast.LENGTH_LONG);
                successMessage.show();
                startActivity(Main);
            }else {
                Toast failedMessage = Toast.makeText(this, "Didn't register, check all entries!", Toast.LENGTH_LONG);
                failedMessage.show();
            }
        }
    }
}