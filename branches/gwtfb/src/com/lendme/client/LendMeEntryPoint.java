package com.lendme.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lendme.fbsdk.FBCore;
import com.lendme.fbsdk.FBEvent;
import com.lendme.fbsdk.FBXfbml;

public class LendMeEntryPoint implements EntryPoint, ValueChangeHandler<String>  {

	private DockPanel mainPanel = new DockPanel ();
	private static SimplePanel mainView = new SimplePanel ();
	private SimplePanel leftSideBarView = new SimplePanel ();
	private LinksPanel topLinksPanel;

	private FBCore fbCore = GWT.create(FBCore.class);
	private FBEvent fbEvent = GWT.create(FBEvent.class);
	private static LendMeAsync lendMeService = GWT.create(LendMe.class);

	private boolean status = true;
	private boolean xfbml = true;
	private boolean cookie = true;

	// The sessionId which will be generated when the user logs in
	private static String currentSessionId = "";
	private static String currentUserId = "";
	private String accessToken = "";

	class SessionInfo {
		private boolean sessionFound = false;
		public SessionInfo(boolean sessionFound){
			this.sessionFound = sessionFound;
		}
		public boolean sessionFound(){
			return sessionFound;
		}
		public void setSessionFound(boolean sessionFound){
			this.sessionFound = sessionFound;
		}
	}

	class UserSearchResultFound implements AsyncCallback<Map<String, String[]>>{

		private Map<String, String[]> result = new HashMap<String, String[]>();
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Problema ocorreu ao obter resultado da pesquisa: "+caught.getMessage());
		}

		@Override
		public void onSuccess(Map<String, String[]> result) {
			this.result = result;
			displayUserSearchResults(result);
		}

