package ca.ualberta.cs.travelexpensetracking;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class ClaimActivity extends Activity {
	
	private Button addExpenseButton = null;
	// creating new claim for testing
	private Claim testClaim =  new Claim("test");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim);
		// add button listener
		addExpenseButton = (Button)findViewById(R.id.addExpenseButton);
		addExpenseButton.setOnClickListener(new addExpenseButtonListener());
		
		// create array adapter
		ArrayList<Expense> testExpenseList= testClaim.getExpenseList();
		ArrayAdapter<Expense> expenseAdapter = new ArrayAdapter<Expense>(this, R.layout.expense_item, testExpenseList);
		
		
		
		
	}

	class addExpenseButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// create a intent object
			Intent intent = new Intent();
			intent.setClass(ClaimActivity.this, AddExpenseActivity.class);
			ClaimActivity.this.startActivity(intent);
		}
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.claim, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
