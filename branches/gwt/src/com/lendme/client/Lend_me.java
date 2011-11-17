package com.lendme.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Lend_me implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final LendMeAsync lendMeService = GWT
			.create(LendMe.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		String[] address = {"Rua Joaquim Caroca", "350"};
		
		lendMeService.registerUser("guilhermesgb", "Guilherme Baptista", address,
				new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(String result) {
			}
			
		});
		
	}
}