		public Map<String, String[]> getResult(){
			return result;
		}
	}

	static class ItemSearchResultFound implements AsyncCallback<Map<String, String[]>>{

		private Map<String, String[]> result = new HashMap<String, String[]>();
		@Override
		public void onFailure(Throwable caught) {
			if ( caught.getMessage().equals("O usuário não tem permissão para visualizar estes itens") ){
				class ErrorMessage extends Composite{
					
					private DockPanel dockPanel;
					
					public ErrorMessage(){
						
						dockPanel = new DockPanel();
						VerticalPanel verticalPanel = new VerticalPanel();
						verticalPanel.add(new Label("Voce nao tem permissao para visualizar estes itens!"));
						dockPanel.add(verticalPanel, DockPanel.CENTER);
						initWidget(dockPanel);
					}
					
				}

				mainView.setWidget(new ErrorMessage());
			}
			else{
				Window.alert("Problema ocorreu ao obter resultado da pesquisa: "+caught.getMessage());
			}
		}

		@Override
		public void onSuccess(Map<String, String[]> result) {
			this.result = result;
			displayItemSearchResults(currentUserId, result);
		}

		public Map<String, String[]> getResult(){
			return result;
		}
	}

	public final SessionInfo sessionInfo = new SessionInfo(false);
	public final UserSearchResultFound userSearchResult = new UserSearchResultFound();
	public final static ItemSearchResultFound itemSearchResult = new ItemSearchResultFound();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		History.addValueChangeHandler ( this );

		accessToken = fbCore.init(ApplicationConstants.APP_ID, ApplicationConstants.APP_SECRET, status, cookie, xfbml);

		RootPanel root = RootPanel.get();
		root.getElement().setId ( "TheApp" );
		mainView.getElement().setId("MainView");
		mainView.setSize("400px", "600px");
		leftSideBarView.getElement().setId("SideBarView");
		mainPanel.add( new TopMenuPanel () , DockPanel.NORTH );
		topLinksPanel = new LinksPanel();
		mainPanel.add ( topLinksPanel, DockPanel.NORTH );
		mainPanel.add( leftSideBarView, DockPanel.WEST );
		mainPanel.add( mainView, DockPanel.CENTER );
		root.add ( mainPanel );

		//
		// Callback used when session status is changed; happens automatically
		//
		class SessionChangeCallback extends Callback<JavaScriptObject> {
			public void onSuccess ( JavaScriptObject response ) {
				// Make sure cookie is set so we can use the non async method
				sessionChangedServerActions();
				String redirectTo = "#home";
				if ( fbCore.getSession() != null ){
					redirectTo = Window.Location.getHash();
				}
				renderApp(redirectTo);
			}
		}

		SessionChangeCallback sessionChangeCallback = new SessionChangeCallback ();
		fbEvent.subscribe("auth.authResponseChange",sessionChangeCallback);

		//
		// Callback happens when F5 is hit or when browser redirects to some url
		//
		class LoginStatusCallback extends Callback<JavaScriptObject> {
			public void onSuccess ( JavaScriptObject response ) {
				sessionChangedServerActions();
				String redirectTo = "#home";
				if ( fbCore.getSession() != null ){
					redirectTo = Window.Location.getHash();
					sessionInfo.setSessionFound(true);
				}
				renderApp(redirectTo);
			}

		}
		LoginStatusCallback loginStatusCallback = new LoginStatusCallback ();

		// Get login status
		fbCore.getLoginStatus( loginStatusCallback );
	}

	/**
	 * Render GUI
	 */
	private void renderApp ( String rawtoken ) {

		//		Window.alert("Rendering app: rawtoken = "+rawtoken);

		rawtoken = rawtoken.replace("#", "");

		if ( rawtoken == null || "".equals ( rawtoken ) || "#".equals ( rawtoken ) ){
			rawtoken = "home";
		}

		final String token = rawtoken;

		//		Window.alert("final token="+token);

		if ( token.contains("registration") && fbCore.getSession() == null ){
			//			Window.alert("will render registration view");
			renderRegistrationView();
		}
		else{
			JavaScriptObject session = fbCore.getSession();
			if ( session != null ){
				//				Window.alert("A session exists");
				fbCore.api("/me", new AsyncCallback<JavaScriptObject>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Failed to retrieve user basic info: "+caught.getMessage());
					}

					@Override
					public void onSuccess(JavaScriptObject result) {
						JSOModel model = result.cast();
						if ( model.get("id").contains("undefined") ){
							//							Window.alert("undefined user is logged");
							Window.Location.replace(ApplicationConstants.APP_URL);
							Window.Location.reload();
							return;
						}
						else{
							currentUserId = model.get("id");
							//							Window.alert("Do action based on token:"+token);
							if ( token.contains("home") ) {
								//								Window.alert("Will render home view, currentUserId is "+currentUserId);
								renderHomeView(currentUserId);
							}
							else if ( token.contains("options" ) ) {

								final String option = token.substring(token.indexOf("options")).split("/")[1];
								//								Window.alert("Will render options view, currentUserId is "+currentUserId+" , option is "+option);

								final String viewedUser = token.substring(0, token.indexOf("options")).split("/")[0];
								if ( !viewedUser.trim().isEmpty() && fbCore.userExists(viewedUser) ){
									lendMeService.userExists(viewedUser, new AsyncCallback<Boolean>(){

										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Problems verifying whether an user "+viewedUser+" exists");
										}

										@Override
										public void onSuccess(Boolean result) {
											Window.alert("User exists in facebook; user exists in system: "+result.booleanValue());
											if ( result.booleanValue() ){
												renderOptions(option, viewedUser);
											}
											else{
												renderOptions(option, currentUserId);
											}
										}

									});
								}
								else{
									Window.alert("User does not exist in facebook");
									renderOptions(option, currentUserId);
								}
							}
							else {
								final String viewedUser = token.split("/")[0];
								if ( !viewedUser.trim().isEmpty() && fbCore.userExists(viewedUser) ){
									lendMeService.userExists(viewedUser, new AsyncCallback<Boolean>(){

										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Problems verifying whether an user "+viewedUser+" exists");
										}

										@Override
										public void onSuccess(Boolean result) {
											Window.alert("User exists in facebook; user exists in system: "+result.booleanValue());
											if ( result.booleanValue() ){
												renderHomeView(viewedUser);
											}
											else{
												renderHomeView(currentUserId);
											}
										}
									});
								}
								else{
									Window.alert("User does not exist in facebook");
									renderHomeView(currentUserId);
								}
							}
						}
					}
				});
			}
			else{
				//				Window.alert("Session is null, render home view, currentUser is "+currentUserId);
				renderHomeView(currentUserId);
			}
		}
	}

	private void renderOptions(String option, String viewedUser){
		if ( option.equals("friends") ){
			leftSideBarView.setWidget(new LeftOptionsSideBarPanel(lendMeService, currentSessionId, currentUserId, viewedUser, fbCore, userSearchResult, itemSearchResult));
			displayCurrentUserFriends(viewedUser);
		}
		else if ( option.equals("items") ){
			leftSideBarView.setWidget(new LeftOptionsSideBarPanel(lendMeService, currentSessionId, currentUserId, viewedUser, fbCore, userSearchResult, itemSearchResult));
			displayCurrentUserItems(viewedUser);
		}
		else if ( option.equals("messages") ){
			leftSideBarView.setWidget(new LeftOptionsSideBarPanel(lendMeService, currentSessionId, currentUserId, viewedUser, fbCore, userSearchResult, itemSearchResult));
			mainView.setWidget(new TopicsViewer(lendMeService, currentSessionId, "todos"));
		}
		else if ( option.equals("history") ){
			leftSideBarView.setWidget(new LeftOptionsSideBarPanel(lendMeService, currentSessionId, currentUserId, viewedUser, fbCore, userSearchResult, itemSearchResult));
			mainView.setWidget(new HistoryViewer(lendMeService, currentSessionId, "all"));
		}
		else {
//			renderHomeView(viewedUser);
		}
	}

	/**
	 * Render GUI when logged in
	 */
	private void renderWhenLoggedIn () {
		mainView.setWidget ( new UserInfoViewController ( currentUserId ) );
		topLinksPanel.setLogoutButtonVisible(true);
		FBXfbml.parse();
	}

	/**
	 * Render GUI when not logged in
	 */
	private void renderWhenNotLoggedIn () {

		class CommentsPanel extends Composite{
			private VerticalPanel verticalBar = new VerticalPanel();

			public CommentsPanel(){
				verticalBar.add(new Label("Ajude a gente!"));
				verticalBar.add(new HTML ("<hr/><fb:like href='"+ApplicationConstants.APP_URL+"' button_count='button_count' action='like'/>"));
				verticalBar.add (new HTML("<div style='margin-top: 7px;'> Diga o que vc pensa dessa nova ideia: </div>"));
				verticalBar.add(new HTML("<hr/><fb:comments numposts='1' xid='gwtfb' width='435px' />"));
				initWidget(verticalBar);
			}
		}

		mainView.setWidget ( new FrontpageViewController (ApplicationConstants.APP_ID) );
		leftSideBarView.setWidget(new CommentsPanel());
		topLinksPanel.setLogoutButtonVisible(false);
		FBXfbml.parse();
	}

	/**
	 * Render home view. If user is logged in display welcome message, otherwise
	 * display login dialog.
	 */
	private void renderHomeView (String token) {
		leftSideBarView.clear();

		if ( fbCore.getSession() == null ) {
			//			Window.alert("Rendering home view unlogged");
			renderWhenNotLoggedIn ();
		} else {
			//			Window.alert("Rendering home view logged");
			leftSideBarView.setWidget(new LeftOptionsSideBarPanel(lendMeService, currentSessionId, currentUserId, token, fbCore, userSearchResult, itemSearchResult));
			renderWhenLoggedIn();
		}
	}

	private void renderRegistrationView() {
		leftSideBarView.clear();

		mainView.setWidget ( new RegistrationViewController() );
		topLinksPanel.setLogoutButtonVisible(false);
		FBXfbml.parse();
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		//		Window.alert("on value change: "+event.getValue());
		renderApp ( event.getValue() );
	}

	/**
	 * Above goes methods that communicate with server
	 */
	public void sessionChangedServerActions(){

		if ( fbCore.getSession() == null ){
			if ( !currentSessionId.trim().isEmpty() ){

				final String sessionToBeClosed = currentSessionId;
				lendMeService.closeSession(currentSessionId, new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Problem when closing session ["+currentSessionId+"]: "+caught.getMessage());
					}

					@Override
					public void onSuccess(Void result) {
						Window.alert("Session ["+sessionToBeClosed+"] closed successfully");
						currentSessionId = "";
					}

				});
			}
			else{
				Window.alert("Session is null and Current session id is empty");
			}
		}
		else {
			if ( currentSessionId.trim().isEmpty() ){

				fbCore.api("/me&access_token="+accessToken, new AsyncCallback<JavaScriptObject>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Failed to retrieve session user id: "+caught.getMessage());
					}

					@Override
					public void onSuccess(JavaScriptObject result) {
						JSOModel model = result.cast();
						currentUserId = model.get("id");

						lendMeService.openSession(currentUserId, new AsyncCallback<String>(){

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Failed to open new session for logged fb user: "+caught.getMessage());
							}

							@Override
							public void onSuccess(String result) {
								currentSessionId = result;

								Window.alert("Session ["+result+"] opened sucessfully");

								lendMeService.getSessionInfo(currentSessionId, new AsyncCallback<String>(){

									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Problems getting logged user session information: "+caught.getMessage());
									}

									@Override
									public void onSuccess(String result) {
										final String toBeViewed = Window.Location.getHash();
										if ( !toBeViewed.trim().isEmpty() && fbCore.userExists(toBeViewed) ){
											lendMeService.userExists(toBeViewed, new AsyncCallback<Boolean>(){

												@Override
												public void onFailure(Throwable caught){
													Window.alert("Problems verifying whether an user "+toBeViewed+" exists");
												}

												@Override
												public void onSuccess(Boolean result) {
													Window.alert("User exists in facebook; user exists in system: "+result.booleanValue());
													if ( result.booleanValue() ){
														renderHomeView(toBeViewed);
													}
													else{
														renderHomeView(currentUserId);
													}
												}

											});
										}
										else{
											Window.alert("User doesnt exist in facebook");
											renderHomeView(currentUserId);
										}
									}

								});
							}

						});

					}
				});

			}
			else{
				Window.alert("Session exists and Current session id is: "+currentSessionId);
			}


		}

	}

	public void displayUserSearchResults(Map<String, String[]> results){
		displayResultMap(results);
		mainView.setWidget(new UserViewer(lendMeService, currentSessionId, currentUserId, userSearchResult.getResult()));
	}

	public static void displayItemSearchResults(String viewedLogin, Map<String, String[]> results){
		displayResultMap(results);
		mainView.setWidget(new ItemsViewer(lendMeService, currentSessionId, viewedLogin, itemSearchResult.getResult()));
	}
	
	public void displayCurrentUserFriends(String viewedLogin){
		lendMeService.getFriends(currentSessionId, viewedLogin, userSearchResult);
	}

	public void displayCurrentUserReceivedFriendshipRequests(){
		lendMeService.getFriendshipRequests(currentSessionId, userSearchResult);
	}

	public static void displayCurrentUserItems(String viewedLogin){
		lendMeService.getItems(currentSessionId, viewedLogin, itemSearchResult);
	}

	public static void displayResultMap(Map<String, String[]> results){
		StringBuilder formatted = new StringBuilder();
		for ( String key : results.keySet() ){
			formatted.append(key+": {");
			for ( String value : results.get(key) ){
				formatted.append(value+", ");
			}
			formatted.append("}\n");
		}
		Window.alert(new String(formatted));
	}

}