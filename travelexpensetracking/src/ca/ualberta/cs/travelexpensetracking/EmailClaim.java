package ca.ualberta.cs.travelexpensetracking;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class EmailClaim extends Activity {
	private ClaimListModel Claims ;
	private ClaimModel currentClaim;
	private String claimIDstr;
	private int claimID;
	private String claimName;
	private String startDate;
	private String endDate;
	private String description;
	final Context context = this;
	private String test;
	private ArrayList<ExpenseModel> expenseList;
	private EditText editTextEmailAddress;
	private Button buttonEmailCancle;
	private Button buttonEmailDone;
	private static final String FILENAME = "save.sav";
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_claim);		
		// get current claim ID
		Intent intent = getIntent();
		claimIDstr = intent.getStringExtra("claimID");
		claimID = Integer.parseInt(claimIDstr);
		// load widgets
		editTextEmailAddress = (EditText)findViewById(R.id.editTextEmailAddress);
		buttonEmailCancle = (Button)findViewById(R.id.buttonEmailCancle);
		buttonEmailDone = (Button)findViewById(R.id.buttonEmailDone);
		
		
		buttonEmailDone.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {

					Intent emailIntent = new Intent(Intent.ACTION_SEND);
					emailIntent.setType("plain/text");
					String[] recipients = {editTextEmailAddress.getText().toString()};
					//set email intent
					//emailIntent.setType("message/rfc822") ; // 
					emailIntent.putExtra(Intent.EXTRA_EMAIL , recipients);   
					emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Travel Expense Claim: "+currentClaim.getClaimName());  
					emailIntent.putExtra(Intent.EXTRA_TEXT,formatOutput());  
					try {
					    startActivity(Intent.createChooser(emailIntent, "Sending mail..."));
					} catch (android.content.ActivityNotFoundException ex) {
					    Toast.makeText(EmailClaim.this, "Error: No email application installed!", Toast.LENGTH_SHORT).show();
					}
					// save in file
					saveInFile();
				
					
				}
			});
		
		
		buttonEmailCancle.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				AlertDialog.Builder ADB = new AlertDialog.Builder(context);
				ADB.setTitle("Exit");
				ADB.setMessage("Are you sure you want to exit ?");
				ADB.setNegativeButton(android.R.string.no, null);
		        ADB .setPositiveButton(android.R.string.yes, new OnClickListener() {
		            public void onClick(DialogInterface arg0, int arg1) {
		            	Intent intentBackPressed = new Intent();
		            	intentBackPressed.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        		intentBackPressed.setClass(EmailClaim.this, MainActivity.class);
		        		EmailClaim.this.startActivity(intentBackPressed);
		            }
		        }).create().show();	
		
		}
		});
		
	}
		
	
	

	@Override
	protected void onStart() {
		super.onStart();
		// load claims
		Claims = this.loadFromFile();	
		// get current claim
		currentClaim = Claims.getClaimList().get(claimID);
		
		
		// email preview
		TextView emailPreview = (TextView)findViewById(R.id.emailPreview);
		emailPreview.setText(formatOutput());
		
		Toast.makeText(getApplicationContext(), test, Toast.LENGTH_LONG).show();
	}
	
	
	
	//format output as a string for email
		public String formatOutput() {
		StringBuffer sb = new StringBuffer();
		// set title
		sb.append("Travel Expense Claim \n");
		// set claim name
		claimName = currentClaim.getClaimName();
		sb.append("Claim Name: "+claimName+"\n");
		// set total amount
		sb.append("Total Amount: "+currentClaim.getSpendStr()+"\n");
		// set start date
		startDate= String.format(Locale.ENGLISH,"%1$tb/%1$td/%1$tY", currentClaim.getStartDate());
		sb.append("Start Date: "+startDate+"\n");
		// set end date
		endDate= String.format(Locale.ENGLISH,"%1$tb/%1$td/%1$tY", currentClaim.getEndDate());
		sb.append("Start Date: "+endDate+"\n");
		// description
		description = currentClaim.getDescription();
		sb.append("Description: "+description+"\n");
		sb.append("-------------------Expenses----------------------\n");
		// add expense loop
		expenseList = currentClaim.getExpenseList();
		for (int i = 0; i < currentClaim.getExpenseList().size();i++){
		// add expense ID
			sb.append("    ");
			String expenseId = String.valueOf(i+1);
			sb.append(expenseId+".");
			sb.append("\n");
		// add expense name
			sb.append("    ");
			String expenseName = expenseList.get(i).getName();
			sb.append("Name: "+expenseName);
			sb.append("\n");
		// add expense date
			sb.append("    ");
			String expenseDate = String.format(Locale.ENGLISH,"%1$tb/%1$td/%1$tY", expenseList.get(i).getDate());
			sb.append("Date: "+expenseDate);
			sb.append("\n");
		// add category
			sb.append("    ");
			String expenseCategory = expenseList.get(i).getCategory();
			sb.append("Category: "+expenseCategory);
			sb.append("\n");
		// add amount spend and currency unit
			sb.append("    ");
			String expenseAmount =String.valueOf( expenseList.get(i).getAmountSpent());
			String expenseUnit = expenseList.get(i).getUnitOfCurrency();
			sb.append("Amount Spend: "+expenseAmount+" "+expenseUnit);
			sb.append("\n");
		// add description
			sb.append("    ");
			String expenseDescription = expenseList.get(i).getTextualDescription();
			sb.append("Description: "+expenseDescription);
			sb.append("\n");
			sb.append("\n");
		
		}
		
		
		
		
		return sb.toString();
	}
		
		
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.email_claim, menu);
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
	private ClaimListModel loadFromFile(){
		Gson gson = new Gson();
		Claims = new  ClaimListModel();
		try{
			FileInputStream fis = openFileInput(FILENAME);
			InputStreamReader in = new InputStreamReader(fis);
			// Taken form Gson java doc
			Type typeOfT = new TypeToken<ClaimListModel>(){}.getType();
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
	
		
	// set on back pressed (exit without warning)
	@Override
	public void onBackPressed(){
		Intent intentBackPressed = new Intent();
		intentBackPressed.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intentBackPressed.setClass(EmailClaim.this, MainActivity.class);
		EmailClaim.this.startActivity(intentBackPressed);
	}
		

}
