package ca.ualberta.cs.xindong1notes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;








import java.util.Collections;
import java.util.Comparator;

import ca.ualberta.cs.travelexpensetracking.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

// main activity, shows detail of all claims
public class MainActivity extends Activity {

	private Button addClaimButton;
	private ClaimListModel Claims ;
	private static final String FILENAME = "save.sav";
	private EditText EditTextAddClaim;
	private String newClaimName;
	private ListView ClaimListView;
	private int onLongClickPos;
	private ClaimListAdapter claimListAdapter;
	private Button buttonEmailClaimDialog ;
	private Button buttonEditClaimDialog ;
	private Button buttonRemoveClaimDialog;
	private Button buttonApproveDialog;
	private Button buttonReturnDialog;
	
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
		Collections.sort(Claims.getClaimList(),new Comparator<ClaimModel>(){
			public int compare(ClaimModel c1, ClaimModel c2) {
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
			    // if claim locked
				String status = Claims.getClaimList().get(onLongClickPos).getStatus();
				if ((status.equalsIgnoreCase("Approved"))||(status.equalsIgnoreCase("Submitted"))){
					Toast.makeText(getApplicationContext(), "Item Locked", Toast.LENGTH_LONG).show();
				}
				else{
			    // jump to the target Claim object
			    Intent intentEnterClaim = new Intent();
			    // save claim id
				intentEnterClaim.putExtra("claimID", Integer.toString(position));
		
				// save in file
				saveInFile();
				// jump to the target Claim object
				intentEnterClaim.setClass(MainActivity.this, ClaimActivity.class);
				MainActivity.this.startActivity(intentEnterClaim);
				}
			}});
		
		
		// set list item on long click listener
		// 
		ClaimListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// save position
				onLongClickPos = position;
				// status checker
				String status = Claims.getClaimList().get(onLongClickPos).getStatus();
				// if status is submitted
				if (status.equalsIgnoreCase("Submitted")){
					
					
					final Dialog dialog2 = new Dialog(MainActivity.this);
					dialog2.setContentView(R.layout.claim_dialog2);
					// set title as claim name
					dialog2.setTitle(Claims.getClaimList().get(position).getClaimName());
					// get dialog content object
					buttonApproveDialog = (Button) dialog2.findViewById(R.id.buttonApproveDialog);
					buttonReturnDialog = (Button) dialog2.findViewById(R.id.buttonReturnDialog);
					buttonRemoveClaimDialog = (Button) dialog2.findViewById(R.id.buttonRemoveClaimDialog2);
					
					dialog2.show();
					
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
							Collections.sort(Claims.getClaimList(),new Comparator<ClaimModel>(){
								public int compare(ClaimModel c1, ClaimModel c2) {
									return c1.getStartDate().compareTo(c2.getStartDate());
								}
							});
							
							claimListAdapter.addAll(Claims.getClaimList());
							claimListAdapter.notifyDataSetChanged();
							// dismiss dialog
							dialog2.dismiss();
						  }
						}
						);
						
					// set status to approved
					buttonApproveDialog.setOnClickListener(new View.OnClickListener() {	
							@Override
							public void onClick(View v) {
								Claims.getClaimList().get(onLongClickPos).setStatus("Approved");
								saveInFile();
								claimListAdapter.clear();
								Claims = loadFromFile();
								claimListAdapter.addAll(Claims.getClaimList());
								claimListAdapter.notifyDataSetChanged();
								dialog2.dismiss();
								}
							  
							}
							);
					// set status to returned
					buttonReturnDialog.setOnClickListener(new View.OnClickListener() {	
							@Override
							public void onClick(View v) {
								Claims.getClaimList().get(onLongClickPos).setStatus("Returned");
								saveInFile();
								claimListAdapter.clear();
								Claims = loadFromFile();
								claimListAdapter.addAll(Claims.getClaimList());
								claimListAdapter.notifyDataSetChanged();
								dialog2.dismiss();
								}
							  }
							
							);
					
				}
				else{
			    // create a dialog
				final Dialog dialog1 = new Dialog(MainActivity.this);
				dialog1.setContentView(R.layout.claim_dialog);
				// set title as claim name
				dialog1.setTitle(Claims.getClaimList().get(position).getClaimName());
				// get dialog content object
				buttonEmailClaimDialog = (Button) dialog1.findViewById(R.id.buttonEmailDialog);
				buttonEditClaimDialog = (Button) dialog1.findViewById(R.id.buttonEditClaimDialog);
				buttonRemoveClaimDialog = (Button) dialog1.findViewById(R.id.buttonRemoveClaimDialog);
				
				
				dialog1.show();

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
					Collections.sort(Claims.getClaimList(),new Comparator<ClaimModel>(){
						public int compare(ClaimModel c1, ClaimModel c2) {
							return c1.getStartDate().compareTo(c2.getStartDate());
						}
					});
					
					claimListAdapter.addAll(Claims.getClaimList());
					claimListAdapter.notifyDataSetChanged();
					// dismiss dialog
					dialog1.dismiss();
				  }
				}
				);
				
				
				buttonEmailClaimDialog.setOnClickListener(new View.OnClickListener() {	
					@Override
					public void onClick(View v) {
						// check status
						String status = Claims.getClaimList().get(onLongClickPos).getStatus();
						// if item locked
						if ((status.equalsIgnoreCase("Approved"))||(status.equalsIgnoreCase("Submitted"))){
							Toast.makeText(getApplicationContext(), "Item Locked", Toast.LENGTH_LONG).show();
							dialog1.dismiss();
						}
						else{
						// create intent
						
						Intent intentEmail = new Intent();
						// save claim id into tent
						intentEmail.putExtra("claimID",String.valueOf(onLongClickPos));
						// jump to email activity
						intentEmail.setClass(MainActivity.this, EmailClaim.class);
						MainActivity.this.startActivity(intentEmail);
						dialog1.dismiss();
						}}
					  
					}
					);
				
				buttonEditClaimDialog.setOnClickListener(new View.OnClickListener() {	
					@Override
					public void onClick(View v) {
						// check status
						String status = Claims.getClaimList().get(onLongClickPos).getStatus();
						// if status is submitted
						if ((status.equalsIgnoreCase("Approved"))||(status.equalsIgnoreCase("Submitted"))){
							Toast.makeText(getApplicationContext(), "Item Locked", Toast.LENGTH_LONG).show();
							dialog1.dismiss();
						}
						else{
						Intent intentEdit = new Intent();
						// save claim id into tent
						intentEdit.putExtra("claimID",String.valueOf(onLongClickPos));
						// jump to email activity
						intentEdit.setClass(MainActivity.this, EditClaimActivity.class);
						MainActivity.this.startActivity(intentEdit);
						dialog1.dismiss();
						}
					  }
					}
					);		
				}
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
		
	// set on back pressed (exit with warning)
	@Override
	public void onBackPressed(){
		new AlertDialog.Builder(this).setTitle("Exit")
		.setMessage("Are you sure you want to exit?")
		.setNegativeButton(android.R.string.no, null)
        .setPositiveButton(android.R.string.yes, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
               	MainActivity.super.finish();
               	
            }
        }).create().show();	
	}
		
		
	}
