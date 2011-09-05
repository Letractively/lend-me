package main;

import java.util.HashSet;
import java.util.Set;

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

	public static void registerItem(String name, String description,
			Category category, User owner) {
		owner.addItem(name, description, category);
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
	
	public static User getUserByLogin(String login) throws Exception{
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

	public static Set<Session> getSessionByUser(String login) {
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
		receiver.receiveMessage(subject, message, sender, true);
	}
	
	private static Session getSessionById(String id) {
		for(Session actualSession : sessions){
			if (actualSession.getId().equals(id)) {
				return actualSession;
			}
		}
		return null;
	}
	
}