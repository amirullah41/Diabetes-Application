package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    myDbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);
        db = new myDbAdapter(this);
    }

    public void registerBtn(View view) {
        Intent RegisterScreen = new Intent(this, RegisterScreen.class);
        startActivity(RegisterScreen);
    }

    public void loginBtn(View view) {

        String loginUsernameEntry = loginUsername.getText().toString();
        String loginPasswordEntry = loginPassword.getText().toString();
        Intent Homepage = new Intent(this, Homepage.class);


        if(loginUsernameEntry.equals("") || loginPasswordEntry.equals("")){
            Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show();
        }else {
            if(db.loginAccount(loginUsernameEntry, loginPasswordEntry)){
                Toast loginSuccess = Toast.makeText(this, "Successfully Logged in!", Toast.LENGTH_LONG);
                loginSuccess.show();
                startActivity(Homepage);
            }else{
                Toast loginFailed = Toast.makeText(this, "Login Failed!", Toast.LENGTH_LONG);
                loginFailed.show();
            }
        }

    }


}