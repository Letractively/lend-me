package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entities.Item;
import entities.Lending;
import entities.Profile;
import entities.Session;
import entities.User;
import entities.util.ComparatorOfItems;
import entities.util.EventDate;
import entities.util.Message;
import entities.util.Topic;

/**
 * The System.
 * Here is where the created Users and open Sessions are located. It also keeps track of the system time.
 * In fact, it contains all Business Logic, but the problem is this is also the System Facade.
 * So we can make no difference between both things.

 * Some methods that are public shouldn't be.

 * In my opinion (Guilherme) the only public methods should be:
 * 
 * resetSystem()
 * getSystemDate()
 * openSession()
 * closeSession()
 * registerUser()
 * searchUsersByName()
 * searchUsersByAddress()
 * askForFriendship()
 * acceptFriendship()
 * declineFriendship()
 * breakFriendship()
 * getFriends() //the friends of this user
 * getFriends() //the friends of another user
 * hasFriend()
 * sendMessage() //off topic
 * sendMessage() //negotiation topic
 * getTopics()
 * getTopicMessages()
 * registerItem()
 * getItems() //the items of this user
 * getItems() //the items of another user
 * deleteItem()
 * registerInterestForItem()
 * requestItem()
 * approveLoan()
 * askForReturnOfItem()
 * returnItem()
 * confirmLendingTermination()
 * denyLendingTermination()
 * getLendingRecords()
 * 
 * Everything else should remain private or protected (if used by other System objects)
 */

public class LendMe {

	private static Set<User> users = new HashSet<User>();
	private static Set<Session> sessions = new HashSet<Session>();
	private static EventDate time = new EventDate("System time");
	public static enum AtributeForSearch {DESCRICAO, NOME, ID, CATEGORIA};
	public static enum DispositionForSearch {CRESCENTE, DECRESCENTE};
	public static enum CriterionForSearch {DATACRIACAO, REPUTACAO};
	
	/**
	 * Resets the whole system: all living sessions are shutdown as well as all users are deleted.
	 * Also, the system clock is set to same as the OS time.
	 * 
	 * <i>This method belongs to the public system interface</i>
	 */
	public static void resetSystem(){
		users = new HashSet<User>();
		sessions = new HashSet<Session>();
		time = new EventDate("System reset at "+time.getDate());
	}
	
	/**
	 * Opens a new session for the user specified by login.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param login the user login
	 * @return the session id
	 * @throws Exception for invalid parameters and if user doesn't exists
	 */
	public static String openSession(String login) throws Exception {
		if (login == null || login.trim().isEmpty()){
			throw new Exception("Login inválido");//"Invalid login");
		}
		LendMe.getUserByLogin(login);
		Session session = new Session(login);
		sessions.add(session);
		return session.getId();
	}

	/**
	 * Returns the system date.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @return the system time
	 */
	public static Date getSystemDate() {
		return time.getDate();
	}
	
	/**
	 * Simulates that a specified amount of days have passed.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param amount the amount of days
	 * @return the string representing the new system time
	 */
	public static String someDaysPassed(int amount){
		time.addDays(amount+1);
		return time.getDate().toString();
	}
	
	/**
	 * Closes the specified session.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param id the session id
	 * @throws Exception for inexistent sessions
	 */
	public static void closeSession(String id) throws Exception{
		sessions.remove(getSessionByID(id));
	}
	
	/**
	 * Registers a new user in the system.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param login the user login
	 * @param name the user name
	 * @param address the user address
	 * @throws Exception for invalid parameters or if a user with login already exists
	 */
	public static void registerUser(String login, String name, String... address) throws Exception{
		if(!users.add(new User(login, name, address))){
			throw new Exception("Já existe um usuário com este login");//"User with this login already exists");
		}
	}
	
	/**
	 * Registers a new item in the system.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param sessionId the owner of the item session id
	 * @param name the item name
	 * @param description the item description
	 * @param category the item category
	 * @return
	 * @throws Exception for invalid parameters
	 */
	public static String registerItem(String sessionId, String name, 
			String description, String category) throws Exception{
		
		if ( category == null || category.trim().isEmpty() ){
			throw new Exception("Categoria inválida");//"Invalid category");
		}
		
		User owner = getUserByLogin(getSessionByID(sessionId).getLogin());
		return owner.addItem(name, description, category);
	}
	
