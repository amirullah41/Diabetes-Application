package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class myDbAdapter {
    myDbHelper myhelper;
    SimpleDateFormat dateFormat;
    String date1;
    Calendar calendar;

    public myDbAdapter(Context context)
    {
        myhelper = new myDbHelper(context);
    }
    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "DiabetesAppDatabase";
        private static final String TABLE_NAME_LOGIN = "loginTable";
        private static final String TABLE_NAME_MEDICATION = "medication";
        private static final String TABLE_NAME_MEALS = "meals";

        private static final int DATABASE_Version = 1;

        private static final String UID="_id";
        private static final String USERNAME = "Name";
        private static final String PASSWORD= "Password";
        private static final String FULL_NAME= "FullName";
        private static final String CID= "_cid";
        private static final String MEDICATION_NAME= "MedicationName";
        private static final String MEDICATION_TIME= "MedicationTime";
        private static final String MEDICATION_DATE= "MedicationDate";
        private static final String MID= "_mid";
        private static final String MEAL_NAME= "mealName";
        private static final String MEAL_TIME = "mealTime";
        private static final String MEAL_DATE = "mealDate";


        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE "+TABLE_NAME_LOGIN+ " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+USERNAME+" VARCHAR(255),"+ PASSWORD+" VARCHAR(225),"+ FULL_NAME+" VARCHAR(225));");
                db.execSQL("CREATE TABLE "+TABLE_NAME_MEDICATION+ " ("+CID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+MEDICATION_NAME+" VARCHAR(255),"+MEDICATION_DATE+" VARCHAR(225),"+ MEDICATION_TIME+" VARCHAR(225));");
                db.execSQL( "CREATE TABLE "+TABLE_NAME_MEALS+ " ("+MID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+MEAL_NAME+" VARCHAR(255),"+MEAL_TIME+" VARCHAR(225),"+MEAL_DATE+" VARCHAR(225));");
            } catch (Exception e) {
                Message.message(context,""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.message(context,"OnUpgrade");
                db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_LOGIN);
                db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_MEDICATION);
                db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_MEALS);
                onCreate(db);
            }catch (Exception e) {
                Message.message(context,""+e);
            }
        }
    }

    public Boolean insertNewAccount(String username, String password, String fullName)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", username);
        contentValues.put("Password", password);
        contentValues.put("Fullname", fullName);
        long id = dbb.insert(myDbHelper.TABLE_NAME_LOGIN, null , contentValues);
        if(id==-1) return false;
        else
            return true;
    }

    public Boolean loginAccount(String username, String password){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + myDbHelper.TABLE_NAME_LOGIN + " where Name = ? and Password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean insertMedication(String medicationName, String medicationDate, String medicationTime){
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MedicationName", medicationName);
        contentValues.put("MedicationTime", medicationDate);
        contentValues.put("MedicationDate", medicationTime);
        long id = dbb.insert(myDbHelper.TABLE_NAME_MEDICATION, null , contentValues);
        if(id==-1) return false;
        else
            return true;
    }

    public String getMedication()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.CID,"MedicationName","MedicationTime", "MedicationDate"};
        Cursor cursor
                =db.query(myDbHelper.TABLE_NAME_MEDICATION,columns,null,null,null,null,myDbHelper.MEDICATION_DATE+" ASC" );
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndexOrThrow(myDbHelper.CID));
            String name
                    =cursor.getString(cursor.getColumnIndexOrThrow("MedicationName"));
            String  time
                    =cursor.getString(cursor.getColumnIndexOrThrow("MedicationTime"));
            String  date
                    =cursor.getString(cursor.getColumnIndexOrThrow("MedicationDate"));
            buffer.append(cid + "   " + name + "   " + time + "   " + date +" \n" +" \n");
        }
        return buffer.toString();
    }

    public Boolean deleteMedication(String ID)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={ID};
        int count =db.delete(myDbHelper.TABLE_NAME_MEDICATION ,myDbHelper.CID+" = ?",whereArgs);
        if (count == -1){
            return false;
        }else{
            return true;
        }
    }

    public Boolean completedMedication(String ID) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("MedicationName", "COMPLETED");
        cv.put("MedicationTime", "COMPLETED");
        cv.put("MedicationDate", "COMPLETED");
        int rows = db.update(myDbHelper.TABLE_NAME_MEDICATION, cv, "_cid = ?", new String[]{String.valueOf(ID)});
        if (rows == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<String> getMedicationToUpdate(String ID) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.CID,"MedicationName","MedicationTime", "MedicationDate"};
        Cursor cursor
                =db.query(myDbHelper.TABLE_NAME_MEDICATION,columns,null,null,null,null,myDbHelper.MEDICATION_DATE+" ASC" );
        StringBuffer buffer= new StringBuffer();
        ArrayList<String> specific = new ArrayList<String>();
        while (cursor.moveToNext()) {
            int cid =cursor.getInt(cursor.getColumnIndexOrThrow(myDbHelper.CID));
            String name
                    =cursor.getString(cursor.getColumnIndexOrThrow("MedicationName"));
            String  time
                    =cursor.getString(cursor.getColumnIndexOrThrow("MedicationTime"));
            String  date
                    =cursor.getString(cursor.getColumnIndexOrThrow("MedicationDate"));
            if(cid == Integer.parseInt(ID)){
                specific.add(Integer.toString(cid));
                specific.add(name);
                specific.add(time);
                specific.add(date);
            }
        }
        return specific;
    }

    public Boolean updateMedication(String ID, String updatedMed, String updatedDate, String updatedTime){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("MedicationName", updatedMed);
        cv.put("MedicationTime", updatedDate);
        cv.put("MedicationDate", updatedTime);
        int rows = db.update(myDbHelper.TABLE_NAME_MEDICATION, cv, "_cid = ?", new String[] {String.valueOf(ID)});
        if (rows == -1){
            return false;
        }else{
            return true;
        }
    }

    public Boolean insertMeal(String mealName, String mealTime, String mealDate){
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mealName", mealName);
        contentValues.put("mealTime", mealTime);
        contentValues.put("mealDate", mealDate);
        long id = dbb.insert(myDbHelper.TABLE_NAME_MEALS, null , contentValues);
        if(id==-1) return false;
        else
            return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<String> getMedicationInformationForToday(){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ArrayList<String> specific = new ArrayList<String>();
        String[] columns = {myDbHelper.CID,"MedicationName","MedicationTime", "MedicationDate"};
        Cursor cursor
                =db.query(myDbHelper.TABLE_NAME_MEDICATION,columns,null,null,null,null,myDbHelper.MEDICATION_DATE+" ASC" );
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date1 = dateFormat.format(calendar.getTime());
        while (cursor.moveToNext()) {
            String name
                    =cursor.getString(cursor.getColumnIndexOrThrow("MedicationName"));
            String  time
                    =cursor.getString(cursor.getColumnIndexOrThrow("MedicationTime"));
            String  date
                    =cursor.getString(cursor.getColumnIndexOrThrow("MedicationDate"));

            if(date.equals(date1)){
                specific.add(name+" \n");
                specific.add(time+" \n");
                specific.add(date+" \n");
            }
        }
        return specific;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<String> getMealInformationForToday(){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ArrayList<String> specific2 = new ArrayList<String>();
        String[] columns = {myDbHelper.MID,"mealName","mealTime", "mealDate"};
        Cursor cursor
                =db.query(myDbHelper.TABLE_NAME_MEALS,columns,null,null,null,null,"mealDate"+" ASC" );
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date1 = dateFormat.format(calendar.getTime());
        while (cursor.moveToNext()) {
            String name
                    =cursor.getString(cursor.getColumnIndexOrThrow("mealName"));
            String  time
                    =cursor.getString(cursor.getColumnIndexOrThrow("mealTime"));
            String  date
                    =cursor.getString(cursor.getColumnIndexOrThrow("mealDate"));

            if(date.equals(date1)){
                specific2.add(name+" \n");
                specific2.add(time+" \n");
                specific2.add(date+" \n");
            }
        }
        return specific2;
    }
}
