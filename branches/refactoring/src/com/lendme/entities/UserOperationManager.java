package com.lendme.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lendme.entities.Lending.LendingStatus;


public class UserOperationManager {
	
	private FriendshipManager friendshipManager;
	private CommunicationManager communicationManager;
	private ItemManager itemManager;
	private int score;
	private User me;
	
	public UserOperationManager(User user) {
		this.me = user;
		this.score = 0;
		this.friendshipManager = new FriendshipManager(user);
		this.communicationManager = new CommunicationManager(user);
		this.itemManager = new ItemManager();
	}
	
	public FriendshipManager getFriendshipManager() {
		return friendshipManager;
	}
	
	public ItemManager getItemManager() {
		return itemManager;
	}
	
	/**
	 * Returns user lending score.
	 * @return
	 */
	public int getScore() {
		return score;
		//TODO remove this field and use lentRecord size
	}
	
	/**
	 * User gains one lending score point.
	 */
	private void point(){
		this.score++;
		//TODO remove this field and use lentRecord size
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
		communicationManager.publishItemRegisteredActivity(itemName);
		return itemManager.addItem(itemName, description, category, me);
	}

	/**
	 * User requests friendship with another user.
	 * @param otherUser
	 * @throws Exception
	 */
	public void requestFriendship(User otherUser) throws Exception{
		friendshipManager.sendFriendshipRequest(otherUser);
	}


	/**
	 * User accepts friendship request from another user.
	 * @param otherUser
	 * @throws Exception
	 */
	public void acceptFriendshipRequest(User otherUser) throws Exception{
		friendshipManager.acceptFriendshipRequest(otherUser);
		communicationManager.publishFriendshipAcceptedActivity(otherUser.getName());
		otherUser.getCommunicationManager().publishFriendshipAcceptedActivity(me.getName());
	}

	
	public CommunicationManager getCommunicationManager() {
		return communicationManager;
	}

	/**
	 * User declines friendship request from another user.
	 * @param otherUser
	 */
	public void declineFriendshipRequest(User otherUser) throws Exception{
		friendshipManager.declineFriendshipRequest(otherUser);
	}
		
	/**
	 * Returns true if user has another user as friend.
	 * @param otherUser
	 * @return
	 */
	public boolean hasFriend(User otherUser) throws Exception{
		return friendshipManager.hasFriend(otherUser);
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
		if ( days <=0 ){
			throw new Exception("Duracao inválida");//"Requested day amount is invalid");
		}
		
		itemManager.verifyLendingAutenticity(item);
		
		if (friendshipManager.hasFriend(lender) && lender.getUserOperationManager().getItemManager().hasItem(item)) {
			if (! lender.getUserOperationManager().getItemManager().hasLentThis(item)) {
				String lendingId = itemManager.borrowItem(item, me, lender, days);
				communicationManager.sendMessage("Empréstimo do item " + item.getName() + " a " +
						me.getName(), me.getName() + " solicitou o empréstimo do item " + item.getName(),
						lender, lendingId);
				return lendingId;
			}
		}
		throw new Exception("Solicitado não possui o item ou item não está disponível");
	}

