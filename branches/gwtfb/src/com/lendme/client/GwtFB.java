package com.lendme.client;

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
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lendme.fbsdk.FBCore;
import com.lendme.fbsdk.FBEvent;
import com.lendme.fbsdk.FBXfbml;

public class GwtFB implements EntryPoint, ValueChangeHandler<String>  {

	public static String APPID = "246859665375002";
	public static String SECRET = "5c2b3a54bf76700d23c1bbc532ba4bc0";

	private DockPanel mainPanel = new DockPanel ();
	private SimplePanel mainView = new SimplePanel ();
	private SimplePanel leftSideBarView = new SimplePanel ();
	private BottonLinksPanel bottonLinksPanel;

	private FBCore fbCore = GWT.create(FBCore.class);
	private FBEvent fbEvent = GWT.create(FBEvent.class);
	private LendMeAsync lendMeService = GWT.create(LendMe.class);
	
	private boolean status = true;
	private boolean xfbml = true;
	private boolean cookie = true;
	
	// The sessionId which will be generated when the user logs in
	private String currentSessionId = "";
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		History.addValueChangeHandler ( this );

		fbCore.init(APPID, status, cookie, xfbml);

		RootPanel root = RootPanel.get();
		root.getElement().setId ( "TheApp" );
		mainView.getElement().setId("MainView");
		leftSideBarView.getElement().setId("SideBarView");
		mainPanel.add( new TopMenuPanel () , DockPanel.NORTH );
		mainPanel.add( leftSideBarView, DockPanel.WEST );
		mainPanel.add( mainView, DockPanel.CENTER );
		bottonLinksPanel = new BottonLinksPanel();
		mainPanel.add ( bottonLinksPanel, DockPanel.SOUTH );
		root.add ( mainPanel );


		//
		// Callback used when session status is changed
		//
		class SessionChangeCallback extends Callback<JavaScriptObject> {
			public void onSuccess ( JavaScriptObject response ) {
				// Make sure cookie is set so we can use the non async method
				lendMeService.LogInWithAdminUser(new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						currentSessionId = result;
					}
					
					public void onFailure(Throwable caught) {
						currentSessionId = "";
						//TODO Show error message
					}
				});
				renderHomeView ();
			}
		}

		//
		// Get notified when user session is changed
		//
		SessionChangeCallback sessionChangeCallback = new SessionChangeCallback ();
		fbEvent.subscribe("auth.sessionChange",sessionChangeCallback);

		// Callback used when checking login status
		class LoginStatusCallback extends Callback<JavaScriptObject> {
			public void onSuccess ( JavaScriptObject response ) {
				renderApp( Window.Location.getHash() );
			}
		}
		LoginStatusCallback loginStatusCallback = new LoginStatusCallback ();

		// Get login status
		fbCore.getLoginStatus( loginStatusCallback );
	}

	/**
	 * Render GUI
	 */
	private void renderApp ( String token ) {

		leftSideBarView.setWidget( new HomeSideBarPanel () );

		token = token.replace("#", "");

		if ( token == null || "".equals ( token ) || "#".equals ( token ) ) 
		{
			token = "home";
		}

		if ( token.endsWith("home") ) {
			renderHomeView ();
		}
		else if ( token.startsWith("options" ) ) {

			String option = token.split("/")[1];

			class TemporaryWidget extends Composite{
				
				VerticalPanel list = new VerticalPanel();
				
				public TemporaryWidget(String kind){
					for ( int i=1; i<=10; i++ ){
						list.add(new Hyperlink("List of " + kind + " pos. "+i, ""));
					}
					initWidget(list);
				}
			}
			
			if ( option.startsWith("friends") ){
				mainView.setWidget(new TemporaryWidget("AMIGOS"));
//				mainView.setWidget( <AQUI FICA A TELA DE LISTAGEM DE AMIGOS> );
			}
			else if ( option.startsWith("items") ){
				mainView.setWidget(new TemporaryWidget("ITENS"));
//				mainView.setWidget( <AQUI FICA A TELA DE LISTAGEM DE ITEMS> );				
			}
			else if ( option.startsWith("messages") ){
				mainView.setWidget(new TemporaryWidget("MENSAGENS"));
//				mainView.setWidget( <AQUI FICA A TELA DE LISTAGEM DE MENSAGENS> );				
			}
			else if ( option.startsWith("history") ){
				mainView.setWidget(new TemporaryWidget("HISTORICO"));
//				mainView.setWidget( <AQUI FICA A TELA DE LISTAGEM DE HISTORICO> );				
			}
			else {
				Window.alert ( "Unknown  url "  + token );
			}

		}
		else {
			Window.alert ( "Unknown  url "  + token );
		}
	}

	/**
	 * Render GUI when logged in
	 */
	private void renderWhenLoggedIn () {
		mainView.setWidget ( new UserInfoViewController ( fbCore ) );
		bottonLinksPanel.setLogoutButtonVisible(true);
		FBXfbml.parse();
	}

	/**
	 * Render GUI when not logged in
	 */
	private void renderWhenNotLoggedIn () {
		mainView.setWidget ( new FrontpageViewController () );
		bottonLinksPanel.setLogoutButtonVisible(false);
		FBXfbml.parse();
	}

	/**
	 * Render home view. If user is logged in display welcome message, otherwise
	 * display login dialog.
	 */
	private void renderHomeView () {
		leftSideBarView.clear();

		if ( fbCore.getSession() == null ) {
			renderWhenNotLoggedIn ();
		} else {
			leftSideBarView.setWidget( new HomeSideBarPanel () );
			renderWhenLoggedIn();
		}
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		renderApp ( event.getValue() );
	}

}