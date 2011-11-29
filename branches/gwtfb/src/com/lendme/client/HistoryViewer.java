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
	private Label systemMsg;
	
	public HistoryViewer(LendMeAsync lendMeService,String solicitorSessionId, String scope) {
		
		container.setStyleName("topicsLines");
		container.setSize("600px", "500px");

		ScrollPanel scrollPanel = new ScrollPanel();
		rootPanel.setStyleName("rootPanelHistory");
		rootPanel.add(scrollPanel, 0, 54);
		scrollPanel.setSize("600px", "550px");
		rootPanel.setSize("600px", "610px");
		
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
		activityPanel.setSize("600px", "548px");
		errorLabel.setVisible(false);

		
		systemMsg = new Label("");
		systemMsg.setStyleName("barra");
		rootPanel.add(systemMsg, 0, 35);
		systemMsg.setSize("600px", "20px");
		
		Label titulo = new Label("Histórico de atividades");
		titulo.setStyleName("titulo-historico");
		rootPanel.add(titulo, 21, 0);
		titulo.setSize("171px", "35px");

		
		rootPanel.add(scrollPanel, 0, 54);
		
		if ( scope.equals("all") ){
			lendMeService.getActivityHistory(solicitorSessionId, new AsyncCallback<Map<String, ArrayList<String[]>>>() {

				@Override
				public void onSuccess(Map<String, ArrayList<String[]>> result) {
					for (String date : result.keySet() ){
						DockPanel datePanel = new DockPanel();
						datePanel.setWidth("660px");
						datePanel.add(new Label("Atividates em "+ date), DockPanel.CENTER);
						systemMsg.setText("Veja o que seus amigos andam fazendo no Lend-me!");
						rootPanel.add(systemMsg);
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
						systemMsg.setText("Veja suas atividades no Lend-me!");
						rootPanel.add(systemMsg);
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
