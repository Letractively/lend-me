package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import entities.util.Address;
import entities.util.Category;
import entities.util.EntitiesConstants;
import entities.util.EventDate;
import entities.util.Message;
import entities.util.Topic;


public class User implements Comparable<User>{

	private String login;
	private String name;
	private Address address;
	private Set<User> myFriends = new HashSet<User>();
	private Set<User> receivedFriendshipRequests = new HashSet<User>();
	private Set<User> sentFriendshipRequests = new HashSet<User>();
	private Map<Item,User> myItems = new HashMap<Item,User>();
	private Set<Lending> receivedItemRequests = new HashSet<Lending>();
	private Set<Lending> myBorrowedItems = new HashSet<Lending>();
	private Set<Topic> negotiationTopics = new HashSet<Topic>();
	private Set<Topic> offTopicTopics = new HashSet<Topic>();
	private Map<Item, ArrayList<User>> itemsDesired = new HashMap<Item, ArrayList<User>>();
	private EventDate dateOfCreation;
	
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
		this.dateOfCreation = new EventDate(String.format(EntitiesConstants.USER_REGISTERED_MESSAGE, this.login, this.name));
		
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
	
	public Map<Item, User> getMyItems() {
		return myItems;
	}

	public void setAddress(String street, String number, String neighborhood,
			String city, String state, String country, String zipCode) {
		this.address = new Address(street, number, neighborhood, city, state, country, zipCode);
	}

	public Address getAddress() {
		return address;
	}

	public boolean nameMatches(String toBeMatched) {
		return this.name.toUpperCase().contains(toBeMatched.toUpperCase());
	}
	
