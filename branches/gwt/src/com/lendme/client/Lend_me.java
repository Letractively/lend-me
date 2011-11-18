package com.lendme.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
@SuppressWarnings("deprecation")
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

		//Creating buttons and fields
		final Button loginButton = new Button("Login");
		final Button authButton = new Button("Autenticar");
		final Button registerButton = new Button("Cadastrar");
		final TextBox loginField = new TextBox();
		loginField.setText("Username");
		final TextBox nameField = new TextBox();
		nameField.setText("Name");
		final TextBox addressField = new TextBox();
		addressField.setText("Address");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		loginButton.addStyleName("loginButton");
		registerButton.addStyleName("registerButton");

		// Add the nameField and registerButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("loginFieldContainer").add(loginField);
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("addressFieldContainer").add(addressField);
		RootPanel.get("registerButtonContainer").add(registerButton);
		RootPanel.get("loginButtonContainer").add(loginButton);
		RootPanel.get("authButtonContainer").add(authButton);
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
		dialogVPanel.add(new HTML("<b>Sending request to the server:</b>"));
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
				registerButton.setFocus(false);
				loginButton.setEnabled(true);
				loginButton.setFocus(true);
				authButton.setEnabled(true);
				authButton.setFocus(false);
			}
		});

		// Create a handler for the registerButton and nameField
		class RegisterUserHandler implements ClickHandler, KeyUpHandler, FocusHandler, FocusListener {
			
			boolean focused = false;
			
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
				if (focused && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
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
								dialogBox
										.setText(caught.getMessage());
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								errorLabel.setText(caught.getMessage());
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML("result");
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}

			@Override
			public void onFocus(FocusEvent event) {
				focused = true;
			}

			@Override
			public void onFocus(Widget sender) {
				focused = true;
			}

			@Override
			public void onLostFocus(Widget sender) {
				focused = false;
			}
		}

		// Add a handler to send the name to the server
		RegisterUserHandler handler = new RegisterUserHandler();
		registerButton.addClickHandler(handler);
		registerButton.addKeyUpHandler(handler);
		
		class SystemLoginHandler implements ClickHandler, KeyUpHandler, FocusHandler, FocusListener {

			boolean focused = false;
			
			@Override
			public void onFocus(Widget sender) {
				focused = true;
			}

			@Override
			public void onLostFocus(Widget sender) {
				focused = false;
			}

			@Override
			public void onFocus(FocusEvent event) {
				focused = true;
			}

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if ( focused && event.getNativeKeyCode() == (KeyCodes.KEY_ENTER) ){
					loginWithFacebook();					
				}
			}

			@Override
			public void onClick(ClickEvent event) {
				loginWithFacebook();
			}

			private void loginWithFacebook() {
				lendMeService.login(new AsyncCallback<String>(){
					public void onFailure(Throwable caught) {
						dialogBox
								.setText(caught.getClass().getName());
						serverResponseLabel
								.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(SERVER_ERROR);
						errorLabel.setText(caught.getClass().getName());
						dialogBox.center();
						closeButton.setFocus(true);
					}

					public void onSuccess(String result) {
						dialogBox.setText("Remote Procedure Call");
						serverResponseLabel
								.removeStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML("Response received");
						errorLabel.setText(result);
						dialogBox.center();
						closeButton.setFocus(true);
					}
				});
			}
		}
		
		SystemLoginHandler handler2 = new SystemLoginHandler();
		loginButton.addClickHandler(handler2);
		loginButton.addKeyUpHandler(handler2);
		
		class SystemAuthHandler implements ClickHandler, KeyUpHandler, FocusHandler, FocusListener {

			boolean focused = false;
			
			@Override
			public void onFocus(Widget sender) {
				focused = true;
			}

			@Override
			public void onLostFocus(Widget sender) {
				focused = false;
			}

			@Override
			public void onFocus(FocusEvent event) {
				focused = true;
			}

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if ( focused && event.getNativeKeyCode() == (KeyCodes.KEY_ENTER) ){
					authWithFacebook();					
				}
			}

			@Override
			public void onClick(ClickEvent event) {
				authWithFacebook();
			}

			private void authWithFacebook() {
				if ( errorLabel.getText().split("&auth_token=").length == 1 ){
					errorLabel.setText("Please login before authenticating user");
					return;
				}
				lendMeService.authenticate(errorLabel.getText().split("&auth_token=")[1], new AsyncCallback<String>(){
					public void onFailure(Throwable caught) {
						dialogBox
								.setText(caught.getMessage());
						serverResponseLabel
								.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(SERVER_ERROR);
						errorLabel.setText(caught.getMessage());
						dialogBox.center();
						closeButton.setFocus(true);
					}

					public void onSuccess(String result) {
						dialogBox.setText("Remote Procedure Call");
						serverResponseLabel
								.removeStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML("Response received");
						errorLabel.setText(result);
						dialogBox.center();
						closeButton.setFocus(true);
					}
				});
			}
		}
		
		SystemAuthHandler handler3 = new SystemAuthHandler();
		authButton.addClickHandler(handler3);
		authButton.addKeyUpHandler(handler3);
		
	}
}
