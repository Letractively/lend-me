package com.lendme.server.entities;

/**
 * @author THE LENDERS
 * This class represents a unique lending record, storing info regarding the lender, the borrower,
 * the lent item, the amount of days required by the borrower, as well as the lending status.
 *
 */

public class Lending implements Identifiable, Comparable<Lending> {
	
	public enum LendingStatus {

		ONGOING, FINISHED, DENIED, CANCELLED, REQUEST_PUBLISHED;

	}
	
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
	private String desiredItemDescription;
	
	/**
	 * @param borrower
	 * @param lender
	 * @param item
	 * @param days
	 */
	public Lending(User borrower, User lender,Item item,int days) {
		this.borrower = borrower;
		this.lender = lender;
		this.item = item;
		this.desiredItemName = null;
		this.desiredItemDescription = null;
		this.requiredDays = days;
		this.requestedBack = false;
		this.requestionDate = new EventDate(String.format(EntitiesConstants.ITEM_REQUESTED_MESSAGE,
				item.getName(), lender.getLogin(), requiredDays, borrower.getLogin()));
		this.id = Integer.toString(((Object) this).hashCode());
		this.status = LendingStatus.ONGOING;
	}

	/**
	 * @param borrower
	 * @param desiredItemName
	 * @param desiredItemDescription
	 * @throws Exception
	 */
	public Lending(User borrower, String desiredItemName, String desiredItemDescription) throws Exception{
		if ( desiredItemName == null || desiredItemName.trim().length() == 0 ){
			throw new Exception("Nome inválido");
		}
		if ( desiredItemDescription == null || desiredItemDescription.trim().length() == 0 ){
			throw new Exception("Descrição inválida");
		}
		this.borrower = borrower;
		this.lender = null;
		this.item = null;
		this.desiredItemName = desiredItemName;
		this.desiredItemDescription = desiredItemDescription;
		this.requiredDays = 1;
		this.requestedBack = false;
		this.requestionDate = new EventDate(String.format(EntitiesConstants.ITEM_REQUEST_PUBLISHED_MESSAGE, borrower.getLogin(), desiredItemName));
		this.id = Integer.toString(((Object) this).hashCode());
		this.status = LendingStatus.REQUEST_PUBLISHED;
	}
	
	/**
	 * @param publishedRequest
	 * @param lender
	 * @param item
	 */
	public Lending(Lending publishedRequest, User lender, Item item ){
		this.borrower = publishedRequest.getBorrower();
		this.lender = lender;
		this.item = item;
		this.desiredItemName = null;
		this.desiredItemDescription = null;
		this.requiredDays = publishedRequest.getRequiredDays();
		this.requestedBack = false;
		this.requestionDate = publishedRequest.getRequestionDate();
		this.id = publishedRequest.getID();
		this.status = LendingStatus.ONGOING;
	}
	
	/**
	 * Retorna a data da devolucao.
	 * @return data.
	 * @throws Exception - caso o item ainda nao foi devolvido.
	 */
	public EventDate getLendingDate() throws Exception{
		if ( lendingDate == null ){
			throw new Exception("Registro ainda não possui data de devolução.");//"Lending slot still does not have a lending date");
		}
		return lendingDate;
	}

	/**
	 * Configura uma data para o emprestimo.
	 */
	public void setLendingDate() {
		this.lendingDate = new EventDate(String.format(EntitiesConstants.ITEM_REQUESTED_MESSAGE,
				item.getName(), lender.getLogin(), requiredDays, borrower.getLogin()));
	}
	
	/**
	 * Retorna a data que o emprestimo foi requisitado.
	 * @return EventDate - data da requisicao.
	 */
	public EventDate getRequestionDate() {
		return requestionDate;
	}

	/**
	 * Retorna true se o item esta sendo requisitado de volta pelo dono.
	 * @return
	 */
	public boolean isRequestedBack() {
		return requestedBack;
	}
	
	/**
	 * Retorna true se o emprestimo foi cancelado ou nao.
	 * @return
	 */
	public boolean isCanceled() {
		return status == LendingStatus.CANCELLED;
	}

	/**
	 * Configura status como cancelado.
	 */
	public void setCancelled() {
		this.status = LendingStatus.CANCELLED;
	}

	/**
	 * Configura que o item esta sendo pedido de volta.
	 * @param requestedBack
	 */
	public void setRequestedBack(boolean requestedBack) {
		this.requestedBack = requestedBack;
	}

	/**
	 * Retorna quem qual eh o usuario que esta pedindo emprestado.
	 * @return usuario.
	 */
	public User getBorrower() {
		return borrower;
	}

	/**
	 * Configura a quantidade de dias desejadas para emprestimo.
	 * @return quantidade de dias.
	 */
	public int getRequiredDays() {
		return requiredDays;
	}

	/**
	 * Retorna o item que esta no processo de emprestimo.
	 * @return item.
	 */
	public Item getItem() {
		return item;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder lendingToString = new StringBuilder();
		lendingToString.append(getID() + "\n \t ");
		if ( lender != null ) lendingToString.append(lender).toString();
		if ( item != null ) lendingToString.append(item.toString());
		lendingToString.append(borrower.toString());
		if ( item == null ) lendingToString.append(desiredItemName);
		lendingToString.append(Integer.toString(requiredDays));
		return lendingToString.toString();
	}
	
	/**
	 * Diz se o item em questao foi devolvido ou nao.
	 * @return
	 */
	public boolean isReturned() {
		return returned;
	}
	
	/**
	 * Configura se o item foi devolvido.
	 * @param returned
	 */
	public void setReturned(boolean returned) {
		this.returned = returned;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
		public int hashCode(){
		return this.toString().hashCode();
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj){
		if(! (obj instanceof Lending)){
			return false;
		}
		Lending oth = (Lending) obj;
		return oth.hashCode() == this.hashCode();
	}
	/**
	 * Retorna o usuario que esta emprestando o item.
	 * @return usuario.
	 */
	public User getLender() {
		return lender;
	}
	/**
	 * Configura um novo emprestador.
	 * @param lender
	 */
	public void setLender(User lender) {
		this.lender = lender;
	}

	/* (non-Javadoc)
	 * @see com.lendme.Identifiable#getID()
	 */
	public String getID() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
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

	/**
	 * Retorna o status do emprestimo.
	 * @return status.
	 */
	public LendingStatus getStatus() {
		return status;
	}

	/**
	 * Configura um novo status para o emprestimo.
	 * @param status
	 */
	public void setStatus(LendingStatus status) {
		this.status = status;
	}

	/**
	 * Retorna o nome do item desejado.
	 * @return nome.
	 */
	public String getDesiredItemName() {
		return desiredItemName;
	}

	/**
	 * Retorna a descricao do item desejado.
	 * @return descricao.
	 */
	public String getDesiredItemDescription() {
		return desiredItemDescription;
	}

}