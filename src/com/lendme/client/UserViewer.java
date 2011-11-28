package com.lendme.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	public UserViewer(LendMeAsync lendMeService, String solicitorSession, String viewedUserLogin, Map<String, String[]> resultToBeViewed) {

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

		containerPanel.add(internalPanel);
		for(String actualResult : resultToBeViewed.keySet()){
			usersRepresentations.add(new LendMeUsersRepresentation(defaultImage, actualResult,
					resultToBeViewed.get(actualResult)[0], resultToBeViewed.get(actualResult)[2], resultToBeViewed.get(actualResult)[1]));
		}
		refreshThis();
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
