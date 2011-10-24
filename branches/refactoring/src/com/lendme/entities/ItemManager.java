package com.lendme.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.lendme.entities.Lending.LendingStatus;


public class ItemManager {
	
	private Map<Item,User> myItems = new HashMap<Item,User>();
	private Set<Lending> myLentItems = new HashSet<Lending>();
	private Set<Lending> sentItemRequests = new HashSet<Lending>();
	private Set<Lending> receivedItemRequests = new HashSet<Lending>();
	private Set<Lending> myBorrowedItems = new HashSet<Lending>();
	private Set<Lending> borrowedRegistryHistory = new HashSet<Lending>();
	private Set<Lending> lentRegistryHistory = new HashSet<Lending>();
	private Set<Lending> lendingDenialRegistryHistory = new HashSet<Lending>();
	private Set<Lending> sentItemDevolutionRequests = new HashSet<Lending>();
	private Set<Lending> publishedItemRequests = new HashSet<Lending>();
	private Map<Item, ArrayList<InterestedOn<Item>>> interestedOnMyItems = new HashMap<Item, ArrayList<InterestedOn<Item>>>();
	
	
	
	public Map<Item, User> getMyItems() {
		return myItems;
	}

	public Set<Lending> getMyLentItems() {
		return myLentItems;
	}

	public Set<Lending> getSentItemRequests() {
		return sentItemRequests;
	}

	public Set<Lending> getReceivedItemRequests() {
		return receivedItemRequests;
	}

	public Set<Lending> getMyBorrowedItems() {
		return myBorrowedItems;
	}

	public Set<Lending> getLentRegistryHistory() {
		return lentRegistryHistory;
	}

	public Set<Lending> getLendingDenialRegistryHistory() {
		return lendingDenialRegistryHistory;
	}

	public void setMyItems(Map<Item, User> myItems) {
		this.myItems = myItems;
	}

	public void setMyLentItems(Set<Lending> myLentItems) {
		this.myLentItems = myLentItems;
	}

	public void setSentItemRequests(Set<Lending> sentItemRequests) {
		this.sentItemRequests = sentItemRequests;
	}

	public void setMyBorrowedItems(Set<Lending> myBorrowedItems) {
		this.myBorrowedItems = myBorrowedItems;
	}

	public void setBorrowedRegistryHistory(Set<Lending> borrowedRegistryHistory) {
		this.borrowedRegistryHistory = borrowedRegistryHistory;
	}

	public void setLentRegistryHistory(Set<Lending> lentRegistryHistory) {
		this.lentRegistryHistory = lentRegistryHistory;
	}

	public void setLendingDenialRegistryHistory(
			Set<Lending> lendingDenialRegistryHistory) {
		this.lendingDenialRegistryHistory = lendingDenialRegistryHistory;
	}

	public void setSentItemDevolutionRequests(
			Set<Lending> sentItemDevolutionRequests) {
		this.sentItemDevolutionRequests = sentItemDevolutionRequests;
	}

	public void setPublishedItemRequests(Set<Lending> publishedItemRequests) {
		this.publishedItemRequests = publishedItemRequests;
	}

	public void setInterestedOnMyItems(
			Map<Item, ArrayList<InterestedOn<Item>>> interestedOnMyItems) {
		this.interestedOnMyItems = interestedOnMyItems;
	}

	public Map<Item, ArrayList<InterestedOn<Item>>> getInterestedOnMyItems() {
		return interestedOnMyItems;
	}
	
	public Set<Lending> getBorrowedRegistryHistory() {
		return borrowedRegistryHistory;
	}
	
	public Set<Lending> getSentItemDevolutionRequests() {
		return sentItemDevolutionRequests;
	}
	
	public Set<Lending> getPublishedItemRequests() {
		return publishedItemRequests;
	}
	
	public void setReceivedItemRequests(Set<Lending> receivedItemRequests) {
		this.receivedItemRequests = receivedItemRequests;
	}
	
