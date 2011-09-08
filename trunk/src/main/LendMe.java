package main;

import java.util.HashSet;
import java.util.Set;

import entities.Item;
import entities.Profile;
import entities.Session;
import entities.User;
import entities.util.Category;

public class LendMe {

	public static Set<User> users = new HashSet<User>();
	public static Set<Session> sessions = new HashSet<Session>();
	
	public static void resetSystem(){
		users = new HashSet<User>();
		sessions = new HashSet<Session>();
	}
	
	public static void registerUser(String login, String name, String... address) throws Exception{
		if(!users.add(new User(login, name, address))){
			throw new Exception("Já existe um usuário com este login");//"User with this login already exists");
		}
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

	public static String registerItem(String sessionId, String name, 
			String description, String category) throws Exception{
		
		if ( category == null || category.trim().isEmpty() ){
			throw new Exception("Categoria inválida");//"Invalid category");
		}
		Category chosenCategory;
		try {
			chosenCategory = Category.valueOf(category.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			throw new Exception("Categoria inexistente");//"Inexistent category");
		}
		
		User owner = getUserByLogin(getSessionById(sessionId).getLogin());
		return registerItem(name, description, chosenCategory, owner);
	}
	
	public static String registerItem(String name, String description,
			Category category, User owner) throws Exception{
		return owner.addItem(name, description, category);
	}
	
	public static void askForFriendship(User solicitor, User solicited){
		//TODO maybe check if solicitor has an open session or receive the session as a parameter and
		//get the solicitor user using getUserByLogin()
		solicitor.requestFriendship(solicited);
	}
	
	public static void acceptFriendship(User solicited, User solicitor){
		//TODO maybe check if solicited has an open session or receive the session as a parameter and
		//get the solicited user using getUserByLogin()
		solicited.acceptFriendshipRequest(solicitor);
	}
	
	public static void declineFriendship(User solicited, User solicitor){
		//TODO maybe check if solicited has an open session or receive the session as a parameter and
		//get the solicited user using getUserByLogin()
		solicited.declineFriendshipRequest(solicitor);
	}
	
	public static void removeFriend(User solicitor, User solicited){
		solicitor.removeFriend(solicited);
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

	public static String openSession(String login) throws Exception {
		if (login == null || login.trim().isEmpty()){
			throw new Exception("Login inválido");//"Invalid login");
		}
		LendMe.getUserByLogin(login);
		Session session = new Session(login);
		sessions.add(session);
		return session.getId();
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
	
	public static void sendMessage(String sessionId, String subject, String message, 
			User sender, User receiver) throws Exception {
		if (getSessionById(sessionId) == null) {
			throw new Exception("Sessão inexistente");//"Inexistent session");
		}
		receiver.receiveMessage(subject, message, sender, true, "");
	}
	
	public static Session getSessionById(String id) throws Exception{
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
	
	public static void sendMessage(String sessionId, String subject, String message, 
			User sender, User receiver, String lendingId) throws Exception {
		if (getSessionById(sessionId) == null) {
			throw new Exception("Sessão inexistente");//"Inexistent session");
		}
		receiver.receiveMessage(subject, message, sender, true, lendingId);
	}
	
	public static void borrowItem(String sessionId, Item item, User borrower, 
			User lender, int days) throws Exception{
		if (getSessionById(sessionId) == null) {
			throw new Exception("Sessão inexistente");//"Inexistent session");
		}
		
		borrower.borrowItem(item, lender, days);
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
		Session session = getSessionById(sessionId);
		User user = getUserByLogin(session.getLogin());
		if (user == null) {
			throw new Exception("Sessão se refere a usuário desconhecido");// "Session belongs to unknown user");
		}
		return Profile.getUserProfile(session, user);
	}
	
	public static void closeSession(String id) throws Exception{
		sessions.remove(getSessionById(id));
	}

	public static void requestFriendShip(String idSessao, String login) throws Exception{
		
		User userApplicant = null;
		User userRequired  = null;
		
		for(Session actualSession : sessions){
			if(actualSession.getId().equals(idSessao)){
				for(User actualUser : users){
					if(actualUser.getLogin().equals(actualSession.getLogin())){
						userApplicant = actualUser;
					}
					else if(actualUser.getLogin().equals(login)){
						userRequired = actualUser;
					}
				}
				if(userRequired == null)
					throw new Exception("User inexistent");
			}
		}
		
		userApplicant.requestFriendship(userRequired);
		
	}

	public static void acceptFriendshipRequeried(String idSessao, String login) throws Exception{
		
		User userApplicant = null;
		User userRequired  = null;
		
		for(Session actualSession : sessions){
			if(actualSession.getId().equals(idSessao)){
				for(User actualUser : users){
					if(actualUser.getLogin().equals(actualSession.getLogin())){
						userApplicant = actualUser;
					}
					else if(actualUser.getLogin().equals(login)){
						userRequired = actualUser;
					}
				}
				if(userRequired == null)
					throw new Exception("User inexistent");
			}
		}
		
		LendMe.acceptFriendship(userApplicant, userRequired);
	}

	public static void declineFriendshipFacade(String idSessao, String login) throws Exception{
		
		User userApplicant = null;
		User userRequired  = null;
		
		for(Session actualSession : sessions){
			if(actualSession.getId().equals(idSessao)){
				for(User actualUser : users){
					if(actualUser.getLogin().equals(actualSession.getLogin())){
						userApplicant = actualUser;
					}
					else if(actualUser.getLogin().equals(login)){
						userRequired = actualUser;
					}
				}
				if(userRequired == null)
					throw new Exception("User inexistent");
			}
		}
		
		LendMe.removeFriend(userRequired, userApplicant);
		
	}

	public static boolean isFriend(String idSessao, String login) throws Exception{
		
		User userApplicant = null;
		User userRequired  = null;
		
		for(Session actualSession : sessions){
			if(actualSession.getId().equals(idSessao)){
				for(User actualUser : users){
					if(actualUser.getLogin().equals(actualSession.getLogin())){
						userApplicant = actualUser;
					}
					else if(actualUser.getLogin().equals(login)){
						userRequired = actualUser;
					}
				}
				if(userRequired == null)
					throw new Exception("User inexistent");
			}
		}
		
		return userApplicant.hasFriend(userRequired);
	}

	public static Set<User> getFriends(String idSessao) throws Exception{
		
		for(Session actualSession : sessions){
			if(actualSession.getId().equals(idSessao)){
				for(User actualUser : users){
					if(actualUser.getLogin().equals(actualSession.getLogin())){
						return actualUser.getFriends();
					}
				}
			}
		}
		return null;
	}

	public static Set<User> getFriends(String idSessao, String login) throws Exception{
		
		for(Session actualSession : sessions){
			if(actualSession.getId().equals(idSessao)){
				for(User actualUser : users){
					if(actualUser.getLogin().equals(actualSession.getLogin())){
						for(User friend : actualUser.getFriends()){
							if(friend.getLogin().equals(login)){
								return friend.getFriends();
							}
						}
					}
				}
			}
		}
		
		return null;
	}
	
}