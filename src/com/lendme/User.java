package com.lendme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
	private Set<Lending> sentItemDevolutionRequests = new HashSet<Lending>();
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

	public boolean hasLentThis(Item item) {
		for ( Lending lentItem : myLentItems ){
			if ( lentItem.getItem().equals(item) ){
				return true;
			}
		}
		return false;
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
			if (! lender.hasLentThis(item)) {
				Lending lendingRequest = new Lending(this, lender, item, days);
				lender.requestItem(lendingRequest);
				sentItemRequests.add(lendingRequest);
				sendMessage("Empréstimo do item " + item.getName() + " a " +
				this.getName(), this.getName() + " solicitou o empréstimo do item " +
				item.getName(), lender, lendingRequest.getID());
				return lendingRequest.getID();
			}
		}
		throw new Exception("Solicitado não possue o item ou item não está disponível");
	}

	private void requestItem(Lending requestLending) {
		receivedItemRequests.add(requestLending);
	}

	public String approveLending(String requestId) throws Exception{
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
	
	protected String lendItem(Item item, User borrower, int days) throws Exception{

		Lending requestAccepted = null;
		for ( Lending record : receivedItemRequests ){
			if ( record.getItem().equals(item) ){
				requestAccepted = record;
				record.setLendingDate();
				record.getLendingDate().addDays(record.getRequiredDays());
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
	
	public String denyLending(String requestId) throws Exception{
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
	
	protected String declineLendingItem(Item item, User borrower, int days) throws Exception{
		
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
	
	public boolean hasBorrowedThis(Item item) {
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
	
	protected String returnItem(Item item) throws Exception{
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
	
	protected void receiveLentItem(Item item) throws Exception{

		Lending requestAttended = null;
		for ( Lending record : myLentItems ){
			if ( record.getItem().equals(item) && record.isReturned() ) {
				requestAttended = record;
				if ( myInterestingItems.containsKey(item) ) {
					Set<User> toBeWarned = new HashSet<User>();
					for ( User interested : myInterestingItems.get(item) ) {
						interested.sendMessage(
								"O item " + item.getName() + " do usuário "
								+ this.getName() + " está disponível",
								"Corra pra pedir emprestado, pois "
								+ (myInterestingItems.get(item).size()-1)
								+ " pessoas além de você pediram por ele!", interested);
						toBeWarned.add(interested);
					}
					myInterestingItems.get(item).removeAll(toBeWarned);
				}
			}
		}
		if ( requestAttended != null ){
			myLentItems.remove(requestAttended);
			if ( !(requestAttended.getStatus() == LendingStatus.CANCELLED) ){
				requestAttended.setStatus(LendingStatus.FINISHED);
			}
			lentRegistryHistory.add(requestAttended);
			myItems.put(item, this);
			requestAttended.getBorrower().removeBorrowedItem(item);
		}
		else{
			throw new Exception("Solicitado quer receber de volta item que não é dele");
		}
	}

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

	protected void denyReceivingLentItem(Item item) throws Exception{
		for(Lending record : myLentItems){
			if(record.getItem().equals(item)){
				if ( !record.isReturned() ){
					throw new Exception("Devolução do item já foi negada");//"Item returning already denied);
				}
				record.getBorrower().setNotReturned(item);
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
	
	private void removeBorrowedItem(Item item) throws Exception{
		Lending requestAttended = null;
		for(Lending record : myBorrowedItems){
			if(record.getItem().equals(item)){
				requestAttended = record;
			}
		}
		if ( requestAttended != null ){
			myBorrowedItems.remove(requestAttended);
			if ( !(requestAttended.getStatus() == LendingStatus.CANCELLED) ){
				requestAttended.setStatus(LendingStatus.FINISHED);
			}
			borrowedRegistryHistory.add(requestAttended);
		}
		else{
			throw new Exception("Solicitante quer devolver item que ele não pegou emprestado");
		}
	}
	
	public String sendMessage(String subject, String message, User receiver) 
			throws Exception {
		
		storeMessage(subject, message, this.getLogin(), receiver.getLogin(),
				true, "");
		
		return receiver.storeMessage(subject, message, this.getLogin(),
				receiver.getLogin(), true, "");
	}
	
	public String sendMessage(String subject, String message, User receiver,
			String lendingId) throws Exception {
		
		if ( lendingId == null || lendingId.trim().isEmpty()) {
			throw new Exception("Identificador da requisição de empréstimo é inválido");// "Invalid lending identifier"); 
		}
		
		if (getLendingByLendingId(lendingId) == null) {
			throw new Exception("Requisição de empréstimo inexistente");
				// "Inexistent lending");
		}
		
		storeMessage(subject, message, this.getLogin(), receiver.getLogin(),
				false, lendingId);
		
		//TODO Treat how is it going to deal with the Lending id
		return receiver.storeMessage(subject, message, this.getLogin(),
				receiver.getLogin(), false, lendingId);
	}

	private String storeMessage(String subject, String message, String senderLogin,
			String receiverLogin, boolean isOffTopic, String lendingId) {
		
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
			boolean isOffTopic,	String lendingId) {
		
		Topic foundTopic = getTopicBySubject(topicSet, subject); 
		
		if ( foundTopic != null) {
			foundTopic.addMessage(subject, message, senderLogin, receiverLogin, 
					isOffTopic, lendingId);
		}
		else {
			Topic newTopic = new Topic(subject);
			newTopic.addMessage(subject, message, senderLogin, receiverLogin, 
					isOffTopic, lendingId);
			topicSet.add(newTopic);
			foundTopic = newTopic;
		}		
		return foundTopic.getID();
	}
	
	public List<Message> getMessagesByTopicId(String topicId) throws Exception {
		Topic foundTopic = getTopicById(negotiationTopics, topicId); 
		
		if ( foundTopic == null) {
			foundTopic = getTopicById(offTopicTopics, topicId);
			
			if ( foundTopic == null) {
				return null;
			}
		}
		Message[] msgArray = foundTopic.getMessages().toArray(
				new Message[foundTopic.getMessages().size()]);
		Arrays.sort(msgArray);
		return Arrays.asList(msgArray);
		
	}
		
	private Topic getTopicById(Set<Topic> topicSet, String topicId) throws Exception{
		if ( topicId == null || topicId.trim().isEmpty() ){
			throw new Exception("Identificador do tópico é inválido");
		}
		for (Topic topic : topicSet) {
			if (topic.getID().equals(topicId)) {
				return topic;
			}
		}
		
		return null;
	}

	public Set<Message> getMessagesByTopicSubject(String topicSubject) throws Exception {
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
		
		throw new Exception("Tópico inexistente");
		//"Could not find any topic with the given subject.");
	}
	
	private Topic getTopicBySubject(Set<Topic> topicSet, String subject) {
		for (Topic topic : topicSet) {
			if (topic.getSubject().equals(subject)) {
				return topic;
			}
		}
		return null;
	}

	public String askForReturnOfItem(String lendingId, Date systemDate) throws Exception{
		if ( lendingId == null || lendingId.trim().isEmpty() ){
			throw new Exception("Identificador do empréstimo é inválido");//"Lending request identifier invalid");
		}
		for ( Lending record : lentRegistryHistory ){
			if ( record.getID().equals(lendingId) ){
				throw new Exception("Item já devolvido");//"Item already returned");				
			}
		}
		for ( Lending record : sentItemDevolutionRequests  ){
			if ( record.getID().equals(lendingId) ){
				if ( !record.isReturned() ){
					throw new Exception("Devolução já requisitada");//"Devolution of item already requested");
				}
				else{
					throw new Exception("Item já devolvido");//"Item already returned");					
				}
			}
		}
		for ( Lending record : myLentItems ){
			if ( record.getID().equals(lendingId) ){
				if ( !record.isReturned() ){
					return requestBack(record.getItem(), systemDate);
				}
				else{
					throw new Exception("Item já devolvido");//"Item already returned");
				}
			}
		}
		throw new Exception("Empréstimo inexistente");//"Inexistent item request");
	}
	
	protected String requestBack(Item item, Date systemDate) throws Exception{
		for ( Lending record : myLentItems ){
			if(record.getItem().equals(item)){
				record.getBorrower().setRequestedBack(item, systemDate);
				if(systemDate.before(record.getLendingDate().getDate())){
					record.setCancelled();
				}
				record.setRequestedBack(true);
				sentItemDevolutionRequests.add(record);
				sendMessage("Empréstimo do item " + item.getName() + " a " +
						record.getBorrower().getName(), this.getName() + " solicitou a devolução do item " +
						item.getName(), record.getBorrower(), record.getID());
				return record.getID();
			}
		}
		throw new Exception("Empréstimo inexistente");//"Inexistent item request");
	}

	private void setRequestedBack(Item item, Date systemDate) throws Exception{
		for(Lending record : myBorrowedItems){
			if(record.getItem().equals(item)){
				record.setRequestedBack(true);
				if(systemDate.before(record.getLendingDate().getDate())){
					record.setCancelled();
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
		
		if (topicType == null || topicType.trim().isEmpty()) {
			throw new Exception("Tipo inválido");//"Invalid type");
		}
		
		if (topicType.equals("negociacao")) {
			topicType = EntitiesConstants.NEGOTIATION_TOPIC;
		} else if (topicType.equals("offtopic")) {
			topicType = EntitiesConstants.OFF_TOPIC;
		} else if (topicType.equals("todos")){
			topicType = EntitiesConstants.ALL_TOPICS;
		} else {
			throw new Exception("Tipo inexistente");//"Inexistent type");
		}
		
		if (topicType.equals(EntitiesConstants.OFF_TOPIC)) {
			
			List<Topic> offTopicsList = new ArrayList<Topic>(offTopicTopics);
			Collections.sort(offTopicsList);
			return offTopicsList;
		}
		
		else if (topicType.equals(EntitiesConstants.NEGOTIATION_TOPIC)) {
			
			List<Topic> negotiationTopicsList = new ArrayList<Topic>(negotiationTopics);
			Collections.sort(negotiationTopicsList);
			return negotiationTopicsList;
		}
		else {

			List<Topic> allTopicsList = new ArrayList<Topic>(offTopicTopics);
			allTopicsList.addAll(negotiationTopics);
			Collections.sort(allTopicsList);
			return allTopicsList;
		}
		
	}

	public void registerInterestForItem(Item item, User owner) throws Exception{

		if ( myFriends.contains(owner) ){
			if ( owner.hasItem(item) ) {
				if ( owner.hasLentThis(item) ) {
					if ( !(owner.isInterestedOnMyItem(item, this)) ){
						owner.markAsInterested(item, this);
						return;
					}
					else{
						throw new Exception("O usuário já registrou interesse neste item");
					}
				}
			}
		}
		throw new Exception("Item inexistente");
	}

	private void markAsInterested(Item item, User interested) {
		if(myInterestingItems.containsKey(item)){
			myInterestingItems.get(item).add(interested);
		}else{
			myInterestingItems.put(item, new ArrayList<User>());
			myInterestingItems.get(item).add(interested);
		}
	}

	public boolean isInterestedOnMyItem(Item item, User interested) throws Exception{
		if ( item == null ){
			throw new Exception("Item inválido");
		}
		if ( interested == null ){
			throw new Exception("Usuário inválido");
		}
		if ( myInterestingItems.containsKey(item) ){
			if ( myInterestingItems.get(item) != null ){
				return this.myInterestingItems.get(item).contains(interested);
			}
			else{
				return false;
			}
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

	public boolean isMyItemRequested(Item item){
		
		for(Lending record: receivedItemRequests){
			if(record.getItem().equals(item))
				return true;
		}
		return false;
	}
		
	public void breakFriendship(User user){
		this.forceRemoveFriend(user);
		user.forceRemoveFriend(this);
	}

	private void forceRemoveFriend(User user){
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

	public void deleteMyItem(String itemId) throws Exception{
		
		Item toBeRemoved = null;
		
		if(itemId == null || itemId.trim().isEmpty()){
			throw new Exception("Identificador do item é inválido");
		}
		
		for(User actualFriend : this.myFriends){
			for(Item itemMyFriend : actualFriend.getAllItems()){
				if(itemMyFriend.getID().equals(itemId))
					throw new Exception("O usuário não tem permissão para apagar este item");
			}
		}
		
		for(Item actualItem : myItems.keySet())
			if(actualItem.getID().equals(itemId))
				toBeRemoved = actualItem;
		
		if(toBeRemoved == null){
			throw new Exception("Item inexistente");
		}
		
		for(Lending actualLending : this.myLentItems){
			if(actualLending.getItem().equals(toBeRemoved)){
				throw new Exception("O usuário não pode apagar este item enquanto estiver emprestado");
			}
		}
		
		for(Lending actualLending : receivedItemRequests){
			if(actualLending.getItem().equals(toBeRemoved)){
				receivedItemRequests.remove(actualLending);
			}
		}
		myItems.remove(toBeRemoved);
	}

	public int getReputation() {
		return myLentItems.size() + lentRegistryHistory.size();
	}
	
}