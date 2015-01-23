package ca.ualberta.cs.travelexpensetracking;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.content.Context;

public class ExpenseListAdapter extends ArrayAdapter<Expense> {

	public ExpenseListAdapter(Context context, ArrayList<Expense> users) {
	  super(context, 0, users);
}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
    // Get the data item for this position
    Expense expense = getItem(position);    
    // Check if an existing view is being reused, otherwise inflate the view
    if (convertView == null) {
       convertView = LayoutInflater.from(getContext()).inflate(R.layout.expense_list_item, parent, false);
    }
    // Lookup view for data population
    TextView TextViewExpenseName = (TextView) convertView.findViewById(R.id.TextViewExpenseName);
    TextView TextViewExpenseCurrency = (TextView) convertView.findViewById(R.id.TextViewExpenseCurrency);
    // Populate the data into the template view using the data object
    TextViewExpenseName.setText(expense.getName());
    TextViewExpenseCurrency.setText(expense.getSpendStr());
    // Return the completed view to render on screen
    return convertView;
}
	
	
	
	
	
}
