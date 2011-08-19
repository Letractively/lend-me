package entities;

import java.util.HashSet;
import java.util.Set;

import entities.util.Address;
import entities.util.Category;


public class User {

	private String login;
	private String name;
	private Address address;
	private Set<Item> myItems = new HashSet<Item>(); 

	public User(){}
	
	public User(String login, String name, String street, String number, String neighborhood,
			String city, String state, String country, String zipCode) {
		this.login = login;
		this.name = name;
		this.address = new Address(street, number, neighborhood,
						city, state, country, zipCode);
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setAddress(String street, String number, String neighborhood,
			String city, String state, String country, String zipCode) {
		this.address = new Address(street, number, neighborhood, city, state, country, zipCode);
	}

	public Address getAddress() {
		return address;
	}

	public boolean nameMatches(String toBeMatched) {
		return this.name.toUpperCase().contains(toBeMatched.toUpperCase());
	}
	
	@Override
	public String toString(){
		StringBuilder userToString = new StringBuilder();
		userToString.append(this.name);
		userToString.append(this.login);
		userToString.append(this.address);
		return userToString.toString();
	}
	
	@Override
	public int hashCode(){
		return this.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof User) ){
			return false;
		}
		
		User other = (User) obj;
		return this.hashCode() == other.hashCode();
	}
	

	public boolean hasItem(Item item) {
		return myItems.contains(item);
	}

	public void addItem(String itemName, String description, Category category) {
		Item myNewItem = new Item(itemName, description, category);
		this.myItems.add(myNewItem);
	}

	public void requestFriendship(User otherUser) {
		// TODO Auto-generated method stub
		
	}

	public boolean hasFriend(User user1) {
		// TODO Auto-generated method stub
		return false;
	}

	public void acceptFriendshipRequest(User user0) {
		// TODO Auto-generated method stub
		
	}
	
}