	/**
	 * User approves lending of one of his avaliable items.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public String approveLending(String requestId) throws Exception{
		if ( requestId == null || requestId.trim().isEmpty() ){
			throw new Exception("Identificador da requisição de empréstimo é inválido");//"Lending request identifier invalid");
		}
		
		Lending currentLending = itemManager.getMyReceivedItemRequestById(requestId);
		if (currentLending != null) {
			communicationManager.publishOngoingLendingActivity(currentLending.getItem().getName(),
					currentLending.getBorrower().getName());
			
			return itemManager.lendItem(currentLending.getItem(), currentLending.getBorrower(),
					currentLending.getRequiredDays());
		}
		if ( itemManager.hasAlreadyApprovedLending(requestId)){
				throw new Exception("Empréstimo já aprovado");//Lending request already approved
			}
		throw new Exception("Requisição de empréstimo inexistente");//"Inexistent item request");
		
	}
	
	/**
	 * User refuses lending one of his available items.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public String denyLending(String requestId) throws Exception{
		return itemManager.denyLending(requestId);
	}
	
	

	/**
	 * User gives item back.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public String approveItemReturning(String lendingId) throws Exception{
		return itemManager.approveItemReturning(lendingId);
	}
	
	/**
	 * User approves item receipt.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public String confirmLendingTermination(String lendingId) throws Exception{
		if ( lendingId == null || lendingId.trim().isEmpty() ){
			throw new Exception("Identificador do empréstimo é inválido");//"Lending identifier is invalid");
		}
		for ( Lending record : itemManager.getMyLentItems() ){
			if ( record.getID().equals(lendingId) ){
				receiveLentItem(record.getItem());
				point();
				for ( Lending recordx: itemManager.getLentRegistryHistory()){
					if ( recordx.getID().equals(lendingId) ){
						communicationManager.publishLendingFinishApprovalActivity(record.getItem().getName());						
						return record.getID();
					}
				}
				throw new Exception("Lending not recorded.");
			}
		}
		for ( Lending record : itemManager.getLentRegistryHistory() ){
			if ( record.getID().equals(lendingId) ){
				throw new Exception("Término do empréstimo já confirmado");
			}
		}
		throw new Exception("Empréstimo inexistente");
	}
	
	protected void receiveLentItem(Item item) throws Exception{

		Lending requestAttended = null;
		for ( Lending record : itemManager.getMyLentItems() ){
			if ( record.getItem().equals(item) && record.isReturned() ) {
				requestAttended = record;
				Map<Item, ArrayList<InterestedOn<Item>>> interestedOnMyItems 
					= itemManager.getInterestedOnMyItems();
				if ( interestedOnMyItems.containsKey(item) ) {
					Set<InterestedOn<Item>> toBeWarned = new HashSet<InterestedOn<Item>>();
					for ( InterestedOn<Item> interested : interestedOnMyItems.get(item) ) {
						warnInterestedThatTargetIsAvailable(item, interested);
						toBeWarned.add(interested);
					}
					interestedOnMyItems.get(item).removeAll(toBeWarned);
				}
			}
		}
		if ( requestAttended != null ){
			itemManager.getMyLentItems().remove(requestAttended);
			if ( !(requestAttended.getStatus() == LendingStatus.CANCELLED) ){
				requestAttended.setStatus(LendingStatus.FINISHED);
			}
			itemManager.getLentRegistryHistory().add(requestAttended);
			itemManager.getMyItems().put(item, me);
			requestAttended.getBorrower().getUserOperationManager().removeBorrowedItem(item);
		}
		else{
			throw new Exception("Solicitado quer receber de volta item que não é dele");
		}
	}

	/**
	 * User denies item receipt.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public String denyLendingTermination(String lendingId) throws Exception{
		return itemManager.denyLendingTermination(lendingId);
	}
	
	public void removeBorrowedItem(Item item) throws Exception{
		Lending requestAttended = null;
		for(Lending record : itemManager.getMyBorrowedItems()){
			if(record.getItem().equals(item)){
				requestAttended = record;
			}
		}
		if ( requestAttended != null ){
			itemManager.getMyBorrowedItems().remove(requestAttended);
			if ( !(requestAttended.getStatus() == LendingStatus.CANCELLED) ){
				requestAttended.setStatus(LendingStatus.FINISHED);
			}
			itemManager.getBorrowedRegistryHistory().add(requestAttended);
		}
		else{
			throw new Exception("Solicitante quer devolver item que ele não pegou emprestado");
		}
	}
	
	/**
	 * Permite o envio de uma menssagem entre dois usuários.
	 * @param subject assunto da menssagem.
	 * @param message Corpo da menssagem.
	 * @param receiver Usuário qu ereceberá a menssagem.
	 * @return retorna o ID do tópico criado para essa menssagem.
	 * @throws Exception
	 */
	public String sendMessage(String subject, String message, User receiver) 
			throws Exception {
		return communicationManager.sendMessage(subject, message, receiver);
	}
	
	/**
	 * Permite o envio de uma menssagem entre dois usuários.
	 * 
	 * @param subject Assunto da menssagem.
	 * @param message Corpo da menssagem.
	 * @param receiver Usuário que receberá a menssagem.
	 * @param lendingId ID da requisição de um pedido de um Item.
	 * @return retorna o ID do tópico criado para essa menssagem.
	 */
	public String sendMessage(String subject, String message, User receiver,
			String lendingId) throws Exception {
		return communicationManager.sendMessage(subject, message, receiver, lendingId);
	}

