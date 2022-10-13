package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class Meals extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button addMeal, addDate;
    myDbAdapter db;
    String mealTime, chosenDate, newDay, newMonth;
    EditText mealInput;
    int hour, minute, positionOfSelectedDataFromSpinner;
    DatePickerDialog datePickerDialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);

        db = new myDbAdapter(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        mealInput = findViewById(R.id.mealInput);
        addMeal = findViewById(R.id.addMeal);
        addDate = findViewById(R.id.addMealDate);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.MealTimeArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        initDatePicker();
        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog2.show();
            }
        });
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                addDate.setText(date);
                chosenDate = date;
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog2 = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year) {

        if(day < 10){
            newDay = "0"+day;
        } else{
            newDay = String.valueOf(day);
        }

        if (month < 10){
            newMonth = "0"+month;
        } else{
            newMonth = String.valueOf(month);
        }
        return newDay + "/" + newMonth + "/" + year;
    }

    public void openDatePicker2(View view) {
        datePickerDialog2.show();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mealTime = (String) adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void addMeal(View view) {
        String userInputtedMeal = mealInput.getText().toString();
        if(userInputtedMeal.equals("") || mealTime.equals("") || chosenDate.equals("")){
            Toast.makeText(this, "Please fill in all Fields!", Toast.LENGTH_SHORT).show();
        }else {
            if(db.insertMeal(userInputtedMeal, mealTime, chosenDate)){
                Toast.makeText(this, "Meal Added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Meal not added!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}