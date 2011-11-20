package com.lendme.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

public class UserViwer extends AbsolutePanel{
	
	private final LendMeAsync lendme = GWT.create(LendMe.class);
	private final String defaultImage = "http://icons.iconarchive.com/icons/fasticon/fast-icon-users/128/user-icon.png";
	private List<LendMeUsersRepresentation> usersRepresentations = new ArrayList<LendMeUsersRepresentation>();
	private AbsolutePanel conteinerPanel;
	private ScrollPanel scrollPanel;
	private int height = 100;
	private int lastX = 30;
	private int lastY = 50;
	
	public UserViwer() {
				
		scrollPanel = new ScrollPanel();
		this.add(scrollPanel, 10, 28);
		scrollPanel.setSize("590px", "362px");
		
		conteinerPanel = new AbsolutePanel();
		scrollPanel.setWidget(conteinerPanel);
		conteinerPanel.setSize("100%", "361px");
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		this.add(absolutePanel, 10, 9);
		absolutePanel.setSize("580px", "17px");
		
		Label lblNewLabel = new Label("USERS");
		absolutePanel.add(lblNewLabel, 270, 0);
		
		final Label errorLabel = new Label("Houve um erro com a comunicação com o servidor.");
		errorLabel.setStyleName("alert");
		absolutePanel.add(errorLabel, 3, 0);
		errorLabel.setVisible(false);
		
		String solicitorSession = "12345";//Numero louco de sessao!
		
		lendme.getFriends(solicitorSession, new AsyncCallback<Map<String,String[]>>() {
			
			@Override
			public void onSuccess(Map<String,String[]> result) {
				for(String actualResult : result.keySet()){
				usersRepresentations.add(new LendMeUsersRepresentation(defaultImage, actualResult,
						result.get(actualResult)[0], result.get(actualResult)[2], result.get(actualResult)[1]));
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				errorLabel.setVisible(true);
			}
		});
			
		this.refresh();
	}
	
	private void refresh(){
		
		int elements = 1;

		for(LendMeUsersRepresentation user : usersRepresentations){
			conteinerPanel.add(user, lastX, lastY);
			this.height = this.height + 80;
			conteinerPanel.setHeight(Integer.toString(this.height)+"px");
									
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
