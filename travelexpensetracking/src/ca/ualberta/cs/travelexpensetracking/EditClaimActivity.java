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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

	public class EditClaimActivity extends Activity {
	private static final String FILENAME = "save.sav";
	private ClaimList Claims ;
	private Claim currentClaim;
	private String claimIDstr;
	private int claimID;
	
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
	private EditText editTextEditClaimName;
	private EditText editTextEditClaimStartDateDay;
	private EditText editTextEditClaimStartDateMonth;
	private EditText editTextEditClaimStartDateYear;
	private EditText editTextEditClaimEndDateDay;
	private EditText editTextEditClaimEndDateMonth;
	private EditText editTextEditClaimEndDateYear;

	private EditText editTextEditClaimDescription;
	private Spinner spinnerEditStatus;
	
	private Button buttonEditClaimDone;
	private Button buttonEditClaimCancle;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);
		// get current claim ID
		Intent intent = getIntent();
		claimIDstr = intent.getStringExtra("claimID");
		claimID = Integer.parseInt(claimIDstr);
		
		
		// load widgets
		editTextEditClaimName = (EditText)findViewById(R.id.editTextEditClaimName);
		editTextEditClaimStartDateDay = (EditText)findViewById(R.id.editTextEditClaimStartDateDay);
		editTextEditClaimStartDateMonth = (EditText)findViewById(R.id.editTextEditClaimStartDateMonth);
		editTextEditClaimStartDateYear = (EditText)findViewById(R.id.editTextEditClaimStartDateYear);
		editTextEditClaimEndDateDay = (EditText)findViewById(R.id.editTextEditClaimEndDateDay);
		editTextEditClaimEndDateMonth = (EditText)findViewById(R.id.editTextEditClaimEndDateMonth);
		editTextEditClaimEndDateYear = (EditText)findViewById(R.id.editTextEditClaimEndDateYear);
		spinnerEditStatus = (Spinner)findViewById(R.id.spinnerEditStatus);
		editTextEditClaimDescription = (EditText)findViewById(R.id.editTextEditClaimDescription);
		buttonEditClaimDone = (Button)findViewById(R.id.buttonEditClaimDone);
		buttonEditClaimCancle = (Button)findViewById(R.id.buttonEditClaimCancle);
		
		// set spinner 
		ArrayList<String> statusList = new ArrayList<String>();
		statusList.add("In progress");
		statusList.add("Submitted");
		statusList.add("Returned");
		statusList.add("Approved");
		// set adapter
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,statusList);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerEditStatus.setAdapter(spinnerAdapter);
		// set listener
		addListenerOnSpinnerItemSelection();
		}
		
		public void addListenerOnSpinnerItemSelection(){
			spinnerEditStatus.setOnItemSelectedListener(new StatusOnItemSelectedListener());
		}
	
	
		

	

	@Override
	protected void onStart(){
		super.onStart();
		// load claims
		Claims = this.loadFromFile();
		
		// load current claim
		currentClaim = Claims.getClaimList().get(claimID);
		
		// display current claim name
		editTextEditClaimName.setText(currentClaim.getClaimName());
		// display current description
		editTextEditClaimDescription.setText(currentClaim.getDescription());
		// set date
		
		editTextEditClaimStartDateDay.setText( String.valueOf(currentClaim.getStartDate().get(Calendar.DATE)));
		editTextEditClaimStartDateMonth.setText( String.valueOf(currentClaim.getStartDate().get(Calendar.MONTH)));
		editTextEditClaimStartDateYear.setText( String.valueOf(currentClaim.getStartDate().get(Calendar.YEAR)));

		editTextEditClaimEndDateDay.setText( String.valueOf(currentClaim.getEndDate().get(Calendar.DATE))); 
		editTextEditClaimEndDateMonth.setText(String.valueOf( currentClaim.getEndDate().get(Calendar.MONTH))); 
		editTextEditClaimEndDateYear.setText(String.valueOf( currentClaim.getEndDate().get(Calendar.YEAR)));  

		// set on click listener for done button
		buttonEditClaimDone.setOnClickListener(new View.OnClickListener(){
		@Override
		public void onClick(View v) {
				Intent intentDone = new Intent();
				
				// get user input text
				newClaimName = editTextEditClaimName.getText().toString();
				startDateDay = Integer.parseInt(editTextEditClaimStartDateDay.getText().toString());
				startDateMonth = Integer.parseInt(editTextEditClaimStartDateMonth.getText().toString());
				startDateYear = Integer.parseInt( editTextEditClaimStartDateYear.getText().toString());
				endDateDay = Integer.parseInt(editTextEditClaimEndDateDay.getText().toString());
				endDateMonth = Integer.parseInt( editTextEditClaimEndDateMonth.getText().toString());
				endDateYear = Integer.parseInt( editTextEditClaimEndDateYear.getText().toString());
				status = String.valueOf(spinnerEditStatus.getSelectedItem());
				description = editTextEditClaimDescription.getText().toString();
				// TODO: exception handle
				
				// create new claim with user input information
				// set claim name
				currentClaim.setClaimName(newClaimName);
				// set description
				currentClaim.setDescription(description);
				// set start date
				Calendar startDate = Calendar.getInstance();
				startDate.set(startDateYear, startDateMonth, startDateDay);
				currentClaim.setStartDate(startDate);
				// set end date
				Calendar endDate = Calendar.getInstance();
				endDate.set(endDateYear, endDateMonth, endDateDay);
				currentClaim.setEndDate(endDate);
				
				// set status
				currentClaim.setStatus(status);
				// set description
				currentClaim.setDescription(description);
				

				
				// save in file
				saveInFile();
				// jump to main activity
				intentDone.setClass(EditClaimActivity.this,MainActivity.class);
				EditClaimActivity.this.startActivity(intentDone);
				
			}
		});
		

		// set on click listener for cancel button
		
		buttonEditClaimCancle.setOnClickListener(new View.OnClickListener(){
		@Override
		public void onClick(View v) {
				Intent intentCancle = new Intent();
				// jump to main activity
				intentCancle.setClass(EditClaimActivity.this,MainActivity.class);
				EditClaimActivity.this.startActivity(intentCancle);
	
	}
	});
	
  };



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_claim, menu);
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
