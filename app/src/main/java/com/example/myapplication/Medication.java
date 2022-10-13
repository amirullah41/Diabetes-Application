package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Medication extends AppCompatActivity {
    //Initialising Variables
    Button updateText, updateButton, completeButton, deleteButton,buttonCap;
    TextView medText;
    EditText enterID;
    myDbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);

        //Assigning Variables


        enterID = findViewById(R.id.editID);
        updateButton = findViewById(R.id.updateBtn);
        buttonCap = findViewById(R.id.buttonCap);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEnteredId = enterID.getText().toString();
                if (userEnteredId.equals("")){
                    Toast.makeText(getApplicationContext(), "Please Enter an ID!", Toast.LENGTH_LONG).show();
                }else {
                    Intent updateMedicationEntry = new Intent(getApplicationContext(), UpdateMedication.class);
                    ArrayList<String> specific = new ArrayList<String>();
                    specific = db.getMedicationToUpdate(userEnteredId);
                    updateMedicationEntry.putExtra("ID", specific.get(0));
                    updateMedicationEntry.putExtra("name", specific.get(1));
                    updateMedicationEntry.putExtra("time", specific.get(2));
                    updateMedicationEntry.putExtra("date", specific.get(3));
                    startActivity(updateMedicationEntry);
                }
            }
        });

        completeButton = findViewById(R.id.completeButton);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEnteredId = enterID.getText().toString();
                if (userEnteredId.equals("")){
                    Toast.makeText(getApplicationContext(), "Please Enter an ID!", Toast.LENGTH_LONG).show();
                }else {
                    if(db.completedMedication(userEnteredId)){
                        Toast.makeText(getApplicationContext(), "Successfully marked as completed!", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        deleteButton = findViewById(R.id.deleteBtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEnteredId = enterID.getText().toString();
                if (userEnteredId.equals("")){
                    Toast.makeText(getApplicationContext(), "Please Enter an ID!", Toast.LENGTH_LONG).show();
                }else {
                    if(db.deleteMedication(userEnteredId)){
                        Toast.makeText(getApplicationContext(), "Deleted Successfully!", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Delete Unsuccessful.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        medText = findViewById(R.id.previous_medication);
        db = new myDbAdapter(this);

        updateText = findViewById(R.id.updateButton);
        medText.setText(db.getMedication());
        updateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medText.setText(db.getMedication());
            }
        });

        buttonCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent capMed = new Intent(getApplicationContext(), CaptureMedication.class);
                startActivity(capMed);
            }
        });
    }
    public void addMed(View view) {
        Intent addMed = new Intent(this, AddNewMedication.class);
        startActivity(addMed);
    }



}