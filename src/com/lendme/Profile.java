package com.lendme;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.lendme.entities.ActivityRegistry;
import com.lendme.entities.Item;
import com.lendme.entities.Lending;
import com.lendme.entities.Message;
import com.lendme.entities.Session;
import com.lendme.entities.Topic;
import com.lendme.entities.User;

/**
 * @author THE LENDERS
 * The permissions management is delegated from LendMe to this class, which is the one who deals with
 * concepts of logged users (observer) and target users (profile owners).
 */

public class Profile {

	private Session observerSession;
	private User owner;
	private Set<User> ownerFriends;
	private Set<Item> ownerItems;
	
	private Profile(Session observer, User user) throws Exception{

		if ( observer == null ){
			throw new Exception("Sessao do usuário observador inválida");//"Invalid observer user");
		}
		if ( observer.getOwner() == null ){
			throw new Exception("Usuário observador inválido");//"Invalid observer user login");
		}
		if ( user == null ){
			throw new Exception("Usuário observado inválido");//"Invalid profile owner user");
		}
		if ( user.getFriends() == null ){
			throw new Exception("Amigos do observado inválidos");//"Invalid profile owner user friends");
		}
		if ( user.getAllItems() == null ){
			throw new Exception("Itens do observado inválidos");//"Invalid profile owner user items");
		}
		if ( user.getName() == null || user.getName().trim().isEmpty() ){
			throw new Exception("Nome do observado inválido");//"Invalid profile owner user name");
		}
		if ( user.getLogin() == null || user.getLogin().trim().isEmpty() ){
			throw new Exception("Login do observado inválido");//"Invalid profile owner user login");
		}
		if ( user.getAddress() == null || user.getAddress().getFullAddress() == null 
				|| user.getAddress().getFullAddress().trim().isEmpty() ){
			throw new Exception("Endereço do observado inválido");//"Invalid profile owner user address");
		}
		
		observerSession = observer;
		owner = user;
		ownerFriends = owner.getFriends();
		if ( observerSession.getOwner().equals(owner) ){
			ownerItems = owner.getAllItems();
		}
		else{
			for ( User friend : ownerFriends ){
				if ( friend.equals(observerSession.getOwner())){
					ownerItems = owner.getAllItems();
					return;
				}
			}
		}
	}
	
	/**
	 * Updates the profile information.
	 * @throws Exception
	 */
	protected void update() throws Exception{
		ownerFriends = owner.getFriends();
		if ( observerSession.getOwner().equals(owner) ){
			ownerItems = owner.getAllItems();
		}
		else{
			for ( User friend : ownerFriends ){
				if ( friend.equals(observerSession.getOwner())){
					ownerItems = owner.getAllItems();
					return;
				}
			}
		}
	}
	
	/**
	 * Returns a profile for the specified user.
	 * @param userSession
	 * @param user
	 * @return
	 * @throws Exception
	 */
	protected static Profile getUserProfile(Session userSession, User user) throws Exception{
		return new Profile(userSession, user);
	}

	/**
	 * Returns the profile of the observer user.
	 * @return
	 * @throws Exception
	 */
	protected Profile viewOwnProfile() throws Exception{
		return new Profile(observerSession, observerSession.getOwner());
	}
	
	/**
	 * Returns the profile of an user.
	 * @param other
	 * @return
	 * @throws Exception
	 */
	protected Profile viewOtherProfile(User other) throws Exception{
		if ( observerSession == null || other == null ){
			throw new Exception("Nao eh possivel visualizar este perfil");//"This profile is not accessible");
		}
		if ( observerSession.getOwner().equals(other) ){
			return viewOwnProfile();
		}
		return new Profile(observerSession, other);
	}

	/**
	 * Returns the profile owner's friends.
	 * @return
	 */
	protected Set<User> getOwnerFriends() {
		return ownerFriends;
	}

