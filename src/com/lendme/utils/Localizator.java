package com.lendme.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


/**
 * This class is a Singleton. It is used
 * to get the latitude and longitude of a user's address.
 *
 */
public class Localizator {
	
	private static Localizator localizatorInstace;
	
	/**
	 * As Localizator is a singleton, it must return 
	 * a single instance of the object.
	 * @return the unique instance of the Localizator in the system
	 */
	public static Localizator getInstance(){
		
		if(localizatorInstace == null)
		    return new Localizator();
		else
		    return localizatorInstace;
	}
	
	/**
	 * This method communicates with GoogleMaps API sending a URL
	 * request in order to get the geographic coordinates of the given address
	 * @param string the string representation of the requested address
	 * @return the latitude and longitude of an address 
	 */
	public double[] getLatAndLong(String string) {
		
		double[] latAndLog = {0.0,0.0};
		String strSlice = "";
		StringBuilder strJSON = new StringBuilder();
		
		StringBuilder strForSearch = new StringBuilder();
		strForSearch.append("http://maps.google.com/maps/api/geocode/json?");
		strForSearch.append("address=");
		string = string.replaceAll(" ", "+");
		strForSearch.append(string);
		strForSearch.append("&sensor=false");
		
		try{
		
			URL url = new URL(strForSearch.toString());
			URLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Request-Method", "GET");
			connection.setDoInput(true);
			connection.setDoOutput(false);
			connection.connect();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			while((strSlice = br.readLine()) != null){
				strJSON.append(strSlice);
			}
			
			 String latStr = strJSON.toString().split("\"location\"")[1].split(",")[0].split(":")[2].replace("}", "").trim();
			 String logStr = strJSON.toString().split("\"location\"")[1].split(",")[1].split(":")[1].replace("}", "").trim();
			 
			 latAndLog[0] = Double.parseDouble(latStr);
			 latAndLog[1] = Double.parseDouble(logStr);

		}catch(Exception e){
			e.getStackTrace();
		}
		
		return latAndLog;
	}
}