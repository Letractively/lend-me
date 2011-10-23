package com.lendme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lendme.entities.ActivityRegistry;
import com.lendme.entities.ActivityRegistry.ActivityKind;
import com.lendme.entities.Item;
import com.lendme.entities.Lending;
import com.lendme.entities.Message;
import com.lendme.entities.Session;
import com.lendme.entities.Topic;
import com.lendme.entities.User;
import com.lendme.utils.ComparatorOfAddressStrategy;
import com.lendme.utils.ComparatorOfDateStrategy;

/**
 * @author THE LENDERS
 * The System.
 * Here is where the created Users and open Sessions are located. It also keeps track of the system time.
 * In fact, it contains most of the Business Logic, and is the access point to User and Profile logic.
 */

public class LendMe {

	private Calendar time;
	private LendMeRepository repository;
	private LendMeItemModule itemModule;
	private LendMeCommunicationModule  communicationModule; 
	private LendMeUserModule userModule;
	public static enum AtributeForSearch {DESCRICAO, NOME, ID, CATEGORIA};
	public static enum DispositionForSearch {CRESCENTE, DECRESCENTE};
	public static enum CriterionForSearch {DATACRIACAO, REPUTACAO};
	
	
	public LendMe() {
		time = GregorianCalendar.getInstance();
		repository = LendMeRepository.getInstance();
		userModule = new LendMeUserModule();
		itemModule = new LendMeItemModule();
		communicationModule = new LendMeCommunicationModule();
	}
	
	/**
	 * Resets the whole system: all living sessions are shutdown as well as all users are deleted.
	 * Also, the system clock is set to same as the OS time.
	 * 
	 * <i>This method belongs to the public system interface</i>
	 */
	public void resetSystem(){
		repository.resetRepository();
		time = GregorianCalendar.getInstance();
	}
	
	/**
	 * Opens a new session for the user specified by login.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param login the user login
	 * @return the session id
	 * @throws Exception for invalid parameters and if user doesn't exists
	 */
	public String openSession(String login) throws Exception {
		return repository.openSession(login);
	}
	
	/**
	 * Returns the system date.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @return the system time
	 */
	public Date getSystemDate() {
		return time.getTime();
	}
	
	/**
	 * Simulates that a specified amount of days have passed.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param amount the amount of days
	 * @return the string representing the new system time
	 */
	public String someDaysPassed(int amount){
		time.add(Calendar.DAY_OF_MONTH, amount+1);
		return time.getTime().toString();
	}
	
