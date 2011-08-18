package entities;

import util.Address;

public class User {

	private String login;
	private String name;
	private Address address;

	public User(){}
	
	public User(String login, String name, Address address) {
		this.login = login;
		this.name = name;
		this.address = address;
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

	public boolean nameMatches(String string) {
		return this.name.toUpperCase().contains(string.toUpperCase());
	}
	
	@Override
	public int hashCode(){
		return this.name.hashCode() + this.login.hashCode() + this.address.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof User) ){
			return false;
		}
		
		User other = (User) obj;
		return this.hashCode() == other.hashCode();
	}
	
}
