package entities;

public class Lending {
	
	private User borrower;
	private User lender;
	private Item item;
	private int requiredDays;
	private boolean returned;

	public Lending(User borrower, User lender,Item item,int days) {
		this.borrower = borrower;
		this.lender = lender;
		this.item = item;
		this.requiredDays = days;
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
	
}
