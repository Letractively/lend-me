package com.lendme.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface LendMe extends RemoteService{

	public String openSession(String login) throws Exception;

	public void closeSession(String sessionId) throws Exception;
	
	public String registerUser(String login, String name, String... address) throws Exception;
	
}
