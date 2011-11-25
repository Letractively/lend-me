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
	private List<LendMeUsersRepresentation> usersRepresentations = new ArrayList<LendMeUsersRepresentation>();
	private AbsolutePanel containerPanel;
	private ScrollPanel rootScrollPanel;
	private AbsolutePanel internalPanel;
	private int height = 100;
	private int lastX = 30;
	private int lastY = 50;
	
	public UserViewer(LendMeAsync lendMeService, String solicitorSession) {

		rootScrollPanel = new ScrollPanel();
		//this.add(rootScrollPanel, 10, 28);
		rootScrollPanel.setSize("590px", "362px");
		
		containerPanel = new AbsolutePanel();
		rootScrollPanel.setWidget(containerPanel);
		containerPanel.setSize("100%", "361px");
		
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
			public void onSuccess(Map<String,String[]> result) {
				for(String actualResult : result.keySet()){
					usersRepresentations.add(new LendMeUsersRepresentation(defaultImage, actualResult,
						result.get(actualResult)[0], result.get(actualResult)[2], result.get(actualResult)[1]));
				}
				refreshThis();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				errorLabel.setText(errorLabel.getText()+": "+caught.getMessage());
				errorLabel.setVisible(true);
			}
		});
		initWidget(rootScrollPanel);
	}
	
	private void refreshThis(){
		
		int elements = 1;

		for(LendMeUsersRepresentation user : usersRepresentations){
			containerPanel.add(user, lastX, lastY);
			this.height = this.height + 80;
			containerPanel.setHeight(Integer.toString(this.height)+"px");
									
			if(elements%2 == 0){
				lastY = lastY + 145;
				lastX = lastX - 270;	
			}else{
				lastX = lastX + 270;
			}	
			elements++;
		}
	}
}
