package entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import entities.util.Address;
import entities.util.Category;


public class User {

	private String login;
	private String name;
	private Address address;
	private Set<User> myFriends = new HashSet<User>();
	private Set<User> receivedFriendshipRequests = new HashSet<User>();
	private Set<User> sentFriendshipRequests = new HashSet<User>();
	private Map<Item,User> myItems = new HashMap<Item,User>();
	private Set<Lending> receivedItemRequests = new HashSet<Lending>();
	private Set<Lending> myBorrowedItems = new HashSet<Lending>();
	

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
		return myItems.containsKey(item);
	}

	public void addItem(String itemName, String description, Category category) {
		Item myNewItem = new Item(itemName, description, category);
		this.myItems.put(myNewItem, this);
	}

	public void requestFriendship(User otherUser) {
		this.sentFriendshipRequests.add(otherUser);
		otherUser.addFriendshipRequest(this);
	}

	private void addFriendshipRequest(User otherUser) {
		receivedFriendshipRequests.add(otherUser);
	}

	public void acceptFriendshipRequest(User otherUser) {
		if ( receivedFriendshipRequests.contains(otherUser) ){
			myFriends.add(otherUser);
			receivedFriendshipRequests.remove(otherUser);
		}
		otherUser.addRequestedFriend(this);
	}
	
	private void addRequestedFriend(User otherUser) {
		if ( sentFriendshipRequests.contains(otherUser) ){
			myFriends.add(otherUser);
			sentFriendshipRequests.remove(otherUser);
		}
	}
	
	public void declineFriendshipRequest(User otherUser){
		if ( receivedFriendshipRequests.contains(otherUser) ){
			receivedFriendshipRequests.remove(otherUser);
		}
		otherUser.removeRequestedFriend(this);
	}

	private void removeRequestedFriend(User otherUser) {
		if ( sentFriendshipRequests.contains(otherUser) ){
			sentFriendshipRequests.remove(otherUser);
		}
	}
	
	public boolean hasFriend(User otherUser) {
		return this.myFriends.contains(otherUser);
	}

	private boolean isLent(Item item) {
		return ! myItems.get(item).equals(this);
	}

	public void borrowItem(Item item, User lender, int days) {
		if (lender.hasItem(item)) {
			if (! lender.isLent(item)) {
				lender.requestItem(item, this, days);
			}
		}
	}

	private void requestItem(Item item, User borrower,  int days) {
		receivedItemRequests.add(new Lending(borrower, this, item, days));
	}


	public void lendItem(Item item, User borrower, int days) {
		if (myItems.containsKey(item)) {
			if (! this.isLent(item)) {
				myItems.put(item, borrower);
				if (receivedItemRequests.contains(new Lending(borrower, this, item, days))) {
					borrower.addRequestedItem(item, this, days);
				}
			}
		}
	}

	private void addRequestedItem(Item item, User lender, int days) {
			myBorrowedItems.add(new Lending(this, lender, item, days));
	}
	
	public void declineLendingItem(Item item, User otherUser, int days) {
		if (myItems.containsKey(item)) {
			if (! this.isLent(item)) {
				if (receivedItemRequests.contains(new Lending(otherUser, this, item, days))) {
					receivedItemRequests.remove(new Lending(otherUser, this, item, days));
				}
			}
		}
	}
	
	public boolean hasBorrowedItem(Item item) {
		for (Lending actual : myBorrowedItems){
			if(actual.getItem().equals(item)){
				return true;
			}
		}
		return false;
	}

	public void returnItem(Item item) {
		for(Lending actual : myBorrowedItems){
			if(actual.getItem().equals(item)){
				actual.getLender().setReturned(item);
				actual.setReturned(true);
			}
		}
		
	}

	private void setReturned(Item item) {
		for(Lending actual : receivedItemRequests){
			if(actual.getItem().equals(item)){
				actual.setReturned(true);
			}
		}
	}

	public void receiveLendedItem(Item item) {
		for(Lending actual : receivedItemRequests){
			if(actual.getItem().equals(item) && actual.isReturned()){
				this.receivedItemRequests.remove(actual);
				this.myItems.put(item, this);
				actual.getBorrower().removeBorrowedItem(item);
			}
		}
		
	}

	private void removeBorrowedItem(Item item) {
		for(Lending actual : myBorrowedItems){
			if(actual.getItem().equals(item)){
				this.myBorrowedItems.remove(actual);
			}
		}
	}
	
}