	/**
	 * Closes the specified session.
	 * 
	 * <i>This method belongs to the public system interface </i>
	 * @param id the session id
	 * @throws Exception for inexistent sessions
	 */
	public void closeSession(String id) throws Exception{
		repository.closeSession(id);
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
	public String registerUser(String login, String name, String... address) throws Exception{
		return repository.registerUser(login, name, address);
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
	public  String registerItem(String sessionId, String name, 
			String description, String category) throws Exception{

		return repository.registerItem(sessionId, name, description, category);
	}
	
	public  List<User> listUsersByDistance(String sessionId) throws Exception{
		
		if(sessionId == null || sessionId.trim().equals("")) throw new Exception("Sessão inválida");
				
		
		List<User> listUsersByDistance = new ArrayList<User>();
		listUsersByDistance.addAll(repository.getUsers());
		Collections.sort(listUsersByDistance, new ComparatorOfDateStrategy());
		
		User ownerOfSession = repository.getUserBySessionId(sessionId);
		
		if(ownerOfSession == null) throw new Exception("Sessão inexistente");
		
		listUsersByDistance.remove(ownerOfSession);
		Collections.sort(listUsersByDistance, new ComparatorOfAddressStrategy(ownerOfSession.getAddress()));
		
		
		return listUsersByDistance;
	}

	/**
	 * Searches for users with given attribute specified by key.
	 * @param sessionId the id of a session of the user who is requiring a search
	 * @param key the search key-value
	 * @param attribute the kind of attribute to guide the search
	 * @return a set of users found by search
	 * @throws Exception if attribute key or kind is invalid or if kind does not exists
	 */
	public  Set<User> searchUsersByAttributeKey(String sessionId,
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
		for ( User user : repository.getUsers() ){
			if ( getUserProfile(sessionId).getObserver().getOwner().equals(user) ){
				continue;
			}
			if ( attribute.equals("nome") ){
				if ( getUserAttribute(user, "nome").contains(key) ){
					results.add(user);
				}
			}
			else if ( attribute.equals("login") ){
				if ( ( getUserAttribute(user, "login").contains(key) ) ){
					results.add(user);
				}
			}
			else if ( attribute.equals("endereco") ){
				if ( ( getUserAttribute(user, "endereco").contains(key) ) ){
					results.add(user);
				}
			}
		}
		return results;
	}
	
	public  String getUserAttribute(String login, String attribute)
			throws Exception{
		return getUserAttribute(repository.getUserByLogin(login), attribute);
	}
	
	/**
	 * Returns some user attribute.
	 * @param login the user login
	 * @param attribute the user attribute kind
	 * @return the user attribute value
	 * @throws Exception if attribute or login is invalid
	 */
	public  String getUserAttribute(User user, String attribute)
			throws Exception{
		
		if ( attribute == null || attribute.trim().isEmpty() ){
			throw new Exception("Atributo inválido");//"Invalid attribute");
		}
		if (!(attribute.equals("nome") || attribute.equals("endereco") || attribute.equals("login") )){
			throw new Exception("Atributo inexistente");//"Inexistent attribute");
		}
		
		if(attribute.equals("nome")){
			return user.getName();
		}
		else if ( attribute.equals("login") ){
			return user.getLogin();
		}
		else{
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
	public  String getItemAttribute(String itemId, String attribute) throws Exception{
		
		String ownerSessionId = repository.searchSessionsByLogin(getItemOwner(itemId).getLogin())
				.iterator().next().getId();
		
		Profile viewerProfile = getUserProfile(ownerSessionId);
		return itemModule.getItemAttribute(itemId, attribute, ownerSessionId, viewerProfile);
	}

	/**
	 * Retrieves a profile for user with existing session specified by session id.
	 * @param sessionId the session id
	 * @return a profile
	 * @throws Exception if user doesn't exists or there is no alive session for that user
	 */
	public  Profile getUserProfile(String sessionId) throws Exception {
		Session session = repository.getSessionByID(sessionId);
		return userModule.getUserProfile(session);
	}

	/**
	 * Returns the friends of user with existing session specified by session id.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId the session id
	 * @return a set of friends
	 * @throws Exception if user doesn't exists
	 */
	public  Set<User> getFriends(String sessionId) throws Exception{
		return userModule.getFriends(getUserProfile(sessionId));
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
	public  Set<User> getFriends(String solicitorSessionId, String solicitedLogin) throws Exception{
		return userModule.getFriends(getUserProfile(solicitorSessionId), repository.getUserByLogin(solicitedLogin));
	}

	/**
	 * Returns the items of the user with existing session specified by session id.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId the session id
	 * @return a set of items
	 * @throws Exception if user doesn't exists
	 */
	public  Set<Item> getItems(String sessionId) throws Exception {
		Profile viewer = userModule.getUserProfile(repository.getSessionByID(sessionId));
		return itemModule.getItems(viewer);
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
	public  Set<Item> getItems(String observerSessionId, String ownerLogin) throws Exception {
		Profile viewer = userModule.getUserProfile(repository.getSessionByID(observerSessionId));
		viewer = viewer.viewOtherProfile(repository.getUserByLogin(ownerLogin));
		return itemModule.getItems(viewer);
	}
	
	/**
	 * Solicitor user sends a friendship request to solicited user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitorSessionId the solicitor session id
	 * @param solicitedLogin the solicited login
	 * @throws Exception if users involved doesn't exists or friendship request was already sent
	 */
	public  void askForFriendship(String solicitorSessionId, String solicitedLogin) throws Exception{
		userModule.askForFriendship(getUserProfile(solicitorSessionId), repository.getUserByLogin(solicitedLogin));
	}

	/**
	 * Solicited user accepts friendship request sent by solicitor user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitedSessionId the solicited session id
	 * @param solicitorLogin the solicitor login
	 * @throws Exception if users involved doesn't exists or friendship request was already accepted
	 */
	public  void acceptFriendship(String solicitedSessionId, String solicitorLogin) throws Exception{
		userModule.acceptFriendship(getUserProfile(solicitedSessionId), repository.getUserByLogin(solicitorLogin));
	}
	
	/**
	 * Solicited user declines friendship request sent by solicitor user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitedSessionId the solicited session id
	 * @param solicitorLogin the solicitor login
	 * @throws Exception if users involved doesn't exists or if solicited user already declined request
	 */
	public  void declineFriendship(String solicitedSessionId, String solicitorLogin) throws Exception{
		userModule.declineFriendship(getUserProfile(solicitedSessionId), repository.getUserByLogin(solicitorLogin));
	}
	
	/**
	 * Solicitor user breaks friendship with solicited user.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param solicitorSessionId the solicitor session id
	 * @param solicitedLogin the solicited login
	 * @throws Exception if users involved doesn't exists
	 */
	public  void breakFriendship(String solicitorSessionId, String solicitedLogin) throws Exception{
		userModule.breakFriendship(getUserProfile(solicitorSessionId), repository.getUserByLogin(solicitedLogin));
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
	public  boolean hasFriend(String solicitorSessionId, String solicitedLogin) throws Exception{
		return userModule.hasFriend(getUserProfile(solicitorSessionId), repository.getUserByLogin(solicitedLogin));
	}

	/**
	 * Gets the user friendship requests.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public  Set<User> getFriendshipRequests(String sessionId) throws Exception{
		return userModule.getOwnerFriendshipRequests(getUserProfile(sessionId));
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
	public  String sendMessage(String senderSessionId, String subject, String message, 
			String receiverLogin, String lendingId) throws Exception {
			
			Session senderSession = null;
			User receiver = null;
			
			try {
				senderSession = repository.getSessionByID(senderSessionId);
				receiver = repository.getUserByLogin(receiverLogin);
			} catch (Exception e) {
				
				if (e.getMessage().equals("Login inválido")) {
					throw new Exception("Destinatário inválido");//"Invalid receiver");
				}
				
				else if (e.getMessage().equals("Usuário inexistente")) {
					throw new Exception("Destinatário inexistente");//"Inexistent receiver");
				}
				
				else if (e.getMessage().equals("Empréstimo inexistente")) {
					throw new Exception("Requisição de empréstimo inexistente" );
				}
				
				throw e;
			}

			return communicationModule.sendMessage(senderSession, subject, message, receiver, lendingId);
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
	public  String sendMessage(String senderSessionId, String subject, String message, 
			String receiverLogin) throws Exception {
			
			Session senderSession = null;
			User receiver = null;
			try {
				senderSession = repository.getSessionByID(senderSessionId);
				receiver = repository.getUserByLogin(receiverLogin);
			} catch (Exception e) {
				
				if (e.getMessage().equals("Login inválido")) {
					throw new Exception("Destinatário inválido");//"Invalid receiver");
				}
				
				else if (e.getMessage().equals("Usuário inexistente")) {
					throw new Exception("Destinatário inexistente");//"Inexistent receiver");
				}else{
					throw new Exception(e.getMessage());
				}
				
//				else if(e.getMessage().equals("Sessão inválida")){
//					throw new Exception();
//				}else if(e.getMessage().equals("Sessão inexistente")){
//					throw new Exception();
//				}
			}
			return communicationModule.sendMessage(senderSession, subject, message, receiver);
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
	public  List<Topic> getTopics(String sessionId, String topicType)
			throws Exception {
			return communicationModule.getTopics(repository.getSessionByID(sessionId), topicType);
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
	public  List<Message> getTopicMessages(String sessionId, String topicId)
			throws Exception {

			return communicationModule.getTopicMessages(repository.getSessionByID(sessionId), topicId);
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
	public  Collection<Lending> getLendingRecords(String sessionId, String kind) throws Exception{
		Profile viewer = userModule.getUserProfile(repository.getSessionByID(sessionId));
		return itemModule.getLendingRecords(viewer, kind);
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
	public  String requestItem(String sessionId, String itemId, int requiredDays) throws Exception {
		Profile viewer = userModule.getUserProfile(repository.getSessionByID(sessionId));
		return itemModule.requestItem(viewer, itemId, requiredDays, repository.getUsers());
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
	public  String approveLending(String sessionId, String requestId)  throws Exception{
		Profile viewer = userModule.getUserProfile(repository.getSessionByID(sessionId));
		return itemModule.approveLending(viewer, requestId);
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
	public  String denyLending(String sessionId, String requestId)  throws Exception{
		Profile viewer = userModule.getUserProfile(repository.getSessionByID(sessionId));
		return itemModule.denyLending(viewer, requestId);
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
	public  String returnItem(String sessionId, String requestId) throws Exception{
		Profile viewer = userModule.getUserProfile(repository.getSessionByID(sessionId));
		return itemModule.returnItem(viewer, requestId);
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
	public  String confirmLendingTermination(String sessionId,
			String lendingId) throws Exception{
		Profile viewer = userModule.getUserProfile(repository.getSessionByID(sessionId));
		return itemModule.confirmLendingTermination(viewer, lendingId);
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
	public  String denyLendingTermination(String sessionId,
			String lendingId) throws Exception{
		Profile viewer = userModule.getUserProfile(repository.getSessionByID(sessionId));
		return itemModule.denyLendingTermination(viewer, lendingId);
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
	public  String askForReturnOfItem(String sessionId,
			String lendingId) throws Exception{
		Profile viewer = userModule.getUserProfile(repository.getSessionByID(sessionId));
		return itemModule.askForReturnOfItem(viewer, lendingId, getSystemDate());
	}
	
	/**
	 * Returns the owner of the item.
	 * 
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	public User getItemOwner(String itemId) throws Exception{
		return itemModule.getItemOwner(itemId, repository.getUsers());
	}
	
	/**
	 * Returns the lending record with specific lending id.
	 * @param lendingId
	 * @return
	 * @throws Exception
	 */
	public  Lending getLendingByLendingId(String lendingId) throws Exception{
		return itemModule.getLendingByLendingId(lendingId, repository.getUsers());		
	}
	
	/**
	 * Returns lending record with specific request id.
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	public  Lending getLendingByRequestId(String requestId) throws Exception{
		return itemModule.getLendingByRequestId(requestId, repository.getUsers());
	}

	/**
	 * Solicitor removes item from his item set.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param itemId
	 * @throws Exception
	 */
	public  void deleteItem(String sessionId, String itemId) throws Exception {
		User ownerUser = repository.getSessionByID(sessionId).getOwner();
		itemModule.deleteItem(ownerUser, itemId);
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
	public  List<Item> searchForItem(String sessionId, String key, String attribute,
			String disposal ,String criteria) throws Exception{
		User ownerUser = repository.getSessionByID(sessionId).getOwner();
		return itemModule.searchForItem(ownerUser, key, attribute, disposal, criteria);
	}
	
	/**
	 * Solicitor registers interest for a specific item.
	 * 
	 * <i>This method belongs to the public system interface<i>
	 * @param sessionId
	 * @param itemId
	 * @throws Exception
	 */
	public  void registerInterestForItem(String sessionId, String itemId) throws Exception{
		Profile viewer = userModule.getUserProfile(repository.getSessionByID(sessionId));
		itemModule.registerInterestForItem(viewer, itemId, repository.getUsers());
	}

	/**
	 * Returns messages with given topic.
	 * 
	 * @param topicId
	 * @return
	 * @throws Exception
	 */
	public  List<Message> getMessagesByTopicId(String topicId) throws Exception{
			return communicationModule.getMessagesByTopicId(topicId, repository.getUsers());

	}
	
	/**
	 * Get system user ranking.
	 * 
	 * @param sessionId
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public  String getRanking(String sessionId, String category) throws Exception{
		Session actualSession = repository.getSessionByID(sessionId);
		return itemModule.getRanking(category, actualSession, repository.getUsers());
	}
	
	/**
	 * Logged user receives access to some user info through his profile.
	 * 
	 * @param solicitorSessionId
	 * @param solicitedUserLogin
	 * @return
	 * @throws Exception
	 */
	public  String viewProfile(String solicitorSessionId, 
			String solicitedUserLogin) throws Exception {
		Profile solicitorViewer = getUserProfile(solicitorSessionId);
		solicitorViewer = solicitorViewer.viewOtherProfile(repository.getUserByLogin(solicitedUserLogin));
		return solicitorViewer.toString();
	}
	
	/**
	 * Returns the user received item requests.
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public  Set<Lending> getReceivedItemRequests(String sessionId)
			throws Exception {
		Profile viewer = userModule.getUserProfile(repository.getSessionByID(sessionId));
		return itemModule.getReceivedItemRequests(viewer);
	}
	
	public  List<ActivityRegistry> getActivityHistory(String solicitorSessionId) throws Exception {
		Profile viewer = getUserProfile(solicitorSessionId);
		return viewer.getActivityHistory();
	}

	public List<ActivityRegistry> getJointActivityHistory(
			String solicitorSessionId) throws Exception {
		
		Session session = repository.getSessionByID(solicitorSessionId);
		User userOwnerSession = session.getOwner();
		List<ActivityRegistry> results = 
				new ArrayList<ActivityRegistry>(userOwnerSession.getMyActivityHistory());
		
		
		for (User actualFriend : userOwnerSession.getFriends()) {
			for (ActivityRegistry actualAR : actualFriend.getMyActivityHistory()) {
				if (actualAR.getKind() == ActivityKind.ADICAO_DE_AMIGO_CONCLUIDA 
						&& actualAR.getDescription().contains(" e " + 
						userOwnerSession.getName() + " são amigos agora")
						|| userOwnerSession.getMyActivityHistory().contains(actualAR)) {
					continue;
				}
				results.add(actualAR);
			}
		}
		
		Collections.sort(results);
		
		return results;
	}

	public String publishItemRequest(String sessionId, String itemName,
			String itemDescription) throws Exception {
		Profile viewer = getUserProfile(sessionId);
		return viewer.publishItemRequest(itemName, itemDescription);
	}

	public void offerItem(String sessionId,
			String requestPublicationId, String itemId) throws Exception{
		if ( requestPublicationId == null || requestPublicationId.trim().isEmpty() ){
			throw new Exception("Identificador da publicação de pedido é inválido");
		}
		if ( itemId == null || itemId.trim().isEmpty() ){
			throw new Exception("Identificador do item é inválido");
		}
		Profile viewer = getUserProfile(sessionId);
		Lending petition = getPetition(requestPublicationId);
		viewer.offerItem(petition, itemId);
	}

	private Lending getPetition(String requestPublicationId) throws Exception{
		for ( User user : repository.getUsers() ){
			for ( Lending publishedRequest : user.getPublishedItemRequests() ){
				if ( publishedRequest.getID().equals(requestPublicationId) ){
					return publishedRequest;
				}
			}
		}
		throw new Exception("Publicação de pedido inexistente");
	}

	public void republishItemRequest(String sessionId,
			String requestPublicationId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		viewer.republishItemRequest(getPetition(requestPublicationId));
	}
}