package com.lendme.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

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

//		String[] address = {"Rua Joaquim Caroca", "350"};
//		
//		lendMeService.registerUser("guilhermesgb", "Guilherme Baptista", address,
//				new AsyncCallback<String>(){
//
//			@Override
//			public void onFailure(Throwable caught) {
//				
//			}
//
//			@Override
//			public void onSuccess(String result) {
//			}
//			
//		});
//		
//	}
		//Creating buttons and fields
		final Button registerButton = new Button("Cadastrar");
		final TextBox loginField = new TextBox();
		loginField.setText("Username");
		final TextBox nameField = new TextBox();
		nameField.setText("Name");
		final TextBox addressField = new TextBox();
		addressField.setText("Address");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		registerButton.addStyleName("registerButton");

		// Add the nameField and registerButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("loginFieldContainer").add(loginField);
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("addressFieldContainer").add(addressField);
		RootPanel.get("registerButtonContainer").add(registerButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the login field when the app loads
		loginField.setFocus(true);
		loginField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				registerButton.setEnabled(true);
				registerButton.setFocus(true);
			}
		});

		// Create a handler for the registerButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the registerButton.
			 */
			public void onClick(ClickEvent event) {
				sendRegistryInformationToServer();
			}

			/**
			 * Fired when the user types registry information.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendRegistryInformationToServer();
				}
			}

			/**
			 * Send the information provided in registry to the server and wait for a response.
			 */
			private void sendRegistryInformationToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = loginField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Por favor, preencha pelo menos 5 caracteres");
					return;
				}

				// Then, we send the input to the server.
				registerButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				String[] addressArray = {addressField.getText() };
				lendMeService.registerUser(loginField.getText(), nameField.getText(), addressArray,
						new AsyncCallback<String>(){
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText(caught.getMessage());
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		registerButton.addClickHandler(handler);
		loginField.addKeyUpHandler(handler);
	}
}
