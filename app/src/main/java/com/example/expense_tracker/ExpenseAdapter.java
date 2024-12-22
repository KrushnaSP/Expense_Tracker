package com.example.expense_tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<String> {

    private List<String> expenseList;
    private DatabaseHelper databaseHelper;

    public ExpenseAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.expenseList = objects;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_expense, parent, false);
        }

        String expense = getItem(position);

        TextView expenseTextView = convertView.findViewById(R.id.expenseTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        expenseTextView.setText(expense);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] parts = expense.split(", Amount: Rs");
                String category = parts[0].substring(parts[0].indexOf(":") + 2);
                double amount = Double.parseDouble(parts[1].split(", Date: ")[0]);
                String date = parts[1].split(", Date: ")[1];

                // Delete from database
                databaseHelper.deleteExpense(category, amount, date);

                // Remove from list and update UI
                expenseList.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}



