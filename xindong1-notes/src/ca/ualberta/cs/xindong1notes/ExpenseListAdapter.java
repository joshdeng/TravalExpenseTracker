package ca.ualberta.cs.xindong1notes;

import java.util.ArrayList;

import ca.ualberta.cs.travelexpensetracking.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

// list adapter for expense list view
public class ExpenseListAdapter extends ArrayAdapter<ExpenseModel> {

	public ExpenseListAdapter(Context context, ArrayList<ExpenseModel> users) {
	  super(context, 0, users);
}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
    // Get the data item for this position
    ExpenseModel expense = getItem(position);    
    // Check if an existing view is being reused, otherwise inflate the view
    if (convertView == null) {
       convertView = LayoutInflater.from(getContext()).inflate(R.layout.expense_list_item, parent, false);
    }
    // Lookup template view for data population
    TextView TextViewExpenseName = (TextView) convertView.findViewById(R.id.TextViewExpenseName);
    TextView TextViewExpenseCurrency = (TextView) convertView.findViewById(R.id.TextViewExpenseCurrency);
    // Populate the data into the template view using the data object
    TextViewExpenseName.setText(expense.getName());
    TextViewExpenseCurrency.setText(expense.getSpendStr());
    // Return the completed view to render on screen
    return convertView;
}
	
	
	
	
	
}
