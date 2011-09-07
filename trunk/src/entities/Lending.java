package entities;

import java.util.Date;


public class Lending implements Identifiable {
	
	private User borrower;
	private User lender;
	private Item item;
	private int requiredDays;
	private boolean returned;
	private boolean requestedBack;
	private boolean canceled;
	private int dayOfRequestion;
	private int dayOfTheLending;
	private String id;

	public Lending(User borrower, User lender,Item item,int days) {
		this.borrower = borrower;
		this.lender = lender;
		this.item = item;
		this.requiredDays = days;
		this.requestedBack = false;
		this.canceled = false;
		this.dayOfRequestion = new Date().getDay();
		this.id = Integer.toString(((Object) this).hashCode());
	}
	
	public int getDayOfTheLending() {
		return dayOfTheLending;
	}

	public void setDayOfTheLending(int dayOfTheLending) {
		this.dayOfTheLending = dayOfTheLending;
	}

	public int getDayOfRequestion() {
		return this.dayOfRequestion;
	}

	public void setDayOfRequestion(int dayOfRequestion) {
		this.dayOfRequestion = dayOfRequestion;
	}

	public boolean isRequestedBack() {
		return requestedBack;
	}
	
	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public void setRequestedBack(boolean requestedBack) {
		this.requestedBack = requestedBack;
	}
	public User getBorrower() {
		return borrower;
	}
	public void setBorrower(User borrower) {
		this.borrower = borrower;
	}
	public int getRequiredDays() {
		return requiredDays;
	}
	public void setRequiredDays(int requiredDays) {
		this.requiredDays = requiredDays;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	
	@Override
	public String toString() {
		StringBuilder lendingToString = new StringBuilder();
		
		lendingToString.append(this.item.toString());
		lendingToString.append(this.borrower.toString());
		lendingToString.append(Integer.toString(this.requiredDays));
		return lendingToString.toString();
	}
	
	public boolean isReturned() {
		return returned;
	}
	
	public void setReturned(boolean returned) {
		this.returned = returned;
	}
	
	@Override
		public int hashCode(){
		return this.toString().hashCode();
	}
	
	
	@Override
	public boolean equals(Object obj){
		if(! (obj instanceof Lending)){
			return false;
		}
		Lending oth = (Lending) obj;
		return oth.hashCode() == this.hashCode();
	}
	public User getLender() {
		return lender;
	}
	public void setLender(User lender) {
		this.lender = lender;
	}

	@Override
	public String getID() {
		return this.id;
	}
	
}
