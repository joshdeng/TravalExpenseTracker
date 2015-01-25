package ca.ualberta.cs.travelexpensetracking;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;








import java.util.Collections;
import java.util.Comparator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

	private Button addClaimButton;
	private ClaimList Claims ;
	private static final String FILENAME = "save.sav";
	private EditText EditTextAddClaim;
	private String newClaimName;
	private ListView ClaimListView;
	private int onLongClickPos;
	private ClaimListAdapter claimListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		// button listener
		addClaimButton = (Button) findViewById(R.id.addClaimButton);
		ClaimListView = (ListView) findViewById(R.id.listViewClaim);
		EditTextAddClaim = (EditText)findViewById(R.id.ClaimText);
		
		// set on click listener for add claim button
		addClaimButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intentAddClaim = new Intent();
				// get new claim name
				newClaimName = EditTextAddClaim.getText().toString();
				// save new claim name to intent
				intentAddClaim.putExtra("newClaimName", newClaimName);
				// save in file
				saveInFile();
				// jump to add claim activity
				intentAddClaim.setClass(MainActivity.this, AddClaimActivity.class);
				MainActivity.this.startActivity(intentAddClaim);
			}
		});
			
		
		
	}
	
	
	
	@Override
	protected void onStart() {
		super.onStart();
		// load claims
		Claims = this.loadFromFile();	
		// sort the claim list with custom comparator
		Collections.sort(Claims.getClaimList(),new Comparator<Claim>(){
			public int compare(Claim c1, Claim c2) {
				return c1.getStartDate().compareTo(c2.getStartDate());
			}
		});
		
		// set claim list with adapter
		claimListAdapter = new ClaimListAdapter (this, Claims.getClaimList());
		ClaimListView.setAdapter(claimListAdapter);
		
		
		// set list item on click listener
		ClaimListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {	
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			    // test: Claim clickedObj = (Claim)parent.getItemAtPosition(position);
			  
			    // jump to the target Claim object
			    Intent intentEnterClaim = new Intent();
			    // save claim id
				intentEnterClaim.putExtra("claimID", Integer.toString(position));
				// save in file
				saveInFile();
				// jump to the target Claim object
				intentEnterClaim.setClass(MainActivity.this, ClaimActivity.class);
				MainActivity.this.startActivity(intentEnterClaim);
				
			}});
		
		
		// set list item on long click listener
		// 
		ClaimListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// save position
				onLongClickPos = position;
			    // create a dialog
				final Dialog dialog = new Dialog(MainActivity.this);
				dialog.setContentView(R.layout.claim_dialog);
				// set title as claim name
				dialog.setTitle(Claims.getClaimList().get(position).getClaimName());
				// get dialog content object
				Button buttonEmailClaimDialog = (Button) dialog.findViewById(R.id.buttonCancleEmailDialog);
				Button buttonEditClaimDialog = (Button) dialog.findViewById(R.id.buttonEditClaimDialog);
				Button buttonRemoveClaimDialog = (Button) dialog.findViewById(R.id.buttonRemoveClaimDialog);
				dialog.show();
				
				
				
				buttonRemoveClaimDialog.setOnClickListener(new View.OnClickListener() {	
				@Override
				public void onClick(View v) {
					// save removed claim list
					Claims.getClaimList().remove(onLongClickPos);
					saveInFile();
					// reload list view
					claimListAdapter.clear();
					Claims = loadFromFile();
					// sort the claim list with custom comparator
					Collections.sort(Claims.getClaimList(),new Comparator<Claim>(){
						public int compare(Claim c1, Claim c2) {
							return c1.getStartDate().compareTo(c2.getStartDate());
						}
					});
					
					claimListAdapter.addAll(Claims.getClaimList());
					claimListAdapter.notifyDataSetChanged();
					// dismiss dialog
					dialog.dismiss();
				  }
				}
				);
				
				
				buttonEmailClaimDialog.setOnClickListener(new View.OnClickListener() {	
					@Override
					public void onClick(View v) {
						// create intent
						Intent intentEmail = new Intent();
						// save claim id into tent
						intentEmail.putExtra("claimID",String.valueOf(onLongClickPos));
						// jump to email activity
						intentEmail.setClass(MainActivity.this, EmailClaim.class);
						MainActivity.this.startActivity(intentEmail);
					  }
					}
					);
				
				buttonEditClaimDialog.setOnClickListener(new View.OnClickListener() {	
					@Override
					public void onClick(View v) {
						// create intent
						Intent intentEdit = new Intent();
						// save claim id into tent
						intentEdit.putExtra("claimID",String.valueOf(onLongClickPos));
						// jump to email activity
						intentEdit.setClass(MainActivity.this, EditClaimActivity.class);
						MainActivity.this.startActivity(intentEdit);
					  }
					}
					);
				
				
				return true;
				
				
			}});
		
		
		
		
		
	}
	
	@Override
	protected void onResume(){
		 super.onResume();
		 Claims = loadFromFile();
	}
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
