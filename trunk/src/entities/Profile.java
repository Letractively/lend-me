package entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import main.LendMe;
import entities.util.Message;
import entities.util.Topic;

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
	
	public void update() throws Exception{
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
	
	public static Profile getUserProfile(Session userSession, User user) throws Exception{
		return new Profile(userSession, user);
	}

	public Profile viewOwnProfile() throws Exception{
		return new Profile(observer, LendMe.getUserByLogin(observer.getLogin()));
	}
	
	public Profile viewOtherProfile(User other) throws Exception{
		if ( observer == null ){
			throw new Exception("Nao eh possivel visualizar este perfil");//"This profile is not accessible");
		}
		if ( observer.getLogin().equals(other.getLogin()) ){
			return viewOwnProfile();
		}
		return new Profile(observer, other);
	}

	public Set<User> getOwnerFriends() {
		return ownerFriends;
	}

	public Set<Item> getOwnerItems() throws Exception{
		if ( ownerItems == null ){
			throw new Exception("O usuário não tem permissão para visualizar estes itens");//"User has no permission to view these items");
		}
		return ownerItems;
	}
	
	public String getOwnerName(){
		return owner.getName();
	}

	public String getOwnerLogin(){
		return owner.getLogin();
	}
	
	public String getOwnerAddress(){
		return owner.getAddress().getFullAddress();
	}
	
	public Set<User> getOwnerFriendshipRequests(){
		return owner.getReceivedFriendshipRequests();
	}

	public Session getObserver() {
		return observer;
	}

	public void askForFriendship() throws Exception{
		if ( observer.getLogin().equals(owner.getLogin()) ){
			throw new Exception("Amizade inexistente");//inválida - pedido de amizade para si próprio");//"Invalid friendship");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		me.requestFriendship(owner);
	}

	public void acceptFriendshipRequest() throws Exception{
		if ( observer.getLogin().equals(owner.getLogin()) ){
			throw new Exception("Amizade inexistente");//inválida - aceitação de amizade para si próprio");//"Invalid friendship");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		me.acceptFriendshipRequest(owner);
	}

	public void declineFriendshipRequest() throws Exception{
		if ( observer.getLogin().equals(owner.getLogin()) ){
			throw new Exception("Amizade inexistente");//inválida - negação de amizade para si próprio");//"Invalid friendship");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		me.declineFriendshipRequest(owner);
	}

	public void breakFriendship() throws Exception{
		if ( observer.getLogin().equals(owner.getLogin()) ){
			throw new Exception("Amizade inexistente");//inválida - rompimento de amizade com si próprio");//"Invalid friendship");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( !me.hasFriend(owner) ){
			throw new Exception("Amizade inexistente");//inválida - rompimento de amizade com alguem que não é seu amigo");
		}

//		TODO Check with Brian whether or not this is the desired behavior
//		TODO Mail sent to him, waiting for answer. The US12 passes with this code commented
//		Set<Item> myLentItems = me.getLentItems();
//		myLentItems.retainAll(owner.getBorrowedItems());
//		for ( Item item : myLentItems ){
//			owner.returnItem(item);
//			me.receiveLendedItem(item);
//		}
//
//		Set<Item> hisLentItems = owner.getLentItems();
//		hisLentItems.retainAll(me.getBorrowedItems());
//		for ( Item item : hisLentItems ){
//			me.returnItem(item);
//			owner.receiveLendedItem(item);
//		}
//		
//		Set<Item> myBorrowedItems = me.getBorrowedItems();
//		myBorrowedItems.retainAll(owner.getLentItems());
//		for ( Item item : myBorrowedItems ){
//			me.returnItem(item);
//			owner.receiveLendedItem(item);
//		}
//
//		Set<Item> hisBorrowedItems = owner.getBorrowedItems();
//		hisBorrowedItems.retainAll(me.getLentItems());
//		for ( Item item : hisBorrowedItems ){
//			owner.returnItem(item);
//			me.receiveLendedItem(item);
//		}
		
		me.breakFriendship(owner);
	}
	
	public boolean isFriendOfOwner() throws Exception{
		if ( observer.getLogin().equals(owner.getLogin()) ){
			throw new Exception("Amizade inválida - consulta para amizade com si próprio");//"Invalid friendship");
		}
		User me = LendMe.getUserByLogin(observer.getLogin());
		return me.hasFriend(owner);
	}

	public boolean searchByName(String key) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( owner.getLogin().equals(me.getLogin()) ){
			return false;
		}
		return owner.getName().contains(key);
	}

	public boolean searchByLogin(String key) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( owner.getLogin().equals(me.getLogin()) ){
			return false;
		}
		return owner.getLogin().contains(key);
	}
	
	public boolean searchByAddress(String key) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( owner.getLogin().equals(me.getLogin()) ){
			return false;
		}
		return owner.getAddress().getFullAddress().contains(key);
	}

	public Collection<Lending> getLendingRecords(String kind) throws Exception{
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

	public String requestItem(String itemId, int requiredDays) throws Exception{
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

	public String approveLoan(String requestId) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( !LendMe.getLendingByRequestId(requestId).getLender().equals(me) ) {
			throw new Exception("O empréstimo só pode ser aprovado pelo dono do item");//Only the owner of the item is allowed to lend it
		}
		return me.approveLoan(requestId);
	}

	public String denyLoan(String requestId) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( !LendMe.getLendingByRequestId(requestId).getLender().equals(me) ) {
			throw new Exception("O empréstimo só pode ser negado pelo dono do item");//Only the owner of the item is allowed to lend it
		}
		return me.denyLoan(requestId);
	}
	
	public String returnItem(String lendingId) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( !LendMe.getLendingByLendingId(lendingId).getBorrower().equals(me) ) {
			throw new Exception("O item só pode ser devolvido pelo usuário beneficiado");//Only the owner of the item is allowed to lend it
		}
		return me.approveItemReturning(lendingId);
	}

	public String confirmLendingTermination(String lendingId) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( !LendMe.getLendingByLendingId(lendingId).getLender().equals(me) ) {
			throw new Exception("O término do empréstimo só pode ser confirmado pelo dono do item");//Only the owner of the item is allowed to confirm success in return process
		}
		return me.confirmLendingTermination(lendingId);
	}

	public String askForReturnOfItem(String lendingId) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		if ( !LendMe.getLendingByLendingId(lendingId).getLender().equals(me) ) {
			throw new Exception("O usuário não tem permissão para requisitar a devolução deste item");//Only the owner of the item is allowed to ask for return of item
		}
		return me.askForReturnOfItem(lendingId, LendMe.getSystemDate());
	}

	public String sendMessage(String subject, String message, String lendingId) throws Exception{
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
	
	public String sendMessage(String subject, String message) throws Exception{
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

	public List<Topic> getTopics(String topicType) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		return me.getTopics(topicType);
	}

	public List<Message> getTopicMessages(String topicId) throws Exception{
		User me = LendMe.getUserByLogin(observer.getLogin());
		
		if (topicId == null || topicId.trim().isEmpty()) {
			throw new Exception("Identificador do tópico é inválido");
			// "Invalid topic identifier");
		}
		
		return me.getMessagesByTopicId(topicId);
	}
	
}