package ca.ualberta.cs.travelexpensetracking;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddClaimActivity extends Activity {
	
	private static final String FILENAME = "save.sav";
	private ClaimList Claims ;
	private Claim newClaim;
	final Context context = this;
	
	// claim variables
	private String newClaimName;
	private int startDateDay;
	private int startDateMonth;
	private int startDateYear;
	private int endDateDay;
	private int endDateMonth;
	private int endDateYear;
	private String status;
	private String description;
	
	// claim widgets
	private EditText editTextClaimName;
	private EditText editTextClaimStartDateDay;
	private EditText editTextClaimStartDateMonth;
	private EditText editTextClaimStartDateYear;
	private EditText editTextClaimEndDateDay;
	private EditText editTextClaimEndDateMonth;
	private EditText editTextClaimEndDateYear;

	private EditText editTextClaimDescription;
	private Spinner spinnerStatus;
	
	private Button buttonNewClaimDone;
	private Button buttonNewClaimCancle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_claim);
		// get intent
		Intent intent = getIntent();

		// load new claim name for intent
		newClaimName= intent.getStringExtra("newClaimName");
		
		
		// load widgets
		editTextClaimName = (EditText)findViewById(R.id.editTextClaimName);
		editTextClaimStartDateDay = (EditText)findViewById(R.id.editTextClaimStartDateDay);
		editTextClaimStartDateMonth = (EditText)findViewById(R.id.editTextClaimStartDateMonth);
		editTextClaimStartDateYear = (EditText)findViewById(R.id.editTextClaimStartDateYear);
		editTextClaimEndDateDay = (EditText)findViewById(R.id.editTextClaimEndDateDay);
		editTextClaimEndDateMonth = (EditText)findViewById(R.id.editTextClaimEndDateMonth);
		editTextClaimEndDateYear = (EditText)findViewById(R.id.editTextClaimEndDateYear);
		//editTextStatus = (EditText)findViewById(R.id.editTextStatus);
		spinnerStatus = (Spinner)findViewById(R.id.spinnerStatus);
		editTextClaimDescription = (EditText)findViewById(R.id.editTextClaimDescription);
		buttonNewClaimDone = (Button)findViewById(R.id.buttonNewClaimDone);
		buttonNewClaimCancle = (Button)findViewById(R.id.buttonNewClaimCancle);
		
		// set spinner 
		ArrayList<String> statusList = new ArrayList<String>();
		statusList.add("In progress");
		statusList.add("Submitted");
		statusList.add("Returned");
		statusList.add("Approved");
		// set adapter
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,statusList);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerStatus.setAdapter(spinnerAdapter);
		// set listener
		addListenerOnSpinnerItemSelection();
		}
		
		public void addListenerOnSpinnerItemSelection(){
			spinnerStatus.setOnItemSelectedListener(new StatusOnItemSelectedListener());
		}
	
	
		

	
	@Override
	protected void onStart(){
		super.onStart();
		// load claims
		Claims = this.loadFromFile();
		// display claim name
		editTextClaimName.setText(newClaimName);
				
		// set on click listener for done button
		buttonNewClaimDone.setOnClickListener(new View.OnClickListener(){
		@Override
		public void onClick(View v) {
				Intent intentDone = new Intent();
				
				// get user input text
				newClaimName = editTextClaimName.getText().toString();
				startDateDay = Integer.parseInt(editTextClaimStartDateDay.getText().toString());
				startDateMonth = Integer.parseInt(editTextClaimStartDateMonth.getText().toString());
				startDateYear = Integer.parseInt( editTextClaimStartDateYear.getText().toString());
				endDateDay = Integer.parseInt(editTextClaimEndDateDay.getText().toString());
				endDateMonth = Integer.parseInt( editTextClaimEndDateMonth.getText().toString());
				endDateYear = Integer.parseInt( editTextClaimEndDateYear.getText().toString());
				status = String.valueOf(spinnerStatus.getSelectedItem());
				description = editTextClaimDescription.getText().toString();
				// TODO: exception handle
				
				// TODO: status spinner 
				
				// create new claim with user input information
				// set claim name
				newClaim = new Claim(newClaimName);
				// set description
				newClaim.setDescription(description);
				// set start date
				Calendar startDate = Calendar.getInstance();
				startDate.set(startDateYear, startDateMonth, startDateDay);
				newClaim.setStartDate(startDate);
				// set end date
				Calendar endDate = Calendar.getInstance();
				endDate.set(endDateYear, endDateMonth, endDateDay);
				newClaim.setEndDate(endDate);
				
				// set status
				newClaim.setStatus(status);
				// set description
				newClaim.setDescription(description);
				
				// save the new claim into claim list
				Claims.addClaim(newClaim);
				
				// save in file
				saveInFile();
				// jump to main activity
				intentDone.setClass(AddClaimActivity.this,MainActivity.class);
				AddClaimActivity.this.startActivity(intentDone);
				
			}
		});
		

		// set on click listener for cancel button with warning
	buttonNewClaimCancle.setOnClickListener(new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			AlertDialog.Builder ADB = new AlertDialog.Builder(context);
			ADB.setTitle("Exit");
			ADB.setMessage("Are you sure you want to exit without saving?");
			ADB.setNegativeButton(android.R.string.no, null);
	        ADB .setPositiveButton(android.R.string.yes, new OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) {
	            	Intent intentBackPressed = new Intent();
	        		intentBackPressed.setClass(AddClaimActivity.this, MainActivity.class);
	        		AddClaimActivity.this.startActivity(intentBackPressed);
	            }
	        }).create().show();	
	
	}
	});
	
  };

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_claim, menu);
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
	
	// set one back pressed
	@Override
	public void onBackPressed(){
		new AlertDialog.Builder(this).setTitle("Exit")
		.setMessage("Are you sure you want to exit without saving?")
		.setNegativeButton(android.R.string.no, null)
        .setPositiveButton(android.R.string.yes, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            	Intent intentBackPressed = new Intent();
        		intentBackPressed.setClass(AddClaimActivity.this, MainActivity.class);
        		AddClaimActivity.this.startActivity(intentBackPressed);
            }
        }).create().show();	
	}
		
}
