package com.lendme.entities;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author THE LENDERS
 * Represents a user of the System.
 */

public class User implements InterestedOn<Item>{

	private String login;
	private String name;
	private Address address;
	private EventDate creationDate;
	private UserOperationManager userOperationManager;
	
	public User(){}
	
	public User(String login, String name, String... address) throws Exception{
		
		if(login == null || login.trim().isEmpty()){
			throw new Exception("Login inválido");//"Invalid login");
		}
		
		if( name == null || name.trim().isEmpty()){
			throw new Exception("Nome inválido");//"Invalid name");
		}
		
		if( address == null || address.length == 0 ){
			throw new Exception("Endereço inválido");//"Invalid address");
		}
		
		for ( String addressElement : address ){
			if ( addressElement == null ){
				throw new Exception("Endereço inválido");//"Invalid address");
			}
		}
		
		this.login = login;
		this.name = name;
		this.address = new Address(address);
		this.creationDate = new EventDate(String.format(EntitiesConstants.USER_REGISTERED_MESSAGE, this.login, this.name));
		this.userOperationManager = new UserOperationManager(this);
	}
	
	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setAddress(String street, String number, String neighborhood,
			String city, String state, String country, String zipCode) {
		this.address = new Address(street, number, neighborhood, city, state, country, zipCode);
	}

	public Address getAddress() {
		return address;
	}
	
	public UserOperationManager getUserOperationManager() {
		return userOperationManager;
	}
	
	public int getScore() {
		return userOperationManager.getScore();
	}

	/**
	 * Returns true if user name matches to string.
	 * @param toBeMatched
	 * @return
	 */
	public boolean nameMatches(String toBeMatched) {
		return this.name.toUpperCase().contains(toBeMatched.toUpperCase());
	}
	
	@Override
	public String toString(){
		StringBuilder userToString = new StringBuilder();
		userToString.append(" \tNome: " + this.name + "\n");
		userToString.append(" \t\tLogin: " + this.login + "\n");
		userToString.append(" \t\tEndereço: " + this.address + "\n");
				
		return userToString.toString();
	}
	
