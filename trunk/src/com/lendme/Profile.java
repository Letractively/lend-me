package com.lendme;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Profile {

	private Session observer;
	private User owner;
	private Set<User> ownerFriends;
	private Set<Item> ownerItems;
	
	private Profile(Session observer, User user) throws Exception{

		if ( observer == null ){
			throw new Exception("Usuário observador inválido");//"Invalid observer user");
		}
		if ( observer.getLogin() == null || observer.getLogin().trim().isEmpty() ){
			throw new Exception("Login do usuário observador inválido");//"Invalid observer user login");
		}
		if ( user == null ){
			throw new Exception("Usuário dono do perfil inválido");//"Invalid profile owner user");
		}
		if ( user.getFriends() == null ){
			throw new Exception("Amigos do dono do perfil inválidos");//"Invalid profile owner user friends");
		}
		if ( user.getAllItems() == null ){
			throw new Exception("Itens do dono do perfil inválidos");//"Invalid profile owner user items");
		}
		if ( user.getName() == null || user.getName().trim().isEmpty() ){
			throw new Exception("Nome do usuário dono do perfil inválido");//"Invalid profile owner user name");
		}
		if ( user.getLogin() == null || user.getLogin().trim().isEmpty() ){
			throw new Exception("Login do usuário dono do perfil inválido");//"Invalid profile owner user login");
		}
		if ( user.getAddress() == null || user.getAddress().getFullAddress() == null 
				|| user.getAddress().getFullAddress().trim().isEmpty() ){
			throw new Exception("Endereço do usuário dono do perfil inválido");//"Invalid profile owner user address");
		}
		
		this.observer = observer;
		owner = user;
		ownerFriends = owner.getFriends();
		if ( observer.getLogin().equals(owner.getLogin()) ){
			ownerItems = owner.getAllItems();
		}
		else{
			for ( User friend : ownerFriends ){
				if ( friend.getLogin().equals(observer.getLogin())){
					ownerItems = owner.getAllItems();
					return;
				}
			}
		}
	}
	
	protected void update() throws Exception{
		ownerFriends = owner.getFriends();
		if ( observer.getLogin().equals(owner.getLogin()) ){
			ownerItems = owner.getAllItems();
		}
		else{
			for ( User friend : ownerFriends ){
				if ( friend.getLogin().equals(observer.getLogin())){
					ownerItems = owner.getAllItems();
					return;
				}
			}
		}
	}
	
	protected static Profile getUserProfile(Session userSession, User user) throws Exception{
		return new Profile(userSession, user);
	}

	protected Profile viewOwnProfile() throws Exception{
		return new Profile(observer, LendMe.getUserByLogin(observer.getLogin()));
	}
	
	protected Profile viewOtherProfile(User other) throws Exception{
		if ( observer == null ){
			throw new Exception("Nao eh possivel visualizar este perfil");//"This profile is not accessible");
		}
		if ( observer.getLogin().equals(other.getLogin()) ){
			return viewOwnProfile();
		}
		return new Profile(observer, other);
	}

	protected Set<User> getOwnerFriends() {
		return ownerFriends;
	}

	protected Set<Item> getOwnerItems() throws Exception{
		if ( ownerItems == null ){
			throw new Exception("O usuário não tem permissão para visualizar estes itens");//"User has no permission to view these items");
		}
		return ownerItems;
	}
	
	protected String getOwnerName(){
		return owner.getName();
	}

	protected String getOwnerLogin(){
		return owner.getLogin();
	}
	
	protected String getOwnerAddress(){
		return owner.getAddress().getFullAddress();
	}
	
	protected Set<User> getOwnerFriendshipRequests(){
		return owner.getReceivedFriendshipRequests();
	}

	protected Session getObserver() {
		return observer;
	}

	protected void askForFriendship() throws Exception{
		if ( observer.getLogin().equals(owner.getLogin()) ){
			throw new Exception("Amizade inexistente");//inválida - pedido de amizade para si próprio");//"Invalid friendship");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		me.requestFriendship(owner);
	}

	protected void acceptFriendshipRequest() throws Exception{
		if ( observer.getLogin().equals(owner.getLogin()) ){
			throw new Exception("Amizade inexistente");//inválida - aceitação de amizade para si próprio");//"Invalid friendship");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		me.acceptFriendshipRequest(owner);
	}

	protected void declineFriendshipRequest() throws Exception{
		if ( observer.getLogin().equals(owner.getLogin()) ){
			throw new Exception("Amizade inexistente");//inválida - negação de amizade para si próprio");//"Invalid friendship");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		me.declineFriendshipRequest(owner);
	}

	protected void breakFriendship() throws Exception{
		if ( observer.getLogin().equals(owner.getLogin()) ){
			throw new Exception("Amizade inexistente");//inválida - rompimento de amizade com si próprio");//"Invalid friendship");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( !me.hasFriend(owner) ){
			throw new Exception("Amizade inexistente");//inválida - rompimento de amizade com alguem que não é seu amigo");
		}
		
		me.breakFriendship(owner);
	}
	
	protected boolean isFriendOfOwner() throws Exception{
		if ( observer.getLogin().equals(owner.getLogin()) ){
			throw new Exception("Amizade inválida - consulta para amizade com si próprio");//"Invalid friendship");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		return me.hasFriend(owner);
	}

	protected boolean searchByName(String key) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( owner.getLogin().equals(me.getLogin()) ){
			return false;
		}
		return owner.getName().contains(key);
	}

	protected boolean searchByLogin(String key) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( owner.getLogin().equals(me.getLogin()) ){
			return false;
		}
		return owner.getLogin().contains(key);
	}
	
	protected boolean searchByAddress(String key) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( owner.getLogin().equals(me.getLogin()) ){
			return false;
		}
		return owner.getAddress().getFullAddress().contains(key);
	}

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

	protected String requestItem(String itemId, int requiredDays) throws Exception{
		if ( itemId == null || itemId.trim().isEmpty() ){
			throw new Exception("Identificador do item é invalido");//"Invalid item identifier");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		try{
			for ( Item item : getOwnerItems() ){
				if ( item.getID().equals(itemId) ){
					return me.borrowItem(item, owner, requiredDays);
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

	protected String approveLoan(String requestId) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( !LendMe.getLendingByRequestId(requestId).getLender().equals(me) ) {
			throw new Exception("O empréstimo só pode ser aprovado pelo dono do item");//Only the owner of the item is allowed to lend it
		}
		return me.approveLending(requestId);
	}

	protected String denyLoan(String requestId) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( !LendMe.getLendingByRequestId(requestId).getLender().equals(me) ) {
			throw new Exception("O empréstimo só pode ser negado pelo dono do item");//Only the owner of the item is allowed to lend it
		}
		return me.denyLending(requestId);
	}
	
	protected String returnItem(String lendingId) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( !LendMe.getLendingByLendingId(lendingId).getBorrower().equals(me) ) {
			throw new Exception("O item só pode ser devolvido pelo usuário beneficiado");//Only the owner of the item is allowed to lend it
		}
		return me.approveItemReturning(lendingId);
	}

	protected String confirmLendingTermination(String lendingId) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( !LendMe.getLendingByLendingId(lendingId).getLender().equals(me) ) {
			throw new Exception("O término do empréstimo só pode ser confirmado pelo dono do item");//Only the owner of the item is allowed to confirm success in return process
		}
		return me.confirmLendingTermination(lendingId);
	}

	protected String denyLendingTermination(String lendingId) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( !LendMe.getLendingByLendingId(lendingId).getLender().equals(me) ) {
			throw new Exception("O término do empréstimo só pode ser negado pelo dono do item");//Only the owner of the item is allowed to confirm success in return process
		}
		return me.denyLendingTermination(lendingId);
	}
	
	protected String askForReturnOfItem(String lendingId) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( !LendMe.getLendingByLendingId(lendingId).getLender().equals(me) ) {
			throw new Exception("O usuário não tem permissão para requisitar a devolução deste item");//Only the owner of the item is allowed to ask for return of item
		}
		return me.askForReturnOfItem(lendingId, LendMe.getSystemDate());
	}

	protected String sendMessage(String subject, String message, String lendingId) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( observer.getLogin().equals(owner.getLogin()) ){
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
	
	protected String sendMessage(String subject, String message) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( observer.getLogin().equals(owner.getLogin()) ){
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

	protected List<Topic> getTopics(String topicType) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		return me.getTopics(topicType);
	}

	protected List<Message> getTopicMessages(String topicId) throws Exception{		
		if (topicId == null || topicId.trim().isEmpty()) {
			throw new Exception("Identificador do tópico é inválido");
			// "Invalid topic identifier");
		}
		List<Message> messages = LendMe.getMessagesByTopicId(topicId);
		Message sampleMessage = messages.iterator().next();

		User me = LendMe.getUserByLogin(observer.getLogin());
		
		if ( !( sampleMessage.getSender().equals(me.getLogin()) 
				|| sampleMessage.getReceiver().equals(me.getLogin())) ) {
			throw new Exception("O usuário não tem permissão para ler as mensagens deste tópico");
		}
		return messages;
	}

	protected void registerInterestForItem(String itemId) throws Exception{

		if ( itemId == null || itemId.trim().isEmpty() ){
			throw new Exception("Identificador do item é inválido");//"Invalid item identifier");
		}
		if ( observer.getLogin().equals(getOwnerLogin()) ){
			throw new Exception("O usuário não pode registrar interesse no próprio item");
		}
		
		User me = LendMe.getUserByLogin(observer.getLogin());

		try{
			for ( Item item : getOwnerItems() ){
				if ( item.getID().equals(itemId) ){
					me.registerInterestForItem(item, owner);
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
	
}