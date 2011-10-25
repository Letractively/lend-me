package com.lendme.entities;

import java.util.HashMap;
import java.util.Map;

import com.lendme.utils.Localizator;

/**
 * @author THE LENDERS
 * User addresses can be of two kinds: a full string with all info in it or a more organized
 * structure with several fields distinguishing the parts that compose the address.
 *
 */

public class Address {

		private enum AddressElements {
			STREET, NUMBER, NEIGHBORHOOD, CITY, STATE, COUNTRY, ZIPCODE,FULL_ADDRESS;
		}
		
		private double latitude;
		private double longitude;
        
		private Map<AddressElements, String> address = new HashMap<AddressElements, String>();
		/**
		 * Recebe uma quatidade arbitraria de parametros, que correspondem aos campos dos enderecos.
		 * Esse numero depende de quais campos o cliente da classe quer passar.
		 * @param addressInfo
		 */
        public Address(String... addressInfo) {
        	for ( AddressElements e : AddressElements.values() ){
        		this.address.put(e, "");
        	}
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
        	
        	Localizator lcz = Localizator.getInstance();
        	double[] lat_and_log = lcz.getLatAndLong(this.getFullAddress());
        	this.setLatitude(lat_and_log[0]);
        	this.setLongitude(lat_and_log[1]);
        }

		/**
		 * Esse metodo retorna o nome da rua do endereco em questao.
		 * @return String - Nome da rua
		 */
		public String getStreet() {
            return this.address.get(AddressElements.STREET);
        }

        /**
         * Retorna o numero do endereco em questao.
         * @return String - Numero do endereco.
         */
        public String getNumber() {
        	return this.address.get(AddressElements.NUMBER);
        }

        /**
         * Retorna o nome do bairro.
         * @return String - Nome do Bairro.
         */
        public String getNeighborhood() {
        	return this.address.get(AddressElements.NEIGHBORHOOD);
        }

        /**
         * Retorna o nome da cidade do endereco em questao.
         * @return String - Nome da cidade.
         */
        public String getCity() {
        	return this.address.get(AddressElements.CITY);
        }

        /**
         * Retorna o nome do estado do endereco em questao.
         * @return String - Nome do estado.
         */
        public String getState() {
        	return this.address.get(AddressElements.STATE);
        }

        /**
         * Retorna o nome do pais do endereco em questao.
         * @return String - Nome do pais.
         */
        public String getCountry() {
        	return this.address.get(AddressElements.COUNTRY);
        }

        /**
         * Retorna o codigo postal do endereco em questao.
         * @return String - Codigo postal.
         */
        public String getZipCode() {
        	return this.address.get(AddressElements.ZIPCODE);
        }
        
        /**
         * Retorna uma representacao completa do endereco em questao,
         * baseado em quais campos o usuario da classe preencheu.
         * @return String - Endereco completo.
         */
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

        /**
         * Verifica se o endereco passado se parece com o endereco atual.
         * Eh feito baseado se uma representacao completa esta dentro da outra.
         * @param toBeMatched
         * @return Boolean - Se os enderecos sao parecidos.
         */
        public boolean addressMatches(String toBeMatched) {
                return this.toString().toUpperCase().contains(toBeMatched.toUpperCase());
        }

		/**
		 * Retorna a latitude do endereco. Obivio que isso se refere a uma aproximacao geralmente.
		 * @return Double - A latitude do endereco.
		 */
		public double getLatitude() {
			return latitude;
		}
		
		/**
		 * Caso se precise configurar manualmente a latitude, esse metodo eh uma opcao.
		 * @param Double - a latitude.
		 */
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
	
		/**
		 * Retorna a longitude do endereco. Obivio que isso se refere a uma aproximacao geralmente.
		 * @return Double - A latitude do endereco.
		 */
	
		public double getLongitude() {
			return longitude;
		}

		/**
		 * Caso se precise configurar manualmente a longitude, esse metodo eh uma opcao.
		 * @param Double - a latitude.
		 */
		
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
}