	/**
	 * User asks for his item back.
	 * @param lendingId
	 * @param systemDate
	 * @return
	 * @throws Exception
	 */
	public String askForReturnOfItem(String lendingId, Date systemDate) throws Exception{
		if ( systemDate == null ){
			throw new Exception("Data inválida");
		}
		if ( lendingId == null || lendingId.trim().isEmpty() ){
			throw new Exception("Identificador do empréstimo é inválido");//"Lending request identifier invalid");
		}
		for ( Lending record : itemManager.getLentRegistryHistory()){
			if ( record.getID().equals(lendingId) ){
				throw new Exception("Item já devolvido");//"Item already returned");				
			}
		}
		for ( Lending record : itemManager.getSentItemDevolutionRequests()  ){
			if ( record.getID().equals(lendingId) ){
				if ( !record.isReturned() ){
					throw new Exception("Devolução já requisitada");//"Devolution of item already requested");
				}
				else{
					throw new Exception("Item já devolvido");//"Item already returned");					
				}
			}
		}
		for ( Lending record : itemManager.getMyLentItems() ){
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
		for ( Lending record : itemManager.getMyLentItems() ){
			if(record.getItem().equals(item)){
				record.getBorrower().getUserOperationManager().setRequestedBack(item, systemDate);
				if(systemDate.before(record.getLendingDate().getDate())){
					record.setCancelled();
				}
				record.setRequestedBack(true);
				itemManager.getSentItemDevolutionRequests().add(record);
				communicationManager.sendMessage("Empréstimo do item " + item.getName() + " a " +
						record.getBorrower().getName(), me.getName() + " solicitou a devolução do item " +
						item.getName(), record.getBorrower(), record.getID());
				return record.getID();
			}
		}
		throw new Exception("Empréstimo inexistente");//"Inexistent item request");
	}

	private void setRequestedBack(Item item, Date systemDate) throws Exception{
		for(Lending record : itemManager.getMyBorrowedItems()){
			if(record.getItem().equals(item)){
				record.setRequestedBack(true);
				if(systemDate.before(record.getLendingDate().getDate())){
					record.setCancelled();
				}
			}
		}
	}
	
	/**
	 * User registers interest for item if he is friend of the owner.
	 * @param item
	 * @param owner
	 * @throws Exception
	 */
	public void registerInterestForItem(Item item, User owner) throws Exception{

		if ( friendshipManager.hasFriend(owner) ){
			if ( owner.getUserOperationManager().getItemManager().hasItem(item) ) {
				if ( owner.getUserOperationManager().getItemManager().hasLentThis(item) ) {
					if ( !(owner.isInterestedOnMyItem(item, me)) ){
						communicationManager.publishInterestOnItemActivity(item.getName(), owner.getName());
						owner.getUserOperationManager().markAsInterested(item, me);
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

	private void markAsInterested(Item item, InterestedOn<Item> interested) {
		Map<Item, ArrayList<InterestedOn<Item>>> interestedOnMyItems = 
				itemManager.getInterestedOnMyItems(); 
		if(interestedOnMyItems.containsKey(item)){
			interestedOnMyItems.get(item).add(interested);
		}else{
			interestedOnMyItems.put(item, new ArrayList<InterestedOn<Item>>());
			interestedOnMyItems.get(item).add(interested);
		}
	}

	/**
	 * Returns true if another user is interested on item whose owner is this user.
	 * @param item
	 * @param interested
	 * @return
	 * @throws Exception
	 */
	public boolean isInterestedOnMyItem(Item item, User interested) throws Exception{
		return itemManager.isInterestedOnMyItem(item, interested);
	}
	
	/**
	 * Returns all items whose owner is this user.
	 * @return
	 */
	public Set<Item> getAllItems() {
		Map<Item, User> toBeReturned = new HashMap<Item, User>();
		toBeReturned.putAll(itemManager.getMyItems());
		return toBeReturned.keySet();
	}

	/**
	 * Returns this user lent items.
	 * @return
	 */
	public Set<Item> getLentItems() {
		Set<Item> result = new HashSet<Item>();
		for ( Lending record : itemManager.getMyLentItems() ){
			result.add(record.getItem());
		}
		return result;
	}

	/**
	 * Returns items borrowed by this user.
	 * @return
	 */
	public Set<Item> getBorrowedItems() {
		Set<Item> result = new HashSet<Item>();
		for ( Lending record : itemManager.getMyBorrowedItems()){
			result.add(record.getItem());
		}
		return result;
	}
	
	/**
	 * Returns user friends.
	 * @return
	 */
	public Set<User> getFriends() {
		Set<User> toBeReturned = new HashSet<User>();
		toBeReturned.addAll(friendshipManager.getFriends());
		return toBeReturned;
	}
		
	/**
	 * User breaks friendship with another user.
	 * @param user
	 */
	public void breakFriendship(User user) throws Exception{
		friendshipManager.breakFriendship(user);
	}
	
	/**
	 * Returns a lending record by its id.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public Lending getLendingByRequestId(String requestId) throws Exception{
		return itemManager.getLendingByRequestId(requestId);
	}

	/**
	 * Returns a lending record by its id.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public Lending getLendingByLendingId(String lendingId) throws Exception{
		return itemManager.getLendingByLendingId(lendingId);
	}	

	public Set<User> getReceivedFriendshipRequests() {
		return friendshipManager.getReceivedFriendshipRequests();
	}
	
	/**
	 * Deletes a item this user is owner.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public void deleteMyItem(String itemId) throws Exception{
		
		Item toBeRemoved = null;
		
		if(itemId == null || itemId.trim().isEmpty()){
			throw new Exception("Identificador do item é inválido");
		}
		
		for(User actualFriend : friendshipManager.getFriends()){
			for(Item itemMyFriend : actualFriend.getAllItems()){
				if(itemMyFriend.getID().equals(itemId))
					throw new Exception("O usuário não tem permissão para apagar este item");
			}
		}
		
		for(Item actualItem : itemManager.getMyItems().keySet())
			if(actualItem.getID().equals(itemId))
				toBeRemoved = actualItem;
		
		if(toBeRemoved == null){
			throw new Exception("Item inexistente");
		}
		
		for(Lending actualLending : itemManager.getMyLentItems()){
			if(actualLending.getItem().equals(toBeRemoved)){
				throw new Exception("O usuário não pode apagar este item enquanto estiver emprestado");
			}
		}
		
		for(Lending actualLending : itemManager.getReceivedItemRequests()){
			if(actualLending.getItem().equals(toBeRemoved)){
				itemManager.getReceivedItemRequests().remove(actualLending);
			}
		}
		itemManager.getMyItems().remove(toBeRemoved);
	}

	/**
	 * Retorna a reputacao do usuario administrado por esse gereciador.
	 * @return - reputacao.
	 */
	public int getReputation() {
		return itemManager.getMyLentItems().size() + itemManager.getLentRegistryHistory().size();
	}

	/**
	 * @return
	 */
	public Set<Lending> getReceivedItemRequests() {
		return itemManager.getReceivedItemRequests();
	}
	
	public String publishItemRequest(String itemName, String itemDescription) throws Exception{
		
		for ( Item item : itemManager.getMyItems().keySet() ){
			if ( item.getName().equals(itemName) && item.getDescription().equals(itemDescription) ){
				throw new Exception("Não se pode publicar pedido de seu próprio item");
			}
		}
		Lending itemRequest = new Lending(me, itemName, itemDescription);
		communicationManager.publishItemRequestActivity(itemName);
		itemManager.getPublishedItemRequests().add(itemRequest);
		return itemRequest.getID();
		
	}
	
	public Set<Lending> getPublishedItemRequests(){
		return itemManager.getPublishedItemRequests();
	}

	public void offerItem(Lending publishedRequest, Item item) throws Exception{
		if ( itemManager.hasLentThis(item) ){
			throw new Exception("Não se pode oferecer um item que já está emprestado");
		}

		User requester = publishedRequest.getBorrower();
		
		sendMessage(
				String.format(EntitiesConstants.REQUESTED_ITEM_LENT_ACTIVITY, me.getName(), item.getName()),
				"Item oferecido: " + item.getName() + " - " + item.getDescription(), requester);		
	}
	
	public void republishItemRequest(Lending petition) throws Exception{
		for ( Item item : itemManager.getMyItems().keySet() ){
			if ( item.getName().equals(petition.getDesiredItemName())
					&& item.getDescription().equals(petition.getDesiredItemDescription()) ){
				throw new Exception("Não se pode publicar pedido de seu próprio item");
			}
		}
		communicationManager.republishItemRequestActivity(petition.getBorrower().getName(), petition.getDesiredItemName());
	}

	public void warnInterestedThatTargetIsAvailable(Item target,
			InterestedOn<Item> interested) throws Exception{

		Item item = target;
		communicationManager.sendMessage(
				"O item " + item.getName() + " do usuário "	+ me.getName() + " está disponível",
				"Corra pra pedir emprestado, pois "	+ (itemManager.getInterestedOnMyItems().get(item).size()-1)
				+ " pessoas além de você pediram por ele!", (User)interested);
	}
	
	public void publishItemRegisteredActivity(String itemName) {
		communicationManager.publishItemRegisteredActivity(itemName);
	}

	public List<ActivityRegistry> getMyActivityHistory() {
		return communicationManager.getMyActivityHistory();
	}

	public List<Message> getMessagesByTopicId(String topicId) throws Exception{
		return communicationManager.getMessagesByTopicId(topicId);
	}

	public List<Topic> getTopics(String topicType) throws Exception{
		return communicationManager.getTopics(topicType);
	}

	public Set<Lending> getSentItemRequests() {
		return itemManager.getSentItemRequests();
	}
}
