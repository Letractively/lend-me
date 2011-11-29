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
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HistoryViewer extends Composite {
	
	private AbsolutePanel rootPanel = new AbsolutePanel();
	private VerticalPanel container = new VerticalPanel();
	private final Label errorLabel; 
	private final String errorMessage = "Erro: LendMe nao conseguiu obter informacao do servidor.";
	private Label systemMsg;
	private Label titulo;
	private ScrollPanel scrollPanel;
	private HorizontalPanel topPanel;
	private Label numberOfMsgsLabel;
	private Label subjectLabel;
	private LendMeAsync lendMeService;
	private VerticalPanel activityPanel;  
	private String solicitorSessionId;
	final String REFRESH_ICON_PATH = "http://icons.iconarchive.com/icons/deleket/button/48/Button-Refresh-icon.png";
	private Image refreshIcon;
	
	
	public HistoryViewer(LendMeAsync lendMeService,String solicitorSessionId, final String scope) {
		
		container.setStyleName("topicsLines");
		container.setSize("600px", "500px");

		scrollPanel = new ScrollPanel();
		rootPanel.setStyleName("rootPanelHistory");
		rootPanel.add(scrollPanel, 0, 70);
		scrollPanel.setSize("600px", "500px");
		rootPanel.setSize("600px", "570px");
		
		scrollPanel.add(container);
		
		errorLabel = new Label("error");
		container.add(errorLabel);
		errorLabel.setStyleName("topicsLines");
		
		topPanel = new HorizontalPanel();
		container.add(topPanel);
		topPanel.setWidth("600px");
		
		numberOfMsgsLabel = new Label("numOfMsgs");
		numberOfMsgsLabel.setText("Visualiza\u00E7ao:");
		numberOfMsgsLabel.setSize("210px", "15px");
		topPanel.add(numberOfMsgsLabel);
		
		subjectLabel = new Label("subject");
		subjectLabel.setText((scope.equals("all")?"Todos do Sistema":"Pessoal"));
		subjectLabel.setSize("210px", "15px");
		topPanel.add(subjectLabel);
		
		topPanel.addStyleName("topicsHeader");
		
		activityPanel = new VerticalPanel();
		container.add(activityPanel);
		activityPanel.setSize("600px", "500px");
		errorLabel.setVisible(false);

		
		systemMsg = new Label("");
		systemMsg.addStyleName("barra");
		rootPanel.add(systemMsg, 0, 58);
		systemMsg.setSize("600px", "10px");
		
		titulo = new Label("Histórico de atividades");
		titulo.addStyleName("tituloHistorico");
		rootPanel.add(titulo, 10, 23);
		titulo.setSize("260px", "29px");

		
		rootPanel.add(scrollPanel, 0, 70);
		refreshPage(scope);

		refreshIcon = new Image((String) null);
		refreshIcon.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				refreshPage(scope);
			}
		});
		refreshIcon.setSize("48px", "48px");
		refreshIcon.setUrl(REFRESH_ICON_PATH);
		rootPanel.add(refreshIcon, 386, 4);

		this.initWidget(rootPanel);
		
		
	}
	
	public void refreshPage(String scope){
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

	}
}