	/**
	 * Returns the profile owner's items.
	 * @return
	 * @throws Exception
	 */
	protected Set<Item> getOwnerItems() throws Exception{
		if ( ownerItems == null ){
			throw new Exception("O usuário não tem permissão para visualizar estes itens");//"User has no permission to view these items");
		}
		return ownerItems;
	}
	
	/**
	 * Return the profile owner name.
	 * @return
	 */
	protected String getOwnerName(){
		return owner.getName();
	}

	/**
	 * Returns the profile owner login.
	 * @return
	 */
	protected String getOwnerLogin(){
		return owner.getLogin();
	}
	
	/**
	 * Returns the profile owner address.
	 * @return
	 */
	protected String getOwnerAddress(){
		return owner.getAddress().getFullAddress();
	}
	
	/**
	 * Returns the profile owner friendship requests.
	 * @return
	 */
	protected Set<User> getOwnerFriendshipRequests(){
		return owner.getReceivedFriendshipRequests();
	}

	/**
	 * Returns the session of the user who is observing this profile.
	 * @return
	 */
	protected Session getObserver() {
		return observerSession;
	}

	/**
	 * Observer asks profile owner friendship
	 * @throws Exception
	 */
	protected void askForFriendship() throws Exception{
		if ( observerSession.getOwner().equals(owner) ){
			throw new Exception("Amizade inexistente");//inválida - pedido de amizade para si próprio");//"Invalid friendship");
		}
		observerSession.getOwner().requestFriendship(owner);
	}

	/**
	 * Observer accepts friendship with profile owner.
	 * @throws Exception
	 */
	protected void acceptFriendshipRequest() throws Exception{
		if ( observerSession.getOwner().equals(owner) ){
			throw new Exception("Amizade inexistente");//inválida - aceitação de amizade para si próprio");//"Invalid friendship");
		}
		observerSession.getOwner().acceptFriendshipRequest(owner);
	}

	/**
	 * Observer declines friendship with profile owner. 	 
	 * @throws Exception
	 */
	protected void declineFriendshipRequest() throws Exception{
		if ( observerSession.getOwner().equals(owner) ){
			throw new Exception("Amizade inexistente");//inválida - negação de amizade para si próprio");//"Invalid friendship");
		}
		observerSession.getOwner().declineFriendshipRequest(owner);
	}

	/**
	 * Observer breaks friendship with profile owner.
	 * @throws Exception
	 */
	protected void breakFriendship() throws Exception{
		if ( observerSession.getOwner().equals(owner) ){
			throw new Exception("Amizade inexistente");//inválida - rompimento de amizade com si próprio");//"Invalid friendship");
		}
		User me = observerSession.getOwner();
		if ( !me.hasFriend(owner) ){
			throw new Exception("Amizade inexistente");//inválida - rompimento de amizade com alguem que não é seu amigo");
		}
		
		me.breakFriendship(owner);
	}
	
	/**
	 * Returns true if observer is friend of profile owner.
	 * @return
	 * @throws Exception
	 */
	protected boolean isFriendOfOwner() throws Exception{
		if ( observerSession.getOwner().equals(owner) ){
			throw new Exception("Amizade inválida - consulta para amizade com si próprio");//"Invalid friendship");
		}
		return observerSession.getOwner().hasFriend(owner);
	}

	/**
	 * Returns true if key matches with profile owner name.
	 * @param key
	 * @return
	 * @throws Exception
	 */
	protected boolean searchByName(String key) throws Exception{
		if ( owner.getLogin().equals(observerSession.getOwner().getLogin()) ){
			return false;
		}
		return owner.getName().contains(key);
	}

	/**
	 * Returns true if key matches with profile owner login.
	 * @param key
	 * @return
	 * @throws Exception
	 */
	protected boolean searchByLogin(String key) throws Exception{
		if ( owner.getLogin().equals(observerSession.getOwner().getLogin()) ){
			return false;
		}
		return owner.getLogin().contains(key);
	}
	
