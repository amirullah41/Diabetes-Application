package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Homepage extends AppCompatActivity {

    TextView todayItems;
    myDbAdapter db;
    ArrayList<String> todayMeds, todayMeals;
    String todayMed = "";
    String todayMeal = "";


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        db = new myDbAdapter(this);
        todayItems = findViewById(R.id.todaysPreview);
        todayItems.setMovementMethod(new ScrollingMovementMethod());

        //todayItems.setText(todayMed+ "\n" +todayMeal);


    }

    public void btn_Nutrition(View view) {
        Intent Nutrition = new Intent(this, Nutrition.class);
        startActivity(Nutrition);
    }

    public void btn_Meds(View view) {
        Intent medication = new Intent(this, Medication.class);
        startActivity(medication);
    }

    public void btn_Meals(View view) {
        Intent meals = new Intent(this, Meals.class);
        startActivity(meals);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateTextView(View view){
        todayMeds = db.getMedicationInformationForToday();
        todayMeals = db.getMealInformationForToday();

        for (int i=0; i<todayMeds.size(); i++){
            if (todayMed.contains(todayMeds.get(i))){
                Toast.makeText(this, "already added", Toast.LENGTH_SHORT).show();
            }else{
                todayMed += todayMeds.get(i);
            }
        }

        for (int i=0; i<todayMeals.size(); i++){
            if (todayMeal.contains(todayMeals.get(i))){
                Toast.makeText(this, "already added", Toast.LENGTH_SHORT).show();

            }else{
                todayMeal += todayMeals.get(i);
            }
        }

        if (!todayMeal.contains(todayMeal) || !todayMed.contains(todayMed)){
            Toast.makeText(this, "Shown", Toast.LENGTH_SHORT).show();
        }else{
            todayItems.setText(todayMed+ "\n" +todayMeal);

        }

    }
}