package com.lendme.client;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;

public class UserViewer extends Composite{
	
	
	public enum FriendshipStatus {FRIEND, NOT_FRIEND, REQUESTED_MY_FRIENDSHIP};
	
//	private final String defaultImage = "http://icons.iconarchive.com/icons/fasticon/fast-icon-users/128/user-icon.png";
	private static final Set<LendMeUsersContainer> usersSet = new HashSet<LendMeUsersContainer>();
	private static AbsolutePanel containerPanel;
	private static ScrollPanel rootScrollPanel;
	private static AbsolutePanel internalPanel;
	private final static AbsolutePanel usersPanel = new AbsolutePanel();
	private static int height = 100;
	private static int lastX = 10;
	private static int lastY = 25;
	
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
		internalPanel.setSize("600px", "72px");
		
		Label lblNewLabel = new Label("USERS");
		lblNewLabel.setStyleName("gwt-OptionsFont");
		internalPanel.add(lblNewLabel, 25, 10);
		
		containerPanel.add(internalPanel, 0, 0);
		internalPanel.add(usersPanel, 0, 72);
		usersPanel.setSize("600px", "528px");
		usersPanel.clear();
		
		PushButton refresh = new PushButton("");
		containerPanel.add(refresh, 350, 10);
		refresh.setSize("48px", "48px");
		refresh.getUpFace().setImage(new Image("http://icons.iconarchive.com/icons/deleket/button/48/Button-Refresh-icon.png"));
		refresh.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				LeftOptionsSideBarPanel.redoUserQuery();
			}
			
		});
		usersSet.clear();
		
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
										
										LendMeUsersContainer container = new LendMeUsersContainer(lendMeService, solicitorSession, userViewerLogin,
												new LendMeUsersRep("http://graph.facebook.com/"+actualResult+"/picture", actualResult,
												searchResults.get(actualResult)[0], searchResults.get(actualResult)[2],
												searchResults.get(actualResult)[1]), curUserFriendshipStatus);
										usersSet.add(container);
									}
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

		for(LendMeUsersContainer container : usersSet){
			usersPanel.add(container, lastX, lastY);
			height = height + 130;
			usersPanel.setHeight(Integer.toString(height)+"px");
									
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
