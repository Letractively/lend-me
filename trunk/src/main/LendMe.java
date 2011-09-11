package main;

import java.util.HashSet;
import java.util.Set;

import entities.Item;
import entities.Lending;
import entities.Profile;
import entities.Session;
import entities.User;

public class LendMe {

	public static Set<User> users = new HashSet<User>();
	public static Set<Session> sessions = new HashSet<Session>();
	
	public static void resetSystem(){
		users = new HashSet<User>();
		sessions = new HashSet<Session>();
	}
	
	public static String openSession(String login) throws Exception {
		if (login == null || login.trim().isEmpty()){
			throw new Exception("Login inválido");//"Invalid login");
		}
		LendMe.getUserByLogin(login);
		Session session = new Session(login);
		sessions.add(session);
		return session.getId();
	}

	public static void closeSession(String id) throws Exception{
		sessions.remove(getSessionByID(id));
	}
	
	public static void registerUser(String login, String name, String... address) throws Exception{
		if(!users.add(new User(login, name, address))){
			throw new Exception("Já existe um usuário com este login");//"User with this login already exists");
		}
	}
	
	public static String registerItem(String sessionId, String name, 
			String description, String category) throws Exception{
		
		if ( category == null || category.trim().isEmpty() ){
			throw new Exception("Categoria inválida");//"Invalid category");
		}
		
		User owner = getUserByLogin(getSessionByID(sessionId).getLogin());
		return owner.addItem(name, description, category);
	}
	
	public static Set<User> searchUsersByName(String name) {
		Set<User> foundUsers = new HashSet<User>();
		
		for ( User user : users ){
			if ( user.nameMatches(name) ){
				foundUsers.add(user);
			}
		}
		
		return foundUsers;
	}
	
	public static Set<User> searchUsersByAddress(String address) {
		Set<User> foundUsers = new HashSet<User>();
		
		for ( User user : users ){
			if ( user.getAddress().addressMatches(address) ){
				foundUsers.add(user);
			}
		}
		
		return foundUsers;
	}
	
	public static Set<Session> searchSessionsByLogin(String login) {
		Set<Session> results = new HashSet<Session>();
		for(Session actualSession : sessions){
			if(actualSession.getLogin().equals(login)){
				results.add(actualSession);
			}
		}
		return results;
	}

	public static Set<User> searchUsersByAttributeKey(String sessionId,
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
	
	public static Session getSessionByID(String id) throws Exception{
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
	
	public static String getUserAttribute(String login, String attribute)
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
	
	public static Profile getUserProfile(String sessionId) throws Exception {
		Session session = getSessionByID(sessionId);
		User user = getUserByLogin(session.getLogin());
		if (user == null) {
			throw new Exception("Sessão se refere a usuário desconhecido");// "Session belongs to unknown user");
		}
		return Profile.getUserProfile(session, user);
	}

	public static Set<User> getFriends(String sessionId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		Set<User> users = viewer.getOwnerFriends();
		return users;
	}

	public static Set<User> getFriends(String solicitorSessionId, String solicitedLogin) throws Exception{
		Profile solicitorViewer = LendMe.getUserProfile(solicitorSessionId);
		solicitorViewer = solicitorViewer.viewOtherProfile(LendMe.getUserByLogin(solicitedLogin));
		Set<User> users = solicitorViewer.getOwnerFriends();
		return users;
	}

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
	
	public static Set<Item> getItems(String sessionId) throws Exception {
		Profile viewer = LendMe.getUserProfile(sessionId);
		Set<Item> items = viewer.getOwnerItems();
		return items;
	}

	public static Set<Item> getItems(String observerSessionId, String ownerLogin) throws Exception {
		Profile viewer = LendMe.getUserProfile(observerSessionId);
		viewer = viewer.viewOtherProfile(LendMe.getUserByLogin(ownerLogin));
		Set<Item> items = viewer.getOwnerItems();
		return items;
	}
	
	public static void askForFriendship(String solicitorSessionId, String solicitedLogin) throws Exception{
		Profile solicitorProfile = getUserProfile(solicitorSessionId);
		solicitorProfile = solicitorProfile.viewOtherProfile(LendMe.getUserByLogin(solicitedLogin));
		solicitorProfile.askForFriendship();
	}
	
	public static void acceptFriendship(String solicitedSessionId, String solicitorLogin) throws Exception{
		Profile solicitedProfile = getUserProfile(solicitedSessionId);
		solicitedProfile = solicitedProfile.viewOtherProfile(LendMe.getUserByLogin(solicitorLogin));
		solicitedProfile.acceptFriendshipRequest();
	}
	
	public static void declineFriendship(String solicitedSessionId, String solicitorLogin) throws Exception{
		Profile solicitedProfile = getUserProfile(solicitedSessionId);
		solicitedProfile = solicitedProfile.viewOtherProfile(LendMe.getUserByLogin(solicitorLogin));
		solicitedProfile.declineFriendshipRequest();
	}
	
	public static void breakFriendship(String solicitorSessionId, String solicitedLogin) throws Exception{
		Profile solicitorProfile = getUserProfile(solicitorSessionId);
		solicitorProfile = solicitorProfile.viewOtherProfile(LendMe.getUserByLogin(solicitedLogin));
		solicitorProfile.breakFriendship();
	}

	public static boolean hasFriend(String solicitorSessionId, String solicitedUserLogin) throws Exception{
		Profile solicitorViewer = getUserProfile(solicitorSessionId);
		solicitorViewer = solicitorViewer.viewOtherProfile(LendMe.getUserByLogin(solicitedUserLogin));
		return solicitorViewer.isFriendOfOwner();
	}

	public static Set<User> getFriendshipRequests(String sessionId) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.getOwnerFriendshipRequests();
	}

	public static Set<Lending> getLendingRecords(String sessionId, String kind) throws Exception{
		Profile viewer = getUserProfile(sessionId);
		return viewer.getLendingRecords(kind);
	}
	
	
	public static String toApproveLoan(String sessionId, String requestId)  throws Exception{
		User userOwerSession = getUserByLogin(getSessionByID(sessionId).getLogin());
		Lending lending = userOwerSession.getLendingByRequestId(requestId);
		userOwerSession.lendItem(lending.getItem(), lending.getBorrower(), lending.getRequiredDays());
		return lending.getID();
	}

	public static String requestItem(String sessionId, String itemId, int requiredDays) throws Exception {
		User userOwerSession = getUserByLogin(getSessionByID(sessionId).getLogin());
		User lender = null;
		Item itemRequest = null;
		for(User actualFriend : userOwerSession.getFriends()){
			for(Item actualItem : actualFriend.getAllItems()){
				if(actualItem.getID().equals(itemId)){
					lender = actualFriend;
					itemRequest = actualItem;
				}
			}
		}
		return userOwerSession.borrowItem(itemRequest, lender, requiredDays);
	}

	public static String toReturnItem(String sessionId, String requestId) throws Exception{
		User userOwerSession = getUserByLogin(getSessionByID(sessionId).getLogin());
		Lending returnLending = null;
		for(Lending actualLending : userOwerSession.getMyBorrowedItems()){
			if(actualLending.getID().equals(requestId))
				returnLending = actualLending;
				break;
		}
		userOwerSession.returnItem(returnLending.getItem());
		
		return returnLending.getID();
	}
	
}