	/**
	 * This method is only used by unity tests
	 * @param name
	 * @return
	 */
	public static Set<User> searchUsersByName(String name) {
		Set<User> foundUsers = new HashSet<User>();
		
		for ( User user : users ){
			if ( user.nameMatches(name) ){
				foundUsers.add(user);
			}
		}
		
		return foundUsers;
	}
	
	/**
	 * This method is only used by unity tests
	 * @param address
	 * @return
	 */
	public static Set<User> searchUsersByAddress(String address) {
		Set<User> foundUsers = new HashSet<User>();
		
		for ( User user : users ){
			if ( user.getAddress().addressMatches(address) ){
				foundUsers.add(user);
			}
		}
		
		return foundUsers;
	}
	
	/**
	 * This method is only used by unity tests
	 * @param login
	 * @return
	 */
	public static Set<Session> searchSessionsByLogin(String login) {
		Set<Session> results = new HashSet<Session>();
		for(Session actualSession : sessions){
			if(actualSession.getLogin().equals(login)){
				results.add(actualSession);
			}
		}
		return results;
	}

	/**
	 * This method is only used by acceptance tests interface
	 * @param sessionId
	 * @param key
	 * @param attribute
	 * @return
	 * @throws Exception
	 */
	protected static Set<User> searchUsersByAttributeKey(String sessionId,
			String key, String attribute) throws Exception{

		if ( attribute == null || attribute.trim().isEmpty() ){
			throw new Exception("Atributo inválido");//"Invalid attribute");
		}
		if (!(attribute.equals("nome") || attribute.equals("login") || attribute.equals("endereco"))){
			throw new Exception("Atributo inexistente");//"Inexistent attribute");
		}
		if ( key == null || key.trim().isEmpty() ){
			throw new Exception("Palavra-chave inválida");//"Invalid search key");
		}
		
		Set<User> results = new HashSet<User>();
		Profile viewer = getUserProfile(sessionId);
		for ( User user : users ){
			viewer = viewer.viewOtherProfile(user);
			if ( attribute.equals("nome") ){
				if ( viewer.searchByName(key) ){
					results.add(user);
				}
			}
			else if ( attribute.equals("login") ){
				if ( viewer.searchByLogin(key) ){
					results.add(user);
				}
			}
			else if ( attribute.equals("endereco") ){
				if ( viewer.searchByAddress(key) ){
					results.add(user);
				}
			}
		}
		return results;
	}
	
	/**
	 * This method is only used by local methods
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private static Session getSessionByID(String id) throws Exception{
		if ( id == null || id.trim().isEmpty() ){
			throw new Exception("Sessão inválida");//"Invalid session");
		}
		for(Session actualSession : sessions){
			if (actualSession.getId().equals(id)) {
				return actualSession;
			}
		}
		throw new Exception("Sessão inexistente");//"Inexistent session");
	}
	
	/**
	 * This method is only used by acceptance tests interface
	 * @param login
	 * @param attribute
	 * @return
	 * @throws Exception
	 */
	protected static String getUserAttribute(String login, String attribute)
			throws Exception{
		
		if ( attribute == null || attribute.trim().isEmpty() ){
			throw new Exception("Atributo inválido");//"Invalid attribute");
		}
		if (!(attribute.equals("nome") || attribute.equals("endereco"))){
			throw new Exception("Atributo inexistente");//"Inexistent attribute");
		}
		if ( login == null || login.trim().isEmpty() ){
			throw new Exception("Login inválido");//"Invalid login");
		}
		
		User user = getUserByLogin(login);

		if(attribute.equals("nome")){
			return user.getName();
		}else{
			return user.getAddress().toString();
		}
	}

	/**
	 * This method is only used by acceptance tests interface
	 * @param itemId
	 * @param attribute
	 * @return
	 * @throws Exception
	 */
	public static String getItemAttribute(String itemId, String attribute) throws Exception{
		
		if ( attribute == null || attribute.trim().isEmpty() ){
			throw new Exception("Atributo inválido");//"Invalid attribute");
		}
		if (!(attribute.equals("nome") || attribute.equals("descricao") || attribute.equals("categoria"))){
			throw new Exception("Atributo inexistente");//"Inexistent attribute");
		}
		if ( itemId == null || itemId.trim().isEmpty() ){
			throw new Exception("Identificador do item é inválido");//"Invalid item identifier");
		}
		
		Item referredItem = getItemByID(itemId);
		
		if(attribute.equals("nome")){
			return referredItem.getName();
		}else if (attribute.equals("descricao")){
			return referredItem.getDescription();
		}
		else{
			String formattedCategory = referredItem.getCategory().toString();
			return formattedCategory.substring(0, 1).toUpperCase() 
			+ formattedCategory.substring(1).toLowerCase();
		}
		
	}