	/**
	 * Returns true if key matches with profile owner address.
	 * @param key
	 * @return
	 * @throws Exception
	 */
	protected boolean searchByAddress(String key) throws Exception{
		if ( owner.getLogin().equals(observerSession.getOwner().getLogin()) ){
			return false;
		}
		return owner.getAddress().getFullAddress().contains(key);
	}

	/**
	 * Returns profile owner lending records.
	 * @param kind
	 * @return
	 * @throws Exception
	 */
	protected Collection<Lending> getLendingRecords(String kind) throws Exception{
		if ( kind == null || kind.trim().isEmpty() ){
			throw new Exception("Tipo inválido");//"Invalid kind of lending");
		}
		if ( kind.equals("emprestador") ){
			List<Lending> result = new ArrayList<Lending>();
			result.addAll(owner.getLentRegistryHistory());
			result.addAll(owner.getMyLentItemsRecord());
			return result;
		}
		else if ( kind.equals("beneficiado") ){
			List<Lending> result = new ArrayList<Lending>();
			result.addAll(owner.getBorrowedRegistryHistory());
			result.addAll(owner.getMyBorrowedItemsRecord());
			return result;
		}
		else if ( kind.equals("todos") ){
			List<Lending> result = new ArrayList<Lending>();
			result.addAll(owner.getLentRegistryHistory());
			result.addAll(owner.getMyLentItemsRecord());
			result.addAll(owner.getBorrowedRegistryHistory());
			result.addAll(owner.getMyBorrowedItemsRecord());
			return result;
		}
		throw new Exception("Tipo inexistente");//"Inexistent kind of lending");
	}

	/**
	 * Observer requests item that belongs to profile owner, if he is allowed to access them.
	 * @param itemId
	 * @param requiredDays
	 * @return
	 * @throws Exception
	 */
	protected String requestItem(String itemId, int requiredDays) throws Exception{
		if ( itemId == null || itemId.trim().isEmpty() ){
			throw new Exception("Identificador do item é invalido");//"Invalid item identifier");
		}
		try{
			for ( Item item : getOwnerItems() ){
				if ( item.getID().equals(itemId) ){
					return observerSession.getOwner().borrowItem(item, owner, requiredDays);
				}
			}
		}
		catch (Exception e){
			if ( e.getMessage().equals("O usuário não tem permissão para visualizar estes itens") ){
				throw new Exception("O usuário não tem permissão para requisitar o empréstimo deste item");
			}
			else{
				throw e;
			}
		}
		throw new Exception("O usuário não possue este item");//"Item does not belong to this user");
	}

	/**
	 * Observer approves lending of item.
	 * @param itemId
	 * @param requiredDays
	 * @return
	 * @throws Exception
	 */
	protected String approveLending(String requestId) throws Exception{
		User me = observerSession.getOwner();
		if ( !LendMe.getLendingByRequestId(requestId).getLender().equals(me) ) {
			throw new Exception("O empréstimo só pode ser aprovado pelo dono do item");//Only the owner of the item is allowed to lend it
		}
		return me.approveLending(requestId);
	}

