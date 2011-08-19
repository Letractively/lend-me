package main;

import java.util.HashSet;
import java.util.Set;

import util.Address;
import entities.User;

public class LendMe {

	public static Set<User> users = new HashSet<User>();

	public static void registerUser(String login, String name,
			String street, String number, String neighborhood, String city,
			String state, String country, String zipCode) {
		
		users.add(new User(login, name, new Address(street, number, neighborhood,
						   city, state, country, zipCode)));
		
	}

	public static Set<User> searchUsersByName(String value) {

		Set<User> foundUsers = new HashSet<User>();
		
		for ( User user : users ){
			if ( user.nameMatches(value) ){
				foundUsers.add(user);
			}
		}
		
		return foundUsers;
	}
	
}