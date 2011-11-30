package com.lendme.client;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HistoryViewer extends Composite {
	
	private AbsolutePanel rootPanel = new AbsolutePanel();
	private VerticalPanel container = new VerticalPanel();
	private final Label errorLabel; 
	private final String errorMessage = "Erro: LendMe nao conseguiu obter informacao do servidor.";
	final String REFRESH_ICON_PATH = "http://icons.iconarchive.com/icons/deleket/button/48/Button-Refresh-icon.png";
	final String GROUP_ICON_PATH = "http://www.iconeasy.com/icon/thumbnails/Internet%20%26%20Web/Users/User%20group%20Icon.jpg";
	final String PERSONAL_ICON_PATH = "http://icons.iconarchive.com/icons/custom-icon-design/pretty-office/48/personal-information-icon.png";
	private String scope;
	private LendMeAsync lendMeService;
	private String solicitorSessionId;
	private VerticalPanel activityPanel; 
	
	public HistoryViewer(LendMeAsync lendMeService,String solicitorSessionId, String scope) {
		
		this.scope = scope;
		this.solicitorSessionId = solicitorSessionId;
		this.lendMeService = lendMeService;
		
		container.setStyleName("topicsLines");
		
		container.setWidth("570px");

		ScrollPanel scrollPanel = new ScrollPanel();
		rootPanel.add(scrollPanel, 10, 80);
		scrollPanel.setSize("600px", "515px");
		rootPanel.setSize("600px", "550px");
		
		scrollPanel.add(container);
		
		errorLabel = new Label("error");
		container.add(errorLabel);
		errorLabel.setStyleName("topicsLines");
		
		HorizontalPanel topPanel = new HorizontalPanel();
		container.add(topPanel);
		topPanel.setWidth("600px");
		
		Label numberOfMsgsLabel = new Label("numOfMsgs");
		numberOfMsgsLabel.setText("Visualiza\u00E7ao:");
		numberOfMsgsLabel.setSize("210px", "15px");
		topPanel.add(numberOfMsgsLabel);
		
		Label subjectLabel = new Label("subject");
		subjectLabel.setText((scope.equals("all")?"Todos do Sistema":"Pessoal"));
		subjectLabel.setSize("210px", "15px");
		topPanel.add(subjectLabel);
		
		topPanel.addStyleName("topicsHeader");
		
		PushButton refreshButton = new PushButton("Refresh button");
		refreshButton.getUpFace().setImage(new Image(REFRESH_ICON_PATH));
		rootPanel.add(refreshButton, 427, 8);
		refreshButton.setSize("48px", "48px");
		refreshButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				refresh();
			}
		});
		
		PushButton groupButton = new PushButton("Group button");
		groupButton .getUpFace().setImage(new Image(GROUP_ICON_PATH));
		rootPanel.add(groupButton , 341, 8);
		groupButton .setSize("48px", "48px");
		groupButton .addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				refresh("all");
			}
		});
		
		PushButton personalButton = new PushButton("Group button");
		personalButton .getUpFace().setImage(new Image(PERSONAL_ICON_PATH));
		rootPanel.add(personalButton , 513, 8);
		personalButton .setSize("48px", "48px");
		personalButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				refresh("personal");
			}
		});
		
		activityPanel = new VerticalPanel();
		container.add(activityPanel);
		activityPanel.setWidth("599px");
		errorLabel.setVisible(false);
		
		rootPanel.add(scrollPanel, 10, 80);
		
		refresh();
		
		this.initWidget(rootPanel);
		
	}
	
	private void refresh() {
		refresh(this.scope);
	}
	
	private void refresh(String newScope) {
		this.scope = newScope;
		if ( scope.equals("all") ){
			lendMeService.getJointActivityHistory(solicitorSessionId, new AsyncCallback<Map<String, ArrayList<String[]>>>() {

				@Override
				public void onSuccess(Map<String, ArrayList<String[]>> result) {
					addActivitiesToPanel(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText(errorMessage + " - " + caught.getMessage());
					errorLabel.setVisible(true);
				}
			});
		}
		else{
			lendMeService.getActivityHistory(solicitorSessionId, new AsyncCallback<Map<String, ArrayList<String[]>>>() {

				@Override
				public void onSuccess(Map<String, ArrayList<String[]>> result) {
					addActivitiesToPanel(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText(errorMessage + " - " + caught.getMessage());
					errorLabel.setVisible(true);
				}
			});
		}
	}
	
	private void addActivitiesToPanel(
			Map<String, ArrayList<String[]>> result) {
		for (String date : result.keySet() ){
			DockPanel datePanel = new DockPanel();
			datePanel.setWidth("660px");
			datePanel.add(new Label("Atividates em "+date), DockPanel.CENTER);
			activityPanel.add(datePanel);
			for ( String[] content : result.get(date) ){
				activityPanel.add(new LendMeActivityRep(content[0], content[1], content[2]));
			}
		}
	}
}
