package ca.ualberta.cs.travelexpensetracking;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.Calendar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddExpenseActivity extends Activity {
	private static final String FILENAME = "save.sav";
	private ClaimList Claims ;
	private Expense newExpense;
	private int claimID ;
	private String claimIDstr;
	// claim variables
	private String newExpenseName;
	private String category;
	private String description;
	private String currencyUnit;
	private int dateDay;
	private int dateMonth;
	private int dateYear;
	private int amountSpend;
	
	// claim widgets
	private EditText editTextExpenseName;
	private EditText editTextExpenseDateDay;
	private EditText editTextExpenseDateMonth;
	private EditText editTextExpenseDateYear;
	private EditText editTextExpenseCategory;
	private EditText editTextExpenseAmoutnSpend;
	private EditText editTextExpenseUnitOfCurrency;
	private EditText editTextExpenseDescription;
	
	private Button buttonNewExpenseCancle;
	private Button buttonNewExpenseDone;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_expense);
		// get intent
		Intent intent = getIntent();
		// load new expense name for intent
		newExpenseName= intent.getStringExtra("newExpenseName");
		// get current claim ID
		claimIDstr = intent.getStringExtra("claimID");
		claimID = Integer.parseInt(claimIDstr);
		
		// load widgets
		editTextExpenseName = (EditText)findViewById(R.id.editTextExpenseName);
		editTextExpenseDateDay = (EditText)findViewById(R.id.editTextExpenseDateDay);
		editTextExpenseDateMonth = (EditText)findViewById(R.id.editTextExpenseDateMonth);
		editTextExpenseDateYear = (EditText)findViewById(R.id.editTextExpenseDateYear);
		editTextExpenseCategory = (EditText)findViewById(R.id.editTextExpenseCategory);
		editTextExpenseAmoutnSpend = (EditText)findViewById(R.id.editTextExpenseAmoutnSpend);
		editTextExpenseUnitOfCurrency = (EditText)findViewById(R.id.editTextExpenseUnitOfCurrency);
		editTextExpenseDescription = (EditText)findViewById(R.id.editTextExpenseDescription);
		buttonNewExpenseCancle = (Button)findViewById(R.id.buttonNewExpenseCancle);
		buttonNewExpenseDone = (Button)findViewById(R.id.buttonNewExpenseDone);
		
	}
	
	
	
	@Override
	protected void onStart() {
		super.onStart();
		// load claims
		Claims = this.loadFromFile();
		// display expense name
		editTextExpenseName.setText(newExpenseName);
		
		// set on click listener for done button
		buttonNewExpenseDone.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
					Intent intentDone = new Intent();
					
					// get user input text	
					newExpenseName = editTextExpenseName.getText().toString();
					category = editTextExpenseCategory.getText().toString();
					description = editTextExpenseDescription.getText().toString();
					currencyUnit = editTextExpenseUnitOfCurrency.getText().toString();
					dateDay = Integer.parseInt(editTextExpenseDateDay.getText().toString());
					dateMonth = Integer.parseInt(editTextExpenseDateMonth.getText().toString());
					dateYear = Integer.parseInt(editTextExpenseDateYear.getText().toString());
					amountSpend = Integer.parseInt(editTextExpenseAmoutnSpend.getText().toString());
					// TODO: exception handle
					
					// create new expense with user input information
					newExpense = new Expense(newExpenseName);
					// set description
					newExpense.setTextualDescription(description);
					// set date
					Calendar date = Calendar.getInstance();
					date.set(dateYear, dateMonth, dateDay);
					newExpense.setDate(date);
					// set category
					newExpense.setCategory(category);
					// set currency unit
					newExpense.setUnitOfCurrency(currencyUnit);
					// set amount spend
					newExpense.setAmountSpent(amountSpend);
					// add new expense
					
				
					Claim currentClaim = Claims.getClaimList().get(claimID);
					currentClaim.addExpense(newExpense);
				
					// save claim id
					intentDone.putExtra("claimID", claimIDstr);
					// save in file
					
					//Toast.makeText(AddExpenseActivity.this, newExpense.getName(), Toast.LENGTH_LONG).show(); 
					saveInFile();
					intentDone.setClass(AddExpenseActivity.this,ClaimActivity.class);
					AddExpenseActivity.this.startActivity(intentDone);
					
					
			}
			});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_expense, menu);
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
	
	//load claim list from file
		private ClaimList loadFromFile(){
			Gson gson = new Gson();
			Claims = new  ClaimList();
			try{
				FileInputStream fis = openFileInput(FILENAME);
				InputStreamReader in = new InputStreamReader(fis);
				// Taken form Gson java doc
				Type typeOfT = new TypeToken<ClaimList>(){}.getType();
				Claims = gson.fromJson(in, typeOfT);
				fis.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Claims;
		}
		
		
		//save claim list from file
		private void saveInFile(){
		Gson gson = new Gson();
			try {
				FileOutputStream fos = openFileOutput(FILENAME, 0);
				OutputStreamWriter osw = new OutputStreamWriter(fos);
				gson.toJson(Claims, osw);
				osw.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
		}

		
	
