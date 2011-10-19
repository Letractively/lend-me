package com.lendme;


/*Esta classe provavelmente sera um singleton
 *para evitar o descontrole nas consultas com API do google
*/
public class Localizator {
	
	private Localizator localizatorInstace;
	
	private Localizator(){
		
		if(localizatorInstace == null)
			localizatorInstace = new Localizator();
				
	}
	
	public Localizator getInstance(){
		return localizatorInstace;
	}
	
	public double[] getLatAndLong(Address addrs){
		
		double[] latAndLog = {0.0,0.0};
		
		StringBuilder strForSearch = new StringBuilder();
		strForSearch.append("http://maps.google.com/maps/api/geocode/json?");
		strForSearch.append("address=");
		strForSearch.append(addrs.getFullAddress());
		strForSearch.append("&sensor=false");
		
		
	    //Fazer aqui a parte da consulta com o objeto URL
		
		return latAndLog;
		
	}
	
	
	
	
}
