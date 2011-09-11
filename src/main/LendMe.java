package main;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entities.Item;
import entities.Lending; //Needed because it is necessary to retrieve a record among all lending records of the system
import entities.Profile; //Needed because for every operation the perspective of observer and profile owner is needed
import entities.Session; //Needed for obvious reasons: every operation requires a living session
import entities.User;
import entities.util.EntitiesConstants;
import entities.util.Message;
import entities.util.Topic;

public class LendMe {

	public static Set<User> users = new HashSet<User>();
	public static Set<Session> sessions = new HashSet<Session>();
	
	/**
	 * This method belongs to the public system interface
	 */
	public static void resetSystem(){
		users = new HashSet<User>();
		sessions = new HashSet<Session>();
	}
	
	/**
	 * This method belongs to the public system interface
	 * @param login
	 * @return
	 * @throws Exception
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
	 * This method belongs to the public system interface
	 * @param id
	 * @throws Exception
	 */
	public static void closeSession(String id) throws Exception{
		sessions.remove(getSessionByID(id));
	}
	
	/**
	 * This method belongs to the public system interface
	 * @param login
	 * @param name
	 * @param address
	 * @throws Exception
	 */
	public static void registerUser(String login, String name, String... address) throws Exception{
		if(!users.add(new User(login, name, address))){
			throw new Exception("Já existe um usuário com este login");//"User with this login already exists");
		}
	}
	
	/**
	 * This method belongs to the public system interface
	 * @param sessionId
	 * @param name
	 * @param description
	 * @param category
	 * @return
	 * @throws Exception
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
		
		Session senderSession = getSessionByID(senderSessionId);
		
		if (getSessionByID(senderSessionId) == null) {
			throw new Exception("SessÃ£o inexistente");//"Inexistent session");
		}
		return getUserByLogin(senderSession.getLogin()).sendMessage(subject, message, 
				getUserByLogin(receiverLogin), lendingId);
	}
	
	public static List<Topic> getTopics(String sessionId, String topicType)
			throws Exception {
		
		String userLogin = getLoginBySessionId(sessionId);
		
		if (topicType.equals("negociacao")) {
			topicType = EntitiesConstants.NEGOTIATION_TOPIC;
		} else if (topicType.equals("offtopic")) {
			topicType = EntitiesConstants.OFF_TOPIC;
		} else {
			topicType = EntitiesConstants.ALL_TOPICS;
		}
		
		return getUserByLogin(userLogin).getTopics(topicType);
	}

	public static List<Message> getTopicMessages(String sessionId, String topicId)
			throws Exception {
		
		String userLogin = getLoginBySessionId(sessionId);
		
		return getUserByLogin(userLogin).getMessagesByTopicId(topicId);
	}
	
	private static String getLoginBySessionId(String sessionId) throws Exception {
		
		Session session = getSessionByID(sessionId);
		
		if (session == null) {
			throw new Exception("SessÃ£o inexistente");//"Inexistent session");
		}
		return session.getLogin();
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
	private static User getItemOwner(String itemId) throws Exception{
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
	
	
	
}