	/**
	 * Observer refuses lending item.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	protected String denyLoan(String requestId) throws Exception{
		User me = observerSession.getOwner();
		if ( !LendMe.getLendingByRequestId(requestId).getLender().equals(me) ) {
			throw new Exception("O empréstimo só pode ser negado pelo dono do item");//Only the owner of the item is allowed to lend it
		}
		return me.denyLending(requestId);
	}
	
	/**
	 * Observer returns borrowed item back.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	protected String returnItem(String lendingId) throws Exception{
		User me = observerSession.getOwner();
		if ( !LendMe.getLendingByLendingId(lendingId).getBorrower().equals(me) ) {
			throw new Exception("O item só pode ser devolvido pelo usuário beneficiado");//Only the owner of the item is allowed to lend it
		}
		return me.approveItemReturning(lendingId);
	}

	/**
	 * Observer confirms that he successfully received item back.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	protected String confirmLendingTermination(String lendingId) throws Exception{
		User me = observerSession.getOwner();
		if ( !LendMe.getLendingByLendingId(lendingId).getLender().equals(me) ) {
			throw new Exception("O término do empréstimo só pode ser confirmado pelo dono do item");//Only the owner of the item is allowed to confirm success in return process
		}
		return me.confirmLendingTermination(lendingId);
	}
	
	/**
	 * Observer denies that he successfully received item back.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	protected String denyLendingTermination(String lendingId) throws Exception{
		User me = observerSession.getOwner();
		if ( !LendMe.getLendingByLendingId(lendingId).getLender().equals(me) ) {
			throw new Exception("O término do empréstimo só pode ser negado pelo dono do item");//Only the owner of the item is allowed to confirm success in return process
		}
		return me.denyLendingTermination(lendingId);
	}
	
	/**
	 * Observer requests lent item back.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	protected String askForReturnOfItem(String lendingId, Date date) throws Exception{
		User me = observerSession.getOwner();
		if ( !LendMe.getLendingByLendingId(lendingId).getLender().equals(me) ) {
			throw new Exception("O usuário não tem permissão para requisitar a devolução deste item");//Only the owner of the item is allowed to ask for return of item
		}
		return me.askForReturnOfItem(lendingId, date);
	}

	/**
	 * Observer sends a message to profile owner.
	 * @param subject
	 * @param message
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	protected String sendMessage(String subject, String message, String lendingId) throws Exception{
		User me = observerSession.getOwner();
		if ( me.equals(owner) ){
			throw new Exception("Usuário não pode mandar mensagem para si mesmo");//"User cannot send messages to himself");
		}
		
		if (message == null || message.trim().isEmpty()) {
			throw new Exception("Mensagem inválida");//"Invalid message");
		}
		
		if (subject == null || subject.trim().isEmpty()) {
			throw new Exception("Assunto inválido");//"Invalid subject");
		}
		
		if (lendingId == null || lendingId.trim().isEmpty()) {
			throw new Exception("Identificador da requisição de empréstimo é" +
					" inválido");//"Invalid lending identifier");
		}
		
		Lending actualLending = LendMe.getLendingByLendingId(lendingId);
		
		if (! actualLending.getLender().equals(me) && ! actualLending.getBorrower().equals(me)) {
			throw new Exception("O usuário não participa deste empréstimo");
			//"Invalid lending identifier");
		}
		
		
		return me.sendMessage(subject, message, owner, lendingId);
	}
	
	/**
	 * Observer sends a message to profile owner.
	 * @param subject
	 * @param message
	 * @return
	 * @throws Exception
	 */
	protected String sendMessage(String subject, String message) throws Exception{
		User me = observerSession.getOwner();
		if ( me.equals(owner.getLogin()) ){
			throw new Exception("Usuário não pode mandar mensagem para si mesmo");//"User cannot send messages to himself");
		}
		
		if (subject == null || subject.trim().isEmpty()) {
			throw new Exception("Assunto inválido");//"Invalid subject");
		}
		
		if (message == null || message.trim().isEmpty()) {
			throw new Exception("Mensagem inválida");//"Invalid message");
		}
		
		return me.sendMessage(subject, message, owner);
	}

	/**
	 * Gets observer topics.
	 * @param topicType
	 * @return
	 * @throws Exception
	 */
	protected List<Topic> getTopics(String topicType) throws Exception{
		return observerSession.getOwner().getTopics(topicType);
	}

	/**
	 * Gets observer topic messages.
	 * @param topicId
	 * @return
	 * @throws Exception
	 */
	protected List<Message> getTopicMessages(String topicId) throws Exception{		
		if (topicId == null || topicId.trim().isEmpty()) {
			throw new Exception("Identificador do tópico é inválido");
			// "Invalid topic identifier");
		}
		List<Message> messages = LendMe.getMessagesByTopicId(topicId);
		Message sampleMessage = messages.iterator().next();

		User me = observerSession.getOwner();
		
		if ( !( sampleMessage.getSender().equals(me.getLogin()) 
				|| sampleMessage.getReceiver().equals(me.getLogin())) ) {
			throw new Exception("O usuário não tem permissão para ler as mensagens deste tópico");
		}
		return messages;
	}

