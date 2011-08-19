package util;

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
	public int hashCode(){
		StringBuilder addressHashCode = new StringBuilder();
		addressHashCode.append(this.street);
		addressHashCode.append(this.number);
		addressHashCode.append(this.neighborhood);
		addressHashCode.append(this.city);
		addressHashCode.append(this.state);
		addressHashCode.append(this.country);
		addressHashCode.append(this.zipCode);
		return addressHashCode.toString().hashCode();
	}
	
}
