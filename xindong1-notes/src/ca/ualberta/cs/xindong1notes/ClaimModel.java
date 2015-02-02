package ca.ualberta.cs.xindong1notes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;




public class ClaimModel {

	protected String claimName;
	protected String status;
	protected Calendar startDate;
	protected Calendar endDate;
	protected String description;
	protected ArrayList<ExpenseModel> expenseList;
	protected ArrayList<Integer> sumList;
	protected ArrayList<String> unitList;
	
	
	// Constructor
	public ClaimModel(String newName){
		claimName  = newName;
		status = "In progress";
		description = "(empty)";
		startDate = Calendar.getInstance();
		endDate = Calendar.getInstance();
		expenseList = new ArrayList<ExpenseModel>();
		sumList = new ArrayList<Integer>();
		unitList = new ArrayList<String>(); 
	}
	
	
	// getter and setter for description
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	// getter and setter for claim name
	public String getClaimName() {
		return claimName;
	}
	public void setClaimName(String claimName) {
		this.claimName = claimName;
	}
	
	// getter and setter for status
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	// getter and setter for start date
	public Calendar getStartDate() {
		return startDate;
	}
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	
	// getter and setter for end date
	public Calendar getEndDate() {
		return endDate;
	}
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	
	
	
	// getter and setter for expense list
	public ArrayList<ExpenseModel> getExpenseList() {
		return expenseList;
	}
	public void setExpenseList(ArrayList<ExpenseModel> expenseList) {
		this.expenseList = expenseList;
	}
	
	public void addExpense(ExpenseModel newExpense){
	// add item and delete item from expense list
		this.expenseList.add(newExpense);
	}
	
	public void removeExpenseByIndex(int index){
		this.expenseList.remove(index);
	}
		
	// show total currency amount
	public void currencySumClaim(){
		sumList = new ArrayList<Integer>();
		unitList = new ArrayList<String>();
		
		for (int x = 0; x < (this.expenseList.size()); x++){
			// initialize
			String currentUnit = this.expenseList.get(x).getUnitOfCurrency();
			Integer currentAmount = this.expenseList.get(x).getAmountSpent();
			Boolean flag = false;
			// adding item to result
			if  (this.unitList.isEmpty() != true){
				for (int y = 0; y < (this.unitList.size()); y++ ){
					String currentULU = this.unitList.get(y);
					if (currentUnit.equals(currentULU)){
						int currentULS = this.sumList.get(y);
						this.sumList.set(y, currentULS + currentAmount);
						flag = true;
					}
					
				}
				if (flag == false){
				unitList.add(currentUnit);
				sumList.add(currentAmount);
				}
				
			}else{
				unitList.add(currentUnit);
				sumList.add(currentAmount);
			
			}
				

			
		}
	
	}
	
	
	// return sum list and unit list
	public ArrayList<Integer> getSumList(){
		
		return this.sumList;
	}
	
	public ArrayList<String> getUnitList(){
		return this.unitList;
	}
	
	// return claim name as string
	@Override
	public String toString(){
		return this.getClaimName();
	}

	// return amount spends with currency units as a string (formated)
	public String getSpendStr(){
		this.currencySumClaim();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < sumList.size();i++){
			sb.append(sumList.get(i).toString());
			sb.append(" ");
			sb.append(unitList.get(i).toUpperCase(Locale.ENGLISH));
			if (i != (sumList.size()-1)){
				sb.append("; ");
				
			}
			
		}
		
		
		return sb.toString();
	}
	
	// make claim object comparable (may not necessary)
	public int compareTo(ClaimModel c) {
		return getStartDate().compareTo(c.getStartDate());
	}
	
	

			
	
	
}
