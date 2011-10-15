package com.lendme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lendme.ActivityRegistry.ActivityKind;

/**
 * @author THE LENDERS
 * The System.
 * Here is where the created Users and open Sessions are located. It also keeps track of the system time.
 * In fact, it contains most of the Business Logic, and is the access point to User and Profile logic.
 */

public class LendMe {

	private static Set<User> users = new HashSet<User>();
	private static Set<Session> sessions = new HashSet<Session>();
	private static Set<Session> sessionsHystory = new HashSet<Session>();
	private static EventDate time = new EventDate("System time");
	public static enum AtributeForSearch {DESCRICAO, NOME, ID, CATEGORIA};
	public static enum DispositionForSearch {CRESCENTE, DECRESCENTE};
	public static enum CriterionForSearch {DATACRIACAO, REPUTACAO};
	
	private static Comparator<? super User> comparator = new Comparator<User>(){

		@Override
		public int compare(User o1, User o2) {
			int result = 0;
			if(o1.getScore() > o2.getScore()){
				result = 1;
			}else if(o1.getScore() < o2.getScore()){
				result = -1;
			}
			return result;				}
	};
	
	/**
	 * Resets the whole system: all living sessions are shutdown as well as all users are deleted.
	 * Also, the system clock is set to same as the OS time.
	 * 
	 * <i>This method belongs to the public system interface</i>
	 */
	protected static void resetSystem(){
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
	protected static String openSession(String login) throws Exception {
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
	protected static Date getSystemDate() {
		return time.getDate();
	}
	
	/**
	 * Simulates that a specified amount of days have passed.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param amount the amount of days
	 * @return the string representing the new system time
	 */
	protected static String someDaysPassed(int amount){
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
	protected static void closeSession(String id) throws Exception{
		Session toBeFinished = getSessionByID(id);
		toBeFinished.finishSession();
		sessions.remove(toBeFinished);
		sessionsHystory.add(toBeFinished);
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
	protected static String registerUser(String login, String name, String... address) throws Exception{
		User newUser = new User(login, name, address);
		if(!users.add(newUser)){
			throw new Exception("Já existe um usuário com este login");//"User with this login already exists");
		}
		return newUser.getLogin();
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
	protected static String registerItem(String sessionId, String name, 
			String description, String category) throws Exception{
		
		if ( category == null || category.trim().isEmpty() ){
			throw new Exception("Categoria inválida");//"Invalid category");
		}
		
		User owner = getUserByLogin(getSessionByID(sessionId).getLogin());
		return owner.addItem(name, description, category);
	}
	
	/**
	 * Searches for users in the system with the given name match.
	 * 
	 * @param name the given name
	 * @return a set with users found by search
	 */
	protected static Set<User> searchUsersByName(String name) {
		Set<User> foundUsers = new HashSet<User>();
		
		for ( User user : users ){
			if ( user.nameMatches(name) ){
				foundUsers.add(user);
			}
		}
		
		return foundUsers;
	}
	
	/**
	 * Searches for users with given address match.
	 * @param address the given address
	 * @return a set with users found by search
	 */
	protected static Set<User> searchUsersByAddress(String address) {
		Set<User> foundUsers = new HashSet<User>();
		
		for ( User user : users ){
			if ( user.getAddress().addressMatches(address) ){
				foundUsers.add(user);
			}
		}
		
		return foundUsers;
	}
	
	/**
	 * Searches for sessions with given login
	 * @param login the login
	 * @return a set of sessions found by search
	 */
	protected static Set<Session> searchSessionsByLogin(String login) {
		Set<Session> results = new HashSet<Session>();
		for(Session actualSession : sessions){
			if(actualSession.getLogin().equals(login)){
				results.add(actualSession);
			}
		}
		return results;
	}

	/**
	 * Searches for users with given attribute specified by key.
	 * @param sessionId the id of a session of the user who is requiring a search
	 * @param key the search key-value
	 * @param attribute the kind of attribute to guide the search
	 * @return a set of users found by search
	 * @throws Exception if attribute key or kind is invalid or if kind does not exists
	 */
	protected static Set<User> searchUsersByAttributeKey(String sessionId,
			String key, String attribute) throws Exception{

		if ( attribute == null || attribute.trim().isEmpty() ){
			throw new Exception("Atributo inválido");//"Invalid attribute");
		}
		if (!(attribute.equals("nome") || attribute.trim().equals("login") || attribute.trim().equals("endereco"))){
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
	 * Returns the session that have the specified id
	 * @param id the session id
	 * @return the session
	 * @throws Exception if session doesn't exists
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
	 * Returns some user attribute.
	 * @param login the user login
	 * @param attribute the user attribute kind
	 * @return the user attribute value
	 * @throws Exception if attribute or login is invalid
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
	 * Returns some item attribute.
	 * @param itemId the item id
	 * @param attribute the user attribute
	 * @return the item attribute value
	 * @throws Exception if parameters are invalid
	 */
	protected static String getItemAttribute(String itemId, String attribute) throws Exception{
		
		String ownerSessionId = searchSessionsByLogin(getItemOwner(itemId).getLogin())
				.iterator().next().getId();
		
		Profile viewer = getUserProfile(ownerSessionId);
		return viewer.getItemAttribute(itemId, attribute);
	}

	/**
	 * Retrieves a profile for user with existing session specified by session id.
	 * @param sessionId the session id
	 * @return a profile
	 * @throws Exception if user doesn't exists or there is no alive session for that user
	 */
	protected static Profile getUserProfile(String sessionId) throws Exception {
		Session session = getSessionByID(sessionId);
		User user = getUserByLogin(session.getLogin());
		if (user == null) {
			throw new Exception("Sessão se refere a usuário desconhecido");// "Session belongs to unknown user");
		}
		return Profile.getUserProfile(session, user);
	}

	/**
	 * Returns the friends of user with existing session specified by session id.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId the session id
	 * @return a set of friends
	 * @throws Exception if user doesn't exists
	 */
	protected static Set<User> getFriends(String sessionId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		Set<User> users = viewer.getOwnerFriends();
		return users;
	}

	/**
	 * Returns the friends of another user if a session specified by solicitor session id exists.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitorSessionId the session id
	 * @param solicitedLogin the user login whose friends are being required
	 * @return a set of friends
	 * @throws Exception if one of the users involved doesn't exists
	 */
	protected static Set<User> getFriends(String solicitorSessionId, String solicitedLogin) throws Exception{
		Profile solicitorViewer = LendMe.getUserProfile(solicitorSessionId);
		solicitorViewer = solicitorViewer.viewOtherProfile(LendMe.getUserByLogin(solicitedLogin));
		Set<User> users = solicitorViewer.getOwnerFriends();
		return users;
	}

	/**
	 * Returns user with given login.
	 * @param login the login
	 * @return the user
	 * @throws Exception if login is invalid or user with given login doesn't exists
	 */
	protected static User getUserByLogin(String login) throws Exception{
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
	 * Returns the items of the user with existing session specified by session id.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId the session id
	 * @return a set of items
	 * @throws Exception if user doesn't exists
	 */
	protected static Set<Item> getItems(String sessionId) throws Exception {
		Profile viewer = LendMe.getUserProfile(sessionId);
		Set<Item> items = viewer.getOwnerItems();
		return items;
	}

	/**
	 * Returns the items of user specified by login if solicitor user has existing session specified by session id.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param observerSessionId the solicitor session id
	 * @param ownerLogin the solicited user login
	 * @return a set of items of the solicited user
	 * @throws Exception if users involved doesn't exists or solicitor user has no permission to access solicited items
	 */
	protected static Set<Item> getItems(String observerSessionId, String ownerLogin) throws Exception {
		Profile viewer = LendMe.getUserProfile(observerSessionId);
		viewer = viewer.viewOtherProfile(LendMe.getUserByLogin(ownerLogin));
		Set<Item> items = viewer.getOwnerItems();
		return items;
	}
	
	/**
	 * Solicitor user sends a friendship request to solicited user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitorSessionId the solicitor session id
	 * @param solicitedLogin the solicited login
	 * @throws Exception if users involved doesn't exists or friendship request was already sent
	 */
	protected static void askForFriendship(String solicitorSessionId, String solicitedLogin) throws Exception{
		Profile solicitorProfile = getUserProfile(solicitorSessionId);
		solicitorProfile = solicitorProfile.viewOtherProfile(LendMe.getUserByLogin(solicitedLogin));
		solicitorProfile.askForFriendship();
	}

	/**
	 * Solicited user accepts friendship request sent by solicitor user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitedSessionId the solicited session id
	 * @param solicitorLogin the solicitor login
	 * @throws Exception if users involved doesn't exists or friendship request was already accepted
	 */
	protected static void acceptFriendship(String solicitedSessionId, String solicitorLogin) throws Exception{
		Profile solicitedProfile = getUserProfile(solicitedSessionId);
		solicitedProfile = solicitedProfile.viewOtherProfile(LendMe.getUserByLogin(solicitorLogin));
		solicitedProfile.acceptFriendshipRequest();
	}
	
	/**
	 * Solicited user declines friendship request sent by solicitor user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitedSessionId the solicited session id
	 * @param solicitorLogin the solicitor login
	 * @throws Exception if users involved doesn't exists or if solicited user already declined request
	 */
	protected static void declineFriendship(String solicitedSessionId, String solicitorLogin) throws Exception{
		Profile solicitedProfile = getUserProfile(solicitedSessionId);
		solicitedProfile = solicitedProfile.viewOtherProfile(LendMe.getUserByLogin(solicitorLogin));
		solicitedProfile.declineFriendshipRequest();
	}
	
	/**
	 * Solicitor user breaks friendship with solicited user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitorSessionId the solicitor session id
	 * @param solicitedLogin the solicited login
	 * @throws Exception if users involved doesn't exists
	 */
	protected static void breakFriendship(String solicitorSessionId, String solicitedLogin) throws Exception{
		Profile solicitorProfile = getUserProfile(solicitorSessionId);
		solicitorProfile = solicitorProfile.viewOtherProfile(LendMe.getUserByLogin(solicitedLogin));
		solicitorProfile.breakFriendship();
	}

	/**
	 * Returns true if solicitor user is friend of solicited user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitorSessionId the solicitor session id
	 * @param solicitedUserLogin the solicited login
	 * @return true if users involved are friends
	 * @throws Exception if users involved doesn't exists or are not friends
	 */
	protected static boolean hasFriend(String solicitorSessionId, String solicitedUserLogin) throws Exception{
		Profile solicitorViewer = getUserProfile(solicitorSessionId);
		solicitorViewer = solicitorViewer.viewOtherProfile(LendMe.getUserByLogin(solicitedUserLogin));
		return solicitorViewer.isFriendOfOwner();
	}

	/**
	 * Gets the user friendship requests.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	protected static Set<User> getFriendshipRequests(String sessionId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.getOwnerFriendshipRequests();
	}

	/**
	 * Solicitor user sends a message to solicited user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param senderSessionId
	 * @param subject
	 * @param message
	 * @param receiverLogin
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	protected static String sendMessage(String senderSessionId, String subject, String message, 
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
	
	/**
	 * Solicitor user sends message to solicited user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param senderSessionId
	 * @param subject
	 * @param message
	 * @param receiverLogin
	 * @return
	 * @throws Exception
	 */
	protected static String sendMessage(String senderSessionId, String subject, String message, 
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
	
	/**
	 * Returns user with session specified by session id message topics.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param topicType
	 * @return
	 * @throws Exception
	 */
	protected static List<Topic> getTopics(String sessionId, String topicType)
			throws Exception {
		
		Profile solicitorViewer = getUserProfile(sessionId);
		return solicitorViewer.getTopics(topicType);
		
	}
	
	/**
	 * Returns user with session specified by sesison id topic messages.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param topicId
	 * @return
	 * @throws Exception
	 */
	protected static List<Message> getTopicMessages(String sessionId, String topicId)
			throws Exception {
		
		Profile solicitorViewer = getUserProfile(sessionId);
		return solicitorViewer.getTopicMessages(topicId);
	}
	
	/**
	 * Returns user lending history.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param kind
	 * @return
	 * @throws Exception
	 */
	protected static Collection<Lending> getLendingRecords(String sessionId, String kind) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.getLendingRecords(kind);
	}
	
	/**
	 * Solicited user requests an item from another user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param itemId
	 * @param requiredDays
	 * @return
	 * @throws Exception
	 */
	protected static String requestItem(String sessionId, String itemId, int requiredDays) throws Exception {
		Profile viewer = getUserProfile(sessionId);
		viewer = viewer.viewOtherProfile(getItemOwner(itemId));
		return viewer.requestItem(itemId, requiredDays);
	}
	
	/**
	 * Solicited user allows lending of item.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	protected static String approveLending(String sessionId, String requestId)  throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.approveLending(requestId);
	}

	/**
	 * Solicited user denies lending of item.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	protected static String denyLending(String sessionId, String requestId)  throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.denyLoan(requestId);
	}
	
	/**
	 * Solicitor user returns item back.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	protected static String returnItem(String sessionId, String requestId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.returnItem(requestId);
	}
	
	/**
	 * Lender confirms returning of item.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	protected static String confirmLendingTermination(String sessionId,
			String lendingId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.confirmLendingTermination(lendingId);
	}

	/**
	 * Lender denies returning of lending.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	protected static String denyLendingTermination(String sessionId,
			String lendingId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.denyLendingTermination(lendingId);
	}
	
	/**
	 * Lender asks his item back.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	protected static String askForReturnOfItem(String sessionId,
			String lendingId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.askForReturnOfItem(lendingId);
	}
	
	/**
	 * Returns the owner of the item.
	 * 
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	protected static User getItemOwner(String itemId) throws Exception{
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
	 * Returns the lending record with specific lending id.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	protected static Lending getLendingByLendingId(String lendingId) throws Exception{
		for ( User user : users ){
			Lending record = user.getLendingByLendingId(lendingId);
			if ( record != null ){
				return record;
			}
		}
		throw new Exception("Empréstimo inexistente");		
	}
	
	/**
	 * Returns lending record with specific request id.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	protected static Lending getLendingByRequestId(String requestId) throws Exception{
		for ( User user : users ){
			Lending record = user.getLendingByRequestId(requestId);
			if ( record != null ){
				return record;
			}
		}
		throw new Exception("Requisição de empréstimo inexistente");
	}

	/**
	 * Solicitor removes item from his item set.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param itemId
	 * @throws Exception
	 */
	protected static void deleteItem(String sessionId, String itemId) throws Exception {
		User userOwnerSession = getUserByLogin(getSessionByID(sessionId).getLogin());
		userOwnerSession.deleteMyItem(itemId);
	}

	/**
	 * Searches for specific with specific criteria and disposals.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param key
	 * @param attribute
	 * @param disposal
	 * @param criteria
	 * @return
	 * @throws Exception
	 */
	protected static List<Item> searchForItem(String sessionId, String key, String attribute,
			String disposal ,String criteria) throws Exception{
		
		List<Item> results = new ArrayList<Item>();
		Session session = getSessionByID(sessionId);
		User userOwnerSession = getUserByLogin(session.getLogin());
		AtributeForSearch atributeAux = AtributeForSearch.DESCRICAO;
		CriterionForSearch criterionAux = CriterionForSearch.DATACRIACAO;
		
		if(key == null || key.trim().isEmpty()){
			throw new Exception("Chave inválida");//"invalid key"
		}
		if(attribute == null || attribute.trim().isEmpty()){
			throw new Exception("Atributo inválido");
		}
		if(!Arrays.toString(AtributeForSearch.values()).toLowerCase().contains(attribute.toLowerCase())){
			throw new Exception("Atributo inexistente");
		}
		if(disposal == null || disposal.trim().isEmpty()){
			throw new Exception("Tipo inválido de ordenação");
		}
		if(!Arrays.toString(DispositionForSearch.values()).toLowerCase().contains(disposal.toLowerCase())){
			throw new Exception("Tipo de ordenação inexistente");
		}
		if(criteria == null || criteria.trim().isEmpty()){
			throw new Exception("Critério inválido de ordenação");
		}
		if(!Arrays.toString(CriterionForSearch.values()).toLowerCase().contains(criteria.toLowerCase())){
			throw new Exception("Critério de ordenação inexistente");
		}
		
		for(AtributeForSearch actual : AtributeForSearch.values()){
			if(actual.toString().toLowerCase().contains(attribute.toLowerCase()))
				atributeAux = actual;
		}
		
		for(CriterionForSearch actual : CriterionForSearch.values()){
			if(actual.toString().toLowerCase().contains(criteria.toLowerCase()))
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
			if (DispositionForSearch.CRESCENTE.toString().toLowerCase().contains(disposal.toLowerCase())) {
				return results;

			} else {
				Collections.reverse(results);
				return results;

			}
		}

		case REPUTACAO: {
			Collections.sort(results, new ComparatorOfItems());
			if (DispositionForSearch.CRESCENTE.toString().toLowerCase().contains(disposal.toLowerCase())) {
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
	
	/**
	 * Solicitor registers interest for a specific item.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param itemId
	 * @throws Exception
	 */
	protected static void registerInterestForItem(String sessionId, String itemId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		viewer = viewer.viewOtherProfile(getItemOwner(itemId));
		viewer.registerInterestForItem(itemId);
	}

	/**
	 * Returns messages with given topic.
	 * 
	 * @param topicId
	 * @return
	 * @throws Exception
	 */
	protected static List<Message> getMessagesByTopicId(String topicId) throws Exception{

		for ( User user : users ){
			List<Message> messages = user.getMessagesByTopicId(topicId);
			if ( messages != null ){
				return messages;
			}
		}
		throw new Exception("Tópico inexistente");//"Inexistent topic");
	}
	
	/**
	 * Get system user ranking.
	 * 
	 * @param sessionId
	 * @param category
	 * @return
	 * @throws Exception
	 */
	protected static String getRanking(String sessionId, String category) throws Exception{
		String ranking = "";
		
		if(sessionId == null || sessionId.trim().equals("")){
			throw new Exception("Sessão inválida");
		}
				
		if(getSessionByID(sessionId) == null){
			throw new Exception("Sessão inexistente");
		}
		
		if(category == null || category.trim().equals("")){
			throw new Exception("Categoria inválida");
		}
		
		if(!category.equals("global") && !category.equals("amigos")){
			throw new Exception("Categoria inexistente");
		}
		
		if(category.equals("amigos")){
			User user = getUserByLogin(getSessionByID(sessionId).getLogin());
			User[] friendList = user.getFriends().toArray(new User[user.getFriends().size() + 1]);
			friendList[user.getFriends().size()] = user;
			
			Arrays.sort(friendList, comparator);
			for(User current : friendList){
				ranking = current.getLogin() + "; " + ranking;
			}
		}
		if(category.equals("global")){
			User[] usersList = users.toArray(new User[users.size()]);
			
			Arrays.sort(usersList, comparator);
			for(User current : usersList){
				ranking = current.getLogin() + "; " + ranking;
			}
		}
		ranking = ranking + "-";
	
		return ranking.replace("; -", "");
	}
	
	/**
	 * Logged user receives access to some user info through his profile.
	 * 
	 * @param solicitorSessionId
	 * @param solicitedUserLogin
	 * @return
	 * @throws Exception
	 */
	protected static String viewProfile(String solicitorSessionId, 
			String solicitedUserLogin) throws Exception {
		Profile solicitorViewer = getUserProfile(solicitorSessionId);
		solicitorViewer = solicitorViewer.viewOtherProfile(LendMe.getUserByLogin(solicitedUserLogin));
		return solicitorViewer.toString();
	}
	
	/**
	 * Returns the user received item requests.
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	protected static Set<Lending> getReceivedItemRequests(String sessionId)
			throws Exception {
		Profile viewer = getUserProfile(sessionId);
		return viewer.getReceivedItemRequests();
	}
	
	protected static List<ActivityRegistry> getActivityHistory(String solicitorSessionId) throws Exception {
		Profile viewer = getUserProfile(solicitorSessionId);
		return viewer.getActivityHistory();
	}

	public static List<ActivityRegistry> getJointActivityHistory(
			String solicitorSessionId) throws Exception {
		
		Session session = getSessionByID(solicitorSessionId);
		User userOwnerSession = getUserByLogin(session.getLogin());
		List<ActivityRegistry> results = 
				new ArrayList<ActivityRegistry>(userOwnerSession.getMyActivityHistory());
		
		
		for (User actualFriend : userOwnerSession.getFriends()) {
			for (ActivityRegistry actualAR : actualFriend.getMyActivityHistory()) {
				if (actualAR.getKind() == ActivityKind.ADICAO_DE_AMIGO_CONCLUIDA 
						&& actualAR.getDescription().contains(" e " + 
								userOwnerSession.getName() + " são amigos agora")) {
					continue;
				}
				results.add(actualAR);
			}
		}
		
		Collections.sort(results);
		
		return results;
	}

	public static String publishItemRequest(String sessionId, String itemName,
			String itemDescription) throws Exception {
		Profile viewer = getUserProfile(sessionId);
		return viewer.publishItemRequest(itemName, itemDescription);
	}

	public static void offerItem(String sessionId,
			String requestPublicationId, String itemId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		Lending petition = LendMe.getPetition(requestPublicationId);
		viewer.offerItem(petition, itemId);
	}

	private static Lending getPetition(String requestPublicationId) throws Exception{
		for ( User user : users ){
			for ( Lending publishedRequest : user.getPublishedItemRequests() ){
				if ( publishedRequest.getID().equals(requestPublicationId) ){
					return publishedRequest;
				}
			}
		}
		throw new Exception("Esse pedido de item não foi publicado ou já foi atendido.");
	}
	
}