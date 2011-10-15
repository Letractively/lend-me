package com.lendme;


/**
 * @author THE LENDERS
 * This class represents a unique lending record, storing info regarding the lender, the borrower,
 * the lent item, the amount of days required by the borrower, as well as the lending status.
 *
 */

public class Lending implements Identifiable, Comparable<Lending> {
	
	private User borrower;
	private User lender;
	private Item item;
	private int requiredDays;
	private boolean returned;
	private boolean requestedBack;
	private EventDate requestionDate;
	private EventDate lendingDate;
	private String id;
	private LendingStatus status;
	private String desiredItemName;

	public Lending(User borrower, User lender,Item item,int days) {
		this.borrower = borrower;
		this.lender = lender;
		this.item = item;
		this.desiredItemName = null;
		this.requiredDays = days;
		this.requestedBack = false;
		this.requestionDate = new EventDate(String.format(EntitiesConstants.ITEM_REQUESTED_MESSAGE,
				item.getName(), lender.getLogin(), requiredDays, borrower.getLogin()));
		this.id = Integer.toString(((Object) this).hashCode());
		this.status = LendingStatus.ONGOING;
	}

	public Lending(User borrower, String desiredItemName){
		this.borrower = borrower;
		this.lender = null;
		this.item = null;
		this.desiredItemName = desiredItemName;
		this.requiredDays = 1;
		this.requestedBack = false;
		this.requestionDate = new EventDate(String.format(EntitiesConstants.ITEM_REQUEST_PUBLISHED_MESSAGE, borrower.getLogin(), desiredItemName));
		this.id = Integer.toString(((Object) this).hashCode());
		this.status = LendingStatus.REQUEST_PUBLISHED;
	}
	
	public Lending(Lending publishedRequest, User lender, Item item ){
		this.borrower = publishedRequest.getBorrower();
		this.lender = lender;
		this.item = item;
		this.desiredItemName = null;
		this.requiredDays = publishedRequest.getRequiredDays();
		this.requestedBack = false;
		this.requestionDate = publishedRequest.getRequestionDate();
		this.id = publishedRequest.getID();
		this.status = LendingStatus.ONGOING;
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
		return status == LendingStatus.CANCELLED;
	}

	public void setCancelled() {
		this.status = LendingStatus.CANCELLED;
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
		
		if ( lender != null ) lendingToString.append(lender).toString();
		if ( item != null ) lendingToString.append(item.toString());
		lendingToString.append(borrower.toString());
		if ( item == null ) lendingToString.append(desiredItemName);
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

	/* (non-Javadoc)
	 * @see com.lendme.Identifiable#getID()
	 */
	public String getID() {
		return this.id;
	}

	@Override
	public int compareTo(Lending o) {
		try {
			o.getLendingDate();
		}
		catch(Exception e){
			try{
				this.getLendingDate();
				return 1;
			}
			catch(Exception f){
				return 0;
			}
		}
		try{
			this.getLendingDate();
		}
		catch(Exception g){
			return -1;
		}
		
		if(this.lendingDate.getDate().after(o.lendingDate.getDate())){
			return 1;
		}else if(this.lendingDate.getDate().before(o.lendingDate.getDate())){
			return -1;
		}
		return 0;
	}

	public LendingStatus getStatus() {
		return status;
	}

	public void setStatus(LendingStatus status) {
		this.status = status;
	}	
	
}