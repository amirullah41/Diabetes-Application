package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class UpdateMedication extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button addTime, addDate, updateMeds;
    int hour, minute, positionOfSelectedDataFromSpinner;
    DatePickerDialog datePickerDialog;
    String medicine, chosenTime, chosenDate, ID, medName, medDesc, medTime, medDate, newDay, newMonth;
    myDbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medication);

        updateMeds = findViewById(R.id.submitMedication1);
        updateMeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMedication();
            }
        });

        ID = getIntent().getExtras().getString("ID");
        medName = getIntent().getStringExtra("name");
        medDesc = getIntent().getStringExtra("desc");
        medTime = getIntent().getStringExtra("time");
        medDate = getIntent().getStringExtra("date");


        db = new myDbAdapter(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.MedicationArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        initDatePicker();
        addTime = findViewById(R.id.time1);
        addDate = findViewById(R.id.date1);

        addTime.setText(medTime);
        addDate.setText(medDate);

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
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
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

    }

    public void updateMedication() {

        if(medicine.equals("")||chosenDate.equals("")||chosenTime.equals("")) {
            Toast.makeText(this, "Please fill in all fields to update this entry!", Toast.LENGTH_SHORT).show();
        }else {
            if(db.updateMedication(ID, medicine, chosenTime, chosenDate)) {
                Toast.makeText(this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, ""+ medicine, Toast.LENGTH_SHORT).show();
            }
        }
    }
}