	@Override
	public String toString(){
		StringBuilder userToString = new StringBuilder();
		userToString.append(this.name);
		userToString.append(this.login);
		userToString.append(this.address);
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
	

	public boolean hasItem(Item item) {
		return myItems.containsKey(item);
	}

	public String addItem(String itemName, String description, Category category)
			throws Exception{
		Item myNewItem = new Item(itemName, description, category);
		this.myItems.put(myNewItem, this);
		return myNewItem.getID();
	}

	public void requestFriendship(User otherUser) throws Exception{
		if ( sentFriendshipRequests.contains(otherUser) ){
			throw new Exception("Requisição já solicitada");//"The request has already been sent");
		}
		if ( myFriends.contains(otherUser) ){
			throw new Exception("Os usuários já são amigos");//"The users are already friends");
		}
		this.sentFriendshipRequests.add(otherUser);
		otherUser.addFriendshipRequest(this);
	}

	private void addFriendshipRequest(User otherUser) {
		receivedFriendshipRequests.add(otherUser);
	}

	public void acceptFriendshipRequest(User otherUser) throws Exception{
		if ( myFriends.contains(otherUser) ){
			throw new Exception("Os usuários já são amigos");//"The users are already friends");
		}
		if ( !receivedFriendshipRequests.contains(otherUser) ){
			throw new Exception("Requisição de amizade inexistente");//Inexistent friendship request");
		}
		else{
			myFriends.add(otherUser);
			receivedFriendshipRequests.remove(otherUser);
			otherUser.addRequestedFriend(this);
		}
	}
	
	private void addRequestedFriend(User otherUser) {
		if ( sentFriendshipRequests.contains(otherUser) ){
			myFriends.add(otherUser);
			sentFriendshipRequests.remove(otherUser);
		}
	}
	
	public void declineFriendshipRequest(User otherUser){
		if ( receivedFriendshipRequests.contains(otherUser) ){
			receivedFriendshipRequests.remove(otherUser);
		}
		otherUser.removeRequestedFriend(this);
	}

	private void removeRequestedFriend(User otherUser) {
		if ( sentFriendshipRequests.contains(otherUser) ){
			sentFriendshipRequests.remove(otherUser);
		}
	}
		
	public boolean hasFriend(User otherUser) {
		return this.myFriends.contains(otherUser);
	}

	private boolean isLent(Item item) {
		return ! myItems.get(item).equals(this);
	}

	public void borrowItem(Item item, User lender, int days) {
		if (myFriends.contains(lender) && lender.hasItem(item)) {                       
			if (! lender.isLent(item)) {
				Lending requestLending = new Lending(this, lender, item, days);
				lender.requestItem(requestLending);
				
				sendMessage("Emprestimo do item " + item.getName() + " a " +
				this.getName(), this.getName() + " solicitou o emprestimo do item " +
				item.getName(), lender, requestLending.getID());//"Lending of item %s to %s, %s wants to borrow item");
			}                                               
		}
	}

	@SuppressWarnings("deprecation")
	private void requestItem(Lending requestLending) {
		requestLending.setDayOfRequestion(new java.util.Date().getDay());
		receivedItemRequests.add(requestLending);
	}


	@SuppressWarnings("deprecation")
	public void lendItem(Item item, User borrower, int days) {
		if (myItems.containsKey(item)) {
			if (! this.isLent(item)) {
				myItems.put(item, borrower);
				
				Lending lending = new Lending(borrower, this, item, days);
				lending.setDayOfTheLending(new Date().getDay());
				
				if (receivedItemRequests.contains(lending)) {
					borrower.addRequestedItem(item, this, days);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void addRequestedItem(Item item, User lender, int days) {
		Lending lending = new Lending(this, lender, item, days);
		lending.setDayOfTheLending(new java.util.Date().getDay());
		myBorrowedItems.add(lending);
	}
	
	public void declineLendingItem(Item item, User otherUser, int days) {
		if (myItems.containsKey(item)) {
			if (! this.isLent(item)) {
				if (receivedItemRequests.contains(new Lending(otherUser, this, item, days))) {
					receivedItemRequests.remove(new Lending(otherUser, this, item, days));
				}
			}
		}
	}
	
	public boolean hasBorrowedItem(Item item) {
		for (Lending actual : myBorrowedItems){
			if(actual.getItem().equals(item)){
				return true;
			}
		}
		return false;
	}

	public void returnItem(Item item) {
		for(Lending actual : myBorrowedItems){
			if(actual.getItem().equals(item)){
				actual.getLender().setReturned(item);
				actual.setReturned(true);
			}
		}
		
	}

	private void setReturned(Item item) {
		for(Lending actual : receivedItemRequests){
			if(actual.getItem().equals(item)){
				actual.setReturned(true);
			}
		}
	}

	public void receiveLendedItem(Item item) {
		for(Lending actual : receivedItemRequests){
			if(actual.getItem().equals(item) && actual.isReturned()){
				if(itemsDesired.containsKey(item)){
					for(User interested : itemsDesired.get(item)){
						interested.sendMessage("Available Item!", "The " + item.getName() +
												" of the " + this.getName() +
												" is available now.", interested);
					}
				}
				
				this.receivedItemRequests.remove(actual);
				this.myItems.put(item, this);
				actual.getBorrower().removeBorrowedItem(item);
			}
		}
		
	}

	private void removeBorrowedItem(Item item) {
		for(Lending actual : myBorrowedItems){
			if(actual.getItem().equals(item)){
				this.myBorrowedItems.remove(actual);
			}
		}
	}

	public void sendMessage(String subject, String message, User receiver) {
		receiver.receiveMessage(subject, message, this, true, "");
	}
	
	public void sendMessage(String subject, String message, User receiver, String lendingId) {
		//TODO Treat how is it going to deal with the Lending id
		receiver.receiveMessage(subject, message, this, false, lendingId);
	}

	public void receiveMessage(String subject, String message, User sender,
			boolean isOffTopic, String lendingId) {
		
		if (isOffTopic) {
			addMessageToTopic(offTopicTopics, subject, message, sender,
					isOffTopic, lendingId);
		}
		
		else {
			addMessageToTopic(negotiationTopics, subject, message, sender,
					isOffTopic, lendingId);
		}
	}

	private void addMessageToTopic(Set<Topic> topicSet, String subject, String message, User sender,
			boolean isOffTopic, String lendingId) {
		
		Topic foundTopic = getTopicBySubject(topicSet, subject); 
		
		if ( foundTopic != null) {
			foundTopic.addMessage(subject, message, sender, isOffTopic, lendingId);
		}
		else {
			topicSet.add(new Topic(subject));
			receiveMessage(subject, message, sender, isOffTopic, lendingId);
		}		
	}

	public Set<Message> getTopicMessages(String topicSubject) throws Exception {
		Topic foundTopic = getTopicBySubject(negotiationTopics, topicSubject); 
		
		if ( foundTopic != null) {
			return foundTopic.getMessages();
		}
		
		else {
			foundTopic = getTopicBySubject(offTopicTopics, topicSubject);
			
			if ( foundTopic != null) {
				return foundTopic.getMessages();
			}
		}
		
		throw new Exception("Could not find any topic with the given subject.");
	}
	
	private Topic getTopicBySubject(Set<Topic> topicSet, String subject) {
		for (Topic topic : topicSet) {
			if (topic.getSubject().equals(subject)) {
				return topic;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public void returnRequest(Item item) {
		for(Lending actual : receivedItemRequests){
			if(actual.getItem().equals(item)){
				actual.getBorrower().setRequestedBack(item);
				
				if(new java.util.Date().getDay() < actual.getDayOfTheLending() + actual.getRequiredDays()){
					actual.setCanceled(true);
				}
				
				actual.setRequestedBack(true);
				
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void setRequestedBack(Item item) {
		for(Lending actual : myBorrowedItems){
			if(actual.getItem().equals(item)){
				actual.setRequestedBack(true);

				if(new java.util.Date().getDay() < actual.getDayOfTheLending() + actual.getRequiredDays()){
					actual.setCanceled(true);
				}
			}
		}
	}
	
	public boolean hasRequestedBack(Item item){
		for(Lending actual : myBorrowedItems){
			if(actual.getItem().equals(item) && actual.isRequestedBack()){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasCanceled(Item item){
		for(Lending actual : myBorrowedItems){
			if(actual.getItem().equals(item) && actual.isCanceled()){
				return true;
			}
		}
		return false;
	}
	
	public Set<Item> getRequestedBackItems(){
		Set<Item> requestedBackItems = new HashSet<Item>();
		for(Lending actual : myBorrowedItems){
			if(actual.isRequestedBack()){
				requestedBackItems.add(actual.getItem());
			}
		}
		return requestedBackItems;
		
	}
	
	public Set<Item> getCanceledItems(){
		Set<Item> canceledItems = new HashSet<Item>();
		for(Lending actual : myBorrowedItems){
			if(actual.isCanceled()){
				canceledItems.add(actual.getItem());
			}
		}
		return canceledItems;
		
	}
	
	public List<Topic> getTopics(String topicType) throws Exception {
		if (topicType.equals(EntitiesConstants.OFF_TOPIC)) {
			Topic[] offTopicArray = this.offTopicTopics.
					toArray(new Topic[offTopicTopics.size()]);
			Arrays.sort(offTopicArray);
			return Arrays.asList(offTopicArray);
		}
		
		else if (topicType.equals(EntitiesConstants.NEGOTIATION_TOPIC)) {
			Topic[] negotiationTopicsArray = this.negotiationTopics.
					toArray(new Topic[negotiationTopics.size()]);
			Arrays.sort(negotiationTopicsArray);
			return Arrays.asList(negotiationTopicsArray);
		}
		
		else if (topicType.equals(EntitiesConstants.ALL_TOPICS)) {
			Set<Topic> allTopics = new HashSet<Topic>();
			allTopics.addAll(offTopicTopics);
			allTopics.addAll(negotiationTopics);
			Topic[] allTopicsArray = allTopics.
					toArray(new Topic[allTopics.size()]);
			Arrays.sort(allTopicsArray);
			return Arrays.asList(allTopicsArray);
		}
		
		throw new Exception("Voce deve escolher um tipo de topico");
	}

	public void registerInterestForItem(Item item, User owner) {
		if(myFriends.contains(owner)){
			if(owner.hasItem(item)){
				if(owner.isLent(item)){
					owner.markAsInterested(item, this);
				}
			}
		}
	}

	private void markAsInterested(Item item, User interested) {
		if(itemsDesired.containsKey(item)){
			itemsDesired.get(item).add(interested);
		}else{
			itemsDesired.put(item, new ArrayList<User>());
			itemsDesired.get(item).add(interested);
		}
	}

	public boolean isMarkedAsInterested(Item item) {
		if(myItems.containsKey(item)){
			return this.itemsDesired.containsKey(item);
		}
		return false;
	}

	public List<Item> searchFromNewestToOldest(String keyWord) {
		List<Item> foundItems = new ArrayList<Item>();
		for(User actual : myFriends){
			for(Item current : actual.getMyItems().keySet()){
				if(current.getName().contains(keyWord) ||
						current.getDescription().contains(keyWord)){
					foundItems.add(current);
				}
			}
		}
		Item[] founItemsSorted = foundItems.toArray(new Item[foundItems.size()]);
		Arrays.sort(founItemsSorted);
		
		return Arrays.asList(founItemsSorted);
	}
	
	public List<Item> searchFromOldestToNewest(String keyWord){
		List<Item> foundItems = searchFromNewestToOldest(keyWord);
		int j = foundItems.size();
		Item[] foundItemsSorted = new Item[j];
		if(j > 0){
			for(int i = 0; i < foundItems.size(); i++){
				foundItemsSorted[--j] = foundItems.get(i);
			}
		}
		
		return Arrays.asList(foundItemsSorted);
	}
	
	
	public Set<Item> getAllItems() {
		Map<Item, User> toBeReturned = new HashMap<Item, User>();
		toBeReturned.putAll(myItems);
		return toBeReturned.keySet();
	}
	
	public Set<User> getFriends() {
		Set<User> toBeReturned = new HashSet<User>();
		toBeReturned.addAll(myFriends);
		return toBeReturned;
	}

	public boolean isRequestItem(Item item){
		
		for(Lending actualLending: receivedItemRequests){
			if(actualLending.getItem().equals(item))
				return true;
		}
		return false;
	}
	
	public void forceRemoveFriend(User user){

		if (this.myFriends.contains(user)) {

			this.myFriends.remove(user);
			for (Lending actualLending : this.receivedItemRequests) {
				if (actualLending.getBorrower().equals(user))
					receivedItemRequests.remove(actualLending);
			}
		}
	}
		
	public void breakFriendship(User user) {
		this.forceRemoveFriend(user);
		user.forceRemoveFriend(this);
	}

	@Override
	public int compareTo(User o) {
		if(this.dateOfCreation.getDate().after(o.getDateOfCreation().getDate())){
			return 1;
		}
		return 0;
	}

	public EventDate getDateOfCreation() {
		return dateOfCreation;
	}

	public Set<User> getReceivedFriendshipRequests() {
		return receivedFriendshipRequests;
	}
	
}