package ca.ualberta.cs.travelexpensetracking;

import java.util.Calendar;


public class Expense {
	
	protected String expenseName;
	protected Calendar date;
	protected String category;
	protected String textualDescription;
	protected int amountSpend;
	protected String unitOfCurrency; 
	
	//constructor
	public Expense(String expenseName) {
		this.expenseName = expenseName;	
	}
	
	// getter and setter for expense name
	public String getName(){
		return this.expenseName;
	}
	public void setName(String newExpenseName){
		this.expenseName = newExpenseName;
	}
	
	// getter and setter for date
	
	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
	
	// getter and setter for category
	public String getCategory(){
		return this.category;
	}


	public void setCategory(String newExpenseCategory){
		this.category = newExpenseCategory;
	}
	
	
	// getter and setter for textual description
	public String getTextualDescription(){
		return this.textualDescription;
	}
	
	public void setTextualDescription(String newExpenseTextualDescription){
		this.textualDescription = newExpenseTextualDescription;
	}
	
	
	// getter and setter for amount spent
	public int getAmountSpent(){
		return this.amountSpend;
	}
	
	public void setAmountSpent(int newAmountSpent){
		this.amountSpend = newAmountSpent;
	}

	// getter and setter for  unit of currency
	public String getUnitOfCurrency() {
		return this.unitOfCurrency;
	}

	public void setUnitOfCurrency(String  newUnitOfCurrency) {
		this.unitOfCurrency = newUnitOfCurrency;
	}
	
	
	
	
	
	
	
	
}
