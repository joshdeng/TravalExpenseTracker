package ca.ualberta.cs.travelexpensetracking;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ClaimActivity extends Activity {
	private static final String FILENAME = "save.sav";
	private ClaimList Claims;

	private int claimID;
	private String claimIDstr;
	private Claim currentClaim;
	private String newExpenseName;
	
	private Button addExpenseButton;
	private TextView TitleClaimName;
	private ListView ExpenseListView;
	private EditText EditTextAddExpense;
	
	
	
	
	
	
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim);
		// get widgets
		addExpenseButton = (Button)findViewById(R.id.addExpenseButton);
		TitleClaimName = (TextView)findViewById(R.id.TitleClaimName);
		ExpenseListView = (ListView) findViewById(R.id.listViewExpense);
		EditTextAddExpense = (EditText)findViewById(R.id.EditTextAddExpenseName);
		
		// get current claim ID
		Intent intent = getIntent();
		claimIDstr = intent.getStringExtra("claimID");
		claimID = Integer.parseInt(claimIDstr);
		
		
		
		
		// set add expense button listener
		addExpenseButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// create intent ,jump to add expense activity
				Intent intentAddExpense = new Intent();
				// get new expense name
				newExpenseName = EditTextAddExpense.getText().toString();
				// save expense and current claim id name to intent
				intentAddExpense.putExtra("newExpenseName", newExpenseName);
				intentAddExpense.putExtra("claimID", claimIDstr);
				// jump
				intentAddExpense.setClass(ClaimActivity.this, AddExpenseActivity.class);
				ClaimActivity.this.startActivity(intentAddExpense);
			}
			
			
			
			
		});
		
		// TODO create array adapter
		
		
		
		
		
	}


	
	@Override
	protected void onStart() {
		super.onStart();
		// load claim list from file
		Claims = this.loadFromFile();
		// get current claim
		currentClaim = Claims.getClaimList().get(claimID);
		// set title text
		TitleClaimName.setText(currentClaim.getClaimName());
		
		// set expense list with
		ExpenseListAdapter expenseListAdapter = new ExpenseListAdapter (this, Claims.getClaimList().get(claimID).getExpenseList());
		ExpenseListView.setAdapter(expenseListAdapter);
		
		
		
	};
	
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