	/**
	 * This method is only used by local methods
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	private static Item getItemByID(String itemId) throws Exception {
		if ( itemId == null || itemId.trim().isEmpty() ){
			throw new Exception("Identificador do item é inválido");
		}
		Item referredItem = null;
		
		for ( User user : users ){
			for ( Item item : user.getAllItems() ){
				if ( item.getID().equals(itemId) ){
					referredItem = item;
					break;
				}
			}
			if ( referredItem != null ){
				break;
			}
		}
		
		if ( referredItem == null ){
			throw new Exception("Item inexistente");//"Inexistent item");
		}
		return referredItem;
	}

	/**
	 * This method is used by unity tests and local methods
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public static Profile getUserProfile(String sessionId) throws Exception {
		Session session = getSessionByID(sessionId);
		User user = getUserByLogin(session.getLogin());
		if (user == null) {
			throw new Exception("Sessão se refere a usuário desconhecido");// "Session belongs to unknown user");
		}
		return Profile.getUserProfile(session, user);
	}

	/**
	 * This method belongs to the public system interface
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public static Set<User> getFriends(String sessionId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		Set<User> users = viewer.getOwnerFriends();
		return users;
	}

	/**
	 * This method belongs to the public system interface
	 * @param solicitorSessionId
	 * @param solicitedLogin
	 * @return
	 * @throws Exception
	 */
	public static Set<User> getFriends(String solicitorSessionId, String solicitedLogin) throws Exception{
		Profile solicitorViewer = LendMe.getUserProfile(solicitorSessionId);
		solicitorViewer = solicitorViewer.viewOtherProfile(LendMe.getUserByLogin(solicitedLogin));
		Set<User> users = solicitorViewer.getOwnerFriends();
		return users;
	}

	/**
	 * This method is only used by acceptance tests interface and business logic
	 * @param login
	 * @return
	 * @throws Exception
	 */
	public static User getUserByLogin(String login) throws Exception{
		if ( login == null || login.trim().isEmpty() ){
			throw new Exception("Login inválido");//"Invalid login");
		}
		for(User actualUser : users){
			if(actualUser.getLogin().equals(login)){
				return actualUser;
			}
		}
		throw new Exception("Usuário inexistente");//"User does not exist");
	}
	
	/**
	 * This method belongs to the public system interface
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public static Set<Item> getItems(String sessionId) throws Exception {
		Profile viewer = LendMe.getUserProfile(sessionId);
		Set<Item> items = viewer.getOwnerItems();
		return items;
	}

	/**
	 * This method belongs to the public system interface
	 * @param observerSessionId
	 * @param ownerLogin
	 * @return
	 * @throws Exception
	 */
	public static Set<Item> getItems(String observerSessionId, String ownerLogin) throws Exception {
		Profile viewer = LendMe.getUserProfile(observerSessionId);
		viewer = viewer.viewOtherProfile(LendMe.getUserByLogin(ownerLogin));
		Set<Item> items = viewer.getOwnerItems();
		return items;
	}
	
	/**
	 * This method belongs to the public system interface
	 * @param solicitorSessionId
	 * @param solicitedLogin
	 * @throws Exception
	 */
	public static void askForFriendship(String solicitorSessionId, String solicitedLogin) throws Exception{
		Profile solicitorProfile = getUserProfile(solicitorSessionId);
		solicitorProfile = solicitorProfile.viewOtherProfile(LendMe.getUserByLogin(solicitedLogin));
		solicitorProfile.askForFriendship();
	}

	/**
	 * This method belongs to the public system interface
	 * @param solicitedSessionId
	 * @param solicitorLogin
	 * @throws Exception
	 */
	public static void acceptFriendship(String solicitedSessionId, String solicitorLogin) throws Exception{
		Profile solicitedProfile = getUserProfile(solicitedSessionId);
		solicitedProfile = solicitedProfile.viewOtherProfile(LendMe.getUserByLogin(solicitorLogin));
		solicitedProfile.acceptFriendshipRequest();
	}
	
