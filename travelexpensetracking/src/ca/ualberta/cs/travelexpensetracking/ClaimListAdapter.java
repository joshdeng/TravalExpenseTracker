package ca.ualberta.cs.travelexpensetracking;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ClaimListAdapter extends ArrayAdapter<Claim> {
	

	private Context context;
	private ArrayList<Claim> ClaimList;

	public ClaimListAdapter(Context context, ArrayList<Claim> ClaimList) {
	       super(context, 0, ClaimList);
	       this.context = context;
	       this.ClaimList=ClaimList;
  }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
       Claim claim = getItem(position);    
       // Check if an existing view is being reused, otherwise inflate the view
       if (convertView == null) {
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.claim_list_item, parent, false);
       }
       // Lookup view for data population
       TextView TextViewClaimName = (TextView) convertView.findViewById(R.id.TextViewClaimName);
       TextView TextViewClaimDate = (TextView) convertView.findViewById(R.id.TextViewClaimDate);
       TextView TextViewClaimStatus = (TextView) convertView.findViewById(R.id.TextViewClaimStatus);
       // Populate the data into the template view using the data object
       TextViewClaimName.setText(claim.getClaimName());
       // test: display date
       //Calendar date = claim.getStartDate();
       //TextViewClaimDate.setText(String.format("%1$tA %1$tb %1$td %1$tY", date));
       TextViewClaimDate.setText(claim.getSpendStr());
       TextViewClaimStatus.setText(claim.getStatus());
       // Return the completed view to render on screen
       return convertView;
   }
	
	
}