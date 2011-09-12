package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import entities.util.Address;
import entities.util.EntitiesConstants;
import entities.util.EventDate;
import entities.util.LendingStatus;
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
	private Set<Lending> sentItemRequests = new HashSet<Lending>();
	private Set<Lending> myBorrowedItems = new HashSet<Lending>();
	private Set<Lending> myLentItems = new HashSet<Lending>();
	private Set<Lending> lentRegistryHistory = new HashSet<Lending>();
	private Set<Lending> borrowedRegistryHistory = new HashSet<Lending>();
	private Set<Lending> loanDenialRegistryHistory = new HashSet<Lending>();
	private Set<Topic> negotiationTopics = new HashSet<Topic>();
	private Set<Topic> offTopicTopics = new HashSet<Topic>();
	private Map<Item, ArrayList<User>> myInterestingItems = new HashMap<Item, ArrayList<User>>();
	private EventDate creationDate;
	
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

	public String addItem(String itemName, String description, String category)
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

	public String borrowItem(Item item, User lender, int days) throws Exception {
		if ( days <=0 ){
			throw new Exception("Duracao inválida");//"Requested day amount is invalid");
		}
		for ( Lending record : sentItemRequests ){
			if ( record.getItem().idMatches(item.getID()) ){
				throw new Exception("Requisição já solicitada");//"Request already sent");
			}
		}
		if (myFriends.contains(lender) && lender.hasItem(item)) {                       
			if (! lender.isLent(item)) {
				Lending lendingRequest = new Lending(this, lender, item, days);
				lender.requestItem(lendingRequest);
				sentItemRequests.add(lendingRequest);
				sendMessage("Empréstimo do item " + item.getName() + " a " +
				this.getName(), this.getName() + " solicitou o empréstimo do item " +
				item.getName(), lender, lendingRequest.getID());
				return lendingRequest.getID();
			}
		}
		throw new Exception("ERR: borrower wants to borrow item that lender doesn have");
	}

	private void requestItem(Lending requestLending) {
		receivedItemRequests.add(requestLending);
	}

	public String approveLoan(String requestId) throws Exception{
		if ( requestId == null || requestId.trim().isEmpty() ){
			throw new Exception("Identificador da requisição de empréstimo é inválido");//"Lending request identifier invalid");
		}
		for ( Lending record : receivedItemRequests ){
			if ( record.getID().equals(requestId) ){
				return lendItem(record.getItem(), record.getBorrower(), record.getRequiredDays());
			}
		}
		for ( Lending record : myLentItems ){
			if ( record.getID().equals(requestId) ){
				throw new Exception("Empréstimo já aprovado");//Lending request already approved
			}
		}
		throw new Exception("Requisição de empréstimo inexistente");//"Inexistent item request");
	}
	
	public String lendItem(Item item, User borrower, int days) throws Exception{

		Lending requestAccepted = null;
		for ( Lending record : receivedItemRequests ){
			if ( record.getItem().equals(item) ){
				requestAccepted = record;
				record.setLendingDate();
				borrower.addRequestedItem(item, this, days);
			}
		}
		if ( requestAccepted != null ){
			receivedItemRequests.remove(requestAccepted);
			myLentItems.add(requestAccepted);
			myItems.put(item, borrower);
			return requestAccepted.getID();
		}
		else{
			throw new Exception("ERR: lender wants to lend item he wasnt asked for");
		}
	}

	private void addRequestedItem(Item item, User lender, int days) throws Exception{
		Lending requestAccepted = null;
		for ( Lending record : sentItemRequests ){
			if ( record.getItem().equals(item) && record.getLender().equals(lender)
					&& record.getRequiredDays() == days ){
				requestAccepted = record;
				record.setLendingDate();
			}
		}
		if ( requestAccepted != null ){
			sentItemRequests.remove(requestAccepted);
			myBorrowedItems.add(requestAccepted);
		}
		else{
			throw new Exception("ERR: borrower wants to receive a item he didnt ask for");
		}
	}
	
	public String denyLoan(String requestId) throws Exception{
		if ( requestId == null || requestId.trim().isEmpty() ){
			throw new Exception("Identificador da requisição de empréstimo é inválido");//"Lending request identifier invalid");
		}
		for ( Lending record : receivedItemRequests ){
			if ( record.getID().equals(requestId) ){
				return declineLendingItem(record.getItem(), record.getBorrower(), record.getRequiredDays());
			}
		}
		for ( Lending record : loanDenialRegistryHistory ){
			if ( record.getID().equals(requestId) ){
				throw new Exception("Empréstimo já negado");//Lending request already approved
			}
		}
		throw new Exception("Requisição de empréstimo inexistente");//"Inexistent item request");
	}
	
	public String declineLendingItem(Item item, User borrower, int days) throws Exception{
		
		Lending requestDenied = null;
		for ( Lending record : receivedItemRequests){
			if (record.getItem().equals(item) && record.getBorrower().equals(borrower) &&
					record.getRequiredDays() == days ) {
				requestDenied = record;
				borrower.removeRequestedItem(item, this, days);
			}
		}
		if ( requestDenied != null ){
			receivedItemRequests.remove(requestDenied);
			requestDenied.setStatus(LendingStatus.DENIED);
			loanDenialRegistryHistory.add(requestDenied);
			return requestDenied.getID();
		}
		else{
			throw new Exception("ERR: lender doesnt want to lend a item that he wasnt asked for");
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
			throw new Exception("ERR: borrower was asked to remove his request for item because it was denied but he doesnt have it");
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
	
	public String returnItem(Item item) throws Exception{
		for(Lending record : myBorrowedItems){
			if(record.getItem().equals(item)){
				if ( record.isReturned() ){
					throw new Exception("Item já devolvido");//"Item already set to be returned);
				}
				record.getLender().setReturned(item);
				record.setReturned(true);
				return record.getID();
			}
		}
		throw new Exception("ERR: borrower wants to return item but he doesn't have it");
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

	public String confirmLendingTermination(String lendingId) throws Exception{
		if ( lendingId == null || lendingId.trim().isEmpty() ){
			throw new Exception("Identificador do empréstimo é inválido");//"Lending identifier is invalid");
		}
		for ( Lending record : myLentItems ){
			if ( record.getID().equals(lendingId) ){
				receiveLentItem(record.getItem());
				return record.getID();
			}
		}
		for ( Lending record : lentRegistryHistory ){
			if ( record.getID().equals(lendingId) ){
				throw new Exception("Término do empréstimo já confirmado");
			}
		}
		throw new Exception("Empréstimo inexistente");
	}
	
	public void receiveLentItem(Item item) throws Exception{
		Lending requestAttended = null;
		for(Lending record : myLentItems){
			if(record.getItem().equals(item) && record.isReturned()){
				requestAttended = record;
				if(myInterestingItems.containsKey(item)){
					for(User interested : myInterestingItems.get(item)){
						interested.sendMessage("Available Item!", "The " + item.getName() +
												" of the " + this.getName() +
												" is available now.", interested, "");
					}
				}
			}
		}
		if ( requestAttended != null ){
			myLentItems.remove(requestAttended);
			requestAttended.setStatus(LendingStatus.FINISHED);
			lentRegistryHistory.add(requestAttended);
			myItems.put(item, this);
			requestAttended.getBorrower().removeBorrowedItem(item);
		}
		else{
			throw new Exception("ERR: lender wants to receive lended item but he doesnt have it");
		}
	}

	private void removeBorrowedItem(Item item) throws Exception{
		Lending requestAttended = null;
		for(Lending record : myBorrowedItems){
			if(record.getItem().equals(item)){
				requestAttended = record;
			}
		}
		if ( requestAttended != null ){
			myBorrowedItems.remove(requestAttended);
			requestAttended.setStatus(LendingStatus.FINISHED);
			borrowedRegistryHistory.add(requestAttended);
		}
		else{
			throw new Exception("ERR: borrower wants to remove his borrowed item he gave back but he doesnt have it");
		}
	}
	
	public String sendMessage(String subject, String message, User receiver,
			String lendingId) throws Exception{
		
		boolean isOffTopic = false;
		if ( lendingId == null ){
			throw new Exception("Identificador da requisição de empréstimo é inválido");
		}
		else if ( lendingId.trim().isEmpty() ) {
			isOffTopic = true;
		}
		else if ( getLendingByLendingId(lendingId) == null ){
			throw new Exception("Requisição de empréstimo inexistente");
		}
		storeMessage(subject, message, this.getLogin(), receiver.getLogin(),
				isOffTopic, lendingId);
		return receiver.storeMessage(subject, message, this.getLogin(),
				receiver.getLogin(), isOffTopic, lendingId);
	}

	public String storeMessage(String subject, String message, String senderLogin,
			String receiverLogin, boolean isOffTopic, String lendingId) throws Exception{
		if (isOffTopic) {
			return addMessageToTopic(offTopicTopics, subject, message, senderLogin,
					receiverLogin, isOffTopic, lendingId);
		}
		else {
			return addMessageToTopic(negotiationTopics, subject, message, senderLogin,
					receiverLogin, isOffTopic, lendingId);
		}
	}

	private String addMessageToTopic(Set<Topic> topicSet, String subject, 
			String message, String senderLogin, String receiverLogin,
			boolean isOffTopic,	String lendingId) throws Exception{
		
		if ( topicSet == null ){
			throw new Exception("Conjunto de tópicos inválido");
		}
		try{
			Topic foundTopic = getTopicBySubject(subject);			
			foundTopic.addMessage(subject, message, senderLogin, receiverLogin, 
					isOffTopic, lendingId);
			return foundTopic.getID();
		}
		catch (Exception e){
			if ( e.getMessage().equals("Tópico inexistente") ){
				Topic newTopic = new Topic(subject);
				newTopic.addMessage(subject, message, senderLogin, receiverLogin, 
						isOffTopic, lendingId);
				topicSet.add(newTopic);
				return newTopic.getID();
			}
			else{
				throw e;
			}
		}
	}
	
	public List<Message> getMessagesByTopicId(String topicId) throws Exception {

		Topic foundTopic = getTopicById(topicId); 
		Message[] msgArray = foundTopic.getMessages().toArray(
				new Message[foundTopic.getMessages().size()]);
		Arrays.sort(msgArray);
		return Arrays.asList(msgArray);
		
	}
		
	private Topic getTopicById(String topicId) throws Exception{
		if ( topicId == null || topicId.trim().isEmpty() ){
			throw new Exception("Identificador do tópico é inválido");
		}
		for (Topic topic : negotiationTopics) {
			if (topic.getID().equals(topicId)) {
				return topic;
			}
		}
		for (Topic topic : offTopicTopics) {
			if (topic.getID().equals(topicId)) {
				return topic;
			}
		}
		throw new Exception("Tópico inexistente");
	}

	public Set<Message> getMessagesByTopicSubject(String topicSubject) throws Exception {
		Topic foundTopic = getTopicBySubject(topicSubject); 
		return foundTopic.getMessages();
	}
	
	private Topic getTopicBySubject(String subject) throws Exception{
		if ( subject == null || subject.trim().isEmpty() ){
			throw new Exception("Assunto inválido");
		}
		for (Topic topic : negotiationTopics) {
			if (topic.getSubject().equals(subject)) {
				return topic;
			}
		}
		for (Topic topic : offTopicTopics) {
			if (topic.getSubject().equals(subject)) {
				return topic;
			}
		}
		throw new Exception("Tópico inexistente");
	}

	public String askForReturnOfItem(String lendingId) throws Exception{
		if ( lendingId == null || lendingId.trim().isEmpty() ){
			throw new Exception("Identificador do empréstimo é inválido");//"Lending request identifier invalid");
		}
		for ( Lending record : myLentItems ){
			if ( record.getID().equals(lendingId) ){
				return requestBack(record.getItem());
			}
		}
		throw new Exception("Empréstimo inexistente");//"Inexistent item request");
	}
	
	public String requestBack(Item item) throws Exception{
		for(Lending record : myLentItems){
			if(record.getItem().equals(item)){
				record.getBorrower().setRequestedBack(item);
				record.getLendingDate().addDays(record.getRequiredDays());
				if(new EventDate().getDate().before(record.getLendingDate().getDate())){
					record.setCanceled(true);
				}
				record.setRequestedBack(true);
				return record.getID();
			}
		}
		throw new Exception("Empréstimo inexistente");//"Inexistent item request");
	}

	private void setRequestedBack(Item item) throws Exception{
		for(Lending actual : myBorrowedItems){
			if(actual.getItem().equals(item)){
				actual.setRequestedBack(true);
				actual.getLendingDate().addDays(actual.getRequiredDays());
				if(new EventDate().getDate().before(actual.getLendingDate().getDate())){
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
		if ( topicType == null || topicType.trim().isEmpty() ){
			throw new Exception("Tipo inválido");
		}
		if (topicType.equals("negociacao")) {
			topicType = EntitiesConstants.NEGOTIATION_TOPIC;
		} else if (topicType.equals("offtopic")) {
			topicType = EntitiesConstants.OFF_TOPIC;
		} else if (topicType.equals("todos")) {
			topicType = EntitiesConstants.ALL_TOPICS;
		}

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
		else{
			throw new Exception("Tipo inexistente");
		}
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
		if(myInterestingItems.containsKey(item)){
			myInterestingItems.get(item).add(interested);
		}else{
			myInterestingItems.put(item, new ArrayList<User>());
			myInterestingItems.get(item).add(interested);
		}
	}

	public boolean isMarkedAsInterested(Item item) {
		if(myItems.containsKey(item)){
			return this.myInterestingItems.containsKey(item);
		}
		return false;
	}
	
	public Set<Item> getAllItems() {
		Map<Item, User> toBeReturned = new HashMap<Item, User>();
		toBeReturned.putAll(myItems);
		return toBeReturned.keySet();
	}
	
	public Set<Item> getLentItems() {
		Set<Item> result = new HashSet<Item>();
		for ( Lending record : myLentItems ){
			result.add(record.getItem());
		}
		return result;
	}

	public Set<Item> getBorrowedItems() {
		Set<Item> result = new HashSet<Item>();
		for ( Lending record : myBorrowedItems ){
			result.add(record.getItem());
		}
		return result;
	}
	
	public Set<User> getFriends() {
		Set<User> toBeReturned = new HashSet<User>();
		toBeReturned.addAll(myFriends);
		return toBeReturned;
	}

	public boolean isItemRequested(Item item){
		
		for(Lending record: receivedItemRequests){
			if(record.getItem().equals(item))
				return true;
		}
		return false;
	}
	
	public void forceRemoveFriend(User user){
		if (this.myFriends.contains(user)) {
			this.myFriends.remove(user);
			for (Lending record : this.receivedItemRequests) {
				if (record.getBorrower().equals(user))
					receivedItemRequests.remove(record);
			}
			for (Lending record : this.sentItemRequests) {
				if (record.getLender().equals(user))
					sentItemRequests.remove(record);
			}
		}
	}
		
	public void breakFriendship(User user){
		this.forceRemoveFriend(user);
		user.forceRemoveFriend(this);
	}
	
	public Lending getLendingByRequestId(String requestId) throws Exception{
		if ( requestId == null || requestId.trim().isEmpty() ){
			throw new Exception("Identificador da requisição de empréstimo é inválido");//"Lending request identifier invalid");
		}
		for (Lending record : this.receivedItemRequests){
			if(record.getID().equals(requestId)){
				return record;
			}
		}
		for (Lending record : this.sentItemRequests ){
			if(record.getID().equals(requestId)){
				return record;
			}
		}
		for (Lending record : this.myBorrowedItems ){
			if(record.getID().equals(requestId)){
				return record;
			}
		}
		for (Lending record : this.myLentItems ){
			if(record.getID().equals(requestId)){
				return record;
			}
		}
		for (Lending record : this.lentRegistryHistory ){
			if(record.getID().equals(requestId)){
				return record;
			}			
		}
		for (Lending record : this.borrowedRegistryHistory ){
			if(record.getID().equals(requestId)){
				return record;
			}			
		}
		for (Lending record : this.loanDenialRegistryHistory ){
			if(record.getID().equals(requestId)){
				return record;
			}			
		}
		return null;
	}

	public Lending getLendingByLendingId(String lendingId) throws Exception{
		if ( lendingId == null || lendingId.trim().isEmpty() ){
			throw new Exception("Identificador do empréstimo é inválido");//"Lending request identifier invalid");
		}
		for (Lending record : this.receivedItemRequests){
			if(record.getID().equals(lendingId)){
				return record;
			}
		}
		for (Lending record : this.sentItemRequests ){
			if(record.getID().equals(lendingId)){
				return record;
			}
		}
		for (Lending record : this.myBorrowedItems ){
			if(record.getID().equals(lendingId)){
				return record;
			}
		}
		for (Lending record : this.myLentItems ){
			if(record.getID().equals(lendingId)){
				return record;
			}
		}
		for (Lending record : this.lentRegistryHistory ){
			if(record.getID().equals(lendingId)){
				return record;
			}
		}
		for (Lending record : this.borrowedRegistryHistory ){
			if(record.getID().equals(lendingId)){
				return record;
			}			
		}
		for (Lending record : this.loanDenialRegistryHistory ){
			if(record.getID().equals(lendingId)){
				return record;
			}			
		}
		return null;
	}	
	@Override
	public int compareTo(User o) {
		if(this.creationDate.getDate().after(o.getCreationDate().getDate())){
			return 1;
		}
		return 0;
	}

	public EventDate getCreationDate() {
		return creationDate;
	}

	public Set<User> getReceivedFriendshipRequests() {
		return receivedFriendshipRequests;
	}
	
	public Set<Lending> getMyBorrowedItemsRecord() {
		return myBorrowedItems;
	}

	public Set<Lending> getMyLentItemsRecord() {
		return myLentItems;
	}
	
	public Set<Lending> getLentRegistryHistory() {
		return lentRegistryHistory;
	}
	
	public Set<Lending> getBorrowedRegistryHistory() {
		return borrowedRegistryHistory;
	}
	
}