	@Override
	public int hashCode(){
		return this.login.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof User) ){
			return false;
		}
		
		User other = (User) obj;
		return this.login.hashCode() == other.getLogin().hashCode();
	}
	
	/**
	 * User receives a new item.
	 * @param itemName
	 * @param description
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public String addItem(String itemName, String description, String category)
			throws Exception{
		return userOperationManager.addItem(itemName, description, category);
	}

	/**
	 * User requests friendship with another user.
	 * @param otherUser
	 * @throws Exception
	 */
	public void requestFriendship(User otherUser) throws Exception{
		userOperationManager.requestFriendship(otherUser);
	}


	/**
	 * User accepts friendship request from another user.
	 * @param otherUser
	 * @throws Exception
	 */
	public void acceptFriendshipRequest(User otherUser) throws Exception{
		userOperationManager.acceptFriendshipRequest(otherUser);
	}

	
	public CommunicationManager getCommunicationManager() {
		return userOperationManager.getCommunicationManager();
	}

	/**
	 * User declines friendship request from another user.
	 * @param otherUser
	 */
	public void declineFriendshipRequest(User otherUser) throws Exception{
		userOperationManager.declineFriendshipRequest(otherUser);
	}
		
	/**
	 * Returns true if user has another user as friend.
	 * @param otherUser
	 * @return
	 */
	public boolean hasFriend(User otherUser) throws Exception{
		return userOperationManager.hasFriend(otherUser);
	}

	
	/**
	 * User sends a borrow request of specified item to another user.
	 * @param item
	 * @param lender
	 * @param days
	 * @return
	 * @throws Exception
	 */
	public String borrowItem(Item item, User lender, int days) throws Exception {
		return userOperationManager.borrowItem(item, lender, days);
	}

	/**
	 * User approves lending of one of his avaliable items.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public String approveLending(String requestId) throws Exception{
		return userOperationManager.approveLending(requestId);
	}
	
	/**
	 * User refuses lending one of his available items.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public String denyLending(String requestId) throws Exception{
		return userOperationManager.denyLending(requestId);
	}
	
	

	/**
	 * User gives item back.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public String approveItemReturning(String lendingId) throws Exception{
		return userOperationManager.approveItemReturning(lendingId);
	}
	
	/**
	 * User approves item receipt.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public String confirmLendingTermination(String lendingId) throws Exception{
		return userOperationManager.confirmLendingTermination(lendingId);
	}
	
	/**
	 * User denies item receipt.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public String denyLendingTermination(String lendingId) throws Exception{
		return userOperationManager.denyLendingTermination(lendingId);
	}
	
	/**
	 * User sends a message to another user.
	 * @param subject
	 * @param message
	 * @param receiver
	 * @return
	 * @throws Exception
	 */
	public String sendMessage(String subject, String message, User receiver) 
			throws Exception {
		return userOperationManager.sendMessage(subject, message, receiver);
	}
	
	/**
	 * User sends a negotiation message to another user.
	 * @param subject
	 * @param message
	 * @param receiver
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public String sendMessage(String subject, String message, User receiver,
			String lendingId) throws Exception {
		return userOperationManager.sendMessage(subject, message, receiver, lendingId);
	}

	/**
	 * User asks for his item back.
	 * @param lendingId
	 * @param systemDate
	 * @return
	 * @throws Exception
	 */
	public String askForReturnOfItem(String lendingId, Date systemDate) throws Exception{
		return userOperationManager.askForReturnOfItem(lendingId, systemDate);
	}
	
	/**
	 * User registers interest for item if he is friend of the owner.
	 * @param item
	 * @param owner
	 * @throws Exception
	 */
	public void registerInterestForItem(Item item, User owner) throws Exception{
		userOperationManager.registerInterestForItem(item, owner);
	}

	/**
	 * Returns true if another user is interested on item whose owner is this user.
	 * @param item
	 * @param interested
	 * @return
	 * @throws Exception
	 */
	public boolean isInterestedOnMyItem(Item item, User interested) throws Exception{
		return userOperationManager.isInterestedOnMyItem(item, interested);
	}
	
	/**
	 * Returns all items whose owner is this user.
	 * @return
	 */
	public Set<Item> getAllItems() {
		return userOperationManager.getAllItems();
	}

	/**
	 * Returns this user lent items.
	 * @return
	 */
	public Set<Item> getLentItems() {
		return userOperationManager.getLentItems();
	}

	/**
	 * Returns items borrowed by this user.
	 * @return
	 */
	public Set<Item> getBorrowedItems() {
		return userOperationManager.getBorrowedItems();
	}
	
	/**
	 * Returns user friends.
	 * @return
	 */
	public Set<User> getFriends() {
		return userOperationManager.getFriends();
	}

	/**
	 * User breaks friendship with another user.
	 * @param user
	 */
	public void breakFriendship(User user) throws Exception{
		userOperationManager.breakFriendship(user);
	}
	
	/**
	 * Returns a lending record by its id.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public Lending getLendingByRequestId(String requestId) throws Exception{
		return userOperationManager.getLendingByRequestId(requestId);
	}
	
	@Override
	public void warnInterestedThatTargetIsAvailable(Item target,
			InterestedOn<Item> interested) throws Exception{
		userOperationManager.warnInterestedThatTargetIsAvailable(target, interested);
	}

	/**
	 * Returns a lending record by its id.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public Lending getLendingByLendingId(String lendingId) throws Exception{
		return userOperationManager.getLendingByLendingId(lendingId);
	}	
	
	public EventDate getCreationDate() {
		return creationDate;
	}

	public Set<User> getReceivedFriendshipRequests() {
		return userOperationManager.getReceivedFriendshipRequests();
	}
	
	/**
	 * Deletes a item this user is owner.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public void deleteMyItem(String itemId) throws Exception{
		userOperationManager.deleteMyItem(itemId);
	}

	public int getReputation() {
		return userOperationManager.getReputation();
	}

	public Set<Lending> getReceivedItemRequests() {
		return userOperationManager.getReceivedItemRequests();
	}
	
	public String publishItemRequest(String itemName, String itemDescription) throws Exception{
		return userOperationManager.publishItemRequest(itemName, itemDescription);
	}
	
	public Set<Lending> getPublishedItemRequests(){
		return userOperationManager.getPublishedItemRequests();
	}

	public void offerItem(Lending publishedRequest, Item item) throws Exception{
		userOperationManager.offerItem(publishedRequest, item);		
	}
	
	public void republishItemRequest(Lending petition) throws Exception{
		userOperationManager.republishItemRequest(petition);
	}

	public Set<Lending> getSentItemRequests() {
		return userOperationManager.getSentItemRequests();
	}

	public List<ActivityRegistry> getMyActivityHistory() {
		return userOperationManager.getMyActivityHistory();
	}

	public List<Message> getMessagesByTopicId(String topicId) throws Exception{
		return userOperationManager.getMessagesByTopicId(topicId);
	}

	public List<Topic> getTopics(String topicType) throws Exception{
		return userOperationManager.getTopics(topicType);
	}
}