	/**
	 * Observer registers interest for item that belongs to the profile owner.
	 * @param itemId
	 * @throws Exception
	 */
	protected void registerInterestForItem(String itemId) throws Exception{

		if ( itemId == null || itemId.trim().isEmpty() ){
			throw new Exception("Identificador do item é inválido");//"Invalid item identifier");
		}
		if ( observerSession.getOwner().getLogin().equals(getOwnerLogin()) ){
			throw new Exception("O usuário não pode registrar interesse no próprio item");
		}
		
		try{
			for ( Item item : getOwnerItems() ){
				if ( item.getID().equals(itemId) ){
					observerSession.getOwner().registerInterestForItem(item, owner);
					return;
				}
			}
		}
		catch (Exception e){
			if ( e.getMessage().equals("O usuário não tem permissão para visualizar estes itens") ){
				throw new Exception("O usuário não tem permissão para registrar interesse neste item");
			}
			else{
				throw e;
			}
		}
		throw new Exception("Item inexistente");
	}

	/**
	 * Returns item attribute value.
	 * @param itemId
	 * @param attribute
	 * @return
	 * @throws Exception
	 */
	protected String getItemAttribute(String itemId, String attribute) throws Exception{
		if ( attribute == null || attribute.trim().isEmpty() ){
			throw new Exception("Atributo inválido");//"Invalid attribute");
		}
		if (!(attribute.equals("nome") || attribute.equals("descricao") || attribute.equals("categoria"))){
			throw new Exception("Atributo inexistente");//"Inexistent attribute");
		}
		
		for ( Item item : getOwnerItems() ){
			if ( item.getID().equals(itemId) ){
				if ( attribute.equals("nome") ) {
					return item.getName();
				}
				else if ( attribute.equals("descricao") ) {
					return item.getDescription();
				}
				else {
					String formattedCategory = item.getCategory().toString();
					return formattedCategory.substring(0, 1).toUpperCase() 
							+ formattedCategory.substring(1).toLowerCase();
				}
			}
		}
		throw new Exception("Item inexistente");
	}
	
	/**
	 * Returns profile owner received item requests.
	 * @return
	 * @throws Exception
	 */
	protected Set<Lending> getReceivedItemRequests() throws Exception {
		return owner.getReceivedItemRequests();
	}
	
	@Override
	public String toString() {
		StringBuilder profileSB = new StringBuilder();
		profileSB.append("\n \tUsuário:\n");
		profileSB.append("\n \t" + owner.toString() + "\n");
		
		profileSB.append("\n \tAmigos:\n");
		for (User friend: ownerFriends) {
			profileSB.append("\n \t" + friend.toString() + "\n\n");
		}
		profileSB.append("\n \tÍtens:\n");
		if (ownerItems != null ) {
			for (Item item: ownerItems) {
				profileSB.append("\n \t" + item.toString() + "\n\n");
			}
		} else {
			profileSB.append(" \tVocê não pode visualizar ítens de um usuário\n" +
					"\tque não é seu amigo.");
		}
		
		return profileSB.toString();
	}
	
	protected List<ActivityRegistry> getActivityHistory() throws Exception {
		return owner.getMyActivityHistory();
	}

	public String publishItemRequest(String itemName, String itemDescription) 
		throws Exception{
		return owner.publishItemRequest(itemName, itemDescription);
	}

	public void offerItem(Lending publishedRequest, String itemId) throws Exception{
		for ( Item item : owner.getAllItems() ){
			if ( item.getID().equals(itemId) ){
				owner.offerItem(publishedRequest, item);
				return;
			}
		}
		throw new Exception("Item inexistente");
	}

	public void republishItemRequest(Lending petition) throws Exception {
		owner.republishItemRequest(petition);
	}
	
}