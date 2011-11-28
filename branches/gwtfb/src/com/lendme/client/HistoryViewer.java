package com.lendme.client;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HistoryViewer extends Composite {
	
	private AbsolutePanel rootPanel = new AbsolutePanel();
	private VerticalPanel container = new VerticalPanel();
	private final Label errorLabel; 
	private final String errorMessage = "Erro: LendMe nao conseguiu obter informacao do servidor.";
	
	public HistoryViewer(LendMeAsync lendMeService,String solicitorSessionId, String scope) {
		
		container.setStyleName("topicsLines");
		
		container.setWidth("570px");

		ScrollPanel scrollPanel = new ScrollPanel();
		rootPanel.add(scrollPanel, 0, 32);
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
		
		final VerticalPanel activityPanel = new VerticalPanel();
		container.add(activityPanel);
		activityPanel.setSize("599px", "481px");
		errorLabel.setVisible(false);
		
		rootPanel.add(scrollPanel, 0, 32);
		
		if ( scope.equals("all") ){
			lendMeService.getActivityHistory(solicitorSessionId, new AsyncCallback<Map<String, ArrayList<String[]>>>() {

				@Override
				public void onSuccess(Map<String, ArrayList<String[]>> result) {
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

				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText(errorMessage + " - " + caught.getMessage());
					errorLabel.setVisible(true);
				}
			});
		}
		else{
			lendMeService.getJointActivityHistory(solicitorSessionId, new AsyncCallback<Map<String, ArrayList<String[]>>>() {

				@Override
				public void onSuccess(Map<String, ArrayList<String[]>> result) {
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

				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText(errorMessage + " - " + caught.getMessage());
					errorLabel.setVisible(true);
				}
			});
		}
		
		this.initWidget(rootPanel);
		
	}
	
}