	/**
	 * This method belongs to the public system interface
	 * @param solicitedSessionId
	 * @param solicitorLogin
	 * @throws Exception
	 */
	public static void declineFriendship(String solicitedSessionId, String solicitorLogin) throws Exception{
		Profile solicitedProfile = getUserProfile(solicitedSessionId);
		solicitedProfile = solicitedProfile.viewOtherProfile(LendMe.getUserByLogin(solicitorLogin));
		solicitedProfile.declineFriendshipRequest();
	}
	
	/**
	 * This method belongs to the public system interface
	 * @param solicitorSessionId
	 * @param solicitedLogin
	 * @throws Exception
	 */
	public static void breakFriendship(String solicitorSessionId, String solicitedLogin) throws Exception{
		Profile solicitorProfile = getUserProfile(solicitorSessionId);
		solicitorProfile = solicitorProfile.viewOtherProfile(LendMe.getUserByLogin(solicitedLogin));
		solicitorProfile.breakFriendship();
	}

	/**
	 * This method belongs to the public system interface
	 * @param solicitorSessionId
	 * @param solicitedUserLogin
	 * @return
	 * @throws Exception
	 */
	public static boolean hasFriend(String solicitorSessionId, String solicitedUserLogin) throws Exception{
		Profile solicitorViewer = getUserProfile(solicitorSessionId);
		solicitorViewer = solicitorViewer.viewOtherProfile(LendMe.getUserByLogin(solicitedUserLogin));
		return solicitorViewer.isFriendOfOwner();
	}

