package com.lendme.server;

public interface AuthenticationClient {

	public String login();
	
	public String authenticate(String token);
	
}
