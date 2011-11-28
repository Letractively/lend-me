package com.lendme.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

public class UserViewer extends Composite{
	
	private final String defaultImage = "http://icons.iconarchive.com/icons/fasticon/fast-icon-users/128/user-icon.png";
	private List<LendMeUsersContainer> usersContainers = new ArrayList<LendMeUsersContainer>();
	private AbsolutePanel containerPanel;
	private ScrollPanel rootScrollPanel;
	private AbsolutePanel internalPanel;
	private int height = 100;
	private int lastX = 10;
	private int lastY = 25;
	
	private final LendMeAsync lendMeService;
	private final String solicitorSession;
	private final boolean viewCurrentFriends;
	
	public UserViewer(LendMeAsync lendMeService, String solicitorSession, boolean viewCurrentFriends) {

		this.lendMeService = lendMeService;
		this.solicitorSession = solicitorSession;
		this.viewCurrentFriends = viewCurrentFriends;
		
		rootScrollPanel = new ScrollPanel();
		//this.add(rootScrollPanel, 10, 28);
		rootScrollPanel.setSize("590px", "550px");
		
		containerPanel = new AbsolutePanel();
		rootScrollPanel.setWidget(containerPanel);
		containerPanel.setSize("590px", "540px");
		
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
		
		// The following 'for' is just for testing. The real code is below
		for (int i = 0; i < 5; i++) {
			usersContainers.add(new LendMeUsersContainer(lendMeService, solicitorSession, 
					new LendMeUsersRepresentation(defaultImage,	"login" + i, "name" + i,
							"address" + i, "reputation" + i), viewCurrentFriends) );
		}

		if (viewCurrentFriends) {
			
//			lendMeService.getFriends(solicitorSession, new AsyncCallback<Map<String,String[]>>() {
//
//				@Override
//				public void onSuccess(Map<String,String[]> result) {
//					addContainers(result);
//					
//					refreshThis();
//				}
//				
//				@Override
//				public void onFailure(Throwable caught) {
//					errorLabel.setText(errorLabel.getText()+": "+caught.getMessage());
//					errorLabel.setVisible(true);
//				}
//			});
		
		} else {
//			lendMeService.getFriendshipRequests(solicitorSession, 
//					new AsyncCallback<Map<String,String[]>>() {
//
//						@Override
//						public void onFailure(Throwable caught) {
//							errorLabel.setText(errorLabel.getText()+": "+caught.getMessage());
//							errorLabel.setVisible(true);
//						}
//
//						@Override
//						public void onSuccess(Map<String, String[]> result) {
//							addContainers(result);
//							
//						}
//			});
		}
		refreshThis();
		
		initWidget(rootScrollPanel);
	}
	
	private void addContainers(Map<String, String[]> result) {
		for(String actualResult : result.keySet()){
			usersContainers.add(new LendMeUsersContainer(lendMeService, solicitorSession, new LendMeUsersRepresentation(defaultImage, actualResult,
				result.get(actualResult)[0], result.get(actualResult)[2], result.get(actualResult)[1]), viewCurrentFriends));
		}
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
