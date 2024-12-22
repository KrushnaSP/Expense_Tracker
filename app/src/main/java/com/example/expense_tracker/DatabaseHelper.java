package com.example.expense_tracker;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
//The main reason for declaring this class is to create the interface for the sqlite database..
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "expense_tracker.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_EXPENSES = "expenses";

    // Common column names
    private static final String KEY_ID = "id";

    // Expenses table column names
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DATE = "date";

    // Table create statements
    private static final String CREATE_TABLE_EXPENSES = "CREATE TABLE "
            + TABLE_EXPENSES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CATEGORY + " TEXT," + KEY_AMOUNT + " REAL,"
            + KEY_DATE + " TEXT" + ")";
    //Running the queries..

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //Created the constructor for initialization..

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EXPENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    // Adding new expense
    public void insertExpense(String category, double amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY, category);
        values.put(KEY_AMOUNT, amount);
        values.put(KEY_DATE, date);

        long id = db.insert(TABLE_EXPENSES, null, values);
        Log.d(TAG, "Expense inserted into database: " + id);
        db.close(); // Closing database connection
    }

    // Getting all expenses
    public List<String> getAllExpenses() {
        List<String> expenseList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(KEY_AMOUNT));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
                String expense = "Category: " + category + ", Amount: Rs" + amount + ", Date: " + date;
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return expenseList;
    }

    // Deleting a single expense
    public void deleteExpense(String category, double amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, KEY_CATEGORY + " = ? AND " + KEY_AMOUNT + " = ? AND " + KEY_DATE + " = ?",
                new String[] { category, String.valueOf(amount), date });
        db.close();
        //here delete is the method which performs the delete operation and works similar to the
        // query i.e, delete from ... where col_name=...
    }
}




//Code made by Krushna Pisal 224033
