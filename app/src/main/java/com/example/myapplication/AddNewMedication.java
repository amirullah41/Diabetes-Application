package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddNewMedication extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button addTime, addDate;
    int hour, minute, positionOfSelectedDataFromSpinner;
    DatePickerDialog datePickerDialog;
    String medicine, chosenTime, chosenDate, newDay, newMonth;
    myDbAdapter db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_medication);

        db = new myDbAdapter(this);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.MedicationArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        initDatePicker();
        addTime = findViewById(R.id.time);
        addDate = findViewById(R.id.date);
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

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year) {
        if(day < 10){
            newDay = "0"+day;
        }else{
            newDay = String.valueOf(day);
        }
        if (month < 10){
            newMonth = "0"+month;
        }else{
            newMonth = String.valueOf(month);
        }
        return newDay + "/" + newMonth + "/" + year;
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void popTimePicker(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int userPickedHour, int userPickedMinute)
            {
                hour = userPickedHour;
                minute = userPickedMinute;
                addTime.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
                chosenTime = String.format(Locale.getDefault(), "%02d:%02d",hour, minute);
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        medicine = (String) adapterView.getItemAtPosition(i);
        Toast.makeText(this, ""+ medicine, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(this, "Please pick a medication", Toast.LENGTH_SHORT).show();
    }

    public void submitMedication(View view) {
        if(db.insertMedication(medicine, chosenTime, chosenDate)) {
            Toast.makeText(this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, ""+ medicine, Toast.LENGTH_SHORT).show();
        }
    }
}