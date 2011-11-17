package com.lendme.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LendMeAsync {

	void openSession(String login, AsyncCallback<String> callback);

	void closeSession(String sessionId, AsyncCallback<Void> callback);

	void registerUser(String login, String name, String[] address,
			AsyncCallback<String> callback);

}
