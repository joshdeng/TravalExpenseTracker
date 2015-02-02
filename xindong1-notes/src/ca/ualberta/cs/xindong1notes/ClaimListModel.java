package ca.ualberta.cs.xindong1notes;

import java.util.ArrayList;

// ClaimListModel: to store claim object
public class ClaimListModel {
	protected ArrayList<ClaimModel> claimList ;
	protected ArrayList<Integer> totalSumList ;
	protected ArrayList<String> totalUnitList ;
	
	
	// constructor
	public ClaimListModel(){
		this.claimList = new ArrayList<ClaimModel>();
		this.totalSumList = new ArrayList<Integer>();
		this.totalUnitList = new ArrayList<String>();
	}

	
	// getter and setter
	public ArrayList<ClaimModel> getClaimList() {
		return claimList;
	}

	public void setClaimList(ArrayList<ClaimModel> claimList) {
		this.claimList = claimList;
	}
	
	// add item and remove item
	public void addClaim(ClaimModel newClaim){
		this.claimList.add(newClaim);
	}
	
	public void removeClaimByIndex(int index){
		this.claimList.remove(index);
	}
	// get total currency amount and unit
	public void currencySumClaimList(){
		this.totalSumList = new ArrayList<Integer>();
		this.totalUnitList = new ArrayList<String>();
		
		for (int i=0; i<this.getClaimList().size();i++) {
			// if unit list empty
			ArrayList<Integer> currentSumList = this.getClaimList().get(i).sumList;
			ArrayList<String> currentUnitList = this.getClaimList().get(i).unitList; 
			if (this.totalUnitList.isEmpty()){
				this.totalSumList = currentSumList;
				this.totalUnitList = currentUnitList;
				
			}
			// if not empty
			else{
				for( int j = 0; j < currentUnitList.size();j++){
					for (int k = 0; k< totalUnitList.size();k++){
						// if unit found
						if (currentUnitList.get(j).equals(totalUnitList.get(k)) ){
							int currentSum = totalSumList.get(k);
							totalSumList.set(k, currentSum+currentSumList.get(j));
						}
						
					}
					// if unit not found
					totalUnitList.add(currentUnitList.get(j));
					totalSumList.add(currentSumList.get(j));
					
					
					
				}
			}
		}
	}
	
	// getter for sum list and unit list
	public ArrayList<Integer> getTotalSumList (){
		return this.totalSumList;
	}
	
	public ArrayList<String> getTotalUnitList(){
		return this.totalUnitList;
	}
	
	
	
	// TODO: sortByStarterDate
	public void sortByStartDate(){
		
	}
	
	// TODO: save records in file
	
	
	
	
	
	// TODO: email claim list or file
}
