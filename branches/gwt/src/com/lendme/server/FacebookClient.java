package com.lendme.server;

import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookJsonRestClient;
import com.google.code.facebookapi.ProfileField;

public class FacebookClient implements AuthenticationClient {
	
	public static String API_KEY = "246859665375002";
	public static String SECRET = "5c2b3a54bf76700d23c1bbc532ba4bc0";

	public FacebookJsonRestClient jsonClient;

	public FacebookClient(){
		jsonClient = new FacebookJsonRestClient(API_KEY, SECRET);
	}


	@Override
	public String login() {
		try {
			String token = jsonClient.auth_createToken();
			String url = "http://www.facebook.com/login.php?api_key=" 
					+ API_KEY + "&v=1.0" 
					+ "&auth_token=" + token;
			return url;
		}
		catch(FacebookException fbExcp){
			if ( !fbExcp.getMessage().contains("The session is invalid because the user logged out.") ){
				return fbExcp.getMessage();
			}
			jsonClient = new FacebookJsonRestClient(API_KEY, SECRET);
			return login();
		}
	}

	@Override
	public String authenticate(String token) {

		try {

			jsonClient.auth_getSession(token,true); 
			jsonClient.getCacheSessionSecret();

			Long userId = jsonClient.users_getLoggedInUser();
			
			Set<Long> userIds = new TreeSet<Long>();
			userIds.add(userId);
			
			Set<ProfileField> fields = new TreeSet<ProfileField>();
			
			fields.add(ProfileField.CURRENT_LOCATION);
			
			JSONArray info = jsonClient.users_getInfo(userIds, fields);

			return info.toString();
		} catch (FacebookException e) {
			return e.getMessage();
		}
	}
} 