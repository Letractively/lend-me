package entities;

import entities.util.EntitiesConstants;
import entities.util.EventDate;


public class Lending implements Identifiable, Comparable<Lending> {
	
	private User borrower;
	private User lender;
	private Item item;
	private int requiredDays;
	private boolean returned;
	private boolean requestedBack;
	private boolean canceled;
	private EventDate requestionDate;
	private EventDate lendingDate;
	private String id;

	public Lending(User borrower, User lender,Item item,int days) {
		this.borrower = borrower;
		this.lender = lender;
		this.item = item;
		this.requiredDays = days;
		this.requestedBack = false;
		this.canceled = false;
		this.requestionDate = new EventDate(String.format(EntitiesConstants.ITEM_REQUESTED_MESSAGE,
				item.getName(), lender.getLogin(), requiredDays, borrower.getLogin()));
		this.id = Integer.toString(((Object) this).hashCode());
	}

	public EventDate getLendingDate() throws Exception{
		if ( lendingDate == null ){
			throw new Exception("Registro ainda não possui data de devolução.");//"Lending slot still does not have a lending date");
		}
		return lendingDate;
	}

	public void setLendingDate() {
		this.lendingDate = new EventDate(String.format(EntitiesConstants.ITEM_REQUESTED_MESSAGE,
				item.getName(), lender.getLogin(), requiredDays, borrower.getLogin()));
	}
	
	public EventDate getRequestionDate() {
		return requestionDate;
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

	public int getRequiredDays() {
		return requiredDays;
	}

	public Item getItem() {
		return item;
	}
	
	@Override
	public String toString() {
		StringBuilder lendingToString = new StringBuilder();
		
		lendingToString.append(lender).toString();
		lendingToString.append(item.toString());
		lendingToString.append(borrower.toString());
		lendingToString.append(Integer.toString(requiredDays));
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

	@Override
	public int compareTo(Lending o) {
		if(this.lendingDate.getDate().after(o.lendingDate.getDate())){
			return 1;
		}
		return 0;
	}
	
}