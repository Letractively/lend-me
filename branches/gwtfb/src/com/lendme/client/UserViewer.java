package com.lendme.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

public class UserViewer extends Composite{
	
	
	public enum FriendshipStatus {FRIEND, NOT_FRIEND, REQUESTED_MY_FRIENDSHIP};
	
//	private final String defaultImage = "http://icons.iconarchive.com/icons/fasticon/fast-icon-users/128/user-icon.png";
	private List<LendMeUsersContainer> usersContainers = new ArrayList<LendMeUsersContainer>();
	private Set<String> userFriends, userFriendshipRequests;
	private AbsolutePanel containerPanel;
	private ScrollPanel rootScrollPanel;
	private AbsolutePanel internalPanel;
	private int height = 100;
	private int lastX = 10;
	private int lastY = 25;
	
	private FriendshipStatus curUserFriendshipStatus;
	
	public UserViewer(final LendMeAsync lendMeService, final String solicitorSession, final String userViewerLogin,
			final Map<String, String[]> searchResults) {
		
		rootScrollPanel = new ScrollPanel();
		//this.add(rootScrollPanel, 10, 28);
		rootScrollPanel.setSize("590px", "342px");
		
		containerPanel = new AbsolutePanel();
		rootScrollPanel.setWidget(containerPanel);
		containerPanel.setSize("590px", "342px");
		
		internalPanel = new AbsolutePanel();
		//this.add(absolutePanel, 10, 9);
		internalPanel.setSize("580px", "17px");
		
		Label lblNewLabel = new Label("USERS");
		internalPanel.add(lblNewLabel, 270, 0);
		
		final Label errorLabel = new Label("Houve um erro com a comunicação com o servidor.");
		errorLabel.setStyleName("alert");
		internalPanel.add(errorLabel, 3, 15);
		errorLabel.setVisible(false);
		
		containerPanel.add(internalPanel);
		

		lendMeService.getFriends(solicitorSession, new AsyncCallback<Map<String,String[]>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Nao foi possivel ter acesso aos amigos do usuario: " + caught.getMessage());
				
			}

			@Override
			public void onSuccess(Map<String, String[]> result) {
				userFriends = result.keySet();
				lendMeService.getFriendshipRequests(solicitorSession, new AsyncCallback<Map<String,String[]>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Nao foi possivel ter acesso as amizades requisitadas ao usuario: " + caught.getMessage());
						
					}

					@Override
					public void onSuccess(Map<String, String[]> result) {
						userFriendshipRequests = result.keySet();
						for(String actualResult : searchResults.keySet()){ 
							curUserFriendshipStatus = userFriends.contains(actualResult) ?
									FriendshipStatus.FRIEND : (userFriendshipRequests.contains(actualResult) ? 
									FriendshipStatus.REQUESTED_MY_FRIENDSHIP : FriendshipStatus.NOT_FRIEND);
							
							
							usersContainers.add(new LendMeUsersContainer(lendMeService, solicitorSession, 
									new LendMeUsersRepresentation("http://graph.facebook.com/"+actualResult+"/picture", actualResult,
									searchResults.get(actualResult)[0], searchResults.get(actualResult)[2],
									searchResults.get(actualResult)[1]), curUserFriendshipStatus));
						}
						refreshThis();
					}
				});
			}
		});
		
			// Uncomment this piece of code and comment the server calls to test without the server connection
//			for (int i = 0; i < 5; i++) {
//				usersContainers.add(new LendMeUsersContainer(lendMeService, solicitorSession, 
//						new LendMeUsersRepresentation(defaultImage,	"login" + i, "name" + i,
//						"address" + i, "reputation" + i), FriendshipStatus.REQUESTED_MY_FRIENDSHIP) );
//			}

		initWidget(rootScrollPanel);
	}
	
	private void refreshThis(){
		
		int elements = 1;

		for(LendMeUsersContainer container : usersContainers){
			containerPanel.add(container, lastX, lastY);
			this.height = this.height + 130;
			containerPanel.setHeight(Integer.toString(this.height)+"px");
									
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
