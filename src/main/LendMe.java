package main;

import java.util.HashSet;
import java.util.Set;

import entities.User;
import entities.util.Category;

public class LendMe {

	public static Set<User> users = new HashSet<User>();

	public static void registerUser(String login, String name,
			String street, String number, String neighborhood, String city,
			String state, String country, String zipCode) {
		
		users.add(new User(login, name, street, number, neighborhood,
						   city, state, country, zipCode));
		
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
	
}