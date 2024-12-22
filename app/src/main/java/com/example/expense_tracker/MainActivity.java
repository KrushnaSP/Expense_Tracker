package com.example.expense_tracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText expenseAmountEditText;
    private EditText expenseDateEditText;
    private Spinner categorySpinner;
    private Button addExpenseButton;
    private ListView expenseListView;
    private Button generateReportButton;
    private List<String> expenseList;
    private ExpenseAdapter expenseAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        expenseAmountEditText = findViewById(R.id.expenseAmountEditText);
        expenseDateEditText = findViewById(R.id.expenseDateEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        addExpenseButton = findViewById(R.id.addExpenseButton);
        expenseListView = findViewById(R.id.expenseListView);
        generateReportButton = findViewById(R.id.generateReportButton);

        // here we have Set up category spinner
        setupCategorySpinner();

        // here we are initializing database helper as we are using the sqlite database
        databaseHelper = new DatabaseHelper(this);

        // Initialize expense list and adapter
        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(this, R.layout.list_item_expense, expenseList);
        expenseListView.setAdapter(expenseAdapter);

        // Load expenses from the database
        loadExpenses();

        // Add expense button click listener
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
            }
        });

        // Generate report button click listener
        generateReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateReport();
            }
        });
    }

    private void setupCategorySpinner() {
        //this function is used to initialize the spinner with the values..
        List<String> categories = new ArrayList<>();
        categories.add("Food");
        categories.add("Transportation");
        categories.add("Shopping");
        categories.add("Entertainment");
        categories.add("Utilities");
        categories.add("Others");

        //The below line creates an ArrayAdapter to bind the categories list to the spinner UI.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void addExpense() {
        //taking the values from the user..
        String amount = expenseAmountEditText.getText().toString().trim();
        String date = expenseDateEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();

        if (!amount.isEmpty() && !date.isEmpty()) {
            String expense = "Category: " + category + ", Amount: Rs" + amount + ", Date: " + date;
            //binding the data into a string on a dialog box
            expenseList.add(expense);
            expenseAdapter.notifyDataSetChanged();
            expenseAmountEditText.setText("");
            expenseDateEditText.setText(""); //clearing the fields

            // Save to database
            databaseHelper.insertExpense(category, Double.parseDouble(amount), date);
        }
    }

    private void loadExpenses() {
        List<String> expenses = databaseHelper.getAllExpenses();
        expenseList.addAll(expenses);
        expenseAdapter.notifyDataSetChanged();
        //This function notifies the adapter that the changes have been made
    }

    private void generateReport() {
        double totalExpenses = 0.0;
        double foodExpenses = 0.0;
        double transportationExpenses = 0.0;
        double shoppingExpenses = 0.0;
        double entertainmentExpenses = 0.0;
        double utilitiesExpenses = 0.0;
        double othersExpenses = 0.0;

        // Calculate total expenses and categorize them
        //Also here we are using the enhanced for loop for better display..
        for (String expense : expenseList) {
            String[] parts = expense.split(", Amount: Rs");
            //used for extracting the string into 2 parts..
            String category = parts[0].substring(parts[0].indexOf(":") + 2);
            //This separates the name of the category by the amount
            double amount = Double.parseDouble(parts[1].split(", Date: ")[0]);

            totalExpenses += amount;//adding the total sum of expense

            // Here we have used the switch statement to categorize the expense
            switch (category) {
                case "Food":
                    foodExpenses += amount;
                    break;
                case "Transportation":
                    transportationExpenses += amount;
                    break;
                case "Shopping":
                    shoppingExpenses += amount;
                    break;
                case "Entertainment":
                    entertainmentExpenses += amount;
                    break;
                case "Utilities":
                    utilitiesExpenses += amount;
                    break;
                case "Others":
                    othersExpenses += amount;
                    break;
                default:
                    break;
            }
        }

        // Display the report onto the dialog box but first taking it into the string
        String report = "Total Expenses: Rs" + totalExpenses + "\n\n" +
                "Food Expenses: Rs" + foodExpenses + "\n" +
                "Transportation Expenses: Rs" + transportationExpenses + "\n" +
                "Shopping Expenses: Rs" + shoppingExpenses + "\n" +
                "Entertainment Expenses: Rs" + entertainmentExpenses + "\n" +
                "Utilities Expenses: Rs" + utilitiesExpenses + "\n" +
                "Others Expenses: Rs" + othersExpenses;

        // Show report in an alert dialog
        //Here alert dialog is the subclass of the dialog used to display the dialog to the user
        //its value is set to ok and null which means it has only one value to exit the dialog bcx
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Financial Report");
        builder.setMessage(report);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}





