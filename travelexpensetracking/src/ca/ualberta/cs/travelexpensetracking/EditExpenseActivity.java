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


public class EditExpenseActivity extends Activity {
	private static final String FILENAME = "save.sav";
	private ClaimList Claims ;
	private Expense currentExpense;
	private int claimID ;
	private String claimIDstr;
	private int expenseID;
	private String expenseIDstr;

	
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
	private EditText editTextEditExpenseName;
	private EditText editTextEditExpenseDateDay;
	private EditText editTextEditExpenseDateMonth;
	private EditText editTextEditExpenseDateYear;
	private EditText editTextEditExpenseCategory;
	private EditText editTextEditExpenseAmoutnSpend;
	private EditText editTextEditExpenseUnitOfCurrency;
	private EditText editTextEditExpenseDescription;
	
	private Button buttonEditExpenseCancle;
	private Button buttonEditExpenseDone;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		// get intent
		Intent intent = getIntent();
		// get current claim ID and expense ID
		claimIDstr = intent.getStringExtra("claimID");
		claimID = Integer.parseInt(claimIDstr);
		expenseIDstr = intent.getStringExtra("expenseID");
		expenseID= Integer.parseInt(expenseIDstr);
		// load widgets
		editTextEditExpenseName = (EditText)findViewById(R.id.editTextEditExpenseName);
		editTextEditExpenseDateDay = (EditText)findViewById(R.id.editTextEditExpenseDateDay);
		editTextEditExpenseDateMonth = (EditText)findViewById(R.id.editTextEditExpenseDateMonth);
		editTextEditExpenseDateYear = (EditText)findViewById(R.id.editTextEditExpenseDateYear);
		editTextEditExpenseCategory = (EditText)findViewById(R.id.editTextEditExpenseCategory);
		editTextEditExpenseAmoutnSpend = (EditText)findViewById(R.id.editTextEditExpenseAmoutnSpend);
		editTextEditExpenseUnitOfCurrency = (EditText)findViewById(R.id.editTextEditExpenseUnitOfCurrency);
		editTextEditExpenseDescription = (EditText)findViewById(R.id.editTextEditExpenseDescription);
		buttonEditExpenseCancle = (Button)findViewById(R.id.buttonEditExpenseCancle);
		buttonEditExpenseDone = (Button)findViewById(R.id.buttonEditExpenseDone);
		
	}
	
	
	
	@Override
	protected void onStart() {
		super.onStart();
		// load claims
		Claims = this.loadFromFile();
		// get current expense
		currentExpense = Claims.getClaimList().get(claimID).getExpenseList().get(expenseID);
		// display current data
		editTextEditExpenseName.setText( currentExpense.getName());
		editTextEditExpenseCategory.setText(currentExpense.getCategory());
		editTextEditExpenseDescription.setText(currentExpense.getTextualDescription());
		editTextEditExpenseAmoutnSpend.setText(String.valueOf(currentExpense.getAmountSpent()));
		editTextEditExpenseUnitOfCurrency.setText(currentExpense.getUnitOfCurrency());
		
		// set on click listener for done button
		buttonEditExpenseDone.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
					Intent intentDone = new Intent();
					
					// get user input text	
					newExpenseName = editTextEditExpenseName.getText().toString();
					category = editTextEditExpenseCategory.getText().toString();
					description = editTextEditExpenseDescription.getText().toString();
					currencyUnit = editTextEditExpenseUnitOfCurrency.getText().toString();
					dateDay = Integer.parseInt(editTextEditExpenseDateDay.getText().toString());
					dateMonth = Integer.parseInt(editTextEditExpenseDateMonth.getText().toString());
					dateYear = Integer.parseInt(editTextEditExpenseDateYear.getText().toString());
					amountSpend = Integer.parseInt(editTextEditExpenseAmoutnSpend.getText().toString());
					// TODO: exception handle
					

					// set name
					currentExpense.setName(newExpenseName);
					// set description
					currentExpense.setTextualDescription(description);
					// set date
					Calendar date = Calendar.getInstance();
					date.set(dateYear, dateMonth, dateDay);
					currentExpense.setDate(date);
					// set category
					currentExpense.setCategory(category);
					// set currency unit
					currentExpense.setUnitOfCurrency(currencyUnit);
					// set amount spend
					currentExpense.setAmountSpent(amountSpend);
					// add new expense
					
				
		
	
				
					// save claim id
					intentDone.putExtra("claimID", claimIDstr);
					// save in file
					
					// toast for testing
					//Toast.makeText(EditExpenseActivity.this, currentExpense.getName(), Toast.LENGTH_LONG).show(); 
					saveInFile();
					intentDone.setClass(EditExpenseActivity.this,ClaimActivity.class);
					EditExpenseActivity.this.startActivity(intentDone);
					
					
			}
			});
		buttonEditExpenseCancle.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
					Intent intentCancle = new Intent();
					intentCancle.putExtra("claimID", claimIDstr);
					intentCancle.setClass(EditExpenseActivity.this,ClaimActivity.class);
					EditExpenseActivity.this.startActivity(intentCancle);
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

		
	
