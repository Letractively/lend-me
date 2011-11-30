package com.lendme.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Image;

public class UserViewer extends Composite{
	
	
	public enum FriendshipStatus {FRIEND, NOT_FRIEND, REQUESTED_MY_FRIENDSHIP};
	
//	private final String defaultImage = "http://icons.iconarchive.com/icons/fasticon/fast-icon-users/128/user-icon.png";
	private static final Map<LendMeUsersContainer, LendMeUsersContainer> usersContainers = new HashMap<LendMeUsersContainer, LendMeUsersContainer>();
	private static AbsolutePanel containerPanel;
	private static ScrollPanel rootScrollPanel;
	private static AbsolutePanel internalPanel;
	private static int height = 100;
	private static int lastX = 10;
	private static int lastY = 25;
	private static Label lblLendme;
	
	public UserViewer(final LendMeAsync lendMeService, final String solicitorSession, final String userViewerLogin,
			final Map<String, String[]> searchResults) {
		
		displayUsers(lendMeService, solicitorSession, userViewerLogin, searchResults);
		
			// Uncomment this piece of code and comment the server calls to test without the server connection
//			for (int i = 0; i < 5; i++) {
//				usersContainers.add(new LendMeUsersContainer(lendMeService, solicitorSession, 
//						new LendMeUsersRepresentation(defaultImage,	"login" + i, "name" + i,
//						"address" + i, "reputation" + i), FriendshipStatus.REQUESTED_MY_FRIENDSHIP) );
//			}

		initWidget(rootScrollPanel);
	}
	
	public static void displayUsers(final LendMeAsync lendMeService, final String solicitorSession, final String userViewerLogin,
			final Map<String, String[]> searchResults){
		
		rootScrollPanel = new ScrollPanel();
		//this.add(rootScrollPanel, 10, 28);
		rootScrollPanel.setSize("600px", "600px");
		
		containerPanel = new AbsolutePanel();
		rootScrollPanel.setWidget(containerPanel);
		containerPanel.setSize("600px", "600px");
		
		internalPanel = new AbsolutePanel();
		internalPanel.setStyleName("backGround1");
		internalPanel.setSize("600px", "70px");
		
		Label lblNewLabel = new Label("USERS");
		lblNewLabel.setStyleName("defaultTitle");
		internalPanel.add(lblNewLabel, 63, 19);
		
		final Label errorLabel = new Label("Houve um erro com a comunicação com o servidor.");
		errorLabel.setStyleName("defaultErrorStyle");
		internalPanel.add(errorLabel, 0, 60);
		errorLabel.setSize("290px", "10px");
		errorLabel.setVisible(false);
		
		containerPanel.add(internalPanel);
		
		Image image = new Image((String) null);
		image.setUrl("http://icons.iconarchive.com/icons/kawsone/teneo/48/Blacklist-icon.png");
		internalPanel.add(image, 6, 5);
		image.setSize("48px", "48px");
		
		lblLendme = new Label("Lend-me!");
		lblLendme.setStyleName("barra");
		internalPanel.add(lblLendme, 0, 58);
		lblLendme.setSize("700px", "10px");
		
		lendMeService.getFriends(solicitorSession, new AsyncCallback<Map<String,String[]>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Nao foi possivel ter acesso aos amigos do usuario: " + caught.getMessage());
				
			}

			@Override
			public void onSuccess(Map<String, String[]> result) {
				final Set<String> userFriends = result.keySet();
				lendMeService.getFriendshipRequests(solicitorSession, new AsyncCallback<Map<String,String[]>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Nao foi possivel ter acesso as amizades requisitadas ao usuario: " + caught.getMessage());
						
					}

					@Override
					public void onSuccess(Map<String, String[]> result) {
						final Set<String> userFriendshipRequests = result.keySet();
						lendMeService.getUserAttributeBySessionId(solicitorSession, "login", new AsyncCallback<String>(){

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Nao foi possivel descobrir o login do dono da sessao atual: "+caught.getMessage());
							}

							@Override
							public void onSuccess(String result) {
								for(String actualResult : searchResults.keySet()){ 
									if ( !actualResult.equals(result) ){
										final FriendshipStatus curUserFriendshipStatus = userFriends.contains(actualResult) ?
												FriendshipStatus.FRIEND : (userFriendshipRequests.contains(actualResult) ? 
												FriendshipStatus.REQUESTED_MY_FRIENDSHIP : FriendshipStatus.NOT_FRIEND);
										
										LendMeUsersContainer container = new LendMeUsersContainer(lendMeService, solicitorSession, userViewerLogin, searchResults,
												new LendMeUsersRepresentation("http://graph.facebook.com/"+actualResult+"/picture", actualResult,
												searchResults.get(actualResult)[0], searchResults.get(actualResult)[2],
												searchResults.get(actualResult)[1]), curUserFriendshipStatus);
										usersContainers.put(container, container);
									}
								}
								Set<String> theRemainingViewed = new HashSet<String>();
								for ( LendMeUsersContainer remaining : usersContainers.keySet() ){
									theRemainingViewed.add(remaining.getViewed());
								}
								Set<String> toBeRemoved = searchResults.keySet();
								toBeRemoved.removeAll(theRemainingViewed);
								for (String viewedLoginToBeExcluded : toBeRemoved ){
									//creating a stub for container whose only important attribute is the viewedLogin
									//because instances of Container with same viewedLogin are considered to be the same
									usersContainers.remove(new LendMeUsersContainer(viewedLoginToBeExcluded));
								}
								refreshThis();
							}
							
						});
					}
				});
			}
		});
	}
	
	public static void refreshThis(){
		
		int elements = 1;
		
		height = 100;
		lastX = 10;
		lastY = 25;

		for(LendMeUsersContainer container : usersContainers.keySet()){
			containerPanel.add(usersContainers.get(container), lastX, lastY);
			height = height + 130;
			containerPanel.setHeight(Integer.toString(height)+"px");
									
			if(elements%2 == 0){
				lastY = lastY + 175;
				lastX = lastX - 280;	
			}else{
				lastX = lastX + 280;
			}	
			elements++;
		}
	}
}