	/**
	 * User receives a new item.
	 * @param itemName
	 * @param description
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public String addItem(String itemName, String description, String category, User owner)
			throws Exception{
		Item myNewItem = new Item(itemName, description, category);
		this.myItems.put(myNewItem, owner);
		return myNewItem.getID();
	}
	
	public void verifyLendingAutenticity(Item item) throws Exception {
		for ( Lending record : sentItemRequests ){
			if ( record.getItem().idMatches(item.getID()) ){
				throw new Exception("Requisição já solicitada");//"Request already sent");
			}
		}
	}
	
	/**
	 * Returns true if user has item.
	 * @param item
	 * @return
	 */
	public boolean hasItem(Item item) {
		return myItems.containsKey(item);
	}
	
	/**
	 * Returns true if user has lent the specified item.
	 * @param item
	 * @return
	 */
	public boolean hasLentThis(Item item) {
		for ( Lending lentItem : myLentItems ){
			if ( lentItem.getItem().equals(item) ){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * User sends a borrow request of specified item to another user.
	 * @param item
	 * @param lender
	 * @param days
	 * @return
	 * @throws Exception
	 */
	public String borrowItem(Item item, User borrower, User lender, int days) throws Exception {
		Lending lendingRequest = new Lending(borrower, lender, item, days);
		Set<Lending> lenderLendings = lender.getUserOperationManager().getItemManager().getReceivedItemRequests();
		lenderLendings.add(lendingRequest);
		lender.getUserOperationManager().getItemManager().setReceivedItemRequests(lenderLendings);
		sentItemRequests.add(lendingRequest);
		return lendingRequest.getID();
	}

	/**
	 * User sends a lending request of item to another user.
	 * @param item
	 * @param borrower
	 * @param days
	 * @return
	 * @throws Exception
	 */
	protected String lendItem(Item item, User borrower, int days) throws Exception{

		Lending requestAccepted = null;
		for ( Lending record : receivedItemRequests ){
			if ( record.getItem().equals(item) ){
				requestAccepted = record;
				record.setLendingDate();
				record.getLendingDate().addDays(record.getRequiredDays());
				borrower.getUserOperationManager().getItemManager().addRequestedItem(item, record.getLender(), days);
			}
		}
		if ( requestAccepted != null ){
			receivedItemRequests.remove(requestAccepted);
			myLentItems.add(requestAccepted);
			myItems.put(item, borrower);
			return requestAccepted.getID();
		}
		else{
			throw new Exception("Solicitante não pediu esse item emprestado");
		}
	}
	
	private void addRequestedItem(Item item, User lender, int days) throws Exception{
		Lending requestAccepted = null;
		for ( Lending record : sentItemRequests ){
			if ( record.getItem().equals(item) && record.getLender().equals(lender)
					&& record.getRequiredDays() == days ){
				requestAccepted = record;
				record.setLendingDate();
				record.getLendingDate().addDays(record.getRequiredDays());
			}
		}
		if ( requestAccepted != null ){
			sentItemRequests.remove(requestAccepted);
			myBorrowedItems.add(requestAccepted);
		}
		else{
			throw new Exception("Solicitante não pediu esse item emprestado");
		}
	}
	
	
	
	/**
	 * User refuses lending one of his available items.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public String denyLending(String requestId) throws Exception{
		if ( requestId == null || requestId.trim().isEmpty() ){
			throw new Exception("Identificador da requisição de empréstimo é inválido");//"Lending request identifier invalid");
		}
		Lending foundLending = getMyReceivedItemRequestById(requestId); 
		
		if (foundLending != null) {
			return declineLendingItem(foundLending.getItem(), foundLending.getBorrower(), foundLending.getRequiredDays());
		} 
		
		if (wasLendingDenied(requestId)) {
				throw new Exception("Empréstimo já negado");//Lending request already approved
			}
		throw new Exception("Requisição de empréstimo inexistente");//"Inexistent item request");
		
	}
	
	/**
	 * @see com.lendme.User#denyLending(String);
	 */
	private String declineLendingItem(Item item, User borrower, int days) throws Exception{
		
		Lending requestDenied = null;
		for ( Lending record : receivedItemRequests){
			if (record.getItem().equals(item) && record.getBorrower().equals(borrower) &&
					record.getRequiredDays() == days ) {
				requestDenied = record;
				borrower.getUserOperationManager().getItemManager().removeRequestedItem(item, record.getLender(), days);
			}
		}
		if ( requestDenied != null ){
			receivedItemRequests.remove(requestDenied);
			requestDenied.setStatus(LendingStatus.DENIED);
			lendingDenialRegistryHistory.add(requestDenied);
			return requestDenied.getID();
		}
		else{
			throw new Exception("Solicitante não pediu esse item emprestado");
		}
	}
	
	private void removeRequestedItem(Item item, User lender, int days) throws Exception{
		Lending requestDenied = null;
		for ( Lending record : sentItemRequests ){
			if ( record.getItem().equals(item) && record.getLender().equals(lender)
					&& record.getRequiredDays() == days ){
				requestDenied = record;
			}
		}
		if ( requestDenied != null ){
			sentItemRequests.remove(requestDenied);
		}
		else{
			throw new Exception("Solicitante não pediu esse item emprestado");
		}
	}
	
	/**
	 * User gives item back.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public String approveItemReturning(String lendingId) throws Exception{
		if ( lendingId == null || lendingId.trim().isEmpty() ){
			throw new Exception("Identificador do empréstimo é inválido");//"Lending request identifier invalid");
		}
		for ( Lending record : myBorrowedItems ){
			if ( record.getID().equals(lendingId) ){
				return returnItem(record.getItem());
			}
		}
		throw new Exception("Empréstimo inexistente");//"Inexistent item request");
	}
	
	/**
	 * @see com.lendme.User#approveItemReturning(String)
	 */
	public String returnItem(Item item) throws Exception{
		for(Lending record : myBorrowedItems){
			if(record.getItem().equals(item)){
				if ( record.isReturned() ){
					throw new Exception("Item já devolvido");//"Item already set to be returned);
				}
				record.getLender().getUserOperationManager().getItemManager().setReturned(item);
				record.setReturned(true);
				return record.getID();
			}
		}
		throw new Exception("Solicitante não possue o item que quer devolver");
	}
	
	private void setReturned(Item item) throws Exception{
		for(Lending record : myLentItems){
			if(record.getItem().equals(item)){
				if ( record.isReturned() ){
					throw new Exception("Item já devolvido");//"Item already set to be returned);
				}
				record.setReturned(true);
				return;
			}
		}
		throw new Exception("ERR: lender was required to set his item as toBeReturned but he doesn't have it");
	}
	
	/**
	 * User denies item receipt.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public String denyLendingTermination(String lendingId) throws Exception{
		if ( lendingId == null || lendingId.trim().isEmpty() ){
			throw new Exception("Identificador do empréstimo é inválido");//"Lending identifier is invalid");
		}
		for ( Lending record : lentRegistryHistory ){
			if ( record.getID().equals(lendingId) ){
				throw new Exception("O empréstimo já foi finalizado");
			}
		}
		for ( Lending record : myLentItems ){
			if ( record.getID().equals(lendingId) ){
				denyReceivingLentItem(record.getItem());
				return record.getID();
			}
		}
		throw new Exception("Empréstimo inexistente");
	}
	
	/**
	 * @see com.lendme.User#denyLendingTermination(String)
	 */
	public void denyReceivingLentItem(Item item) throws Exception{
		for(Lending record : myLentItems){
			if(record.getItem().equals(item)){
				if ( !record.isReturned() ){
					throw new Exception("Devolução do item já foi negada");//"Item returning already denied);
				}
				record.getBorrower().getUserOperationManager().getItemManager().setNotReturned(item);
				record.setReturned(false);
				return;
			}
		}
		throw new Exception("Solicitante quer devolver item que ele não pegou emprestado");
	}
	
	private void setNotReturned(Item item) throws Exception{
		for(Lending record : myBorrowedItems){
			if(record.getItem().equals(item)){
				if ( !record.isReturned() ){
					throw new Exception("Devolução do item já foi negada");//"Item returning already denied);
				}
				record.setReturned(false);
				return;
			}
		}
		throw new Exception("Solicitante quer devolver item que ele não pegou emprestado");
	}
	
	/**
	 * Returns true if another user is interested on item whose owner is this user.
	 * @param item
	 * @param interested
	 * @return
	 * @throws Exception
	 */
	public boolean isInterestedOnMyItem(Item item, User interested) throws Exception{
		if ( item == null ){
			throw new Exception("Item inválido");
		}
		if ( interested == null ){
			throw new Exception("Usuário inválido");
		}
		
		Map<Item, ArrayList<InterestedOn<Item>>> interestedOnMyItems = 
				getInterestedOnMyItems();
		if ( interestedOnMyItems.containsKey(item) ){
			if ( interestedOnMyItems.get(item) != null ){
				return interestedOnMyItems.get(item).contains(interested);
			}
			else{
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Returns a lending record by its id.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public Lending getLendingByRequestId(String requestId) throws Exception{
		if ( requestId == null || requestId.trim().isEmpty() ){
			throw new Exception("Identificador da requisição de empréstimo é inválido");//"Lending request identifier invalid");
		}
		for (Lending record : getReceivedItemRequests()){
			if(record.getID().equals(requestId)){
				return record;
			}
		}
		for (Lending record : getSentItemRequests() ){
			if(record.getID().equals(requestId)){
				return record;
			}
		}
		for (Lending record : getMyBorrowedItems() ){
			if(record.getID().equals(requestId)){
				return record;
			}
		}
		for (Lending record : getMyLentItems() ){
			if(record.getID().equals(requestId)){
				return record;
			}
		}
		for (Lending record : getLentRegistryHistory() ){
			if(record.getID().equals(requestId)){
				return record;
			}			
		}
		for (Lending record : getBorrowedRegistryHistory() ){
			if(record.getID().equals(requestId)){
				return record;
			}			
		}
		for (Lending record : getLendingDenialRegistryHistory()){
			if(record.getID().equals(requestId)){
				return record;
			}			
		}
		return null;
	}
	
	/**
	 * Returns a lending record by its id.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public Lending getLendingByLendingId(String lendingId) throws Exception{
		if ( lendingId == null || lendingId.trim().isEmpty() ){
			throw new Exception("Identificador do empréstimo é inválido");//"Lending request identifier invalid");
		}
		for (Lending record : getReceivedItemRequests()){
			if(record.getID().equals(lendingId)){
				return record;
			}
		}
		for (Lending record : getSentItemRequests() ){
			if(record.getID().equals(lendingId)){
				return record;
			}
		}
		for (Lending record : getMyBorrowedItems() ){
			if(record.getID().equals(lendingId)){
				return record;
			}
		}
		for (Lending record : getMyLentItems() ){
			if(record.getID().equals(lendingId)){
				return record;
			}
		}
		for (Lending record : getLentRegistryHistory() ){
			if(record.getID().equals(lendingId)){
				return record;
			}
		}
		for (Lending record : getBorrowedRegistryHistory()){
			if(record.getID().equals(lendingId)){
				return record;
			}			
		}
		for (Lending record : getLendingDenialRegistryHistory()){
			if(record.getID().equals(lendingId)){
				return record;
			}
		}
		return null;
	}	
	
	
	/**
	 * Returns true if user has borrowed the specified item.
	 * @param item
	 * @return
	 */
	public boolean hasBorrowedThis(Item item) {
		for (Lending actual : myBorrowedItems){
			if(actual.getItem().equals(item)){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasAlreadyApprovedLending(String requestId) {
		for ( Lending record : myLentItems ){
			if ( record.getID().equals(requestId) ){
				return true;
			}
		}
		return false;
	}
	
	public boolean wasLendingDenied(String requestId) {
		for ( Lending record : lendingDenialRegistryHistory ){
			if ( record.getID().equals(requestId) ){
				return true;
			}
		}
		return false;
	}
	
	public Lending getMyReceivedItemRequestById(String requestId) throws Exception {
		Lending foundLending = null;
		for ( Lending record : receivedItemRequests ){
			if ( record.getID().equals(requestId) ){
				foundLending = record;
			}
		}
		return foundLending;
	}
	
	public Lending getMySentItemRequestByItem(Item item, User lender, int days) {
		Lending foundLending = null;
		for ( Lending record : sentItemRequests ){
			if ( record.getItem().equals(item) && record.getLender().equals(lender)
					&& record.getRequiredDays() == days ){
				foundLending = record;
			}
		}
		return foundLending;
	}
	
	
}