	/**
	 * This method is only user by the acceptance tests interface
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	protected static Set<User> getFriendshipRequests(String sessionId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.getOwnerFriendshipRequests();
	}

	public static String sendMessage(String senderSessionId, String subject, String message, 
			String receiverLogin, String lendingId) throws Exception {
		
		Profile solicitorViewer = null;
		
		try {
			solicitorViewer = getUserProfile(senderSessionId);
		
			solicitorViewer = solicitorViewer.viewOtherProfile(
				LendMe.getUserByLogin(receiverLogin));
		
			return solicitorViewer.sendMessage(subject, message, lendingId);
			
		} catch (Exception e) {
			
			if (e.getMessage().equals("Login inválido")) {
				throw new Exception("Destinatário inválido");//"Invalid receiver");
			}
			
			else if (e.getMessage().equals("Usuário inexistente")) {
				throw new Exception("Destinatário inexistente");//"Inexistent receiver");
			}
			
			else if (e.getMessage().equals("Empréstimo inexistente")) {
				throw new Exception("Requisição de empréstimo inexistente" );
				//"Inexistent lending");
			}
			
			throw e;
		}
		
		
	}
	
	public static String sendMessage(String senderSessionId, String subject, String message, 
			String receiverLogin) throws Exception {
		
		Profile solicitorViewer = null;
		
		try {
			solicitorViewer = getUserProfile(senderSessionId);
		
			solicitorViewer = solicitorViewer.viewOtherProfile(
				LendMe.getUserByLogin(receiverLogin));
		
		} catch (Exception e) {
			
			if (e.getMessage().equals("Login inválido")) {
				throw new Exception("Destinatário inválido");//"Invalid receiver");
			}
			
			else if (e.getMessage().equals("Usuário inexistente")) {
				throw new Exception("Destinatário inexistente");//"Inexistent receiver");
			}
			
			throw e;
		}
		
		return solicitorViewer.sendMessage(subject, message);
		
	}
	
	public static List<Topic> getTopics(String sessionId, String topicType)
			throws Exception {
		
		Profile solicitorViewer = getUserProfile(sessionId);
		return solicitorViewer.getTopics(topicType);
		
	}
	
	public static List<Message> getTopicMessages(String sessionId, String topicId)
			throws Exception {
		
		Profile solicitorViewer = getUserProfile(sessionId);
		return solicitorViewer.getTopicMessages(topicId);
	}
	
	/**
	 * This method belongs to the public system interface
	 * @param sessionId
	 * @param kind
	 * @return
	 * @throws Exception
	 */
	public static Collection<Lending> getLendingRecords(String sessionId, String kind) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.getLendingRecords(kind);
	}
	
	/**
	 * This method belongs to the public system interface
	 * @param sessionId
	 * @param itemId
	 * @param requiredDays
	 * @return
	 * @throws Exception
	 */
	public static String requestItem(String sessionId, String itemId, int requiredDays) throws Exception {
		Profile viewer = getUserProfile(sessionId);
		viewer = viewer.viewOtherProfile(getItemOwner(itemId));
		return viewer.requestItem(itemId, requiredDays);
	}
	
	/**
	 * This method belongs to the public system interface
	 * @param sessionId
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public static String approveLoan(String sessionId, String requestId)  throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.approveLoan(requestId);
	}

	/**
	 * This method belongs to the public system interface
	 * @param sessionId
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public static String denyLoan(String sessionId, String requestId)  throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.denyLoan(requestId);
	}
	
	/**
	 * This method belongs to the public system interface
	 * @param sessionId
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public static String returnItem(String sessionId, String requestId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.returnItem(requestId);
	}
	
	/**
	 * This method belongs to the public system interface
	 * @param sessionId
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public static String confirmLendingTermination(String sessionId,
			String lendingId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.confirmLendingTermination(lendingId);
	}

	public static String denyLendingTermination(String sessionId,
			String lendingId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.denyLendingTermination(lendingId);
	}
	
	/**
	 * This method belongs to the public system interface
	 * @param sessionId
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public static String askForReturnOfItem(String sessionId,
			String lendingId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.askForReturnOfItem(lendingId);
	}
	
	/**
	 * This method is only used by local methods
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	public static User getItemOwner(String itemId) throws Exception{
		if ( itemId == null || itemId.trim().isEmpty() ){
			throw new Exception("Identificador do item é inválido");//"Invalid item identifier");
		}
		for ( User user : users ){
			for ( Item item : user.getAllItems() ){
				if ( item.getID().equals(itemId) ){
					return user;
				}
			}
		}
		throw new Exception("Item inexistente");//"Inexistent item");
	}
	
	/**
	 * This method is only used by system logic
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public static Lending getLendingByLendingId(String lendingId) throws Exception{
		for ( User user : users ){
			Lending record = user.getLendingByLendingId(lendingId);
			if ( record != null ){
				return record;
			}
		}
		throw new Exception("Empréstimo inexistente");		
	}
	
	/**
	 * This method is only used by system logic
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public static Lending getLendingByRequestId(String requestId) throws Exception{
		for ( User user : users ){
			Lending record = user.getLendingByRequestId(requestId);
			if ( record != null ){
				return record;
			}
		}
		throw new Exception("Requisição de empréstimo inexistente");
	}

	public static void deleteItem(String sessionId, String itemId) throws Exception {
		User userOwnerSession = getUserByLogin(getSessionByID(sessionId).getLogin());
		userOwnerSession.deleteMyItem(itemId);
	}

	private static void validator(String key, String atribute, String disposition, String criterion) throws Exception{
		
		if(key == null || key.trim().isEmpty()){
			throw new Exception("Chave inválida");//"invalid key"
		}
		if(atribute == null || atribute.trim().isEmpty()){
			throw new Exception("Atributo inválido");
		}
		if(!Arrays.toString(AtributeForSearch.values()).toLowerCase().contains(atribute.toLowerCase())){
			throw new Exception("Atributo inexistente");
		}
		if(disposition == null || disposition.trim().isEmpty()){
			throw new Exception("Tipo inválido de ordenação");
		}
		if(!Arrays.toString(DispositionForSearch.values()).toLowerCase().contains(disposition.toLowerCase())){
			throw new Exception("Tipo de ordenação inexistente");
		}
		if(criterion == null || criterion.trim().isEmpty()){
			throw new Exception("Critério inválido de ordenação");
		}
		if(!Arrays.toString(CriterionForSearch.values()).toLowerCase().contains(criterion.toLowerCase())){
			throw new Exception("Critério de ordenação inexistente");
		}
		
	}
	
	public static ArrayList<Item> searchItem(String idSessao, String key, String atribute, String disposition ,String criterion) throws Exception{
		
		ArrayList<Item> results = new ArrayList<Item>();
		Session session = getSessionByID(idSessao);
		User userOwnerSession = getUserByLogin(session.getLogin());
		AtributeForSearch atributeAux = AtributeForSearch.DESCRICAO;
		CriterionForSearch criterionAux = CriterionForSearch.DATACRIACAO;
		
		validator(key, atribute, disposition, criterion);
		
		for(AtributeForSearch actual : AtributeForSearch.values()){
			if(actual.toString().toLowerCase().contains(atribute.toLowerCase()))
				atributeAux = actual;
		}
		
		for(CriterionForSearch actual : CriterionForSearch.values()){
			if(actual.toString().toLowerCase().contains(criterion.toLowerCase()))
				criterionAux = actual;
		}
		
		switch(atributeAux){
	
		case DESCRICAO:{
			for(User actualFriend : userOwnerSession.getFriends()){
				for(Item itemOfMyFriend : actualFriend.getAllItems()){
					if(itemOfMyFriend.getDescription().toUpperCase().contains(key.toUpperCase()))
						results.add(itemOfMyFriend);
				}
			}
			 break;
		}
		
		case NOME:{
			for(User actualFriend : userOwnerSession.getFriends()){
				for(Item itemOfMyFriend : actualFriend.getAllItems()){
					if(itemOfMyFriend.getName().toUpperCase().contains(key.toUpperCase()))
						results.add(itemOfMyFriend);
				}
			}
			 break;
		}
		
		case ID: {
			for(User actualFriend : userOwnerSession.getFriends()){
				for(Item itemOfMyFriend : actualFriend.getAllItems()){
					if(itemOfMyFriend.getID().toUpperCase().contains(key.toUpperCase()))
						results.add(itemOfMyFriend);
				}
			}
			 break;
		}
		
		case CATEGORIA: {
			for(User actualFriend : userOwnerSession.getFriends()){
				for(Item itemOfMyFriend : actualFriend.getAllItems()){
					if(itemOfMyFriend.getCategory().toString().toUpperCase().contains(key.toUpperCase()))
						results.add(itemOfMyFriend);
				}
			}
			 break;
		}
		
		default:	throw new Exception("Atributo  inválido");
	}

		switch (criterionAux) {

		case DATACRIACAO: {
			Collections.sort(results);
			if (DispositionForSearch.CRESCENTE.toString().toLowerCase().contains(disposition.toLowerCase())) {
				return results;

			} else {
				Collections.reverse(results);
				return results;

			}
		}

		case REPUTACAO: {
			Collections.sort(results, new ComparatorOfItems());
			if (DispositionForSearch.CRESCENTE.toString().toLowerCase().contains(disposition.toLowerCase())) {
				return results;
			} else {
				Collections.reverse(results);
				return results;
			}
		}

		default:
			return results;
		}
	}
	
	public static void registerInterestForItem(String sessionId, String itemId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		viewer = viewer.viewOtherProfile(getItemOwner(itemId));
		viewer.registerInterestForItem(itemId);
	}

	public static List<Message> getMessagesByTopicId(String topicId) throws Exception{

		for ( User user : users ){
			List<Message> messages = user.getMessagesByTopicId(topicId);
			if ( messages != null ){
				return messages;
			}
		}
		throw new Exception("Tópico inexistente");//"Inexistent topic");
	}

	
	public static String getRanking(String idSession, String categoria) throws Exception{
		String ranking = "";
		
		if(idSession == null || idSession.trim().equals("") ||
				! sessions.contains(getSessionByID(idSession))){
			throw new Exception("Sessão inválida");
		}
		
		if(!categoria.equals("global") && !categoria.equals("amigos")){
			throw new Exception("Categoria inválida");
		}
		
		if(categoria.equals("amigos")){
			User user = getUserByLogin(getSessionByID(idSession).getLogin());
			User[] friendList = user.getFriends().toArray(new User[user.getFriends().size()]);
			
			
			Comparator<? super User> c = null;
			//			Arrays.sort(friendList, >>tem q ter um comparator aki dentro<<);
			Arrays.sort(friendList, c);
			for(User current : friendList){
				ranking = current.getLogin() + ";" + ranking; // vai dar um erro na forma da string, mas isso eh de menos agora.
			}

		if(categoria.equals("global")){
		
			
			}

		}
		return ranking;
	}
	
	
}