package main;

import java.util.HashSet;
import java.util.Set;

import entities.Session;
import entities.User;
import entities.util.Category;

public class LendMe {

	public static Set<User> users = new HashSet<User>();
	public static Set<Session> sessions = new HashSet<Session>();
	
	public static void registerUser(String login, String name, String... address) throws Exception{
		if(!users.add(new User(login, name, address))){
			throw new Exception("User with this login already exists");
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
		solicitor.requestFriendship(solicited);
	}
	
	public static void acceptFriendship(User solicitor, User solicited){
		solicitor.acceptFriendshipRequest(solicited);
	}
	
	public static void declineFriendship(User solicitor, User solicited){
		solicitor.declineFriendshipRequest(solicited);
	}

	
	public static User getUserByLogin(String login) {
		for(User actualUser : users){
			if(actualUser.getLogin().equals(login)){
				return actualUser;
			}
		}
		return null;
	}

	public static void openSession(String login) throws Exception {
		if(LendMe.getUserByLogin(login) == null){
			throw new Exception("User does not exist");
		}
		sessions.add(new Session(login));
	}


	public static Session getSessionByUser(String login) {
		for(Session actualSession : sessions){
			if(actualSession.getLogin().equals(login)){
				//TODO Se ligar q esta retornando apenas o primeiro
				return actualSession;
			}
		}
		return null;
	}
	

	
}