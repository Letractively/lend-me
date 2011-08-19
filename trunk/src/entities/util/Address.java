package entities.util;

public class Address {
	
	private String street;
	private String number;
	private String neighborhood;
	private String city;
	private String state;
	private String country;
	private String zipCode;
	
	public Address(String street, String number, String neighborhood,
			String city, String state, String country, String zipCode) {
		this.street = street;
		this.number = number;
		this.neighborhood = neighborhood;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipCode = zipCode;
	}

	public String getStreet() {
		return this.street;
	}

	public String getNumber() {
		return this.number;
	}

	public String getNeighborhood() {
		return this.neighborhood;
	}

	public String getCity() {
		return this.city;
	}

	public String getState() {
		return this.state;
	}

	public String getCountry() {
		return this.country;
	}

	public String getZipCode() {
		return this.zipCode;
	}
	
	@Override
	public String toString(){
		StringBuilder addressToString = new StringBuilder();
		addressToString.append(this.street);
		addressToString.append(this.number);
		addressToString.append(this.neighborhood);
		addressToString.append(this.city);
		addressToString.append(this.state);
		addressToString.append(this.country);
		addressToString.append(this.zipCode);
		return addressToString.toString();
	}

	@Override
	public int hashCode(){
		return this.toString().hashCode();
	}

	public boolean addressMatches(String toBeMatched) {
		return this.toString().toUpperCase().contains(toBeMatched.toUpperCase());
	}
	
}
