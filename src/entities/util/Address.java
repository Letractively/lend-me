package entities.util;

import java.util.HashMap;
import java.util.Map;

public class Address {

		private enum AddressElements {
			STREET, NUMBER, NEIGHBORHOOD, CITY, STATE, COUNTRY, ZIPCODE,FULL_ADDRESS;
		}
        
		private Map<AddressElements, String> address = new HashMap<AddressElements, String>();
		
        public Address(String... addressInfo) {
        	if (addressInfo.length == 1) {
        		for(int i = 0; i < addressInfo.length - 1; i++ ){
            		this.address.put(AddressElements.values()[i], "");
        		}
        		this.address.put(AddressElements.FULL_ADDRESS, addressInfo[0]);
        		
        	}
        	else {
        		StringBuilder fullAddress = new StringBuilder();
	        	for ( int i=0; i<addressInfo.length; i++ ){
	        		this.address.put(AddressElements.values()[i], addressInfo[i]);
	        		if ( i == addressInfo.length-1 ){
	        			fullAddress.append(addressInfo[i]);
	        		}
	        		else {
	        			fullAddress.append(addressInfo[i]+", ");
	        		}
	        	}
	        	this.address.put(AddressElements.FULL_ADDRESS, fullAddress.toString());
        	}
        }

		public String getStreet() {
            return this.address.get(AddressElements.STREET);
        }

        public String getNumber() {
        	return this.address.get(AddressElements.NUMBER);
        }

        public String getNeighborhood() {
        	return this.address.get(AddressElements.NEIGHBORHOOD);
        }

        public String getCity() {
        	return this.address.get(AddressElements.CITY);
        }

        public String getState() {
        	return this.address.get(AddressElements.STATE);
        }

        public String getCountry() {
        	return this.address.get(AddressElements.COUNTRY);
        }

        public String getZipCode() {
        	return this.address.get(AddressElements.ZIPCODE);
        }
        
        public String getFullAddress() {
        	return this.address.get(AddressElements.FULL_ADDRESS);
        }
        
        @Override
        public String toString(){
            return this.getFullAddress().toString();
        }

        @Override
        public int hashCode(){
                return this.toString().hashCode();
        }

        public boolean addressMatches(String toBeMatched) {
                return this.toString().toUpperCase().contains(toBeMatched.toUpperCase());
        }
}